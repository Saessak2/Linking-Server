package com.linking.todo.persistence;

import com.linking.assign.domain.Status;
import com.linking.project.domain.Project;
import com.linking.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query(value = "SELECT t FROM Todo t" +
            " WHERE :project = t.project" +
            " AND function('date_format', :today, '%Y%m%d') = function('date_format', t.dueDate, '%Y%m%d')")
    List<Todo> findByProjectAndMonth(@Param("project") Project project, @Param("today") LocalDate today);

    @Query(value = "SELECT t FROM Todo t" +
            " WHERE :project = t.project" +
            " AND function('date_format', :date, '%Y%m%d')" +
            " BETWEEN function('date_format', t.startDate, '%Y%m%d') AND function('date_format', t.dueDate, '%Y%m%d')")
    List<Todo> findByProjectAndDateContains(@Param("project") Project project, @Param("date") LocalDate date);

    @Query(value = "SELECT t FROM Todo t" +
            " WHERE :project = t.project" +
            " AND function('date_format', :date, '%Y%m')" +
            " BETWEEN function('date_format', t.startDate, '%Y%m') AND function('date_format', t.dueDate, '%Y%m')")
    List<Todo> findByProjectAndMonthContains(@Param("project") Project project, @Param("date") LocalDate date);
}
