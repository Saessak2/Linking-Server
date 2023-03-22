package com.linking.group;

import org.assertj.core.api.Assertions;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    @Rollback(value = false)
    void save() {
        Group group = Group.builder()
                .name("document")
                .index(0)
                .depth(0)
                .build();

        Long id = groupRepository.save(group);

        Group findGroup = groupRepository.findById(id);
        assertThat(group).isEqualTo(findGroup);
    }

    @Test
    @Rollback(value = false)
    void findAll() {
        Group group1 = Group.builder()
                .name("document")
                .index(0)
                .depth(0)
                .build();
        groupRepository.save(group1);

        Group group2 = Group.builder()
                .name("그룹2")
                .index(0)
                .depth(1)
                .parent(group1)
                .build();
        groupRepository.save(group2);

        Group group3 = Group.builder()
                .name("그룹3")
                .index(1)
                .depth(1)
                .parent(group1)
                .build();
        groupRepository.save(group3);

        Group group4 = Group.builder()
                .name("그룹4")
                .index(2)
                .depth(1)
                .parent(group1)
                .build();
        groupRepository.save(group4);

        Group findGroup = groupRepository.findById(group1.getId());

        System.out.println(findGroup.toString());
    }

    @Test
    @Rollback(value = true)
    void nameLengthTest() {
        // 10자 이하 이름
        Group group = Group.builder()
                .name("가나다라마바사아자차")
                .index(2)
                .depth(1)
                .build();

        groupRepository.save(group);

        // 10자 초과 이름 예외발생
        Group group1 = Group.builder()
                .name("가나다라마바사아자차카타파하")
                .index(2)
                .depth(1)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            groupRepository.save(group1);
        });
    }

    @Test
    @Rollback(value = false)
    void notNullTest() {
        // 이름 null
        Group group = Group.builder()
                .index(0)
                .depth(0)
                .build();

        Long id = groupRepository.save(group);
        Group findGroup = groupRepository.findById(id);
        assertThat(findGroup.getName()).isEqualTo("New Group");

        // depth Null - index 마찬가지
        Group group1 = Group.builder()
                .name("그룹1")
                .depth(0)
                .build();

        groupRepository.save(group1);
//        assertThrows(RuntimeException.class, () -> {
//            groupRepository.save(group1);
//        });
//
    }

}
