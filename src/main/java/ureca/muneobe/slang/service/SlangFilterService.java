package ureca.muneobe.slang.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class SlangFilterService {

    private final SlangHolder slangHolder;

    public int countSlang(String text) {
        return slangHolder.countSlang(text);
    }

    public boolean isSafe(String text) {
        return slangHolder.countSlang(text) == 0;
    }
}
