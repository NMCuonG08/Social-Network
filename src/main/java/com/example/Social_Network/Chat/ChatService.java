package com.example.Social_Network.Chat;

import com.example.Social_Network.Chat.Conversation.ConversationEntity;
import com.example.Social_Network.Chat.Conversation.ConversationRepository;
import com.example.Social_Network.Security.UserDetail;
import com.example.Social_Network.User.User;
import com.example.Social_Network.WebSocket.OnlineOfflineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j

public class ChatService {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private final ConversationRepository conversationRepository;

    private final OnlineOfflineService onlineOfflineService;


    public void sendMessageToConvId(
            ChatMessage chatMessage,
            String conversationId,
            SimpMessageHeaderAccessor headerAccessor
    ){
        UserDetail userDetail = getUser();
        UUID fromUserId = userDetail.getId();
        UUID toUserId = chatMessage.getReceiverID();
        populateContext(chatMessage, userDetail);

        boolean isTargetOnline = onlineOfflineService.isUserOnline(toUserId);
        boolean isTargetSubscribed =
                onlineOfflineService.isUserSubscribed(toUserId, "/topic/" + conversationId);
        chatMessage.setId(UUID.randomUUID());

        ConversationEntity.ConversationEntityBuilder conversationEntityBuilder = ConversationEntity.builder();

        conversationEntityBuilder
                .id(chatMessage.getId())
                .fromUser(fromUserId)
                .toUser(toUserId)
                .content(chatMessage.getContent())
                .convId(conversationId)
                .time(Timestamp.valueOf(LocalDate.now().atStartOfDay()));

        if(!isTargetOnline){
            log.info("{} is not online. Context saved in unseen message! " , chatMessage.getSenderEmail() );
            conversationEntityBuilder.deliveryStatus(MessageDeliveryStatusEnum.NOT_DELIVERED.toString());
            chatMessage.setMessageDeliveryStatusEnum(MessageDeliveryStatusEnum.NOT_DELIVERED);
        }
        else if(!isTargetSubscribed){
            log.info("{} is online but not subscribed. sending to their private subscription",chatMessage.getReceiverUsername());
            conversationEntityBuilder.deliveryStatus(MessageDeliveryStatusEnum.DELIVERED.toString());
            chatMessage.setMessageDeliveryStatusEnum(MessageDeliveryStatusEnum.DELIVERED);
            simpMessageSendingOperations.convertAndSend("/topic/ " + toUserId.toString(), chatMessage);
        }
        else {
            conversationEntityBuilder.deliveryStatus(MessageDeliveryStatusEnum.SEEN.toString());
            chatMessage.setMessageDeliveryStatusEnum(MessageDeliveryStatusEnum.SEEN);

        }
        conversationRepository.save(conversationEntityBuilder.build());
        simpMessageSendingOperations.convertAndSend("/topic/" + conversationId , chatMessage);

    }

    private void populateContext(ChatMessage chatMessage, UserDetail userDetails) {
        chatMessage.setSenderEmail(userDetails.getEmail());
        chatMessage.setSenderID(userDetails.getId());
    }

    public UserDetail getUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (UserDetail) object;
    }


}
