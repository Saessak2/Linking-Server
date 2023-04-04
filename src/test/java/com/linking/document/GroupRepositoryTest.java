package com.linking.document;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class GroupRepositoryTest {
//
//    @Autowired
//    private GroupRepository groupRepository;
//
//    @Test
//    @Rollback(value = false)
//    void saveTest() {
//        Group group = Group.builder()
//                .name("document")
//                .doc_depth(0)
//                .doc_index(0)
//                .childDocList(new ArrayList<>())
//                .build();
//        groupRepository.save(group);
//
//        Group group2 = Group.builder()
//                .name("그룹1")
//                .doc_depth(1)
//                .doc_index(0)
//                .childDocList(new ArrayList<>())
//                .build();
//        groupRepository.save(group2);
////        Group group1 = groupRepository.findById(group2.getId())
////                .orElseThrow(() -> new NoSuchElementException());
//
//        groupRepository.findById(group2.getId()).get().setParent(group); // 수정되는지 확인
//        groupRepository.save(group2);
//
//        Group group3 = Group.builder()
//                .name("그룹2")
//                .doc_depth(1)
//                .doc_index(1)
//                .parent(group)
//                .childDocList(new ArrayList<>())
//                .build();
//        groupRepository.save(group3);
//
//        Optional<Group> groupById = groupRepository.findById(group.getId());
//        groupById.get().addChild(group2);
//        groupById.get().addChild(group3);
//        groupRepository.save(groupById.get());
//        //save 안함
//
//        Assertions.assertThat(groupById.get().getChildDocList().get(0)).isEqualTo(group2);
//    }
//
//    @Test
//    @Rollback(value = false)
//    void deleteTest() {
//        Group group = Group.builder()
//                .name("document")
//                .doc_depth(0)
//                .doc_index(0)
//                .childDocList(new ArrayList<>())
//                .build();
//        groupRepository.save(group);
//
//        Group group2 = Group.builder()
//                .name("그룹1")
//                .doc_depth(1)
//                .doc_index(0)
//                .childDocList(new ArrayList<>())
//                .build();
//        groupRepository.save(group2);
//
//        groupRepository.findById(group2.getId()).get().setParent(group); // 수정되는지 확인
//        groupRepository.save(group2);
//
//        Optional<Group> groupById = groupRepository.findById(group.getId());
//        groupById.get().addChild(group2);
//        groupRepository.save(groupById.get());
//        Long id = groupById.get().getId();
//        groupRepository.delete(groupById.get());
////        Assertions.assertThatThrownBy(() -> groupRepository.findById(1L))
////                .isInstanceOf(NoSuchElementException.class);
//
//    }
}
