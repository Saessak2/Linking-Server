package com.linking.assign.persistence;

import com.linking.assign.domain.Assign;
import com.linking.assign.domain.Status;
import com.linking.assign.dto.AssignCountRes;
import com.linking.participant.domain.Participant;
import com.linking.project.domain.Project;
import com.linking.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface AssignRepository extends JpaRepository<Assign, Long> {

    @Query("SELECT NEW com.linking.assign.dto.AssignCountRes" +
            " (p, COUNT(a.assignId), SUM(CASE WHEN a.status = :status THEN 1 ELSE 0 END))" +
            " FROM Assign a right outer join Participant p ON a.participant = p" +
            " WHERE p in :partList GROUP BY p.participantId")
    List<AssignCountRes> findCountByParticipantAndStatus(@Param("status") Status status, @Param("partList") List<Participant> partList);

    List<Assign> findByTodo(@Param("todo") Todo todo);

    Assign findByTodoAndParticipant(@Param("todo") Todo todo, @Param("Participant") Participant participant);

    @Query("SELECT a FROM Assign a WHERE DATE(:date) > a.todo.dueDate AND a.status = :status")
    List<Assign> findByDateAndStatus(@Param("date") LocalDate date, @Param("status") Status status);

    @Query(value = "SELECT a FROM Assign a" +
            " WHERE a.participant in :partList AND (a.status = 'INCOMPLETE' OR a.status = 'INCOMPLETE_PROGRESS')" +
            " AND function('date_format', :date, '%Y%m%d') > a.todo.dueDate")
    Set<Assign> findByParticipantAndStatusAndDate(
            @Param("partList") List<Participant> partList, @Param("date") LocalDate date);

    @Query(value = "SELECT a FROM Assign a" +
            " WHERE a.participant in :partList" +
            " AND function('date_format', :date, '%Y%m%d') = function('date_format', a.todo.dueDate, '%Y%m%d')")
    List<Assign> findByParticipantAndDate(@Param("partList") List<Participant> partList, @Param("date") LocalDate date);

    @Query(value = "SELECT a.todo FROM Assign a" +
            " WHERE :project = a.todo.project AND (a.status = 'INCOMPLETE' OR a.status = 'INCOMPLETE_PROGRESS')" +
            " AND function('date_format', :date, '%Y%m%d') > a.todo.dueDate")
    Set<Todo> findByProjectAndStatusAndDate(
            @Param("project") Project project, @Param("date") LocalDate date);

    @Query(value = "SELECT a FROM Assign a" +
            " WHERE a.participant in :partList" +
            " AND function('date_format', :date, '%Y%m%d')" +
            " BETWEEN function('date_format', a.todo.startDate, '%Y%m%d') AND function('date_format', a.todo.dueDate, '%Y%m%d') ")
    List<Assign> findByParticipantAndDateContains(@Param("partList") List<Participant> partList, @Param("date") LocalDate date);

}
