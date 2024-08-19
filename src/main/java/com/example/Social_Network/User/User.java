package com.example.Social_Network.User;

import com.example.Social_Network.Post.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private UUID id;

    private String  userName;

    private String email;

    private String passWord;

    private LocalDate birthDay;

    private String gender;

    @Lob
    private Blob image;

    private String address;

    private boolean isEnable;

    private String role;

    @OneToMany(mappedBy = "user" ,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    private String job;

    public User(String email, String passWord, String role, boolean isEnable) {
        this.email = email;
        this.passWord = passWord;
        this.role = role;
        this.isEnable = isEnable;
    }
}
