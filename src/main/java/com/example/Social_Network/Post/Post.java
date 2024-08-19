package com.example.Social_Network.Post;


import com.example.Social_Network.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    private UUID id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;

    private LocalDate date;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<URLSave> urls;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Tag> tags;

    public void addUrlLink(URLSave urlLink){
        if(urls == null ){
            urls = new ArrayList<>();
        }
        urlLink.setPost(this);
        urls.add(urlLink);
    }

    public void addTag(Tag tag){
        if(tags == null ){
            tags = new ArrayList<>();
        }
        tag.setPost(this);
        tags.add(tag);
    }


}
