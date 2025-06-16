package ureca.muneobe.slang;

import org.ahocorasick.trie.Trie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import ureca.muneobe.slang.service.SlangFilterService;

@SpringBootTest
@ActiveProfiles("test")
public class SlangFilterServiceTest {

    @Autowired
    SlangFilterService slangFilterService;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("Trie 빈이 정상적으로 등록됩니다.")
    void trieBeanTest() {
        // 빈 등록 확인
        Assertions.assertTrue(applicationContext.containsBean("trie"));
        // 빈 등록 확인
        Assertions.assertNotNull(applicationContext.getBean(Trie.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "닥쳐",
            "미친",
    })
    @DisplayName("비속어를 정상적으로 필터링합니다.")
    void filteringSlangTest(String context) {
        boolean result = slangFilterService.filteringSlang(context);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "닥쳐",
            "미친",
    })
    @DisplayName("비속어를 정상적으로 필터링합니다.")
    void noFilteringTest(String context) {
        boolean result = slangFilterService.filteringSlang(context);
        Assertions.assertFalse(result);
    }
}
