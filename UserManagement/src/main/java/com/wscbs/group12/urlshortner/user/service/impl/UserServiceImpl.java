package com.wscbs.group12.urlshortner.user.service.impl;

import com.wscbs.group12.urlshortner.user.entities.UserEntity;
import com.wscbs.group12.urlshortner.user.exceptions.UserException;
import com.wscbs.group12.urlshortner.user.model.UserRequest;
import com.wscbs.group12.urlshortner.user.model.UserResponse;
import com.wscbs.group12.urlshortner.user.repository.UserRepository;
import com.wscbs.group12.urlshortner.user.service.UserService;
import com.wscbs.group12.urlshortner.user.util.ErrorCode;
import com.wscbs.group12.urlshortner.user.util.ResponseErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserServiceHelper helper;

    @Override
    @Transactional
    public UserResponse create(UserRequest userRequest) {
        UserEntity entity = helper.buildUserEntityFromRequest(userRequest);
        return helper.buildResponseFromEntity(userRepository.saveAndFlush(entity));
    }

    @Override
    public UserResponse getUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            log.error("Object not found - {}", userId);
            throw new UserException(ResponseErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR, "Object not found");
        }
        return helper.buildResponseFromEntity(userEntity);
    }

    @Override
    public UserResponse getUserByIdAndPassword(String userId, String password) {
        UserEntity userEntity = userRepository.findByUserIdAndPassword(userId, password);
        if (userEntity == null) {
            log.error("Object not found - {}", userId);
            throw new UserException(ResponseErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR, "Object not found");
        }
        return helper.buildResponseFromEntity(userEntity);
    }

    @Override
    @Transactional
    public UserResponse update(String userId, UserRequest userRequest) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            log.error("Object not found - {}", userId);
            throw new UserException(ResponseErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR, "Object not found");
        }
        userEntity.setName(userRequest.getName());
        userEntity.setEmail(userRequest.getEmail());
        return helper.buildResponseFromEntity(userRepository.saveAndFlush(userEntity));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return helper.buildResponseFromEntities(userEntities);
    }

    @Override
    @Transactional
    public boolean deleteById(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            log.error("Object not found - {}", userId);
            throw new UserException(ResponseErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR, "Object not found");
        }
        userRepository.delete(userEntity);
        return true;
    }
}
