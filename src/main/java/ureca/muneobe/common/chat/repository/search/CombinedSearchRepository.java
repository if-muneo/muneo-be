package ureca.muneobe.common.chat.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.addon.entity.QAddon;
import ureca.muneobe.common.addongroup.entity.QAddonGroup;
import ureca.muneobe.common.chat.service.strategy.rdb.input.AddonCondition;
import ureca.muneobe.common.chat.service.strategy.rdb.input.Condition;
import ureca.muneobe.common.chat.service.strategy.rdb.input.MplanCondition;
import ureca.muneobe.common.chat.service.strategy.rdb.input.Range;
import ureca.muneobe.common.chat.service.strategy.rdb.output.FindingMplan;
import ureca.muneobe.common.mplan.entity.QMplan;
import ureca.muneobe.common.mplan.entity.QMplanDetail;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ureca.muneobe.common.chat.repository.search.SearchUtils.*;

@Repository
@RequiredArgsConstructor
public class CombinedSearchRepository implements SearchRepository {

    private final int COUNT_VALUE = 1;
    private final String PERCENT = "%";
    private final long LIMIT_VALUE = 3L;
    private final JPAQueryFactory jpaQueryFactory;
    private final QMplan mplan = QMplan.mplan;
    private final QMplanDetail detail = QMplanDetail.mplanDetail;
    private final QAddonGroup addonGroup = QAddonGroup.addonGroup;
    private final QAddon addon = QAddon.addon;

    @Override
    public List<FindingMplan> search(final Condition condition) {
        BooleanBuilder whereBuilder = buildWhereCondition(condition);
        if (whereBuilder == null) return Collections.emptyList();

        List<Long> planIds = jpaQueryFactory
                .select(mplan.id)
                .from(mplan)
                .join(mplan.mplanDetail, detail)
                .leftJoin(mplan.addonGroup, addonGroup)
                .where(whereBuilder)
                .limit(LIMIT_VALUE)
                .fetch();

        if (planIds.isEmpty()) return Collections.emptyList();

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

    private BooleanBuilder buildWhereCondition(Condition condition) {
        MplanCondition mcond = condition.getMplanCondition();
        AddonCondition acond = condition.getAddonCondition();

        boolean hasMplanCond = !isMplanConditionEmpty(mcond);
        boolean hasAddonCond = !isAddonConditionEmpty(acond);

        if (!hasMplanCond && !hasAddonCond) return null;

        BooleanBuilder whereBuilder = new BooleanBuilder();

        if (hasMplanCond) {
            whereBuilder.and(buildPredicateMplan(mcond));
        }

        if (hasAddonCond) {
            Optional<Long> addonGroupIdOpt = fetchAddonGroupIdByCondition(acond);
            addonGroupIdOpt.ifPresent(id -> whereBuilder.and(mplan.addonGroup.id.eq(id)));
            if (addonGroupIdOpt.isEmpty()) return null;
        }

        return whereBuilder;
    }

    private boolean isMplanConditionEmpty(MplanCondition mcond) {
        if (mcond == null) return true;

        return isRangeEmpty(mcond.getMonthlyPrice()) &&
                isRangeEmpty(mcond.getBasicDataAmount()) &&
                isRangeEmpty(mcond.getDailyData()) &&
                isRangeEmpty(mcond.getSharingData()) &&
                isRangeEmpty(mcond.getVoiceCallVolume()) &&
                isRangeEmpty(mcond.getSubDataSpeed()) &&
                mcond.getTextMessage() == null &&
                mcond.getDataType() == null &&
                mcond.getMplanType() == null &&
                mcond.getQualification() == null;
    }

    private boolean isAddonConditionEmpty(AddonCondition cond) {
        if (cond == null) return true;

        return isRangeEmpty(cond.getPrice()) &&
                (cond.getNames() == null || cond.getNames().isEmpty()) &&
                (cond.getAddonTypes() == null || cond.getAddonTypes().isEmpty());
    }

    private boolean isRangeEmpty(Range range) {
        return range == null || (range.getBaseNumber() == null &&
                range.getSubNumber() == null &&
                range.getOperator() == null);
    }

    private Optional<Long> fetchAddonGroupIdByCondition(final AddonCondition cond) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (cond.getNames() != null && !cond.getNames().isEmpty()) {
            BooleanBuilder likeBuilder = new BooleanBuilder();
            for (String name : cond.getNames()) {
                String lowerName = name.toLowerCase();
                likeBuilder.or(addon.name.like(PERCENT + lowerName + PERCENT));
            }
            predicate.and(likeBuilder);
        }

        if (cond.getPrice() != null) {
            applyRange(predicate, addon.price, cond.getPrice());
        }

        if (!predicate.hasValue()) return Optional.empty();

        return Optional.ofNullable(
                jpaQueryFactory
                        .select(addonGroup.id)
                        .from(addonGroup)
                        .join(addonGroup.addons, addon)
                        .where(predicate)
                        .groupBy(addonGroup.id)
                        .having(addon.count().goe(cond.getNames().size()))
                        .fetchFirst()
        );
    }
}