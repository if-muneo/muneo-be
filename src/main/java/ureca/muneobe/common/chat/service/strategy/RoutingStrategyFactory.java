package ureca.muneobe.common.chat.service.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ureca.muneobe.common.chat.dto.result.FirstPromptResult;
import ureca.muneobe.common.chat.service.strategy.inappropriate.InappropriateStrategy;
import ureca.muneobe.common.chat.service.strategy.invalid.InvalidStrategy;
import ureca.muneobe.common.chat.service.strategy.rdb.RdbStrategy;
import ureca.muneobe.common.chat.service.strategy.vector.VectorStrategy;

@Component
@RequiredArgsConstructor
public class RoutingStrategyFactory {
    private final ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public RoutingStrategy select(FirstPromptResult firstPromptResult) {
        switch (firstPromptResult.getFirstPromptResponse().getRouter().toUpperCase()) {
            case "RDB" -> {
                return applicationContext.getBean("rdbStrategy", RdbStrategy.class);
            }
            case "VECTOR" -> {
                return applicationContext.getBean("vectorStrategy", VectorStrategy.class);
            }
            case "INAPPROPRIATESTRATEGY" -> {
                return applicationContext.getBean("inappropriateStrategy", InappropriateStrategy.class);
            }
            default -> {
                return applicationContext.getBean("invalidStrategy", InvalidStrategy.class);
            }
        }
    }
}
