package com.linking.participant.persistence;

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

    List<Participant> findByProject(@Param("project") Project project);

    @Query(value = "SELECT p.project FROM Participant p WHERE p.user.userId = :userId")
    List<Project> findProjectsByUser(@Param("userId") Long userId);

    @Query(value = "SELECT p FROM Participant p WHERE p.user.userId = :userId AND p.project.projectId = :projectId")
    List<Participant> findByUserAndProject(@Param("userId") Long userId, @Param("projectId") Long projectId);

    /**
     * 작성자 이은빈
     */
    @Query(value = "SELECT p FROM Participant p WHERE p.user.userId = :userId AND p.project.projectId = :projectId")
    Optional<Participant> findByUserAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);

    @Query("SELECT p FROM Participant p WHERE p.project.projectId = :projectId")
    List<Participant> findAllByProjectId(@Param("projectId") Long projectId);

}
