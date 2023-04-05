package com.linking.group.domain;

import com.linking.document.domain.Document;
import com.linking.page.domain.Page;
import com.linking.project.domain.Project;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "\"group\"")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id")
    private Document document;

    @Builder
    public Group(Document document) {
        this.document = document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}

