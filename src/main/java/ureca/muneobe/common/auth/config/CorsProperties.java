package ureca.muneobe.common.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.cors")
@Data
public class CorsProperties {
    private List<String> allowedOrigins;
    private String allowedMethods = "*";
    private String allowedHeaders = "*";
    private boolean allowCredentials = true;
    private String pathPattern = "/**";
}
