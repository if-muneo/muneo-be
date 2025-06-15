package ureca.muneobe.common.chat.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.chat.entity.*;
import ureca.muneobe.common.chat.service.rdb.input.AddonCondition;
import ureca.muneobe.common.chat.service.rdb.input.Condition;
import ureca.muneobe.common.chat.service.rdb.input.MplanCondition;
import ureca.muneobe.common.chat.service.rdb.input.Range;
import ureca.muneobe.common.chat.service.rdb.output.FindingMplan;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ureca.muneobe.common.chat.repository.search.SearchUtils.*;

@Repository
@RequiredArgsConstructor
public class CombinedSearchRepository implements SearchRepository {

    private static final long LIMIT_VALUE = 3L;
    private final JPAQueryFactory jpaQueryFactory;
    private final QMplan mplan = QMplan.mplan;
    private final QMplanDetail detail = QMplanDetail.mplanDetail;
    private final QAddonGroup addonGroup = QAddonGroup.addonGroup;
    private final QAddon addon = QAddon.addon;

    @Override
    public List<FindingMplan> search(final Condition condition) {
        MplanCondition mcond = condition.getMplanCondition();
        AddonCondition acond = condition.getAddonCondition();

        boolean hasMplanCond = !isMplanConditionEmpty(mcond);
        boolean hasAddonCond = !isAddonConditionEmpty(acond);

        if (!hasMplanCond && !hasAddonCond) {
            return Collections.emptyList();
        }

        BooleanBuilder whereBuilder = new BooleanBuilder();

        if (hasMplanCond) {
            BooleanBuilder mplanPred = buildPredicateMplan(mcond);
            whereBuilder.and(mplanPred);
        }

        if (hasAddonCond) {
            Optional<Long> addonGroupIdOpt = fetchAddonGroupIdByCondition(acond);
            if (addonGroupIdOpt.isEmpty()) {
                return Collections.emptyList();
            }
            whereBuilder.and(mplan.addonGroup.id.eq(addonGroupIdOpt.get()));
        }

        // 1. 먼저 mplan.id 리스트 가져오기
        List<Long> planIds = jpaQueryFactory
                .select(mplan.id)
                .from(mplan)
                .join(mplan.mplanDetail, detail)
                .leftJoin(mplan.addonGroup, addonGroup)
                .where(whereBuilder)
                .limit(LIMIT_VALUE)
                .fetch();

        List<Tuple> tuples = jpaQueryFactory
                .select(
                        mplan.id,
                        mplan.name,
                        detail.basicDataAmount,
                        detail.dailyData,
                        detail.sharingData,
                        detail.monthlyPrice,
                        detail.voiceCallVolume,
                        detail.textMessage,
                        detail.subDataSpeed,
                        detail.qualification,
                        detail.dataType,
                        detail.mplanType,
                        addon.name,
                        addon.description,
                        addon.price,
                        addon.addonType
                )
                .from(mplan)
                .join(mplan.mplanDetail, detail)
                .leftJoin(addon).on(addon.addonGroup.id.eq(mplan.addonGroup.id))
                .where(mplan.id.in(planIds))
                .fetch();

        return groupTuples(tuples);
    }

    private boolean isMplanConditionEmpty(MplanCondition mcond) {
        if (mcond == null) {
            return true;
        }

        Range monthly = mcond.getMonthlyPrice();
        if (monthly != null && !isRangeEmpty(monthly)) return false;
        Range basic = mcond.getBasicDataAmount();
        if (basic != null && !isRangeEmpty(basic)) return false;
        Range daily = mcond.getDailyData();
        if (daily != null && !isRangeEmpty(daily)) return false;
        Range sharing = mcond.getSharingData();
        if (sharing != null && !isRangeEmpty(sharing)) return false;
        Range voice = mcond.getVoiceCallVolume();
        if (voice != null && !isRangeEmpty(voice)) return false;
        Range subSpeed = mcond.getSubDataSpeed();

        if (subSpeed != null && !isRangeEmpty(subSpeed)) return false;
        if (mcond.getTextMessage() != null) return false;
        if (mcond.getDataType() != null) return false;
        if (mcond.getMplanType() != null) return false;
        if (mcond.getQualification() != null) return false;
        return true;
    }

    private boolean isRangeEmpty(Range r) {
        return r.getBaseNumber() == null && r.getSubNumber() == null && r.getOperator() == null;
    }

    /**
     * AddonCondition 내부가 실제 비어있는지 판단.
     */
    private boolean isAddonConditionEmpty(AddonCondition cond) {
        if (cond == null) {
            return true;
        }
        Range price = cond.getPrice();
        boolean priceEmpty = (price == null) || isRangeEmpty(price);
        boolean namesEmpty = (cond.getNames() == null || cond.getNames().isEmpty());
        boolean typesEmpty = (cond.getAddonTypes() == null || cond.getAddonTypes().isEmpty());
        return priceEmpty && namesEmpty && typesEmpty;
    }

    /**
     * AddonCondition으로부터 addonGroup ID 목록을 조회.
     * - names만 여러 개이고 types/price 없으면 GROUP BY+HAVING
     * - names 하나 이상+types/price 섞이면 alias JOIN
     * - names empty & types/price만 있을 때 OR 방식
     * - 모두 empty면 빈 리스트
     */

    private Optional<Long> fetchAddonGroupIdByCondition(final AddonCondition cond) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (cond.getNames() != null && !cond.getNames().isEmpty()) {
            predicate.and(addon.name.in(cond.getNames()));
        }

        if (cond.getAddonTypes() != null && !cond.getAddonTypes().isEmpty()) {
            predicate.and(addon.addonType.in(cond.getAddonTypes()));
        }

        if (cond.getPrice() != null) {
            applyRange(predicate, addon.price, cond.getPrice());
        }

        if (!predicate.hasValue()) {
            return Optional.empty();
        }

        return Optional.ofNullable(
                jpaQueryFactory
                        .select(addonGroup.id)
                        .from(addonGroup)
                        .join(addonGroup.addon, addon)
                        .where(predicate)
                        .groupBy(addonGroup.id)
                        .having(addon.count().goe(1)) // 최소 하나라도 매칭되면
                        .fetchFirst()
        );
    }
}