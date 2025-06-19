package ureca.muneobe.common.mplan.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.addon.entity.Addon;
import ureca.muneobe.common.addon.entity.AddonType;
import ureca.muneobe.common.addongroup.entity.AddonGroup;
import ureca.muneobe.common.addongroup.repository.AddonGroupRepository;
import ureca.muneobe.common.chat.repository.AddonRepository;
import ureca.muneobe.common.mplan.entity.*;
import ureca.muneobe.common.mplan.repository.MplanDetailRepository;
import ureca.muneobe.common.mplan.repository.MplanRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MplanControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MplanRepository mplanRepository;

    @Autowired
    private MplanDetailRepository mplanDetailRepository;

    @Autowired
    private AddonGroupRepository addonGroupRepository;

    @Autowired
    private AddonRepository addonRepository;

    @BeforeEach
    void SetUp(){
        MplanDetail mplanDetail = MplanDetail.builder()
                .basicDataAmount(10000000)
                .sharingData(10000)
                .monthlyPrice(33000)
                .voiceCallVolume(0)
                .textMessage(true)
                .subDataSpeed(5000)
                .qualification(Qualification.ALL)
                .mplanType(MplanType.LTE_5G)
                .dataType(DataType._5G)
                .build();

        Addon addon = Addon.builder()
                .name("Sample Addon")
                .description("sample addon data")
                .price(10000)
                .addonType(AddonType.MEDIA)
                .build();

        addonRepository.save(addon);

        AddonGroup addonGroup = AddonGroup.builder()
                .addonGroupName("addon group 1")
                .addons(List.of(addon))
                .build();

        addonGroupRepository.save(addonGroup);

        mplanDetailRepository.save(mplanDetail);

        mplanRepository.save(Mplan.builder()
                .name("5G 프리미어 에센셜")
                .mplanDetail(mplanDetail)
                .addonGroup(addonGroup)
                .build());
    }

    @Test
    @DisplayName("Mplan 목록 조회 테스트")
    void readMplans() throws Exception {
        mockMvc.perform(get("/v1/mplan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.mplansResponse.content.length()").value(1));
//                .andExpect(jsonPath("$.data.mplansResponse.content.length()").value(4));
    }
}
