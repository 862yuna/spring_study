package com.gn.mvc.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket 
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer{

	private final BasicWebSocketHandler basicWebSocketHandler; // 바깥쪽에 등록된 basicWebSocketHandler를 여기서 사용 가능
	private final ChatWebSocketHandler chatWebSocketHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(basicWebSocketHandler, "/ws/basic")
		.setAllowedOrigins("http://localhost:8080"); // 통신하고 있는 도메인을 뜻한다.
		
		registry.addHandler(chatWebSocketHandler, "/ws/chat")
		.setAllowedOrigins("http://localhost:8080");
	}

}
