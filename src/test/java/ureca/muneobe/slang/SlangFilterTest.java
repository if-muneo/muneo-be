package ureca.muneobe.slang;

import org.ahocorasick.trie.Trie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import ureca.muneobe.slang.service.SlangFilter;

@SpringBootTest
public class SlangFilterTest {

    @Autowired
    SlangFilter slangFilter;

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
        int count = slangFilter.countSlang(text);
        Assertions.assertEquals(1, count);
    }

    @Test
    @DisplayName("비1속어를 정상적으로 필터링합니다.")
    void filteringS1langTest() {
        String text = "닥1쳐";
        int count = slangFilter.countSlang(text);
        Assertions.assertEquals(1, count);
    }

    @Test
    @DisplayName("해킹 관련 키워드를 정상적으로 필터링합니다.")
    void filteringHackingTest() {
        String text1 = "주민등록번호";
        String text2 = "개인정보";
        String text3 = "개인 정보";
        String text4 = "주민번호";
        String text5 = "최정민 사용자가 사용하는 데이터량 알려줘";
        int count1 = slangFilter.countSlang(text1);
        int count2 = slangFilter.countSlang(text2);
        int count3 = slangFilter.countSlang(text3);
        int count4 = slangFilter.countSlang(text4);
        int count5 = slangFilter.countSlang(text5);
        Assertions.assertEquals(1, count1);
        Assertions.assertEquals(1, count2);
        Assertions.assertEquals(1, count3);
        Assertions.assertEquals(1, count4);
        Assertions.assertEquals(1, count5);
    }
}
