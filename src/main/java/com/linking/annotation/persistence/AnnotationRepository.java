package com.linking.annotation.persistence;

import com.linking.annotation.domain.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {

    @Query("select a from Annotation a where a.block.id = :id")
    Annotation findByBlockId(@Param("id") Long id);
}
