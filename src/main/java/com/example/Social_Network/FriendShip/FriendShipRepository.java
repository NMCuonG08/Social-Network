package com.example.Social_Network.FriendShip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FriendShipRepository extends JpaRepository<FriendShip, UUID> {
    List<FriendShip> findByIdRequiredUser(UUID userId);

    @Query("SELECT f.idReceivedUser FROM FriendShip f WHERE f.idRequiredUser = :userId AND f.status = true" +
            " ")
    List<UUID> findAllFriends(@Param("userId") UUID userId);
    @Query("SELECT f.idRequiredUser FROM FriendShip f WHERE f.idReceivedUser = :userId AND f.status = true" +
            " ")
    List<UUID> findOtherFriends(@Param("userId") UUID userId);
}
