package com.example.Social_Network.User;

import com.example.Social_Network.Exception.UserNotFoundException;
import com.example.Social_Network.User.Registration.RegistrationRequest;
import com.example.Social_Network.User.Registration.VerificationToken;
import com.example.Social_Network.User.Registration.VerificationTokenRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String validateToken(String token) {
        VerificationToken theToken = verificationTokenRepository.findByToken(token);
        if(token == null){
            return "Invalid Verification Token";
        }
        User user = theToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (theToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0){
            return "Token already expired!";
        }
        user.setEnable(true);
        userRepository.save(user);
        return "Valid";
    }

    @Override
    public User register(RegistrationRequest request) {

        Optional<User> user = userRepository.findByEmail(request.email());
        if(user.isPresent()){
            throw new UserNotFoundException("User with email" + request.email() + " already exits!");
        }
        User newUser =new User();
        newUser.setId(UUID.randomUUID());
        newUser.setEmail(request.email());
        newUser.setPassWord(passwordEncoder.encode(request.passWord()));
        newUser.setEnable(false);
        newUser.setRole(request.role());
        return userRepository.save(newUser);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public UserResponse convertToResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUserName(user.getUserName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassWord(user.getPassWord());
        userResponse.setBirthDay(user.getBirthDay());
        userResponse.setGender(user.getGender());

        // Convert Blob to byte[]
        Blob blob = user.getImage();
        if (blob != null) {
            try (InputStream inputStream = blob.getBinaryStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                userResponse.setImage(outputStream.toByteArray());
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
        userResponse.setAddress(user.getAddress());
        userResponse.setEnable(user.isEnable());
        userResponse.setRole(user.getRole());
        userResponse.setJob(user.getJob());
        user.setPosts(user.getPosts());
        return userResponse;
    }

    @Override
    public User updateUser(UUID id, String userName, LocalDate birthDay, String gender, String address, String job, byte[] photoBytes) throws SQLException {
        User user = userRepository.findById(id).orElseThrow( () -> new UsernameNotFoundException("User not found! hu hu"));
        user.setUserName(userName);
        user.setAddress(address);
        user.setBirthDay(birthDay);
        user.setJob(job);
        user.setGender(gender);
        if( photoBytes != null && photoBytes.length > 0 ){

             user.setImage(new SerialBlob(photoBytes));
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<UUID> getUserIds() {
        return userRepository.findUserIds();
    }

    @Override
    public UUID getUserId(String email) {
        return userRepository.findByEmail(email).get().getId();
    }

    @Override
    public List<User> findUserByNameOrEmail(String name, String currentEmail) {
        return userRepository.findUserByNameOrEmail(name, currentEmail);
    }
}
