package com.linking.block.persistence;

import com.linking.block.domain.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("select b from Block b where b.page.id = :pageId order by b.blockOrder asc")
    List<Block> findAllByPageId(@Param("pageId") Long pageId);

}
