package ureca.muneobe.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;
import ureca.muneobe.global.response.ResponseBody;

@RestController
public class DemoController {
    public ResponseEntity<ResponseBody<String>> demo_success(){
        if("B" != "A") throw new GlobalException(ErrorCode.DEMO_ERROR);
        return ResponseEntity.ok(new ResponseBody<>("Hello World"));
    }
}
