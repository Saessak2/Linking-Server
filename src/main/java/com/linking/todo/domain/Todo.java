package com.linking.todo.domain;

import com.linking.assign.domain.Assign;
import com.linking.project.domain.Project;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_todo_id")
    private Todo parentTodo;

    @Column(name = "is_parent", nullable = false)
    @ColumnDefault("true")
    private boolean isParent;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false, length = 28)
    @ColumnDefault("")
    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentTodo", cascade = CascadeType.ALL)
    private List<Todo> childTodoList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "todo", cascade = CascadeType.ALL)
    private List<Assign> assignList;

    public Todo(Long todoId){
        this.todoId = todoId;
    }

    public void setAssignList(List<Assign> assignList) {
        this.assignList = assignList;
    }

}
