package com.linking.pageCheck.domain;

import com.linking.page.domain.Page;
import com.linking.participant.domain.Participant;
import com.linking.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagecheck")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class PageCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagecheck_id")
    private Long id;

    // nullable
    private LocalDateTime lastChecked;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "page_id")
    private Page page;


    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public void setPage(Page page) {
        this.page = page;
        if (!page.getPageCheckList().contains(this)) {
            page.getPageCheckList().add(this);
        }
    }
}
