package com.linking.pageCheck.persistence;

import com.linking.pageCheck.domain.PageCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageCheckRepository extends JpaRepository<PageCheck, Long> {
}
