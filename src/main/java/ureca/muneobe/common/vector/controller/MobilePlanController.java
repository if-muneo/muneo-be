package ureca.muneobe.common.vector.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ureca.muneobe.common.vector.dto.request.RecommendationRequest;
import ureca.muneobe.common.vector.dto.response.EmbeddingGenerateResponse;
import ureca.muneobe.common.vector.dto.response.VectorSearchResponse;
import ureca.muneobe.common.vector.service.FatService;
import ureca.muneobe.global.response.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MobilePlanController {

    private final FatService fatService;

    @PostMapping("/v1/vector")
    public ResponseEntity<ResponseBody<VectorSearchResponse>> searchByText(@RequestBody RecommendationRequest request) {
        List<String> query = new ArrayList<>();
        query.add(request.getQuery());
        return ResponseEntity.ok().body(
                ResponseBody.success(fatService.search(query)));
    }

    /**
     * POST plans/generate
     *
     * embedding 컬럼이 null인 요금제에 대해 OpenAI API로 임베딩 생성 후 저장합니다.
     */
    @PostMapping("/v1/embedding/generate")
    public ResponseEntity<ResponseBody<EmbeddingGenerateResponse>> generateEmbeddings() {
        return ResponseEntity.ok().body(
                    ResponseBody.success(fatService.generateAndSaveAllNullEmbeddings()));
    }

    /**
     * POST plans/update/{id}
     *
     * update 요청이 들어온 요금제 id에 대해 OpenAI API로 임베딩 생성 후 저장합니다.
     */
    @PatchMapping("/v1/embedding/generate/{id}")
    public ResponseEntity<ResponseBody<EmbeddingGenerateResponse>> generateUpdateEmbeddings(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                ResponseBody.success(fatService.generateAndSaveAllUpdateEmbeddings(id)));
    }
}

