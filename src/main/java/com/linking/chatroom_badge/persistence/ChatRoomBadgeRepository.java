package com.linking.chatroom_badge.persistence;

import com.linking.chatroom_badge.domain.ChatRoomBadge;
import com.linking.participant.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomBadgeRepository extends JpaRepository<ChatRoomBadge, Long> {

    @Query(value = "SELECT crb FROM ChatRoomBadge crb WHERE crb.participant IN :participantList")
    List<ChatRoomBadge> findChatRoomBadgesByParticipantContaining(@Param("participantList") List<Participant> participantList);


}
