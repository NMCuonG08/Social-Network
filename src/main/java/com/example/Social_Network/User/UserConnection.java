package com.example.Social_Network.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserConnection {
    private UUID connectionID;
    private String connectionUsername;
    private String convId;
    private int unSeen;

    @JsonProperty("isOnline")
    private boolean isOnline;

}
