package ureca.muneobe.common.mypage.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.entity.enums.Category;
import ureca.muneobe.common.auth.entity.enums.Gender;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.auth.utils.SessionUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MyPageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    private Member loginMember;

    @BeforeEach
    void setUp(){
        loginMember = Member.builder()
                .name("test")
                .password("password")
                .phoneNumber("010-0000-0000")
                .email("test@gmail.com")
                .old(20)
                .gender(Gender.F)
                .category(Category.OFFICE_WORKER)
                .build();

        memberRepository.save(loginMember);
    }

    @Test
    @DisplayName("mypage 조회 테스트")
    void readMyPage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionUtil.MEMBER_SESSION_KEY, loginMember);

        mockMvc.perform(get("/v1/mypage")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(loginMember.getEmail()));
    }
}
