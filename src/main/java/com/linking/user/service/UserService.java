package com.linking.user.service;

import com.linking.user.domain.User;
import com.linking.user.dto.*;
import com.linking.user.persistence.UserRepository;
import com.linking.user.persistence.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<UserDetailedRes> addUser(UserSignUpDefaultReq userSignUpDefaultReq)
            throws SQLIntegrityConstraintViolationException {
        return Optional.of(
                userMapper.toDto(
                        userRepository.save(
                                userMapper.toEntity(userSignUpDefaultReq))));
    }

    public boolean findDuplicatedEmail(UserEmailVerifyReq emailReq){
        return userRepository.findUserByEmail(emailReq.getEmail()).isPresent();
    }

    public List<UserDetailedRes> findUserByPartOfEmail(UserEmailReq userEmailReq){
        List<User> data = userRepository.findUsersByPartOfEmail(userEmailReq.getPartOfEmail());
        if(data.isEmpty())
            return null;
        return userMapper.toDto(data);
    }

    public Optional<UserDetailedRes> findUser(Long userId){
        return userRepository.findById(userId).map(userMapper::toDto);
    }

    public Optional<UserDetailedRes> findUser(UserSignInDefaultReq userSignInDefaultReq){
        String email = userSignInDefaultReq.getEmail(), pw = userSignInDefaultReq.getPassword();
        return userRepository.findUserByEmailAndPassword(email, pw).map(userMapper::toDto);
    }

    public Optional<UserDetailedRes> deleteUser(Long userId){
        Optional<User> userData = userRepository.findById(userId);
        if(userData.isPresent()) {
            userRepository.delete(userData.get());
            return Optional.of(userMapper.toDto(userData.get()));
        }
        return Optional.empty();
    }

}
