package com.linking.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class GroupRepository {

    private final EntityManager em;

    @Autowired
    public GroupRepository(EntityManager em) {
        this.em = em;
    }

    public Long save(Group group) {
        em.persist(group);
        return group.getId();
    }

    public Group findById(Long id) {
        return em.find(Group.class, id);
    }



}
