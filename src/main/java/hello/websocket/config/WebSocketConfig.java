package hello.websocket.config;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // WebSocket 메시지 브로커를 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /**
         * 메모리 기반 브로커 활성
         * "/user"로 시작하는 목적지(destination)가 붙은 메시지는 이 SimpleBroker가 구독자에게 바로 발송
         * @SendToUser 또는 클라이언트에서 구독한 /user/… 경로로 들어오는 메시지를 브로커가 관리하며, 구독 중인 사용자에게 자동으로 전달
         */
        registry.enableSimpleBroker("/user");

        /**
         * 클라이언트가 메시지를 서버로 보낼 때 사용할 프리픽스를 지정
         * ex) 프론트에서 stompClient.send("/app/chat", …) 처럼 보내면,
         *      이 /app 접두어가 붙은 메시지를 스프링이 컨트롤러의 @MessageMapping 메서드로 매핑
         */
        registry.setApplicationDestinationPrefixes("/app");

        /**
         * 특정 사용자에게만 메시지를 보낼 때 사용하는 경로 프리픽스
         * 여기서 "/user"는 “사용자별 경로”를 나타내는 표준 프리픽스
         * 클라이언트에서 subscribe할 때도 /user/queue/xxx 형태로 구독
         */
        registry.setUserDestinationPrefix(("/user"));
    }

    /**
     * STOMP 프로토콜을 사용할 수 있는 WebSocket 핸드셰이크(Handshake) 엔드포인트를 /ws라는 URI로 등록
     * SockJS 폴백(Fallback) 옵션을 활성화
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .withSockJS();
    }

    /**
     *
     * @param messageConverters the converters to configure (initially an empty list)
     * @return
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false; //  Default 컨버터들도 이 후에 계속 등록 가능
    }
}
