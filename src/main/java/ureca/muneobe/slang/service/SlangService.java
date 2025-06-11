package ureca.muneobe.slang.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.chat.entity.Slang;
import ureca.muneobe.slang.SlangRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SlangService {

    private final SlangHolder slangHolder;
    private final SlangRepository slangRepository;

    public void resetSlang() {
        slangHolder.rebuild();
    }

    @Transactional
    public void addSlang(String word) {
        Slang findWord = slangRepository.findByWord(word);
        if (findWord != null) {
            log.info("{}는 이미 등록된 금칙어입니다.", word);
            return;
        }

        slangRepository.save(new Slang(word));
    }

    public void addSlang(List<String> words) {
        words.forEach(this::addSlang);
    }

    @Transactional
    public void removeSlang(String word) {
        slangRepository.removeByWord(word);
    }

    public void removeSlang(List<String> words) {
        words.forEach(this::removeSlang);
    }
}
