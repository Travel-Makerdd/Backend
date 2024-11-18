package com.hsu.travelmaker.domain.tripfavorite.repository;

import com.hsu.travelmaker.domain.tripfavorite.entity.TripFavorite;
import com.hsu.travelmaker.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripFavoriteRepository extends JpaRepository<TripFavorite, Long> {
    List<TripFavorite> findByUserId(User user);
}