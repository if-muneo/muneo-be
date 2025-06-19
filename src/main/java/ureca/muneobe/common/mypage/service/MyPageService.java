package ureca.muneobe.common.mypage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.mypage.dto.response.MyPageResponse;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;

    public MyPageResponse findMyInfo(Member loginMember) {
        return getMyPageResponse(loginMember.getId());
    }

    private MyPageResponse getMyPageResponse(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new GlobalException(ErrorCode.UNAUTHORIZED));
        return MyPageResponse.from(member);
    }
}
