package com.example.Social_Network.Chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@CrossOrigin("http://localhost:5173")
@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat/sendMessage/{convId}")
    public ChatMessage sendMessageToConvId(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor simpMessageHeaderAccessor,
            @DestinationVariable("convId") String conversationId
    ) {
        chatService.sendMessageToConvId(chatMessage, conversationId, simpMessageHeaderAccessor);
        return chatMessage;
    }

}
