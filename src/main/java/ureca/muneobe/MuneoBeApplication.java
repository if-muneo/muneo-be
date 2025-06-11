package ureca.muneobe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MuneoBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MuneoBeApplication.class, args);
    }

}
