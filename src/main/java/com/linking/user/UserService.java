package com.linking.user;

import com.linking.user.dto.UserSignInDefaultReq;
import com.linking.user.dto.UserSignUpDefaultReq;
import com.linking.user.dto.UserResDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserResDto> addUser(UserSignUpDefaultReq userSignUpDefaultReq){
        return Optional.of(userRepository.save(new User(userSignUpDefaultReq)).toDto());
    }

    public Optional<UserResDto> findUser(UserSignInDefaultReq userSignInDefaultReq){
        Optional<User> data = userRepository.findUserByEmailAndPassword(
                userSignInDefaultReq.getEmail(), userSignInDefaultReq.getPassword());
        return data.map(User::toDto).or(Optional::empty);
    }

}
