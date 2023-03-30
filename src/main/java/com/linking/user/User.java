package com.linking.user;

import com.linking.annotation.Annotation;
import com.linking.user.dto.UserReqDto;
import com.linking.user.dto.UserResDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    public User(UserReqDto userReqDto){
        this.lastName = userReqDto.getLastName();
        this.firstName = userReqDto.getFirstName();
        this.email = userReqDto.getEmail();
        this.phoneNumber = userReqDto.getPhoneNumber();
    }

    public UserResDto toDto(){
        return new UserResDto(userId, lastName, firstName, email, phoneNumber);
    }

}
