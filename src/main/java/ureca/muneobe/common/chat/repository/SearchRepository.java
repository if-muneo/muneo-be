package ureca.muneobe.common.chat.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ureca.muneobe.common.chat.entity.*;
import ureca.muneobe.common.chat.service.rdb.input.*;
import ureca.muneobe.common.chat.service.rdb.output.FindingAddon;
import ureca.muneobe.common.chat.service.rdb.output.FindingMplan;

import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

@Repository
@RequiredArgsConstructor
public class SearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QMplan mplan = QMplan.mplan;
    private final QMplanDetail detail = QMplanDetail.mplanDetail;
    private final QAddonGroup addonGroup = QAddonGroup.addonGroup;
    private final QAddon addon = QAddon.addon;
    private final QCombined combined = QCombined.combined;

    // mplan_detail 에서 monthly_price 유지
    // total

    // 부가 서비스 이름 빼고
    // 1. 모바일 요금제 -> 이름, 가격
    // 2. 모바일 요금제 + 부가 서비스
    // 3. 부가 서비스
    public List<FindingMplan> searchByMplan(final Condition condition) {

        BooleanBuilder whereMplan = buildMplanPredicate(condition.getMplanCondition());
        return jpaQueryFactory
                .from(mplan)
                .innerJoin(detail)
                .on(detail.id.eq(mplan.mplanDetail.id))
                .leftJoin(addonGroup)
                .on(addonGroup.id.eq(mplan.addonGroup.id))
                .leftJoin(addon)
                .on(addon.addonGroup.id.eq(addonGroup.id))
                .where(whereMplan)
                .transform(
                        groupBy(mplan.id).list(
                                Projections.constructor(
                                        FindingMplan.class,
                                        mplan.name,
                                        detail.monthlyPrice,
                                        detail.basicDataAmount,
                                        detail.dailyData,
                                        detail.sharingData,
                                        detail.monthlyPrice,
                                        detail.voiceCallVolume,
                                        detail.textMessage,
                                        detail.subDataSpeed,
                                        detail.qualification,
                                        detail.mplanType,
                                        detail.dataType,
                                        list(
                                                Projections.constructor(
                                                        FindingAddon.class,
                                                        addon.name,
                                                        addon.price,
                                                        addon.addonType
                                                )
                                        )
                                )
                        )
                );
    }

    private BooleanBuilder buildMplanPredicate(MplanCondition mc) {
        BooleanBuilder b = new BooleanBuilder();
        if (mc.getMonthlyPrice() != null) {
            applyRange(b, detail.monthlyPrice, mc.getMonthlyPrice());
        }

        if (mc.getName() != null && !mc.getName().isEmpty()) {
            b.and(mplan.name.eq(mc.getName()));
        }

        return b;
    }

    private BooleanBuilder buildAddonPredicate(List<AddonCondition> acs) {
        BooleanBuilder b = new BooleanBuilder();
        for (AddonCondition a : acs) {
            BooleanBuilder single = new BooleanBuilder();
            if (a.getPrice() != null) {
                applyRange(single, addon.price, a.getPrice());
            }
            if (!CollectionUtils.isEmpty(a.getNames())) {
                single.and(addon.name.in(a.getNames()));
            }
            if (a.getAddonType() != null) {
                single.and(addon.addonType.eq(a.getAddonType()));
            }
            b.and(single);
        }
        return b;
    }

    private void applyRange(
            BooleanBuilder builder,
            NumberPath<Integer> path,
            Range cond
    ) {
        if (cond == null || cond.getOperator() == null) {
            return;
        }
        Integer base = cond.getBaseNumber();
        Integer sub = cond.getSubNumber();

        switch (cond.getOperator()) {
            case "이상":      // >=
                builder.and(path.goe(base));
                break;
            case "이하":      // <=
                builder.and(path.loe(base));
                break;
            case "동등":      // ==
                builder.and(path.eq(base));
                break;
            case "초과":      // >
                builder.and(path.gt(base));
                break;
            case "미만":      // <
                builder.and(path.lt(base));
                break;
            case "사이":      // between (base ~ sub)
                if (sub != null) {
                    builder.and(path.between(base, sub));
                }
                break;
            default:
                // 알 수 없는 operator → 무시 또는 예외 처리
                break;
        }
    }
}
