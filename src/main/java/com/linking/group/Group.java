package com.linking.group;

import com.linking.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "\"group\"")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @Column(columnDefinition = "varchar(10) default 'New Group'")
    private String name;

    @NotNull
    private int depth;

    @Column(name = "\"index\"")
    @NotNull
    private int index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_group_id")
    @NotNull
    private Group parentGroup;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentGroup")
    private List<Group> groupList;

    // 프로젝트 pk

}

