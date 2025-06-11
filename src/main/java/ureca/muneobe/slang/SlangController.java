package ureca.muneobe.slang;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ureca.muneobe.slang.service.SlangService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/slang")
public class SlangController {

    private final SlangService slangService;
}
