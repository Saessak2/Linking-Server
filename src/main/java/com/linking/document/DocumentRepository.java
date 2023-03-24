package com.linking.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class DocumentRepository {

    private final EntityManager em;

    @Autowired
    public DocumentRepository(EntityManager em) {
        this.em = em;
    }

    public Long savePage(Page page) {
        em.persist(page);
        return page.getId();
    }

    public Long saveGroup(Group group) {
        em.persist(group);
        return group.getId();
    }

    public Group findGroupById(Long id) {
        return em.find(Group.class, id);
    }

    public Page findPageById(Long id) {
        return em.find(Page.class, id);
    }

    // TODO 실제로는 프로젝트 아이디를 파라미터로 가져야함.
    public List<Document> findAllDocuments() {
//        String jpql = "select d from Document as d where project_id = {}";

        String jpql = "select d from Document as d";
        List<Document> resultList =
            em.createQuery(jpql, Document.class).getResultList();
        return resultList;
    }

}
