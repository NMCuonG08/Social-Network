package com.example.Social_Network.FriendShip;

import com.example.Social_Network.Chat.Chat;
import com.example.Social_Network.Chat.ChatRepository;
import com.example.Social_Network.Exception.UserNotFoundException;
import com.example.Social_Network.User.User;
import com.example.Social_Network.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendShipService implements IFriendShipService {

    private final UserRepository userRepository;
    private  final FriendShipRepository friendShipRepository;
    private  final ChatRepository chatRepository;

    @Override
    public Optional<FriendShip> addNewFriend(UUID userId, String content, UUID receiveUserId) {

        Optional<User> requireUser = userRepository.findById(userId);
        Optional<User> receivedUser = userRepository.findById(receiveUserId);
        if(receivedUser.isPresent() && receivedUser.isPresent()){
            FriendShip friendShip = new FriendShip(userId,receiveUserId,content,false);
            return Optional.of(friendShipRepository.save(friendShip));
        }
        else {
            throw  new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<FriendShip> showAllRequiredFriend(UUID userId) {
        return friendShipRepository.findByIdRequiredUser(userId);
    }

    @Override
    public FriendShipResponse convertToResponse(FriendShip friendShip) {
        FriendShipResponse friendShipResponse = new FriendShipResponse();
        friendShipResponse.setId(friendShip.getId());
        friendShipResponse.setContent(friendShip.getContent());
        friendShipResponse.setStatus(friendShip.isStatus());
        friendShipResponse.setIdRequiredUser(friendShip.getIdRequiredUser());
        friendShipResponse.setIdReceivedUser(friendShip.getIdReceivedUser());
        return friendShipResponse;
    }

    @Override
    public List<User> showAllFriend(UUID userId) {

        List<UUID> userIDs = friendShipRepository.findAllFriends(userId);
        List<UUID> otherIDs = friendShipRepository.findOtherFriends(userId);
        Set<UUID> allFriendIDs = new HashSet<>(userIDs);
        allFriendIDs.addAll(otherIDs);
        List<User> users = allFriendIDs.stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found: " + id)))
                .collect(Collectors.toList());

        return users;
    }


    @Override
    public Optional<FriendShip> acceptFriendRequest(Long id) {
        Optional<FriendShip> friendShip = friendShipRepository.findById(id);
        if (friendShip.isPresent()){
            friendShip.get().setStatus(true);
            return Optional.of(friendShipRepository.save(friendShip.get()));
        }
        else {
            throw new UserNotFoundException("User not found!");
        }

    }

    @Override
    public void cancelFriendRequest(Long id) {
        Optional<FriendShip> friendShip = friendShipRepository.findById(id);
        if (friendShip.isPresent()){
            friendShipRepository.delete(friendShip.get());
        }
        else {
            throw new UserNotFoundException("User not found!");
        }

    }

    @Override
    public void addNewChat(Long id, String content) {
        Optional<FriendShip> friendShipOptional = friendShipRepository.findById(id);
        if (friendShipOptional.isPresent()) {
            FriendShip friendShip = friendShipOptional.get();
            Chat chat = new Chat(content, LocalDate.now(), friendShip);
            friendShip.getChat().add(chat);
            chatRepository.save(chat);
        } else {
            throw new UserNotFoundException("FriendShip with id " + id + " not found.");
        }
    }

    @Override
    public FriendShip findIsFriend(UUID userId, UUID receiveUserId) {
        return friendShipRepository.findByUserIdAndReceiveUserId(userId, receiveUserId);

    }

}
