package com.example.Social_Network.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    @Query("Select u.id from User u")
    List<UUID> findUserIds();

    @Query("SELECT u FROM User u WHERE (u.userName LIKE %:name% OR u.email LIKE %:name%) AND u.email <> :currentEmail")
    List<User> findUserByNameOrEmail(@Param("name") String name, @Param("currentEmail") String currentEmail);

}
