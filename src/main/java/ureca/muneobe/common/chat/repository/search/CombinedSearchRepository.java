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

    private final long LIMIT_VALUE = 3L;
    private final JPAQueryFactory jpaQueryFactory;
    private final QMplan mplan = QMplan.mplan;
    private final QMplanDetail detail = QMplanDetail.mplanDetail;
    private final QAddonGroup addonGroup = QAddonGroup.addonGroup;
    private final QAddon addon = QAddon.addon;

    @Override
    public List<FindingMplan> search(final Condition condition) {

        final List<Long> planIdsAfterMplan = fetchPlanIdsByMplanCondition(condition.getMplanCondition());

        if (planIdsAfterMplan != null && planIdsAfterMplan.isEmpty()) {
            return Collections.emptyList();
        }

        final AddonCondition acond = condition.getAddonCondition();

        if (!(acond.getPrice().getBaseNumber() == null && acond.getPrice().getSubNumber() == null && acond.getPrice().getOperator() == null
                && acond.getNames().size() == 0 && acond.getAddonTypes().size() == 0)
        ) {

            final List<Long> addonGroupIds = fetchAddonGroupIdsByCondition(acond);

            if (addonGroupIds.isEmpty()) {
                return Collections.emptyList();
            }

            final List<Long> finalPlanIds = fetchPlanIdsByAddonGroupIds(addonGroupIds, planIdsAfterMplan);

            if (finalPlanIds.isEmpty()) {
                return Collections.emptyList();
            }

            return fetchAndGroup(finalPlanIds);
        }

        if (planIdsAfterMplan != null) {
            return fetchAndGroup(planIdsAfterMplan);
        }

        return Collections.emptyList();
    }

    /**
     * MplanCondition이 주어졌을 때 plan ID 목록 조회
     * @param mcond MplanCondition; null이면 조건 없음
     * @return 조건 없음일 때 null, 조건이 있고 결과가 없으면 빈 리스트, 결과가 있으면 ID 리스트
     */
    private List<Long> fetchPlanIdsByMplanCondition(final MplanCondition mcond) {
        if (mcond == null) {
            return null;
        }

        final BooleanBuilder mplanPredicate = buildPredicateMplan(mcond);

        return jpaQueryFactory
                .select(mplan.id)
                .from(mplan)
                .innerJoin(mplan.mplanDetail, detail)
                .where(mplanPredicate)
                .limit(LIMIT_VALUE)
                .fetch();
    }

    /**
     * addonGroupIds와 MplanCondition 결과(planIdsAfterMplan)를 조합하여 plan ID 조회
     * @param addonGroupIds 부가서비스 그룹 ID 목록 (빈이 아님)
     * @param planIdsAfterMplan MplanCondition 결과 ID 목록; null이면 MplanCondition 없음
     * @return plan ID 목록 (조건에 맞거나 빈 리스트)
     */
    private List<Long> fetchPlanIdsByAddonGroupIds(final List<Long> addonGroupIds, final List<Long> planIdsAfterMplan) {
        if (planIdsAfterMplan != null) {
            return jpaQueryFactory
                    .select(mplan.id)
                    .from(mplan)
                    .where(mplan.id.in(planIdsAfterMplan)
                            .and(mplan.addonGroup.id.in(addonGroupIds)))
                    .distinct()
                    .limit(LIMIT_VALUE)
                    .fetch();
        } else {
            return jpaQueryFactory
                    .select(mplan.id)
                    .from(mplan)
                    .where(mplan.addonGroup.id.in(addonGroupIds))
                    .distinct()
                    .limit(LIMIT_VALUE)
                    .fetch();
        }
    }

    /**
     * plan ID 목록을 받아서 Tuple 조회 후 FindingMplan DTO로 변환
     * @param planIds plan ID 목록 (비어 있지 않음을 전제로 호출)
     * @return FindingMplan 리스트
     */
    private List<FindingMplan> fetchAndGroup(final List<Long> planIds) {
        final List<Tuple> tuples = fetchTuples(jpaQueryFactory, planIds);
        return groupTuples(tuples);
    }


    /**
     * AddonCondition으로부터 addonGroup ID 목록 조회
     * @param cond AddonCondition; names/types/price 조합 조건
     * @return 조건에 맞는 addonGroup ID 리스트 (조건이 없으면 빈 리스트 또는 null이 아닌 빈 리스트; 호출 측에서 null 처리하지 않음)
     */
    private List<Long> fetchAddonGroupIdsByCondition(final AddonCondition cond) {

        final List<String> names = cond.getNames();
        final List<AddonType> types = cond.getAddonTypes();
        final Range priceRange = cond.getPrice();

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

        if (names != null && !names.isEmpty()) {
            final List<QAddon> aliases = new ArrayList<>(names.size());
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

        final BooleanBuilder predicate = new BooleanBuilder();
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