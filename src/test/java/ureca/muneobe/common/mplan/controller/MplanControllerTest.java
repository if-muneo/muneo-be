package ureca.muneobe.common.mplan.controller;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.mplan.repository.MplanRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MplanControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Mplan 목록 조회 테스트")
    void readMplans() throws Exception {
        mockMvc.perform(get("/v1/mplan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.mplansResponse.content.length()").value(4));
    }
}
