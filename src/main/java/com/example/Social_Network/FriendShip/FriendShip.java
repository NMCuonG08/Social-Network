package com.example.Social_Network.FriendShip;

import com.example.Social_Network.Chat.Chat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class FriendShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID idRequiredUser;

    private UUID idReceivedUser;

    private String content;

    private boolean status;

    @OneToMany(mappedBy =  "friendShip", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Chat> chat;

    public void addNewChat(Chat newChat){
        if(chat == null){
            chat = new ArrayList<>();
        }
        chat.add(newChat);
    }


    public FriendShip(UUID idRequiredUser, UUID idReceivedUser, String content, boolean status) {
        this.idRequiredUser = idRequiredUser;
        this.idReceivedUser = idReceivedUser;
        this.content = content;
        this.status = status;
    }
}
