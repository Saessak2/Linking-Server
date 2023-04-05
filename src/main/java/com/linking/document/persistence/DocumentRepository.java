package com.linking.document.persistence;

import com.linking.document.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Modifying
    @Transactional
    @Query(value = "update Document d set d.title = :title where d.id = :docId")
    void updateTitle(@Param("docId") Long docId, @Param("title") String title);
}
