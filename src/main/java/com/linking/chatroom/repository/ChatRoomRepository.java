package com.linking.chatroom.repository;

import com.linking.chatroom.domain.ChatRoom;
import com.linking.project.domain.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @EntityGraph(attributePaths = {"project"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<ChatRoom> findChatRoomByProject(Project project);

}
