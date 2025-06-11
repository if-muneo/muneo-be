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
    public List<FindingMplan> search(Condition condition) {

        // 1) MplanCondition으로 먼저 필터링된 planId 목록 조회 (없으면 null)
        List<Long> planIdsAfterMplan = fetchPlanIdsByMplanCondition(condition.getMplanCondition());
        if (planIdsAfterMplan != null && planIdsAfterMplan.isEmpty()) {
            return Collections.emptyList();
        }

        // 2) AddonCondition으로 addonGroupId 목록 조회
        AddonCondition acond = condition.getAddonCondition();
        if (acond != null) {
            List<Long> addonGroupIds = fetchAddonGroupIdsByCondition(acond);
            if (addonGroupIds.isEmpty()) {
                return Collections.emptyList();
            }
            // planIdsAfterMplan과 결합하거나, MplanCondition이 없으면 addonGroupIds만 사용
            List<Long> finalPlanIds = fetchPlanIdsByAddonGroupIds(addonGroupIds, planIdsAfterMplan);
            if (finalPlanIds.isEmpty()) {
                return Collections.emptyList();
            }
            return fetchAndGroup(finalPlanIds);
        }

        // 3) AddonCondition이 없고 MplanCondition만 있을 때
        if (planIdsAfterMplan != null) {
            return fetchAndGroup(planIdsAfterMplan);
        }

        // 4) 둘 다 없으면 빈 리스트 반환
        return Collections.emptyList();
    }

    private List<Long> fetchPlanIdsByMplanCondition(MplanCondition mcond) {
        if (mcond == null) {
            return null;
        }
        BooleanBuilder mplanPredicate = buildPredicateMplan(mcond);
        // limit 적용
        return jpaQueryFactory
                .select(mplan.id)
                .from(mplan)
                .innerJoin(mplan.mplanDetail, detail)
                .where(mplanPredicate)
                .limit(LIMIT_VALUE)   // 미리 제한
                .fetch();
    }

    private List<Long> fetchPlanIdsByAddonGroupIds(List<Long> addonGroupIds, List<Long> planIdsAfterMplan) {
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

    private List<FindingMplan> fetchAndGroup(List<Long> planIds) {
        List<Tuple> tuples = fetchTuples(jpaQueryFactory, planIds);
        return groupTuples(tuples);
    }

    private List<Long> fetchAddonGroupIdsByCondition(AddonCondition cond) {
        List<String> names = cond.getNames();
        List<AddonType> types = cond.getAddonTypes();
        Range priceRange = cond.getPrice();

        // 1) names 여러 개만 있고 types/price 없으면 GROUP BY + HAVING
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

        // 2) names 하나 이상 있거나 types/price 섞인 경우: alias JOIN 방식
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

        // 3) names 비어있고 types/price만 있을 때: OR 방식
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