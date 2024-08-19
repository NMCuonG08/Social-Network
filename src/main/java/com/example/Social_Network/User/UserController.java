package com.example.Social_Network.User;

import com.example.Social_Network.WebSocket.OnlineOfflineService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@CrossOrigin("http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    private final OnlineOfflineService onlineOfflineService;

    @PutMapping("/update/{id}")
    public String updateUser(
            @PathVariable UUID id,
            @RequestParam String userName,
            @RequestParam LocalDate birthDay,
            @RequestParam String gender,
            @RequestParam String address,
            @RequestParam String job,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException, SQLException {
        byte[] photoBytes  = image != null &&!image.isEmpty() ? image.getBytes() : null;
        User user = userService.updateUser(id,userName,birthDay,gender,address,job,photoBytes);
        return "User with id = " + id + " have been updated successfully!";
    }
    @GetMapping("/get-user")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email){
        Optional<User> user = userService.getUserByEmail(email);
        UserResponse userResponse = null;
        if ((user.isPresent())){
            userResponse = userService.convertToResponse(user.get());
        }
        return ResponseEntity.ok(userResponse);
    }
    @GetMapping("/{id}/get-user")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id){
        Optional<User> user = userService.getUserById(id);
        UserResponse userResponse = null;
        if ((user.isPresent())){
            userResponse = userService.convertToResponse(user.get());
        }
        return ResponseEntity.ok(userResponse);
    }
    @GetMapping("/online")
    @PreAuthorize("hasAuthority(ADMIN)")
    List<UserResponse> getOnlineUser(){
        return onlineOfflineService.getOnlineUsers();
    }


    @GetMapping("/subscriptions")
    @PreAuthorize("hasAuthority('ADMIN')")
    Map<String, Set<String>> getSubscriptions() {
        return onlineOfflineService.getUserSubscribed();
    }
    @GetMapping("/ids")
    public List<UUID> getUserId(){
        return userService.getUserIds();

    }

}
