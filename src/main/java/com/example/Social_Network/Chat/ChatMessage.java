package com.example.Social_Network.Chat;

import com.example.Social_Network.User.UserConnection;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    private UUID id;
    private MessageType messageType;
    private String content;
    private UUID senderID;
    private String senderEmail;

    private UUID receiverID;
    private String receiverUsername;

    private UserConnection userConnection;

    private MessageDeliveryStatusEnum messageDeliveryStatusEnum;

    private List<MessageDeliveryStatusUpdate> messageDeliveryStatusUpdates;

}
