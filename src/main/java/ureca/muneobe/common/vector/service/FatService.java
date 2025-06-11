package ureca.muneobe.common.vector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.vector.embedding.EmbeddingSentence;
import ureca.muneobe.common.vector.entity.Fat;
import ureca.muneobe.common.vector.repository.FatJdbcRepository;
import ureca.muneobe.common.vector.repository.FatRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FatService {

    private final FatRepository fatRepository;
    private final FatJdbcRepository fatJdbcRepository;
    private final EmbeddingSentence embeddingSentence;

    @Transactional
    public void generateAndSaveAllNullEmbeddings() throws IOException, InterruptedException {
        List<Fat> fats = fatRepository.findByEmbeddingFalse();


        for (Fat fat : fats) {
            List<String> texts = fat.makeDisriptionForEmbedding();

            for(String text : texts) {
                float[] embeddingVector = embeddingSentence.requestEmbeddingFromOpenAI(text);
                fatJdbcRepository.insertEmbedding(fat.getId(), embeddingVector);
            }
        }
    }


    public List<Fat> search(String userQuery) {
        try {
            // 1. 쿼리를 임베딩 벡터로 변환
            float[] queryVector = embeddingSentence.requestEmbeddingFromOpenAI(userQuery);

            // 2. pgvector 유사도 기반 검색 수행
            return fatJdbcRepository.findSimilarPlans(queryVector, 3); // 상위 3개 결과 반환
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("요금제 검색 중 오류 발생", e);
        }
    }
}
