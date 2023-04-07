package com.linking.pageCheck.domain;

import com.linking.page.domain.Page;
import com.linking.participant.domain.Participant;
import com.linking.user.domain.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagecheck")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate
public class PageCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagecheck_id")
    private Long id;

    // nullable
    private LocalDateTime lastChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    public PageCheck(Participant participant, Page page) {
        setParticipant(participant);
        setPage(page);
        updateLastChecked();
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public void setPage(Page page) {
        this.page = page;
        if (!page.getPageCheckList().contains(this)) {
            page.getPageCheckList().add(this);
        }
    }

    public void updateLastChecked() {
        this.lastChecked = LocalDateTime.now();
    }
}
