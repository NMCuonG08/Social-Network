package com.example.Social_Network.Chat.Conversation;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "conversation")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ConversationEntity {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "conv_id", length = -1)
    private String convId;

    @Column(name = "from_user", columnDefinition = "BINARY(16)")
    private UUID fromUser;

    @Column(name = "to_user", columnDefinition = "BINARY(16)")
    private UUID toUser;

    @Column(name = "time")
    @CreatedDate
    private Timestamp time;

    @Column(name = "last_modified")
    @LastModifiedDate
    private Timestamp lastModified;

    @Column(name = "content", length = -1)
    private String content;

    @Column(name = "delivery_status", length = -1)
    private String deliveryStatus;
}
