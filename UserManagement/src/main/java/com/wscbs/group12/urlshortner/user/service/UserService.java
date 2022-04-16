package com.wscbs.group12.urlshortner.user.service;

import com.wscbs.group12.urlshortner.user.model.UserRequest;
import com.wscbs.group12.urlshortner.user.model.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest userRequest);

    UserResponse getUser(String userId);

    UserResponse getUserByIdAndPassword(String userId, String password);

    UserResponse update(String userId, UserRequest userRequest);

    List<UserResponse> getAllUsers();

    boolean deleteById(String userId);
}
