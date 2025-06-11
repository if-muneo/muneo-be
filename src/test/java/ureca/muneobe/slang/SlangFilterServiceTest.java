package ureca.muneobe.slang;

import org.ahocorasick.trie.Trie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import ureca.muneobe.slang.service.SlangFilterService;

@SpringBootTest
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

    @Test
    @DisplayName("비속어를 정상적으로 필터링합니다.")
    void filteringSlangTest() {
        String text = "닥쳐";
        int count = slangFilterService.countSlang(text);
        Assertions.assertEquals(1, count);
    }
}
