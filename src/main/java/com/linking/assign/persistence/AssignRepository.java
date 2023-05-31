package com.linking.assign.persistence;

import com.linking.assign.domain.Assign;
import com.linking.assign.domain.Status;
import com.linking.assign.dto.AssignCountRes;
import com.linking.participant.domain.Participant;
import com.linking.project.domain.Project;
import com.linking.todo.domain.Todo;
import com.linking.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface AssignRepository extends JpaRepository<Assign, Long> {

    @EntityGraph(attributePaths = {"todo, participant"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT NEW com.linking.assign.dto.AssignCountRes" +
            " (p, COUNT(a.assignId), SUM(CASE WHEN a.status = :status THEN 1 ELSE 0 END))" +
            " FROM Assign a join Participant p ON a.participant = p" +
            " WHERE p in :partList GROUP BY p.participantId")
    List<AssignCountRes> findCountByParticipantAndStatus(@Param("status") Status status, @Param("partList") List<Participant> partList);

    @EntityGraph(attributePaths = {"todo, participant"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Assign> findByTodo(@Param("todo") Todo todo);

    @EntityGraph(attributePaths = {"todo, participant"}, type = EntityGraph.EntityGraphType.FETCH)
    Assign findByTodoAndParticipant(@Param("todo") Todo todo, @Param("Participant") Participant participant);

    @EntityGraph(attributePaths = {"todo, participant"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT a FROM Assign a WHERE DATE(:date) > a.todo.dueDate AND a.status = :status")
    List<Assign> findByDateAndStatus(@Param("date") LocalDate date, @Param("status") Status status);

    @EntityGraph(value = "Assign.fetchTodoAndParticipant", type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT DISTINCT a FROM Assign a" +
            " WHERE a.participant.user.userId = :userId" +
            " AND (a.status = 'INCOMPLETE' OR a.status = 'INCOMPLETE_PROGRESS')" +
            " AND function('date_format', :date, '%Y%m%d') > a.todo.dueDate")
    List<Assign> findByParticipantAndStatusAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @EntityGraph(value = "Assign.fetchTodoAndParticipant", type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT a FROM Assign a WHERE a.participant.user.userId = :userId " +
            " AND function('date_format', :date, '%Y%m%d') = function('date_format', a.todo.dueDate, '%Y%m%d')")
    List<Assign> findByParticipantAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @EntityGraph(attributePaths = {"todo"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT DISTINCT a FROM Assign a" +
            " WHERE :projectId = a.todo.project.projectId" +
            " AND (a.status = 'INCOMPLETE' OR a.status = 'INCOMPLETE_PROGRESS')" +
            " AND function('date_format', :date, '%Y%m%d') > a.todo.dueDate")
    List<Assign> findByProjectAndStatusAndDate(@Param("projectId") Long projectId, @Param("date") LocalDate date);

    @EntityGraph(attributePaths = {"todo, participant"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT a FROM Assign a" +
            " WHERE a.participant in :partList" +
            " AND function('date_format', :date, '%Y%m%d')" +
            " BETWEEN function('date_format', a.todo.startDate, '%Y%m%d') AND function('date_format', a.todo.dueDate, '%Y%m%d') ")
    List<Assign> findByParticipantAndDateContains(@Param("partList") List<Participant> partList, @Param("date") LocalDate date);

}
