package com.linking.participant;

import com.linking.participant.domain.Participant;
import com.linking.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query(value = "SELECT p FROM Participant p WHERE p.project.projectId = :projectId")
    List<Participant> findByProject(@Param("projectId") Long projectId);

}
