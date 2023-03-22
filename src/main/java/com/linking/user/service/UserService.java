package com.linking.user.service;

import com.linking.user.dto.UserEmailVerifyReq;
import com.linking.user.repository.UserRepository;
import com.linking.user.dto.UserSignInDefaultReq;
import com.linking.user.dto.UserSignUpDefaultReq;
import com.linking.user.dto.UserDetailedRes;
import com.linking.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<UserDetailedRes> addUser(UserSignUpDefaultReq userSignUpDefaultReq){
        return Optional.of(
                userMapper.toRes(
                        userRepository.save(
                                userMapper.toUser(userSignUpDefaultReq))));
    }

    // TODO: login security needs to be upgraded
    public Optional<UserDetailedRes> findUser(UserSignInDefaultReq userSignInDefaultReq){
        return Optional.of(
                userMapper.toRes(
                        userRepository.findUserByEmailAndPassword(
                                userSignInDefaultReq.getEmail(), userSignInDefaultReq.getPassword())));
    }

    public boolean findDuplicatedEmail(UserEmailVerifyReq emailReq){
        return userRepository.findUserByEmail(emailReq.getEmail()) != null;
    }

}
