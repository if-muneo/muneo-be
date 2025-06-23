package ureca.muneobe.common.vector.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorSearchResponse {
    private List<String> descriptions;

    public static VectorSearchResponse from(List<String> s){
        return VectorSearchResponse.builder()
                .descriptions(s)
                .build();
    }
}