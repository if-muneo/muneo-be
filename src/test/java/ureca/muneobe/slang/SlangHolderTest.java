package ureca.muneobe.slang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.chat.entity.Slang;
import ureca.muneobe.common.slang.SlangRepository;
import ureca.muneobe.common.slang.service.SlangHolder;

@SpringBootTest
@Transactional
public class SlangHolderTest {

    @Autowired
    SlangHolder slangHolder;

    @Autowired
    SlangRepository slangRepository;

    @Test
    @DisplayName("새로운 비속어를 포함한 rebuild가 잘 됩니다.")
    public void rebuildTest() {
        String newWord = "새로운 비속어";
        long beforeCount = slangRepository.count();
        slangRepository.save(new Slang(newWord));
        long rebuildCount = slangHolder.rebuild();

        Assertions.assertEquals(beforeCount + 1, rebuildCount);
        slangRepository.deleteByWord(newWord);
    }
}
