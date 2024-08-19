package com.example.Social_Network.Chat;

import com.example.Social_Network.FriendShip.FriendShip;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "friendId")
    @JsonIgnore
    private FriendShip friendShip;

    public Chat(String content, LocalDate date, FriendShip friendShip) {
        this.content = content;
        this.date = date;
        this.friendShip = friendShip;
    }
}
