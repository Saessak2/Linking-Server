package com.linking.group;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "\"group\"")
@Setter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    private String name;

//    @NotNull
    private int depth;

    @Column(name = "\"index\"")
//    @NotNull
    // Todo int형 default로 0이 들어가서 값을 입력해주지 않아도 notnull 예외가 발생하지 않는 문제. index가 0으로 들어가면 안됨.
    private int index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_group_id")
    private Group parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Group> childList;  //나중에 이름 변경

    @PrePersist
    public void prePersist() {
        this.name = this.name == null ? "New Group" : this.name;
    }

    // 프로젝트 pk

//    setParent가 필요있을까

//    public void addChild(Group group) {
//        if ()
//        childList.add(group);
//    }
}

