package com.linking.user;

import com.linking.user.dto.UserReqDto;
import com.linking.user.dto.UserResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResDto> signUp(@RequestBody @Valid UserReqDto userReqDto){
        Optional<UserResDto> result = userService.addUser(userReqDto);
        return result
                .map(userResDto -> ResponseEntity.ok().body(userResDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
