package ureca.muneobe.common.chat.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.chat.dto.chat.MemberInfoMeta;
import ureca.muneobe.common.chat.repository.AddonRepository;
import ureca.muneobe.common.mplan.entity.DataType;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.common.mplan.entity.MplanDetail;
import ureca.muneobe.common.subscription.entity.Subscription;
import ureca.muneobe.common.subscription.repository.SubscriptionRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberInfoMetaBuilder {

    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AddonRepository addonRepository;

    /**
     * memberName으로 MemberInfoMeta 생성 - 방법1 적용
     */
    public MemberInfoMeta buildFromMemberName(String memberName) {
        // 1단계: Member 조회
        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + memberName));

        // 2단계: Subscription + Mplan + MplanDetail 조회 (N+1 방지)
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findByMemberIdWithMplan(member.getId());

        if (subscriptionOpt.isPresent()) {
            Subscription subscription = subscriptionOpt.get();

            // 3단계: Addon 이름들만 별도 조회
            List<String> addonNames = addonRepository.findAddonNamesByMplanId(subscription.getMplan().getId());

            return buildWithSubscription(member, subscription, addonNames);
        } else {
            return buildWithoutSubscription(member);
        }
    }


    /**
     * 구독 정보가 있는 경우 - addonNames를 파라미터로 받음
     */
    private MemberInfoMeta buildWithSubscription(Member member, Subscription subscription, List<String> addonNames) {
        Mplan mplan = subscription.getMplan();
        MplanDetail mplanDetail = mplan.getMplanDetail();

        String addonNamesStr = String.join(",", addonNames);
        String mplanDetailStr = buildMplanDetailString(subscription, mplanDetail);

        return new MemberInfoMeta(
                member.getName(),                    // memberName
                member.getUseAmount(),              // useAmount
                subscription.getFee(),               // fee
                mplan.getName(),                     // name
                mplanDetail.getBasicDataAmount() == 10_000_000 ? "무제한" : mplanDetail.getBasicDataAmount() + "MB",   // basicDataAmount
                mplanDetail.getDataType(),          // dataType
                addonNamesStr,                       // addonNamesStr
                mplan.getName(),                     // mplanName
                mplanDetailStr,                      // mplanDetailStr
                addonNamesStr                        // addonGroupStr
        );
    }

    /**
     * 구독 정보가 없는 경우
     */
    private MemberInfoMeta buildWithoutSubscription(Member member) {
        return new MemberInfoMeta(
                member.getName(),
                member.getUseAmount(),
                0,                    // fee
                "",                   // name
                "",                    // basicDataAmount
                "UNKNOWN",           // dataType
                "",                   // addonNamesStr
                "",                   // mplanName
                "fee: 0, basicDataAmount: 0, dataType: UNKNOWN", // mplanDetailStr
                ""                    // addonGroupStr
        );
    }

    /**
     * MplanDetail 문자열 생성
     */
    private String buildMplanDetailString(Subscription subscription, MplanDetail mplanDetail) {

        String dataAmountStr = mplanDetail.getBasicDataAmount() == 10_000_000
                ? "무제한"
                : mplanDetail.getBasicDataAmount() + "MB";

        return String.format(
                "요금제 가격: %d원, 데이터 제공량: %s, dataType: %s",
                subscription.getFee(),
                dataAmountStr,
                mplanDetail.getDataType() == DataType._5G ? "5G" : "LTE"
        );
    }
}
