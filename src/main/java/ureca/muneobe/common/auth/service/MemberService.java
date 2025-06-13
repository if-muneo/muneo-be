package ureca.muneobe.common.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ureca.muneobe.common.auth.dto.request.SignupRequest;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.respository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    //TODO: 사용자 회원가입할때 직접 기입받을 정보들 수정 예정

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member authenticate(String email, String password) {
        Optional<Member> memberOpt = memberRepository.findByEmail(email);

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            System.out.println("입력된 비밀번호: " + password);
            System.out.println("저장된 해시: " + member.getPassword());
            System.out.println("매칭 결과: " + passwordEncoder.matches(password, member.getPassword()));
            if (passwordEncoder.matches(password, member.getPassword())) {
                return member;
            }
        }
        return null;
    }

    public Member signup(SignupRequest request) {
        if (memberRepository.existsByName(request.getMemberName())) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.builder()
                .name(request.getMemberName())
                .password(encodedPassword)
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .old(request.getOld())
                .gender(request.getGender())
                .category(request.getCategory())
                .build();

        return memberRepository.save(member);
    }

    public Member findByMemberName(String name) {
        return memberRepository.findByName(name).orElse(null);
    }
}
