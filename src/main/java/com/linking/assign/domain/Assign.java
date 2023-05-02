package com.linking.assign.domain;

import com.linking.participant.domain.Participant;
import com.linking.todo.domain.Todo;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "assign")
public class Assign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assign_id")
    private Long assignId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @Enumerated(value = STRING)
    @Column(nullable = false, length = 20)
    private Status status;

}
