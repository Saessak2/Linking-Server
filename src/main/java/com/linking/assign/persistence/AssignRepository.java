package com.linking.assign.persistence;

import com.linking.assign.domain.Assign;
import com.linking.assign.dto.AssignCountReq;
import com.linking.participant.domain.Participant;
import com.linking.todo.domain.Todo;
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

    @Query(value = "SELECT a.participant.participantId, count(a.participant)" +
            " FROM Assign a WHERE a.participant in :partList ORDER BY a.participant.participantId")
    List<AssignCountReq> findCountByParticipant(@Param("partList") List<Participant> partList);

    @Query(value = "SELECT a.participant.participantId, count(a.participant) FROM Assign a" +
            " WHERE a.participant in :partList AND a.status = :status ORDER BY a.participant.participantId")
    List<AssignCountReq> findCountByParticipantAndStatus(
            @Param("partList") List<Participant> partList, @Param("status") String status);

}
