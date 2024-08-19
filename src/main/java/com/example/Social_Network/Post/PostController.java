package com.example.Social_Network.Post;

import com.example.Social_Network.Cloudinary.CloudinaryService;
import com.example.Social_Network.Exception.UserNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final IPostService postService;

    @PostMapping("/add/{id}")
    public String addNewPost(
            @PathVariable UUID id,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) List<MultipartFile> files,
            @RequestParam(required = false) List<String> tags
            ) throws IOException, InterruptedException {

           Post post = postService.addNewPost(id,content,files,tags);
           if (post != null) {
               return "Post Successfully";
           }
           else {
               return "Post fails";
           }
    }
    @GetMapping("/find-posts/{id}")
    public ResponseEntity<List<PostResponse>> getPostByUserId(@PathVariable UUID id){
        List<Post> posts = postService.getPostById(id);
        List<PostResponse> postResponses = posts.stream()
                .map(post -> postService.convertToResponse(post))
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }

}
