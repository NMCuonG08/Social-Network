package com.example.Social_Network.FriendShip;


import com.example.Social_Network.User.IUserService;
import com.example.Social_Network.User.User;
import com.example.Social_Network.User.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendShipController {

    private final IFriendShipService friendShipService;
    private  final IUserService userService;
    @PostMapping("/add/{userId}")
    public String requestAddNewFriend(
            @PathVariable UUID userId,
            @RequestParam String content,
            @RequestParam UUID receiveUserId
    ){
        Optional<FriendShip> friendShip= friendShipService.addNewFriend(userId,content,receiveUserId);
        if(friendShip.isPresent()){
            return "Send required to add new friends successfully! ";
        }
        return "Error!";
    }
    @GetMapping("/required/{userId}")
    public ResponseEntity<List<FriendShipResponse>> showAllRequiredFriend(@PathVariable UUID userId) {
        List<FriendShip> allRequired = friendShipService.showAllRequiredFriend(userId);
        List<FriendShipResponse> friendShipResponses = allRequired.stream()
                .map(friendShip -> friendShipService.convertToResponse(friendShip))
                .collect(Collectors.toList());
        return ResponseEntity.ok(friendShipResponses);
    }
    @GetMapping("/all-friend/{userId}")
    public ResponseEntity<List<UserResponse>> getAllFriend(
            @PathVariable UUID userId
    ){
        List<User> allFriend = friendShipService.showAllFriend(userId);
        List<UserResponse> userResponses = allFriend.stream()
                .map(friend -> userService.convertToResponse(friend))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }
    @PutMapping("/accept/{id}")
    public String AcceptFriendRequest(@PathVariable Long id ){
        Optional<FriendShip> friendShip = friendShipService.acceptFriendRequest(id);
        if(friendShip.isPresent()){
            return "Accept Friend Successfully!";
        }
        return "Error";
    }
    @DeleteMapping("/cancel/{id}")
    public String cancelFriendRequest(@PathVariable Long id ){
        friendShipService.cancelFriendRequest(id);
        return "Cancel Friend Successfully!";
    }
    @PostMapping("/add-chat/{id}")
    public void addNewChat(@PathVariable Long id,
        @RequestParam String content
    ){
        friendShipService.addNewChat(id, content);
    }
    @GetMapping("/isFriend/{userId}")
    public ResponseEntity<FriendShipResponse> isFriend(
            @PathVariable UUID userId,
            @RequestParam UUID receiveUserId) {
        FriendShip friendShip = friendShipService.findIsFriend(userId, receiveUserId);

        if (friendShip == null) {
            return ResponseEntity.notFound().build();
        }

        FriendShipResponse friendShipResponse = friendShipService.convertToResponse(friendShip);
        return ResponseEntity.ok(friendShipResponse);
    }


}
