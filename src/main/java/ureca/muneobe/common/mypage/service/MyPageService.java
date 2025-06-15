package ureca.muneobe.common.mypage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.chat.entity.Subscription;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;


}
