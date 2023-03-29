package com.linking.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query(value = "SELECT d FROM Document d WHERE d.project.id = :projectId")
    List<Document> findAllByProjectId(@Param("projectId") Long projectId);

}
