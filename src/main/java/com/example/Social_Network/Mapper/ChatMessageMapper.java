package com.example.Social_Network.Mapper;

import com.example.Social_Network.Chat.ChatMessage;
import com.example.Social_Network.Chat.Conversation.ConversationEntity;
import com.example.Social_Network.Chat.MessageType;
import com.example.Social_Network.Security.UserDetail;
import com.example.Social_Network.User.User;
import com.example.Social_Network.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatMessageMapper {

    private final UserRepository userRepository;


    public List<ChatMessage> toChatMessage(
            List<ConversationEntity> conversationEntities,
            UserDetail userDetail,
            MessageType messageType
    ){
        List<UUID> fromUserIds =
                conversationEntities.stream().map(ConversationEntity::getFromUser).toList();
        Map<UUID, String> formUserIdsToUserEmail  =
                userRepository.findAllById(fromUserIds).stream()
                        .collect(Collectors.toMap(User::getId, User::getEmail));
        return conversationEntities.stream().map(
                e -> toChatMessage(e, userDetail, formUserIdsToUserEmail, messageType)
        ).toList();
    }

    private static ChatMessage toChatMessage(
            ConversationEntity e,
            UserDetail userDetail,
            Map<UUID, String> formUserIdsToUserEmail,
            MessageType messageType
    ){
        return ChatMessage.builder()
                .id(e.getId())
                .messageType(messageType)
                .content(e.getContent())
                .receiverID(e.getToUser())
                .receiverUsername(userDetail.getEmail())
                .senderID(e.getFromUser())
                .senderEmail(formUserIdsToUserEmail.get(e.getFromUser()))
                .build();
    }



}
