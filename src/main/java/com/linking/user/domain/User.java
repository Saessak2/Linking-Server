package com.linking.user.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(unique = true, nullable = false, length = 60)
    private String email;

    @Column(length = 20)
    private String password;

    @Setter
    private String fcmToken;

    public User(Long ownerId) {
        this.userId = ownerId;
    }

    public String getFullName() {
        return lastName + firstName;
    }

}
