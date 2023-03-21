package com.linking.user;

import com.linking.user.dto.UserSignUpDefaultReq;
import com.linking.user.dto.UserResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    private String password;

    public User(UserSignUpDefaultReq userSignUpDefaultReq){
        this.lastName = userSignUpDefaultReq.getLastName();
        this.firstName = userSignUpDefaultReq.getFirstName();
        this.email = userSignUpDefaultReq.getEmail();
        this.phoneNumber = userSignUpDefaultReq.getPhoneNumber();
        this.password = userSignUpDefaultReq.getPassword();
    }

    public UserResDto toDto(){
        return new UserResDto(userId, lastName, firstName, email, phoneNumber, password);
    }

}
