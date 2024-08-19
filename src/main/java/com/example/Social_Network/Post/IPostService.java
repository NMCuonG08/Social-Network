package com.example.Social_Network.Post;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IPostService {
    Post addNewPost(UUID id, String content, List<MultipartFile> files, List<String> tags) throws IOException;

    List<Post> getPostById(UUID id);
    PostResponse convertToResponse(Post post);
}
