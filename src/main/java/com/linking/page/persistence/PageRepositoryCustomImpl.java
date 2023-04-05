package com.linking.page.persistence;

import com.linking.page.domain.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class PageRepositoryCustomImpl extends QuerydslRepositorySupport implements PageRepositoryCustom {

    public PageRepositoryCustomImpl() {
        super(Page.class);
    }

    @Override
    public List<List<Page>> findPages(Long beforeGroupId, Long afterGroupId) {



        return null;
    }
}
