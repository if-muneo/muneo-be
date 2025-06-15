package ureca.muneobe.common.chat.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.chat.entity.*;
import ureca.muneobe.common.chat.service.strategy.rdb.input.Condition;
import ureca.muneobe.common.chat.service.strategy.rdb.input.MplanCondition;
import ureca.muneobe.common.chat.service.strategy.rdb.output.FindingMplan;

import java.util.*;

import static ureca.muneobe.common.chat.repository.search.SearchUtils.*;

@Repository
@RequiredArgsConstructor
public class MplanSearchRepository implements SearchRepository{

    private static final long LIMIT_VALUE = 3L;
    private final JPAQueryFactory jpaQueryFactory;

    private final QMplan mplan = QMplan.mplan;
    private final QMplanDetail detail = QMplanDetail.mplanDetail;

    @Override
    public List<FindingMplan> search(final Condition condition) {
        final MplanCondition cond = condition.getMplanCondition();
        final BooleanBuilder predicate = buildPredicateMplan(cond);
        final List<Long> planIds = fetchPlanIds(predicate);

        if (planIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Tuple> tuples = fetchTuples(jpaQueryFactory, planIds);
        return groupTuples(tuples);
    }

    private List<Long> fetchPlanIds(final BooleanBuilder predicate) {
        return jpaQueryFactory
                .select(mplan.id)
                .from(mplan)
                .innerJoin(mplan.mplanDetail, detail)
                .where(predicate)
                .limit(LIMIT_VALUE)
                .fetch();
    }
}