package ureca.muneobe.common.addon.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ureca.muneobe.common.addon.dto.request.DefaultAddonCreateRequest;
import ureca.muneobe.common.addon.entity.AddonType;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.auth.utils.SessionUtil;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class DefaultAddonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    private int addonId;
    private DefaultAddonCreateRequest addonCreateRequest;

    @BeforeEach
    void setUp(){
        addonId = 1;

        addonCreateRequest = DefaultAddonCreateRequest.builder()
                .name("Sample Addon")
                .description("sample addon data")
                .price(10000)
                .addonType(AddonType.MEDIA)
                .build();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Addon 목록 조회 테스트")
    void readAddons() throws Exception {
        mockMvc.perform(get("/v1/addon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.addonsResponse.content.length()").value(8));
    }

    @Test
    @DisplayName("Addon 단일 조회 테스트")
    void readAddon() throws Exception {
        mockMvc.perform(get("/v1/addon/" + addonId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(addonId));
    }

    @Test
    @DisplayName("Addon 정상 생성 테스트")
    void createAddon() throws Exception {
        Member loginMember = memberRepository.findByName("최정민").orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionUtil.MEMBER_SESSION_KEY, loginMember);

        mockMvc.perform(post("/v1/addon")
                        .content(asJsonString(addonCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }
}
