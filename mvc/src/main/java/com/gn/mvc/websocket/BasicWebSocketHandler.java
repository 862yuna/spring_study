package com.gn.mvc.websocket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.NoArgsConstructor;


@Component
@NoArgsConstructor
public class BasicWebSocketHandler extends TextWebSocketHandler{
	
	private static final List<WebSocketSession> sessionList 
		= new ArrayList<WebSocketSession>(); 

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 새로운 WebSocket 연결된 순간 동작하는 메소드
		// System.out.println("서버 : 연결");
		sessionList.add(session); // 지금 연결된 서버의 정보
 		
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 클라이언트 -> 서버 메시지(send)한 순간 동작하는 메소드
		String payload = message.getPayload(); // 서버 입장에서 받은 메세지를 꺼내옴
		System.out.println("서버 : 메시지 받음 : "+payload);
		
		for(WebSocketSession temp : sessionList) {
			temp.sendMessage(new TextMessage(payload));
		}
		// session.sendMessage(new TextMessage(payload)); // payload는 클라이언트가 서버한테 보낸거 
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// WebSocket 연결 끊겼을 때 동작하는 메소드 // 여기서 session은
		// System.out.println("연결 끊김");
		sessionList.remove(session);
		
	}

	
}
