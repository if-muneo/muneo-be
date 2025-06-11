package ureca.muneobe.slang;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.muneobe.common.chat.entity.Slang;

public interface SlangRepository extends JpaRepository<Slang, Long> {
    boolean existsByWord(String word);      // 금칙어 존재 여부 체크
    void deleteByWord(String word);        // 단어로 삭제
    Slang findByWord(String word);           // 단어로 찾기
    void removeByWord(String word);
}
