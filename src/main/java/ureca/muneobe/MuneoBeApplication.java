package ureca.muneobe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry        //retry 로직 : Mplan 추가
@EnableScheduling
@SpringBootApplication
public class MuneoBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MuneoBeApplication.class, args);
    }

}
