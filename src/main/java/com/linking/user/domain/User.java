package com.linking.user.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    // TODO: phoneNumber should be unique key
    @Column(name = "phone_number")
    private String phoneNumber;

    private String password;

    public User(Long ownerId) {
        this.userId = ownerId;
    }

    public String getFullName() {
        return lastName + firstName;
    }
}
