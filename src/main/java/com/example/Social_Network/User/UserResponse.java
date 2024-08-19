package com.example.Social_Network.User;

import com.example.Social_Network.Post.Post;
import jakarta.persistence.Lob;
import lombok.*;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;

    private String  userName;

    private String email;

    private String passWord;

    private LocalDate birthDay;

    private String gender;
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    private String address;

    private String job;

    private boolean isEnable;

    private String role;

    private List<Post> posts;

}
