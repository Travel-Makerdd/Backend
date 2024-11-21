package com.hsu.travelmaker.domain.user.repository;

import com.hsu.travelmaker.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String userEmail);

    @Modifying
    @Query(value = "DELETE FROM user WHERE user_id = :userId", nativeQuery = true)
    void deleteUserDirectly(@Param("userId") Long userId);
}
