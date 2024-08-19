package com.example.Social_Network.FriendShip;

import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendShipResponse {

    private Long id;

    private UUID idRequiredUser;

    private UUID idReceivedUser;

    private String content;

    private boolean status;
}
