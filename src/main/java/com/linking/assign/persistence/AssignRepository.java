package com.linking.assign.persistence;

import com.linking.assign.domain.Assign;
import com.linking.assign.domain.Status;
import com.linking.assign.dto.AssignCountRes;
import com.linking.participant.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssignRepository extends JpaRepository<Assign, Long> {

    @Query(value = "SELECT a FROM Assign a WHERE a.participant in :partList AND :date = a.todo.dueDate")
    List<Assign> findByParticipantAndDate(@Param("partList") List<Participant> partList, @Param("date") LocalDate date);

    @Query(value = "SELECT NEW com.linking.assign.dto.AssignCountRes(" +
            " a.participant.participantId, COUNT(a.assignId), SUM(CASE WHEN a.status = :status THEN 1 ELSE 0 END))" +
            " FROM Assign a WHERE a.participant in :partList GROUP BY a.participant ORDER BY a.participant.participantId")
    List<AssignCountRes> findCountByParticipantAndStatus(@Param("status") Status status, @Param("partList") List<Participant> partList);


}
