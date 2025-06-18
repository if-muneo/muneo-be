package ureca.muneobe.common.vector.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EmbeddingGenerateResponse {
    private String answer;

    public static EmbeddingGenerateResponse from(String s){
        return EmbeddingGenerateResponse.builder()
                .answer(s)
                .build();
    }
}
