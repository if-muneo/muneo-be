package ureca.muneobe.slang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ureca.muneobe.common.slang.service.SlangFilterService;

@SpringBootTest
public class SlangFilterServiceTest {

    @Autowired
    SlangFilterService slangFilterService;

    @ParameterizedTest
    @ValueSource(strings = {
            "닥쳐",
            "씨발"
    })
    void filteringTest(String slang) {
        boolean filtered = slangFilterService.filteringSlang(slang);
        Assertions.assertTrue(filtered);
    }
}
