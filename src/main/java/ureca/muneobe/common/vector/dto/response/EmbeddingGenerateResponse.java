package ureca.muneobe.common.vector.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingGenerateResponse {
    private String answer;

    public static EmbeddingGenerateResponse from(String s){
        return EmbeddingGenerateResponse.builder()
                .answer(s)
                .build();
    }
}
