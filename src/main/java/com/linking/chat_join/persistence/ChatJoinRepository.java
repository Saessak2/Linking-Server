package com.linking.chat_join.persistence;

import com.linking.chat_join.domain.ChatJoin;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.participant.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatJoinRepository extends JpaRepository<ChatJoin, Long> {

    @Query(value = "SELECT cj.participant FROM ChatJoin cj WHERE cj.chatRoom = :chatRoom")
    List<Participant> findByChatroom(@Param("chatRoom") ChatRoom chatRoom);

}
