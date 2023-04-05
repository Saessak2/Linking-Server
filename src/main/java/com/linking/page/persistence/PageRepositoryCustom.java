package com.linking.page.persistence;

import com.linking.page.domain.Page;

import java.util.List;

public interface PageRepositoryCustom {

    public List<List<Page>> findPages(Long beforeGroupId, Long afterGroupId);
}
