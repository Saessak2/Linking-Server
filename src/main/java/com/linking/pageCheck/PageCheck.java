package com.linking.pageCheck;

import com.linking.page.Page;
import com.linking.page.PageService;
import com.linking.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "page_check")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PageCheck {
    /**
     * field
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_check_id")
    private Long id;

    // TODO nullable
    private LocalDateTime lastChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    /**
     * constructor
     */

    public PageCheck(LocalDateTime lastChecked, User user, Page page) {
        this.lastChecked = lastChecked;
        this.user = user;
        this.page = page;
    }

    /**
     * method
     */

    public void setUser(User user) {
        this.user = user;
    }

    public void setPage(Page page) {
        this.page = page;
        if (!page.getPageCheckList().contains(this)) {
            page.getPageCheckList().add(this);
        }
    }
}
