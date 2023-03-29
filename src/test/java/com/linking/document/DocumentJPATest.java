package com.linking.document;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class DocumentJPATest {

    @Autowired
    private DocumentRepository documentRepository;

//    @Test
//    @Rollback(value = false)
//    void createTest() {
//        Group group = Group.builder()
////                .name("document")
//                .doc_depth(0)
//                .doc_index(0)
//                .build();
//
//        Long groupId = documentRepository.saveGroup(group);
//        documentRepository.findGroupById(groupId);
//
//        assertThat(group.getName()).isEqualTo("untitled");
//        assertThat(group.getDoc_depth()).isEqualTo(0);
//
//        Page page = Page.builder()
//                .doc_depth(1)
//                .doc_index(0)
//                .createdDatetime(LocalDateTime.now())
//                .updatedDatetime(LocalDateTime.now())
//                .build();
//
//        Long pageId = documentRepository.savePage(page);
//        documentRepository.findPageById(pageId);
//
//        assertThat(page.getName()).isEqualTo("untitled");
//        assertThat(page.getDoc_depth()).isEqualTo(1);
//        assertThat(page.getDoc_index()).isEqualTo(0);
//    }
//
//    @Test
//    @Rollback(value = false)
//    void parentAndChildTest() {
//        Group group = Group.builder()
//                .name("document")
//                .doc_depth(0)
//                .doc_index(0)
//                .documentList(new ArrayList<>())
//                .build();
//        documentRepository.saveGroup(group);
//
//        Group group1 = Group.builder()
//                .name("group1")
//                .doc_depth(1)
//                .doc_index(0)
//                .documentList(new ArrayList<>())
//                .build();
//        group1.setParent(group);
//        documentRepository.saveGroup(group1);
//
//
//        Page page1 = Page.builder()
//                .name("page1")
//                .doc_depth(1)
//                .doc_index(1)
//                .createdDatetime(LocalDateTime.now())
//                .updatedDatetime(LocalDateTime.now())
//                .build();
//        page1.setParent(group);
//        documentRepository.savePage(page1);
//
//        Page page2 = Page.builder()
//                .name("page2")
//                .doc_depth(1)
//                .doc_index(2)
//                .createdDatetime(LocalDateTime.now())
//                .updatedDatetime(LocalDateTime.now())
//                .build();
//        page2.setParent(group);
//        documentRepository.savePage(page2);
//
//        Page page3 = Page.builder()
//                .name("page3")
//                .doc_depth(2)
//                .doc_index(0)
//                .createdDatetime(LocalDateTime.now())
//                .updatedDatetime(LocalDateTime.now())
//                .build();
//        page3.setParent(group1);
//        documentRepository.savePage(page3);
//
//        Group findGroup = documentRepository.findGroupById(group.getId());
//
//        Group findGroup1 = documentRepository.findGroupById(group1.getId());
//        Assertions.assertThat(findGroup1.getParent()).isEqualTo(group);
//
//        Page findPage1 = documentRepository.findPageById(page1.getId());
//        Assertions.assertThat(findPage1.getParent()).isEqualTo(group);
//
//        Page findPage2 = documentRepository.findPageById(page2.getId());
//        Assertions.assertThat(findPage2.getParent()).isEqualTo(group);
//
//        Page findPage3 = documentRepository.findPageById(page3.getId());
//        Assertions.assertThat(findPage3.getParent()).isEqualTo(group1);
//
//        Assertions.assertThat(findGroup.getDocumentList().size()).isEqualTo(3);
//        Assertions.assertThat(findGroup1.getDocumentList().size()).isEqualTo(1);
//
//        Assertions.assertThat(findGroup.getDocumentList().get(0).getName()).isEqualTo("group1");
//        Assertions.assertThat(findGroup.getDocumentList().get(1).getName()).isEqualTo("page1");
//        Assertions.assertThat(findGroup.getDocumentList().get(2).getName()).isEqualTo("page2");
//        Assertions.assertThat(findGroup1.getDocumentList().get(0).getName()).isEqualTo("page3");
//
//        System.out.println("group.getDocumentList() = " + group.getDocumentList());
//        System.out.println("group1.getDocumentList() = " + group1.getDocumentList());
//    }
//
//    @Test
//    @Rollback(value = false)
//    void findAllTest() {
//        Group group = Group.builder()
//                .name("document")
//                .doc_depth(0)
//                .doc_index(0)
//                .documentList(new ArrayList<>())
//                .build();
//        documentRepository.saveGroup(group);
//
//        Group group1 = Group.builder()
//                .name("group1")
//                .doc_depth(1)
//                .doc_index(0)
//                .documentList(new ArrayList<>())
//                .build();
//        group1.setParent(group);
//        documentRepository.saveGroup(group1);
//
//
//        Page page1 = Page.builder()
//                .name("page1")
//                .doc_depth(1)
//                .doc_index(1)
//                .createdDatetime(LocalDateTime.now())
//                .updatedDatetime(LocalDateTime.now())
//                .build();
//        page1.setParent(group);
//        documentRepository.savePage(page1);
//
//        Page page2 = Page.builder()
//                .name("page2")
//                .doc_depth(1)
//                .doc_index(2)
//                .createdDatetime(LocalDateTime.now())
//                .updatedDatetime(LocalDateTime.now())
//                .build();
//        page2.setParent(group);
//        documentRepository.savePage(page2);
//
//        Page page3 = Page.builder()
//                .name("page3")
//                .doc_depth(2)
//                .doc_index(0)
//                .createdDatetime(LocalDateTime.now())
//                .updatedDatetime(LocalDateTime.now())
//                .build();
//        page3.setParent(group1);
//        documentRepository.savePage(page3);
//
//        List<Document> documentList = documentRepository.findAllDocuments();
//        for (Document doc: documentList) {
//            System.out.println("doc = " + doc);
//            System.out.println("doc.getName() = " + doc.getName());
//            System.out.println("doc.getDocumentList() = " + doc.getDocumentList());
//            System.out.println("------------------------------");
//        }
//    }
//
//    @Test
//    @Transactional
//    void findOneTest() {
//        Group group = Group.builder()
//                .name("document")
//                .doc_depth(0)
//                .doc_index(0)
//                .documentList(new ArrayList<>())
//                .build();
//        documentRepository.saveGroup(group);
//
//        Group group1 = Group.builder()
//                .name("group1")
//                .doc_depth(1)
//                .doc_index(0)
//                .documentList(new ArrayList<>())
//                .build();
//        group1.setParent(group);
//        documentRepository.saveGroup(group1);
//
//
//        Page page1 = Page.builder()
//                .name("page1")
//                .doc_depth(1)
//                .doc_index(1)
//                .createdDatetime(LocalDateTime.now())
//                .updatedDatetime(LocalDateTime.now())
//                .build();
//        page1.setParent(group);
//        documentRepository.savePage(page1);
//
//        Page page2 = Page.builder()
//                .name("page2")
//                .doc_depth(1)
//                .doc_index(2)
//                .createdDatetime(LocalDateTime.now())
//                .updatedDatetime(LocalDateTime.now())
//                .build();
//        page2.setParent(group);
//        documentRepository.savePage(page2);
//
//        Page page3 = Page.builder()
//                .name("page3")
//                .doc_depth(2)
//                .doc_index(0)
//                .createdDatetime(LocalDateTime.now())
//                .updatedDatetime(LocalDateTime.now())
//                .build();
//        page3.setParent(group1);
//        documentRepository.savePage(page3);
//
//        Long groupId = documentRepository.detach(group);
//        documentRepository.detach(group1);
//        documentRepository.detach(page1);
//        documentRepository.detach(page2);
//        documentRepository.detach(page3);
//
//        System.out.println("--------------------------------------------");
//
////        Page page = documentRepository.findPageById(page3.getId());
//        Page page = documentRepository.getEm().getReference(Page.class, page3.getId());
//        System.out.println("isLoaded = " + documentRepository.getEm().getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(page));
//        System.out.println("page.getId() = " + page.getId());
//        System.out.println("isLoaded = " + documentRepository.getEm().getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(page));
//        System.out.println("page.getName() = " + page.getName());
//        System.out.println("isLoaded = " + documentRepository.getEm().getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(page));
//
////        Document document = documentRepository.findGroupById(groupId);
////        List<Document> documentList = document.getDocumentList();
////        for (Document doc: documentList) {
////            System.out.println("doc.getClass().getName() = " + doc.getClass().getName());
////            System.out.println("doc.getName() = " + doc.getName());
////            System.out.println("doc.getClass().getName() = " + doc.getClass().getName());
////            System.out.println("---------------------------------------");
////        }
////
////        List<Document> documentList1 = group1.getDocumentList();
////        for (Document doc: documentList1) {
////            System.out.println("doc.getClass().getName() = " + doc.getClass().getName());
////            System.out.println("doc.getName() = " + doc.getName());
////            System.out.println("doc.getClass().getName() = " + doc.getClass().getName());
////            System.out.println("---------------------------------------");
////        }
//
//    }
}
