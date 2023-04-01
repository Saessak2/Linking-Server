package com.linking.participant;

import com.linking.participant.domain.Participant;
import com.linking.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByUserEmail(@Param("email") String email);
    List<Participant> findByProject(@Param("project") Project project);

}
