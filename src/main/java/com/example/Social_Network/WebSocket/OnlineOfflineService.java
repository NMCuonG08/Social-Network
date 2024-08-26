package com.example.Social_Network.WebSocket;

import com.example.Social_Network.Chat.*;
import com.example.Social_Network.Chat.Conversation.ConversationEntity;
import com.example.Social_Network.Security.UserDetail;
import com.example.Social_Network.User.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class OnlineOfflineService {

    private final Set<UUID> onlineUsers;
    private final Map<UUID, Set<String>> userSubscribed;
    private final UserRepository userRepository;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final UserService userService;


    @Autowired
    public OnlineOfflineService(
            UserRepository userRepository, SimpMessageSendingOperations simpMessageSendingOperations, UserService userService) {
        this.userService = userService;
        this.onlineUsers = new ConcurrentSkipListSet<>();
        this.userSubscribed = new ConcurrentHashMap<>();

        this.userRepository = userRepository;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    public void addOnlineUser(Principal user){
        if(user != null ) {
            UserDetail userDetails = getUserDetails(user);
            if (userDetails != null && userDetails.getId() != null) {
                log.info("{} went offline", userDetails.getEmail());
                onlineUsers.remove(userDetails.getId());
                userSubscribed.remove(userDetails.getId());
                for (UUID id : onlineUsers){
                    simpMessageSendingOperations.convertAndSend(
                            "/topic" + id,
                            ChatMessage.builder().
                                    messageType(MessageType.FRIEND_OFFLINE)
                                    .userConnection(UserConnection.builder().connectionID(userDetails.getId()).build())
                                    .build());
                }
            } else {
                log.error("UserDetails or User ID is null for Principal: {}", user);
            }
        } else {
            log.error("Principal is null");
        }
    }



    public boolean isUserOnline(UUID userId) {
        return onlineUsers.contains(userId);
    }

    public void removeOnlineUser(Principal user) {
        if (user != null) {
            UserDetail userDetails = getUserDetails(user);
            log.info("{} went offline", userDetails.getUsername());
            onlineUsers.remove(userDetails.getId());
            userSubscribed.remove(userDetails.getId());
            for (UUID id : onlineUsers) {
                simpMessageSendingOperations.convertAndSend(
                        "/topic/" + id,
                        ChatMessage.builder()
                                .messageType(MessageType.FRIEND_OFFLINE)
                                .userConnection(UserConnection.builder().connectionID(userDetails.getId()).build())
                                .build());
            }
        }
    }

    private UserDetail getUserDetails(Principal principal) {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
        Object object = user.getPrincipal();
        return (UserDetail) object;
    }

    public List<UserResponse> getOnlineUsers() {
        return userRepository.findAllById(onlineUsers).stream()
                .map(user -> userService.convertToResponse(user))
                .toList();
    }

    public void addUserSubscribed(Principal user, String subscribedChannel) {
        UserDetail userDetails = getUserDetails(user);

        if (userDetails == null || userDetails.getId() == null) {
            log.error("UserDetails or User ID is null");
            return;
        }

        log.info("{} subscribed to {}", userDetails.getUsername(), subscribedChannel);
        Set<String> subscriptions = userSubscribed.getOrDefault(userDetails.getId(), new HashSet<>());
        subscriptions.add(subscribedChannel);
        userSubscribed.put(userDetails.getId(), subscriptions);
    }

    public void removeUserSubscribed(Principal user, String subscribedChannel) {
        UserDetail userDetails = getUserDetails(user);
        log.info("unSubscription! {} unSubscribed {}", userDetails.getUsername(), subscribedChannel);
        Set<String> subscriptions = userSubscribed.getOrDefault(userDetails.getId(), new HashSet<>());
        subscriptions.remove(subscribedChannel);
        userSubscribed.put(userDetails.getId(), subscriptions);
    }

    public boolean isUserSubscribed(UUID username, String subscription) {
        Set<String> subscriptions = userSubscribed.getOrDefault(username, new HashSet<>());
        return subscriptions.contains(subscription);
    }

    public Map<String, Set<String>> getUserSubscribed() {
        Map<String, Set<String>> result = new HashMap<>();
        List<User> users = userRepository.findAllById(userSubscribed.keySet());
        users.forEach(user -> result.put(user.getEmail(), userSubscribed.get(user.getId())));
        return result;
    }
    public void notifySender(
            UUID senderId,
            List<ConversationEntity> entities,
            MessageDeliveryStatusEnum messageDeliveryStatusEnum) {
        if (!isUserOnline(senderId)) {
            log.info(
                    "{} is not online. cannot inform the socket. will persist in database",
                    senderId.toString());
            return;
        }
        List<MessageDeliveryStatusUpdate> messageDeliveryStatusUpdates =
                entities.stream()
                        .map(
                                e ->
                                        MessageDeliveryStatusUpdate.builder()
                                                .id(e.getId())
                                                .messageDeliveryStatusEnum(messageDeliveryStatusEnum)
                                                .content(e.getContent())
                                                .build())
                        .toList();
        for (ConversationEntity entity : entities) {
            simpMessageSendingOperations.convertAndSend(
                    "/topic/" + senderId,
                    ChatMessage.builder()
                            .id(entity.getId())
                            .messageDeliveryStatusUpdates(messageDeliveryStatusUpdates)
                            .messageType(MessageType.MESSAGE_DELIVERY_UPDATE)
                            .content(entity.getContent())
                            .build());
        }
    }

}
