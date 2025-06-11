package ureca.muneobe.slang.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.springframework.stereotype.Service;
import ureca.muneobe.slang.SlangRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SlangHolder {

    private final SlangRepository slangRepository;

    private volatile Trie trie;
    private Trie.TrieBuilder trieBuilder;

    public int countSlang(String text) {
        Collection<Emit> emits = trie.parseText(text);
        return emits.size();
    }

    public void reset() {
        this.trie = trieBuilder.build();
    }

    public void addSlang(String text) {
        // trie에 text가 존재하는지 확인
        boolean contain = trie.containsMatch(text);
        if (contain) {
            log.info("{}는 이미 비속어로 등록되어 있습니다.", text);
            return;
        }

        this.trieBuilder.addKeyword(text);
    }

    public void deleteSlang(String text) {

    }

    public void addSlang(List<String> texts) {
        for (String text : texts) {
            addSlang(text);
        }
    }

    public void deleteSlang(List<String> texts) {

    }

    public boolean contain(String text) {
//        trie.
        return false;
    }
}
