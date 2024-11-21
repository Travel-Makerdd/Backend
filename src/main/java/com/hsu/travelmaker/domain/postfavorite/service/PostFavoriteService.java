package com.hsu.travelmaker.domain.postfavorite.service;

import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFavoriteService {
    ResponseEntity<CustomApiResponse<?>> addPostFavorite(Long postId);
    ResponseEntity<CustomApiResponse<?>> removePostFavorite(Long postId);
    ResponseEntity<CustomApiResponse<?>> getPostFavorite();
}
