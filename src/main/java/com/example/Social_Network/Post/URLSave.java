package com.example.Social_Network.Post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class URLSave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String url;

    private String public_id;

    @ManyToOne
    @JoinColumn(name = "postId")
    @JsonIgnore
    private Post post;

    public URLSave(String url, String public_id) {
        this.url = url;
        this.public_id = public_id;
    }
}
