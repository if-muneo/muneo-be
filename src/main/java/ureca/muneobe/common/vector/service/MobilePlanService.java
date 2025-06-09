package ureca.muneobe.common.vector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.vector.embedding.EmbeddingSentence;
import ureca.muneobe.common.vector.entity.MobilePlan;
import ureca.muneobe.common.vector.repository.MobilePlanJdbcRepository;
import ureca.muneobe.common.vector.repository.MobilePlanRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MobilePlanService {

    private final MobilePlanRepository mobilePlanRepository;
    private final MobilePlanJdbcRepository mobilePlanJdbcRepository;
    private final EmbeddingSentence embeddingSentence;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public void generateAndSaveAllNullEmbeddings() throws IOException, InterruptedException {
        List<MobilePlan> plans = mobilePlanRepository.findByEmbeddingIsNull();


        for (MobilePlan mobilePlan : plans) {
            String combinedText = mobilePlan.buildCombinedText();
            float[] embeddingVector = embeddingSentence.requestEmbeddingFromOpenAI(combinedText);

            mobilePlanJdbcRepository.insertEmbedding(mobilePlan.getId(), embeddingVector);
        }
    }


    public List<MobilePlan> search(String userQuery) {
        try {
            // 1. 쿼리를 임베딩 벡터로 변환
            float[] queryVector = embeddingSentence.requestEmbeddingFromOpenAI(userQuery);

            // 2. pgvector 유사도 기반 검색 수행
            return mobilePlanJdbcRepository.findSimilarPlans(queryVector, 3); // 상위 3개 결과 반환
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("요금제 검색 중 오류 발생", e);
        }
    }
}
