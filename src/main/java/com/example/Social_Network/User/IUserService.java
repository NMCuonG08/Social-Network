package com.example.Social_Network.User;

import com.example.Social_Network.User.Registration.RegistrationRequest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {


    String validateToken(String token);

    User register(RegistrationRequest request);

    void saveUserVerificationToken(User theUser, String verificationToken);

    UserResponse convertToResponse(User friend);

    User updateUser(UUID id, String userName, LocalDate birthDay, String gender, String address, String job, byte[] photoBytes) throws SQLException;

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(UUID id);

    List<UUID> getUserIds();

    UUID getUserId(String email);

    List<User> findUserByNameOrEmail(String name, String currentEmail);
}
