package com.linking.document;

import com.linking.project.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 상속 - 조인전략
 * 장점) 테이블 정규화, 외래키 참조 무결성 제약조건 활용 가능, 저장공간 효율적
 * 단점) 조회 시 조인 많이 발생, 조회 쿼리 복잡, insert sql 두번 실행
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "document")
public abstract class Document {

    /**
     * field
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long id;

//    만약 dtype이 꼭 필요하다면 dtype을 필드를 만들고 읽기 전용(insert=false, update=false)으로 JPA @Column 컬럼 매핑하시면 됩니다.
    @Column(insertable = false, updatable = false, name = "dtype")
    private String dType;

    @NotNull
    private int doc_depth;

    @NotNull    // Todo int형 default로 0이 들어가서 값을 입력해주지 않아도 notnull 예외가 발생하지 않는 문제. index가 0으로 들어가면 안됨.
    private int doc_index;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    /**
     * constructor
     *
     * @Builder
     * Document is abstract; cannot be instantiated
     */


    public Document(int doc_depth, int doc_index, Project project) {
        this.doc_depth = doc_depth;
        this.doc_index = doc_index;
        this.project = project;
    }

    /**
     * method
     */


    // TODO Page를 setParent로 하면 안되는데 어떻게 막지??

    public void setProject(Project project) {
        this.project = project;
        if (!project.getDocumentList().contains(this)) {
            project.getDocumentList().add(this);
        }
    }
}
