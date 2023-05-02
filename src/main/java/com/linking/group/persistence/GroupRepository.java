package com.linking.group.persistence;

import com.linking.group.domain.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query(value = "update Group g set g.name = :name where g.id = :groupId")
//    void updateName(@Param("groupId") Long groupId, @Param("name") String name);

    @EntityGraph(attributePaths = {"pageList"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select g from Group g where g.project.projectId = :projectId order by g.groupOrder asc")
    List<Group> findAllByProjectId(@Param("projectId") Long projectId);
}
