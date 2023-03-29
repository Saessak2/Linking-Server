package com.linking.user.repository;

import com.linking.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    Optional<User> findUserByEmail(@Param("email") String email);

}
