package com.example.Social_Network.Post;

import com.example.Social_Network.User.User;
import com.example.Social_Network.User.UserResponse;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private UUID id;

    private String content;

    private UserResponse user;

    private LocalDate date;

    private List<URLSave> urls;

    private List<Tag> tags;
}
