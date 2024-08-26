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

    Optional<FriendShip> acceptFriendRequest(Long id);

    void cancelFriendRequest(Long id);

    void addNewChat(Long id, String content);

    FriendShip findIsFriend(UUID userId, UUID receiveUserId);
}
