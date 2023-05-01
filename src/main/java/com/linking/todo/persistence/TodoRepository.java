package com.linking.todo.persistence;

import com.linking.project.domain.Project;
import com.linking.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query(value = "SELECT t FROM Todo t WHERE :project = t.project " +
            "AND :date BETWEEN t.startDate and t.dueDate AND t.isParent = true")
    List<Todo> findByProjectAndDate(@Param("project") Project project, @Param("date") LocalDate date);

    @Query(value = "SELECT t FROM Todo t WHERE :project = t.project" +
            " AND function('date_format', :today, '%Y%m') BETWEEN t.startDate AND t.dueDate")
    List<Todo> findByProjectAndMonth(@Param("project") Project project, @Param("today") LocalDate today);

}
