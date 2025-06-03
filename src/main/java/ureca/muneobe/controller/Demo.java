package ureca.muneobe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ureca.muneobe.service.DemoService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class Demo {

    private final DemoService demoService;

    @PostMapping("/hello")
    public ResponseEntity<String> generate(@RequestBody Map<String, String> request) {
        String userInput = request.get("message");
        String response = demoService.generate(userInput);
        return ResponseEntity.ok(response);
    }
}
