package com.example.Social_Network.Chat.Conversation;

import com.example.Social_Network.Chat.ChatMessage;
import com.example.Social_Network.User.UserConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin("http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conversation")
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping("/friends")
    public List<UserConnection> getUserFriends(){
        return conversationService.getUserFriend();
    }
    @GetMapping("/unseenMessages")
    public List<UnseenMessageCountResponse> getUnseenMessageCount(){
        return conversationService.getUnseenMessageCount();
    }
    @GetMapping("/unseenMessages/{fromUserId}")
    public List<ChatMessage> getUnseenMessage(@PathVariable UUID fromUserId){
        return conversationService.getUnseenMessages(fromUserId);
    }
    @PutMapping("/setReadMessages")
    public List<ChatMessage> readMessage(@RequestBody List<ChatMessage> chatMessages ){
        return conversationService.setReadMessages(chatMessages);
    }





}
