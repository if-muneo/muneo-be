package ureca.muneobe.common.addongroup.controller;
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ureca.muneobe.common.addon.entity.Addon;
import ureca.muneobe.common.addon.entity.AddonType;
import ureca.muneobe.common.addongroup.dto.request.AddonCreateRequest;
import ureca.muneobe.common.addongroup.dto.request.AddonGroupCreateRequest;
import ureca.muneobe.common.addongroup.entity.AddonGroup;
import ureca.muneobe.common.addongroup.repository.AddonGroupRepository;
import ureca.muneobe.common.auth.entity.Member;
import ureca.muneobe.common.auth.respository.MemberRepository;
import ureca.muneobe.common.auth.utils.SessionUtil;
import ureca.muneobe.common.chat.repository.AddonRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AddonGroupControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AddonRepository addonRepository;

    @Autowired
    private AddonGroupRepository addonGroupRepository;

    private String addonGroupName;
    private Long[] defaultAddonIdList;
    private Member loginMember;

    @BeforeEach
    void setUp(){
        addonGroupName = "테스트용 addon 그룹";
        //defaultAddonIdList = new Long[]{1L, 2L, 4L, 11L};
        defaultAddonIdList = new Long[]{1L};
        //loginMember = memberRepository.findByName("최정민").orElseThrow(() -> new NoSuchElementException("Admin 계정을 찾지 못했습니다."));

        addonGroupRepository.save(AddonGroup.builder()
                .addonGroupName("테스트용 addon group")
                .build());

        Addon addon = Addon.builder()
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

    private AddonGroupCreateRequest createAddonGroupRequest() {
        List<AddonCreateRequest> addonCreateRequests = Arrays.stream(defaultAddonIdList)
                .map(id -> AddonCreateRequest.builder()
                        .id(id)
                        .build())
                .collect(Collectors.toList());

        return AddonGroupCreateRequest.builder()
                .name(addonGroupName)
                .addonsCreateRequest(addonCreateRequests)
                .build();
    }

    private void verifyEachCreated(List<Addon> addonList, Long groupId) {
        assertEquals(defaultAddonIdList.length, addonList.size());

        for(Addon addon : addonList){
            assertEquals(addon.getAddonGroup().getId(), groupId);
        }
    }

    @Test
    @DisplayName("Addon-group 목록 조회 테스트")
    void readAddons() throws Exception {
        mockMvc.perform(get("/v1/addon-group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.addonGroupsResponse.content.length()").value(1));
                //.andExpect(jsonPath("$.data.addonGroupsResponse.content.length()").value(8));
    }

//    @Test
//    @DisplayName("addon-group 생성 테스트")
//    void createAddonGroup() throws Exception {
//        AddonGroupCreateRequest request = createAddonGroupRequest();
//
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionUtil.MEMBER_SESSION_KEY, loginMember);
//
//        MvcResult mvcResult = mockMvc.perform(post("/v1/addon-group")
//                        .content(asJsonString(request))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .session(session)
//                )
//                .andExpect(status().isOk())
//                .andReturn();
//
//        int id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.data.id");
//
//        Optional<AddonGroup> optionalAddonGroup =  addonGroupRepository.findById((long) id);
//        assertTrue(optionalAddonGroup.isPresent());
//        AddonGroup addonGroup = optionalAddonGroup.get();
//
//        verifyEachCreated(addonRepository.findAddonsByAddonGroup(addonGroup), (long) id);
//    }
//
//    @Test
//    @DisplayName("Addon-group 단일 조회 테스트")
//    void readAddonGroupAddons() throws Exception {
//        int addonGroupId = 1;
//        MvcResult mvcResult = mockMvc.perform(get("/v1/addon-group/"+addonGroupId))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseJson = mvcResult.getResponse().getContentAsString();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode addonsArray = objectMapper.readTree(responseJson).at("/data/addonGroupAddonsResponse");
//
//        for(JsonNode addonNode : addonsArray){
//            long addonId = addonNode.get("id").asLong();
//
//            Addon addon = addonRepository.findById(addonId).orElseThrow(() -> new NoSuchElementException("addon이 정상적으로 생성되지 않았습니다."));
//            assertEquals(addonGroupId, addon.getAddonGroup().getId());
//        }
//    }
}
