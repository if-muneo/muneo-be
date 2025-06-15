package ureca.muneobe.common.chat.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.chat.entity.AddonType;
import ureca.muneobe.common.chat.entity.QAddon;
import ureca.muneobe.common.chat.entity.QAddonGroup;
import ureca.muneobe.common.chat.entity.QMplan;
import ureca.muneobe.common.chat.service.rdb.input.AddonCondition;
import ureca.muneobe.common.chat.service.rdb.input.Condition;
import ureca.muneobe.common.chat.service.rdb.input.Range;
import ureca.muneobe.common.chat.service.rdb.output.FindingMplan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ureca.muneobe.common.chat.repository.search.SearchUtils.*;

@Repository
@RequiredArgsConstructor
public class AddonSearchRepository implements SearchRepository {

    private static final long LIMIT_VALUE = 3L;
    private final JPAQueryFactory jpaQueryFactory;

    private final QMplan mplan = QMplan.mplan;
    private final QAddonGroup addonGroup = QAddonGroup.addonGroup;
    private final QAddon addon = QAddon.addon;

    @Override
    public List<FindingMplan> search(final Condition condition) {

        AddonCondition cond = condition.getAddonCondition();
        if (cond == null) {
            return Collections.emptyList();
        }

        List<String> names = cond.getNames();
        List<AddonType> types = cond.getAddonTypes();
        Range priceRange = cond.getPrice();

        // 1) names 여러 개만 있고, types/price 조건이 없으면 GROUP BY + HAVING
        if (names != null && names.size() > 1
                && (types == null || types.isEmpty())
                && priceRange == null) {
            return searchByNamesOnly(names);
        }

        // 2) names가 하나 이상 있으면 alias JOIN 방식으로 AND 매칭
        if (names != null && !names.isEmpty()) {
            return searchByNamesWithFilters(names, types, priceRange);
        }

        // 3) names 비어있고 types/price만 있을 때: OR 방식으로 addon_group 추출
        if ((types != null && !types.isEmpty()) || priceRange != null) {
            return searchByTypeOrPrice(types, priceRange);
        }

        return Collections.emptyList();
    }

    // 1) GROUP BY + HAVING: names 리스트의 모든 이름을 포함하는 addon_group.id 조회
    private List<FindingMplan> searchByNamesOnly(List<String> names) {
        long requiredCount = names.size();
        List<Long> addonGroupIds = jpaQueryFactory
                .select(addonGroup.id)
                .from(addonGroup)
                .join(addonGroup.addon, addon)
                .where(addon.name.in(names))
                .groupBy(addonGroup.id)
                .having(addon.name.countDistinct().eq(requiredCount))
                .fetch();

        return fetchPlansByAddonGroupIds(addonGroupIds);
    }

    // 2) alias JOIN: names 각각 AND 매칭 + types/price on절에 결합
    private List<FindingMplan> searchByNamesWithFilters(
            List<String> names, List<AddonType> types, Range priceRange) {
        // alias 개수 = names.size()
        List<QAddon> aliases = new ArrayList<>(names.size());
        for (int i = 0; i < names.size(); i++) {
            aliases.add(new QAddon("addonAlias" + i));
        }

        // addonGroup.id 조회 쿼리 빌드
        var query = jpaQueryFactory
                .select(addonGroup.id)
                .from(addonGroup);

        for (int i = 0; i < names.size(); i++) {
            QAddon a = aliases.get(i);
            BooleanBuilder on = new BooleanBuilder()
                    .and(a.name.eq(names.get(i)));
            if (types != null && !types.isEmpty()) {
                on.and(a.addonType.in(types));
            }
            if (priceRange != null) {
                applyRange(on, a.price, priceRange);
            }
            query = query.leftJoin(addonGroup.addon, a).on(on);
        }

        // 모두 매칭되었음을 확인
        BooleanBuilder notNullAll = new BooleanBuilder();
        for (QAddon a : aliases) {
            notNullAll.and(a.id.isNotNull());
        }

        List<Long> addonGroupIds = query
                .where(notNullAll)
                .fetch();

        return fetchPlansByAddonGroupIds(addonGroupIds);
    }

    // 3) types/price만 있을 때: OR 방식으로 any addon 매칭되는 그룹 추출
    private List<FindingMplan> searchByTypeOrPrice(List<AddonType> types, Range priceRange) {
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

        List<Long> addonGroupIds = jpaQueryFactory
                .select(addonGroup.id)
                .from(addonGroup)
                .join(addonGroup.addon, addon)
                .where(predicate)
                .distinct()
                .fetch();

        return fetchPlansByAddonGroupIds(addonGroupIds);
    }

    private List<FindingMplan> fetchPlansByAddonGroupIds(List<Long> addonGroupIds) {
        if (addonGroupIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> planIds = jpaQueryFactory
                .select(mplan.id)
                .from(mplan)
                .where(mplan.addonGroup.id.in(addonGroupIds))
                .distinct()
                .limit(LIMIT_VALUE)
                .fetch();

        if (planIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Tuple> tuples = fetchTuples(jpaQueryFactory, planIds);
        return groupTuples(tuples);
    }
}