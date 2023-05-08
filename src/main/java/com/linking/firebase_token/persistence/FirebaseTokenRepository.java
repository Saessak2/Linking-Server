package com.linking.firebase_token.persistence;

import com.linking.firebase_token.domain.FirebaseToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirebaseTokenRepository extends JpaRepository<FirebaseToken, Long> {


}
