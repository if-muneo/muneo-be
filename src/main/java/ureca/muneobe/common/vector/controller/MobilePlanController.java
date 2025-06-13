package ureca.muneobe.common.vector.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ureca.muneobe.common.vector.dto.RecommendationRequest;
import ureca.muneobe.common.vector.entity.Fat;
import ureca.muneobe.common.vector.service.FatService;

import java.util.List;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class MobilePlanController {

    private final FatService fatService;

    @PostMapping("/search")
    public ResponseEntity<List<Fat>> searchByText(@RequestBody RecommendationRequest request) {
        String userQuery = request.getQuery();
        List<Fat> similarPlans = fatService.search(userQuery);
        return ResponseEntity.ok(similarPlans);
    }

    /**
     * POST plans/generate
     *
     * embedding 컬럼이 null인 요금제에 대해 OpenAI API로 임베딩 생성 후 저장합니다.
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateEmbeddings() {
        try {
            fatService.generateAndSaveAllNullEmbeddings();
            return ResponseEntity.ok("embedding 생성 완료");
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body("임베딩 생성 실패: " + e.getMessage());
        }
    }

    /**
     * POST plans/update/{id}
     *
     * update 요청이 들어온 요금제 id에 대해 OpenAI API로 임베딩 생성 후 저장합니다.
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<String> generateUpdateEmbeddings(@PathVariable Long id) {
        try {
            fatService.generateAndSaveAllUpdateEmbeddings(id);
            return ResponseEntity.ok("embedding 생성 완료");
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body("임베딩 생성 실패: " + e.getMessage());
        }
    }
}

