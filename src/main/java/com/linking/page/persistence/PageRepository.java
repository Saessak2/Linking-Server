package com.linking.page.persistence;

import com.linking.page.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update Page p set p.title = :title where p.id = :pageId")
    void updateTitle(@Param("pageId") Long pageId, @Param("title") String title);

    @Query(value = "select p from Page p where p.group.id = :groupId")
    List<Page> findAllByGroup(@Param("groupId") Long groupId);
}
