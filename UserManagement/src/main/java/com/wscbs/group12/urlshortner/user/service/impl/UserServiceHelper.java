package com.wscbs.group12.urlshortner.user.service.impl;

import com.wscbs.group12.urlshortner.user.entities.UserEntity;
import com.wscbs.group12.urlshortner.user.model.UserRequest;
import com.wscbs.group12.urlshortner.user.model.UserResponse;
import com.wscbs.group12.urlshortner.user.repository.UserRepository;
import com.wscbs.group12.urlshortner.user.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceHelper {

    private final CommonUtil commonUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity buildUserEntityFromRequest(UserRequest request) {
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .uuid(UUID.randomUUID().toString())
                .userId(request.getUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    public UserResponse buildResponseFromEntity(UserEntity entity) {
        return UserResponse.builder()
                .refId(entity.getUuid())
                .email(entity.getEmail())
                .name(entity.getName())
                .build();
    }

    public List<UserResponse> buildResponseFromEntities(List<UserEntity> entities) {
        List<UserResponse> userResponses = new LinkedList<>();
        for (UserEntity entity : entities) {
            userResponses.add(UserResponse.builder()
                    .refId(entity.getUuid())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .build());
        }
        return userResponses;
    }
}
