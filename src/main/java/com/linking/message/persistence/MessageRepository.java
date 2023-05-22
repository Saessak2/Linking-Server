package com.linking.message.persistence;

import com.linking.chatroom.domain.ChatRoom;
import com.linking.message.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT m FROM Message m" +
            " WHERE m.chatroom = :chatRoom ORDER by m.sentDatetime DESC")
    Page<Message> findMessagesByChatroom(ChatRoom chatRoom, Pageable pageable);

}