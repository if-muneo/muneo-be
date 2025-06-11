package ureca.muneobe.slang.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SlangService {

    private final SlangHolder slangHolder;

    public void resetSlang() {
        slangHolder.rebuild();
    }
}
