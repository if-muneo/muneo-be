package ureca.muneobe.common.chat.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.chat.entity.*;
import ureca.muneobe.common.chat.service.strategy.rdb.input.AddonCondition;
import ureca.muneobe.common.chat.service.strategy.rdb.input.Condition;
import ureca.muneobe.common.chat.service.strategy.rdb.input.MplanCondition;
import ureca.muneobe.common.chat.service.strategy.rdb.input.Range;
import ureca.muneobe.common.chat.service.strategy.rdb.output.FindingMplan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        // 둘 다 조건이 없으면 바로 빈 리스트
        if (!hasMplanCond && !hasAddonCond) {
            return Collections.emptyList();
        }

        // where절 조합용 BooleanBuilder
        BooleanBuilder whereBuilder = new BooleanBuilder();

        // 1) MplanCondition이 있으면 predicate 결합
        if (hasMplanCond) {
            BooleanBuilder mplanPred = buildPredicateMplan(mcond);
            whereBuilder.and(mplanPred);
        }

        // 2) AddonCondition이 있으면, addonGroup.id in (matchingGroupIds) 형태로 where절 추가
        if (hasAddonCond) {
            List<Long> addonGroupIds = fetchAddonGroupIdsByCondition(acond);
            if (addonGroupIds.isEmpty()) {
                // 부가서비스 조건을 만족하는 그룹이 없으면 결과 없음
                return Collections.emptyList();
            }
            whereBuilder.and(mplan.addonGroup.id.in(addonGroupIds));
        }

        // 3) 최종 조회: join + select + whereBuilder + limit 적용
        //    SearchUtils.fetchTuples 내부를 사용하신다면, fetchTuples(jpaQueryFactory, whereBuilder, LIMIT_VALUE) 등의 형태로 조정.
        List<Tuple> tuples = jpaQueryFactory
                .select(
                        // 예: mplan.id, mplan.name, detail.monthlyPrice, detail.dataType, addon.name 등
                        mplan.id,
                        mplan.name,
                        detail.monthlyPrice,
                        detail.dataType,
                        // 필요시 addon 정보를 groupTuples 단계에서 따로 모으도록 할 수도 있음
                        // 예: addon.name, addon.addonType, addon.price 등
                        addon.name,
                        addon.addonType,
                        addon.price
                )
                .from(mplan)
                .join(mplan.mplanDetail, detail)   // mplan_detail join
                .leftJoin(mplan.addonGroup, addonGroup)
                .leftJoin(addonGroup.addon, addon)
                .where(whereBuilder)
                .distinct()    // 중복 제거가 필요하면
                .limit(LIMIT_VALUE)
                .fetch();

        // 4) Tuple 목록을 FindingMplan DTO로 변환
        return groupTuples(tuples);
    }

    /**
     * MplanCondition 내부가 실제로 비어있는지 판단.
     */
    private boolean isMplanConditionEmpty(MplanCondition mcond) {
        if (mcond == null) {
            return true;
        }
        // 예: Range 필드가 null 혹은 내부가 모두 null일 때 empty
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
        // Boolean/enum 필드: null이 아니면 조건으로 간주
        if (mcond.getTextMessage() != null) return false;
        if (mcond.getDataType() != null) return false;
        if (mcond.getMplanType() != null) return false;
        if (mcond.getQualification() != null) return false;
        // 필요시 다른 필드도 확인
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
    private List<Long> fetchAddonGroupIdsByCondition(final AddonCondition cond) {
        final List<String> names = cond.getNames();
        final List<AddonType> types = cond.getAddonTypes();
        final Range priceRange = cond.getPrice();

        // 1) names 여러 개만 AND 매칭
        if (names != null && names.size() > 1
                && (types == null || types.isEmpty())
                && priceRange == null) {
            long requiredCount = names.size();
            return jpaQueryFactory
                    .select(addonGroup.id)
                    .from(addonGroup)
                    .join(addonGroup.addon, addon)
                    .where(addon.name.in(names))
                    .groupBy(addonGroup.id)
                    .having(addon.name.countDistinct().eq(requiredCount))
                    .fetch();
        }
        // 2) names 하나 이상 OR types/price 섞인 복합
        if (names != null && !names.isEmpty()) {
            List<QAddon> aliases = new ArrayList<>(names.size());
            for (int i = 0; i < names.size(); i++) {
                aliases.add(new QAddon("addonAlias" + i));
            }
            var query = jpaQueryFactory
                    .select(addonGroup.id)
                    .from(addonGroup);
            for (int i = 0; i < names.size(); i++) {
                QAddon a = aliases.get(i);
                BooleanBuilder on = new BooleanBuilder().and(a.name.eq(names.get(i)));
                if (types != null && !types.isEmpty()) {
                    on.and(a.addonType.in(types));
                }
                if (priceRange != null) {
                    applyRange(on, a.price, priceRange);
                }
                query = query.leftJoin(addonGroup.addon, a).on(on);
            }
            BooleanBuilder notNullAll = new BooleanBuilder();
            for (QAddon a : aliases) {
                notNullAll.and(a.id.isNotNull());
            }
            return query.where(notNullAll).fetch();
        }
        // 3) names empty, types/price만 있을 때 OR
        BooleanBuilder predicate = new BooleanBuilder();
        if (types != null && !types.isEmpty()) {
            predicate.and(addon.addonType.in(types));
        }
        if (priceRange != null) {
            applyRange(predicate, addon.price, priceRange);
        }
        if (!predicate.hasValue()) {
            return Collections.emptyList();
        }
        return jpaQueryFactory
                .select(addonGroup.id)
                .from(addonGroup)
                .join(addonGroup.addon, addon)
                .where(predicate)
                .distinct()
                .fetch();
    }
}
