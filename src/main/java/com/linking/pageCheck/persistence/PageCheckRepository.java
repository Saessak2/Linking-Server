package com.linking.pageCheck.persistence;

import com.linking.pageCheck.domain.PageCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageCheckRepository extends JpaRepository<PageCheck, Long> {

    @Query("SELECT p FROM PageCheck p WHERE p.participant.participantId = :id")
    List<PageCheck> findAllAByParticipantId(@Param("id") Long participant);
}
