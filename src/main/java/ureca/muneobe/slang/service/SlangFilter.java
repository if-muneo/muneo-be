package ureca.muneobe.slang.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SlangFilter {

    private final SlangHolder slangHolder;

    public int countSlang(String text) {
        return slangHolder.countSlang(text);
    }

    public boolean isSafe(String text) {
        return slangHolder.countSlang(text) == 0;
    }
}
