package com.example.Social_Network.FriendShip;

import com.example.Social_Network.User.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IFriendShipService {

    Optional<FriendShip> addNewFriend(UUID userId, String content, UUID receiveUserId);

    List<FriendShip> showAllRequiredFriend(UUID userId);

    FriendShipResponse convertToResponse(FriendShip friendShip);

    List<User> showAllFriend(UUID userId);

    Optional<FriendShip> acceptFriendRequest(UUID id);

    void cancelFriendRequest(UUID id);

    void addNewChat(UUID id, String content);
}
