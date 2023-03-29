package com.linking.user.service;

import com.linking.user.domain.User;
import com.linking.user.dto.*;
import com.linking.user.repository.UserRepository;
import com.linking.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<UserDetailedRes> addUser(UserSignUpDefaultReq userSignUpDefaultReq)
            throws SQLIntegrityConstraintViolationException {
        return Optional.of(
                userMapper.toRes(
                        userRepository.save(
                                userMapper.toUser(userSignUpDefaultReq))));
    }

    public boolean findDuplicatedEmail(UserEmailVerifyReq emailReq){
        return userRepository.findUserByEmail(emailReq.getEmail()).isPresent();
    }

    public boolean findDuplicatedEmail(UserSignUpDefaultReq req){
        return userRepository.findUserByEmail(req.getEmail()).isPresent();
    }

    public Optional<UserDetailedRes> findUser(UserIdReq userIdReq){
        return userRepository.findById(userIdReq.getUserId()).map(userMapper::toRes);
    }

    public Optional<UserDetailedRes> findUser(UserSignInDefaultReq userSignInDefaultReq){
        String email = userSignInDefaultReq.getEmail(), pw = userSignInDefaultReq.getPassword();
        return userRepository.findUserByEmailAndPassword(email, pw).map(userMapper::toRes);
    }

    public Optional<UserDetailedRes> deleteUser(UserIdReq userIdReq){
        Optional<User> userData = userRepository.findById(userIdReq.getUserId());
        if(userData.isPresent()) {
            userRepository.delete(userData.get());
            return Optional.of(userMapper.toRes(userData.get()));
        }
        return Optional.empty();
    }

}
