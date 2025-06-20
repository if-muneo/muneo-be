package ureca.muneobe.common.chat.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // 핸드셰이크를 수행하는 URL
                .addInterceptors(new UserHandshakeInterceptor())
                .setHandshakeHandler(new CustomHandshakeHandler())
                .setAllowedOriginPatterns("*") // 프론트엔드의 origin에 상관없이 /muneo-chat 엔드 포인트에 접근 가능
                .withSockJS()
                .setHeartbeatTime(1000); // 브라우저가 websocket을 지원하지 않거나 네트워크 환경상 websocket 연결이 불가능할 때
        // HTTP 롱폴링이나 폴백 기법으로 연결을 유지하도록 SockJS 라이브러리를 활성화
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트가 구독할 브로커 주소를 /topic으로 지정
        config.enableSimpleBroker("/topic", "/queue")
                .setHeartbeatValue(new long[]{100000000, 1000})
                .setTaskScheduler(websocketConnectScheduler());

        // 클라이언트가 stomp send 시 url 앞에 /chat을 붙여야 스프링의 @MessageMapping으로 들어온다.
        config.setApplicationDestinationPrefixes("/pub");

        config.setUserDestinationPrefix("/user");
    }

    @Bean
    public ThreadPoolTaskScheduler websocketConnectScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("웹소켓 커넥션");
        scheduler.initialize();
        return scheduler;
    }
}