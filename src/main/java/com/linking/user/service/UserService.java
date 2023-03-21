package com.linking.user.service;

import com.linking.user.domain.User;
import com.linking.user.repository.UserRepository;
import com.linking.user.dto.UserSignInDefaultReq;
import com.linking.user.dto.UserSignUpDefaultReq;
import com.linking.user.dto.UserRes;
import com.linking.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<UserRes> addUser(UserSignUpDefaultReq userSignUpDefaultReq){
        return Optional.of(
                userMapper.toRes(
                        userRepository.save(
                                userMapper.toUser(userSignUpDefaultReq))));
    }

    public Optional<UserRes> findUser(UserSignInDefaultReq userSignInDefaultReq){
        Optional<User> data = userRepository.findUserByEmailAndPassword(
                userSignInDefaultReq.getEmail(), userSignInDefaultReq.getPassword());
        return data.map(userMapper::toRes);
    }

}
