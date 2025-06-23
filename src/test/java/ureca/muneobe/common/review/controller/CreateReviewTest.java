package ureca.muneobe.common.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ahocorasick.trie.Trie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.entity.enums.Category;
import ureca.muneobe.common.auth.entity.enums.Gender;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.auth.utils.SessionUtil;
import ureca.muneobe.common.chat.entity.Slang;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.common.mplan.repository.MplanRepository;
import ureca.muneobe.common.review.dto.request.ReviewCreateRequest;
import ureca.muneobe.common.slang.SlangRepository;
import ureca.muneobe.common.slang.service.SlangService;
import ureca.muneobe.common.subscription.entity.Subscription;
import ureca.muneobe.common.subscription.repository.SubscriptionRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@ActiveProfiles("test")
class CreateReviewTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MplanRepository mplanRepository;
    @Autowired private SubscriptionRepository subscriptionRepository;

    private Member saveMember;
    private Mplan saveMplan;
    @Autowired
    private SlangRepository slangRepository;
    @Autowired
    private SlangService slangService;

    @BeforeEach
    void setUp() {
        saveMember = memberRepository.save(Member.builder()
                .name("테스터")
                .email("test@ex.com")
                .password("pw")
                .phoneNumber("010-0000-0000")
                .old(25)
                .gender(Gender.M)
                .category(Category.BOY)
                .build());

        saveMplan = mplanRepository.save(Mplan.builder()
                .name("요금제 테스트")
                .addonGroup(null)
                .mplanDetail(null)
                .build());

        subscriptionRepository.save(Subscription.builder()
                .member(saveMember)
                .mplan(saveMplan)
                .fee(33000)
                .build());

        slangRepository.save(new Slang("시발"));
        slangService.resetSlang();
    }

    private MockHttpSession getLoginSession() {
        MockHttpSession session = new MockHttpSession();
        SessionUtil.setMemberSession(session, saveMember);
        return session;
    }

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("모든 조건을 만족한 정상적인 리뷰 글을 달았을 경우")
    void createReview_success() throws Exception {
        ReviewCreateRequest req = new ReviewCreateRequest("이 리뷰는 유효한 정상 리뷰입니다.");
        MockHttpSession session = getLoginSession();

        Trie trie = applicationContext.getBean(Trie.class);
        System.out.println("trie = " + trie);

        mockMvc.perform(post("/v1/{mplanId}/review", saveMplan.getId())
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    @DisplayName("비속어를 달았을 경우")
    void createReview_fail_slang() throws Exception {
        ReviewCreateRequest req = new ReviewCreateRequest("이 리뷰는 시발 정상 리뷰입니다.");
        MockHttpSession session = getLoginSession();

        Trie trie = applicationContext.getBean(Trie.class);
        System.out.println("trie = " + trie);

        mockMvc.perform(post("/v1/{mplanId}/review", saveMplan.getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문장이 짧을 경우")
    void createReview_fail_shortSentence() throws Exception {
        ReviewCreateRequest req = new ReviewCreateRequest("이 리뷰는 굳");
        MockHttpSession session = getLoginSession();

        Trie trie = applicationContext.getBean(Trie.class);
        System.out.println("trie = " + trie);

        mockMvc.perform(post("/v1/{mplanId}/review", saveMplan.getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("가입되지 않은 요금제에 리뷰를 달 경우")
    void createReview_fail_NotRegister() throws Exception {
        ReviewCreateRequest req = new ReviewCreateRequest("이 리뷰는 완벽한 요금제입니다.");
        MockHttpSession session = getLoginSession();

        Trie trie = applicationContext.getBean(Trie.class);
        System.out.println("trie = " + trie);

        mockMvc.perform(post("/v1/2/review", saveMplan.getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }
}