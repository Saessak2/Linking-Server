package com.linking.domain.project.persistence;

import com.linking.domain.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT p FROM Project p WHERE p.owner.userId = :ownerId")
    List<Project> findByOwner(@Param("ownerId") Long ownerId);

}
