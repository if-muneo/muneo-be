package ureca.muneobe.common.mplan.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.mplan.dto.request.MplanDetailCreateRequest;
import ureca.muneobe.common.mplan.entity.*;
import ureca.muneobe.common.auth.utils.SessionUtil;
import org.springframework.mock.web.MockHttpSession;
import ureca.muneobe.common.mplan.repository.MplanDetailRepository;

import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MplanDetailControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MplanDetailRepository mplanDetailRepository;

    private MplanDetailCreateRequest request;
    private Member loginMember;

    @BeforeEach
    void setUp(){
        mplanDetailRepository.save(MplanDetail.builder()
                .basicDataAmount(10000000)
                .sharingData(10000)
                .monthlyPrice(33000)
                .voiceCallVolume(0)
                .textMessage(true)
                .subDataSpeed(5000)
                .qualification(Qualification.ALL)
                .mplanType(MplanType.LTE_5G)
                .dataType(DataType._5G)
                .build());

        request = MplanDetailCreateRequest.builder()
                .basicDataAmount(3000)
                .sharingData(1000)
                .monthlyPrice(30000)
                .voiceCallVolume(0)
                .textMessage(true)
                .subDataSpeed(3)
                .qualification(Qualification.ALL)
                .mplanType(MplanType.LTE_5G)
                .dataType(DataType._5G)
                .build();

        //loginMember = memberRepository.findByName("최정민").orElseThrow(() -> new NoSuchElementException("Admin 계정을 찾지 못했습니다."));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("mplan detail 목록 조회 테스트")
    void readMplanDetails() throws Exception {
        mockMvc.perform(get("/v1/mplan-detail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.mplanDetailsResponse.content.length()").value(1));
//                .andExpect(jsonPath("$.data.mplanDetailsResponse.content.length()").value(4));
    }

//    @Test
//    @DisplayName("mplan detail 정상 생성 테스트")
//    void createMplanDetail() throws Exception {
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionUtil.MEMBER_SESSION_KEY, loginMember);
//
//        mockMvc.perform(post("/v1/mplan-detail")
//                .content(asJsonString(request))
//                .contentType(MediaType.APPLICATION_JSON)
//                .session(session)
//            )
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.data.id").exists());
//    }
}
