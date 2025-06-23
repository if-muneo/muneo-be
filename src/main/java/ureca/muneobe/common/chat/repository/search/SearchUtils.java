package ureca.muneobe.common.chat.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import ureca.muneobe.common.addon.entity.AddonType;
import ureca.muneobe.common.addon.entity.QAddon;
import ureca.muneobe.common.addongroup.entity.QAddonGroup;
import ureca.muneobe.common.chat.service.strategy.rdb.input.MplanCondition;
import ureca.muneobe.common.chat.service.strategy.rdb.input.Range;
import ureca.muneobe.common.chat.service.strategy.rdb.output.FindingAddon;
import ureca.muneobe.common.chat.service.strategy.rdb.output.FindingMplan;
import ureca.muneobe.common.mplan.entity.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SearchUtils {

    private static final QMplan mplan = QMplan.mplan;
    private static final QMplanDetail detail = QMplanDetail.mplanDetail;
    private static final QAddonGroup addonGroup = QAddonGroup.addonGroup;
    private static final QAddon addon = QAddon.addon;

    public static void applyRange(
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
            case "이상":
                if (base != null) {
                    builder.and(path.goe(base));
                }
                break;
            case "이하":
                if (base != null) {
                    builder.and(path.loe(base));
                }
                break;
            case "동등":
                if (base != null) {
                    builder.and(path.eq(base));
                }
                break;
            case "초과":
                if (base != null) {
                    builder.and(path.gt(base));
                }
                break;
            case "미만":
                if (base != null) {
                    builder.and(path.lt(base));
                }
                break;
            case "사이":
                if (base != null && sub != null) {
                    builder.and(path.between(base, sub));
                }
                break;
            default:
                break;
        }
    }

    public static List<Tuple> fetchTuples(JPAQueryFactory jpaQueryFactory, List<Long> planIds) {
        return jpaQueryFactory
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
                .innerJoin(mplan.mplanDetail, detail)
                .leftJoin(mplan.addonGroup, addonGroup)
                .leftJoin(addonGroup.addons, addon)
                .where(mplan.id.in(planIds))
                .fetch();
    }

    public static  List<FindingMplan> groupTuples(List<Tuple> tuples) {
        Map<Long, FindingMplan> grouped = new LinkedHashMap<>();
        for (Tuple t : tuples) {
            Long planId = t.get(mplan.id);
            FindingMplan current = grouped.get(planId);
            if (current == null) {
                String name = t.get(mplan.name);
                Integer basicDataAmount = t.get(detail.basicDataAmount);
                Integer dailyData = t.get(detail.dailyData);
                Integer sharingData = t.get(detail.sharingData);
                Integer monthlyPrice = t.get(detail.monthlyPrice);
                Integer voiceCallVolume = t.get(detail.voiceCallVolume);
                Boolean textMessage = t.get(detail.textMessage);
                Integer subDataSpeed = t.get(detail.subDataSpeed);
                Qualification qualification = t.get(detail.qualification);
                DataType dataType = t.get(detail.dataType);
                MplanType mplanType = t.get(detail.mplanType);

                current = new FindingMplan(
                        name,
                        basicDataAmount,
                        dailyData,
                        sharingData,
                        monthlyPrice,
                        voiceCallVolume,
                        textMessage,
                        subDataSpeed,
                        qualification,
                        dataType,
                        mplanType,
                        new ArrayList<>()
                );
                grouped.put(planId, current);
            }

            String addonName = t.get(addon.name);
            if (addonName != null) {
                String addonDesc = t.get(addon.description);
                Integer addonPrice = t.get(addon.price);
                AddonType addonType = t.get(addon.addonType);
                FindingAddon fa = FindingAddon.builder()
                        .name(addonName)
                        .description(addonDesc)
                        .price(addonPrice)
                        .addonType(addonType)
                        .build();
                current.getFindingAddons().add(fa);
            }
        }
        return new ArrayList<>(grouped.values());
    }

    public static BooleanBuilder buildPredicateMplan(final MplanCondition cond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (cond.getName() != null && !cond.getName().isEmpty()) {
            builder.and(mplan.name.eq(cond.getName()));
        }
        if (cond.getMonthlyPrice() != null) {
            applyRange(builder, detail.monthlyPrice, cond.getMonthlyPrice());
        }
        if (cond.getDailyData() != null) {
            applyRange(builder, detail.dailyData, cond.getDailyData());
        }
        if (cond.getBasicDataAmount() != null) {
            applyRange(builder, detail.basicDataAmount, cond.getBasicDataAmount());
        }
        if (cond.getSharingData() != null) {
            applyRange(builder, detail.sharingData, cond.getSharingData());
        }
        if (cond.getVoiceCallVolume() != null) {
            applyRange(builder, detail.voiceCallVolume, cond.getVoiceCallVolume());
        }
        if (cond.getTextMessage() != null) {
            builder.and(detail.textMessage.eq(cond.getTextMessage()));
        }
        if (cond.getSubDataSpeed() != null) {
            applyRange(builder, detail.subDataSpeed, cond.getSubDataSpeed());
        }
        if (cond.getQualification() != null) {
            builder.and(detail.qualification.eq(cond.getQualification()));
        }
        if (cond.getDataType() != null) {
            builder.and(detail.dataType.eq(cond.getDataType()));
        }
        if (cond.getMplanType() != null) {
            builder.and(detail.mplanType.eq(cond.getMplanType()));
        }
        return builder;
    }
}
