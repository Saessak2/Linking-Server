package com.linking.user;

import com.linking.user.dto.UserReqDto;
import com.linking.user.dto.UserResDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserResDto> addUser(UserReqDto userReqDto) {
        return Optional.of(userRepository.save(new User(userReqDto)).toDto());
    }

    // login -> findEmailById -> LoginHandler

}
