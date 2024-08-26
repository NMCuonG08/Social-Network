package com.example.Social_Network.Chat.Conversation;

import static com.example.Social_Network.DbBoiii.getConvId;

import com.example.Social_Network.Chat.ChatMessage;
import com.example.Social_Network.Chat.MessageDeliveryStatusEnum;
import com.example.Social_Network.Chat.MessageType;
import com.example.Social_Network.FriendShip.FriendShipService;
import com.example.Social_Network.Mapper.ChatMessageMapper;
import com.example.Social_Network.Security.SecurityUtils;
import com.example.Social_Network.Security.UserDetail;
import com.example.Social_Network.User.User;
import com.example.Social_Network.User.UserConnection;
import com.example.Social_Network.User.UserRepository;
import com.example.Social_Network.WebSocket.OnlineOfflineService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Slf4j
public class ConversationService {

    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final ChatMessageMapper chatMessageMapper;
    private final ConversationRepository conversationRepository;
    private final OnlineOfflineService onlineOfflineService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final FriendShipService friendShipService;
    @Autowired
    public ConversationService(UserRepository userRepository, SecurityUtils securityUtils, ChatMessageMapper chatMessageMapper, ConversationRepository conversationRepository, OnlineOfflineService onlineOfflineService, SimpMessageSendingOperations simpMessageSendingOperations,FriendShipService friendShipService) {
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
        this.chatMessageMapper = chatMessageMapper;
        this.conversationRepository = conversationRepository;
        this.onlineOfflineService = onlineOfflineService;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.friendShipService = friendShipService;
    }

    List<UserConnection> getUserFriend(){
        UserDetail userDetail = securityUtils.getUser();
        String username = userDetail.getEmail();
        UUID userId = userDetail.getId();
        List<User> users = userRepository.findAll();
        User thisUser = users.stream()
                .filter(user -> user.getEmail().equals(username))
                .findFirst()
                .orElseThrow(EntityExistsException::new);
        return users.stream()
                .filter(user -> !user.getEmail().equals(username))
                .map(
                        user ->
                                UserConnection.builder()
                                        .connectionID(user.getId())
                                        .connectionUsername(user.getEmail())
                                        .convId(getConvId(user, thisUser))
                                        .unSeen(0)
                                        .isOnline(onlineOfflineService.isUserOnline(user.getId()))
                                        .build())
                .toList();

    }
    List<UserConnection> getUserFriends(){
        UserDetail userDetail = securityUtils.getUser();
        String username = userDetail.getEmail();
        UUID userId = userDetail.getId();
        List<User> users = friendShipService.showAllFriend(userId);
        User thisUser = userRepository.findById(userId).get();
        return users.stream()
                .filter(user -> !user.getEmail().equals(username))
                .map(
                        user ->
                                UserConnection.builder()
                                        .connectionID(user.getId())
                                        .connectionUsername(
                                                user.getUserName() != null && !user.getUserName().isEmpty()
                                                        ? user.getUserName()
                                                        : user.getEmail()
                                        )
                                        .convId(getConvId(user, thisUser))
                                        .unSeen(0)
                                        .isOnline(onlineOfflineService.isUserOnline(user.getId()))
                                        .build())
                .toList();

    }
    public UserConnection getUserAndFriend(UUID friendId) {
        UserDetail userDetail = securityUtils.getUser();
        String email = userDetail.getEmail();
        User friend = userRepository.findById(friendId).get();
        User user = userRepository.findByEmail(email).get();
        return UserConnection.builder()
                .connectionID(user.getId())
                .connectionUsername(email)
                .convId(getConvId(user,  friend))
                .unSeen(0)
                .isOnline(onlineOfflineService.isUserOnline(user.getId()))
                .build();
    }

    public List<UnseenMessageCountResponse> getUnseenMessageCount() {
        List<UnseenMessageCountResponse> result = new ArrayList<>();
        UserDetail userDetails = securityUtils.getUser();
        List<ConversationEntity> unseenMessages =
                conversationRepository.findUnseenMessagesCount(userDetails.getId());

        if (!CollectionUtils.isEmpty(unseenMessages)) {
            Map<UUID, List<ConversationEntity>> unseenMessageCountByUser = new HashMap<>();
            for (ConversationEntity entity : unseenMessages) {
                List<ConversationEntity> values =
                        unseenMessageCountByUser.getOrDefault(entity.getFromUser(), new ArrayList<>());
                values.add(entity);
                unseenMessageCountByUser.put(entity.getFromUser(), values);
            }
            log.info("there are some unseen messages for {}", userDetails.getUsername());
            unseenMessageCountByUser.forEach(
                    (user, entities) -> {
                        result.add(
                                UnseenMessageCountResponse.builder()
                                        .count((long) entities.size())
                                        .fromUser(user)
                                        .build());
                        updateMessageDelivery(user, entities, MessageDeliveryStatusEnum.DELIVERED);
                    });
        }
        return result;
    }

    public List<ChatMessage> getUnseenMessages(UUID fromUserId) {
        List<ChatMessage> result = new ArrayList<>();
        UserDetail userDetails = securityUtils.getUser();
        List<ConversationEntity> unseenMessages =
                conversationRepository.findUnseenMessages(userDetails.getId(), fromUserId);

        if (!CollectionUtils.isEmpty(unseenMessages)) {
            log.info(
                    "there are some unseen messages for {} from {}", userDetails.getUsername(), fromUserId);
            updateMessageDelivery(fromUserId, unseenMessages, MessageDeliveryStatusEnum.SEEN);
            result = chatMessageMapper.toChatMessage(unseenMessages, userDetails, MessageType.UNSEEN);
        }
        return result;
    }

    private void updateMessageDelivery(
            UUID user,
            List<ConversationEntity> entities,
            MessageDeliveryStatusEnum messageDeliveryStatusEnum) {
        entities.forEach(e -> e.setDeliveryStatus(messageDeliveryStatusEnum.toString()));
        onlineOfflineService.notifySender(user, entities, messageDeliveryStatusEnum);
        conversationRepository.saveAll(entities);
    }

    public List<ChatMessage> setReadMessages(List<ChatMessage> chatMessages) {
        List<UUID> inTransitMessageIds = chatMessages.stream().map(ChatMessage::getId).toList();
        List<ConversationEntity> conversationEntities =
                conversationRepository.findAllById(inTransitMessageIds);
        conversationEntities.forEach(
                message -> message.setDeliveryStatus(MessageDeliveryStatusEnum.SEEN.toString()));
        List<ConversationEntity> saved = conversationRepository.saveAll(conversationEntities);

        return chatMessageMapper.toChatMessage(saved, securityUtils.getUser(), MessageType.CHAT);
    }

}
