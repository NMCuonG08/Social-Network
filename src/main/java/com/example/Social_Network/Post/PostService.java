package com.example.Social_Network.Post;

import com.example.Social_Network.Cloudinary.CloudinaryService;
import com.example.Social_Network.Exception.UserNotFoundException;
import com.example.Social_Network.User.IUserService;
import com.example.Social_Network.User.User;
import com.example.Social_Network.User.UserResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService  implements IPostService {

    private final PostRepository postRepository;
    private final CloudinaryService cloudinaryService;
    private final IUserService userService;

    @Override
    public Post addNewPost(UUID id, String content, List<MultipartFile> files, List<String> tags) throws IOException {
        Optional<User> user = userService.getUserById(id);
        if(user.isPresent()){
            Post post = new Post();
            post.setUser(user.get());
            post.setDate(LocalDate.now());
            if(content != null) {
                post.setContent(content);
            }
            if(files != null && !files.isEmpty() ) {
                for (MultipartFile file : files){
                    String link =  cloudinaryService.uploadFile(file, "post").get("secure_url").toString();
                    String public_id =  cloudinaryService.uploadFile(file, "post").get("public_id").toString();
                    URLSave urlSave = new URLSave(link,public_id);
                    post.addUrlLink(urlSave);
                }
            }
            if(tags != null && !tags.isEmpty() ) {
                for (String tag : tags){
                    Tag tagLink = new Tag(tag);
                    post.addTag(tagLink);
                }
            }
            return postRepository.save(post);
        }
        else throw new UserNotFoundException("User not found ha ha");

    }

    @Override
    public List<Post> getPostById(UUID id) {
        return postRepository.findByUserId(id);
    }

    @Override
    public PostResponse convertToResponse(Post post){
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setDate(post.getDate());
        postResponse.setContent(post.getContent());
        UserResponse userResponse = userService.convertToResponse(post.getUser());
        postResponse.setUser(userResponse);
        postResponse.setTags(post.getTags());
        postResponse.setUrls(post.getUrls());
        return postResponse;
    }
}
