package com.linking.page.persistence;

import com.linking.page.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    @Query(value = "select p from Page p where p.group.id = :groupId order by p.pageOrder asc")
    List<Page> findAllByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT p FROM Page p JOIN FETCH p.pageCheckList WHERE p.id = :pageId")
    Optional<Page> findByIdFetchPageChecks(@Param("pageId") Long pageId);

//    @Query(value = "SELECT p FROM Page p LEFT JOIN FETCH p.blockList WHERE p.id = :pageId")
    @EntityGraph(attributePaths = {"blockList"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT p FROM Page p WHERE p.id = :pageId")
    Optional<Page> findByIdFetchBlocks(@Param("pageId") Long pageId);
}
