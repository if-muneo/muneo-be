package ureca.muneobe.common.vector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.vector.dto.response.EmbeddingGenerateResponse;
import ureca.muneobe.common.vector.dto.response.VectorSearchResponse;
import ureca.muneobe.common.vector.embedding.EmbeddingSentence;
import ureca.muneobe.common.vector.entity.Fat;
import ureca.muneobe.common.vector.entity.FatEmbedding;
import ureca.muneobe.common.vector.repository.FatEmbeddingRepository;
import ureca.muneobe.common.vector.repository.FatJdbcRepository;
import ureca.muneobe.common.vector.repository.FatRepository;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FatService {

    private final FatRepository fatRepository;
    private final FatJdbcRepository fatJdbcRepository;
    private final EmbeddingSentence embeddingSentence;
    private final FatEmbeddingRepository fatEmbeddingRepository;

    @Transactional
    public EmbeddingGenerateResponse generateAndSaveAllNullEmbeddings() {
        List<Fat> fats = fatRepository.findByEmbeddingFalse();

        for (Fat fat : fats) {
            List<String> texts = fat.makeDisriptionForEmbedding();

            try {
                for(String text : texts) {
                    float[] embeddingVector = embeddingSentence.requestEmbeddingFromOpenAI(text);
                    fatJdbcRepository.insertEmbedding(fat.getId(), embeddingVector);
                }

                fat.setEmbedding(true);
                fatRepository.save(fat);
            } catch (Exception e) {
                throw  new GlobalException(ErrorCode.EMBEDDING_FAILED);
            }
        }

        return EmbeddingGenerateResponse.from("embedding 생성 완료");
    }

    @Transactional
    public EmbeddingGenerateResponse generateAndSaveAllUpdateEmbeddings(Long fat_id) {

        Fat fat = fatRepository.findById(fat_id).orElseThrow(() -> new GlobalException(ErrorCode.EMBEDDING_FAILED));

        for(FatEmbedding embedding : fatEmbeddingRepository.findByFatId(fat_id)) fatEmbeddingRepository.deleteById(embedding.getId());

        List<String> texts = fat.makeDisriptionForEmbedding();

        try {
            for(String text : texts) {
                float[] embeddingVector = embeddingSentence.requestEmbeddingFromOpenAI(text);
                fatJdbcRepository.insertEmbedding(fat.getId(), embeddingVector);
            }

            fat.setEmbedding(true);
            fatRepository.save(fat);

        } catch (Exception e) {
            throw new GlobalException(ErrorCode.EMBEDDING_FAILED);
        }

        return EmbeddingGenerateResponse.from("embedding 생성 완료");
    }


    public VectorSearchResponse search(String userQuery) {
        try {
            float[] queryVector = embeddingSentence.requestEmbeddingFromOpenAI(userQuery);

            List<Fat> fatlist = fatJdbcRepository.findSimilarPlans(queryVector, 3);

            List<String> answer = new ArrayList<>();
            for(Fat f : fatlist){
                answer.add(f.makeDiscriptionForResponse());
            }

            return VectorSearchResponse.from(answer);

        } catch (IOException | InterruptedException e) {
            throw new GlobalException(ErrorCode.VECTOR_SEARCH_FAILED);
        }
    }
}
