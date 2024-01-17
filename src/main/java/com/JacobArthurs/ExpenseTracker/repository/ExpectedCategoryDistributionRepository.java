package com.JacobArthurs.ExpenseTracker.repository;

import com.JacobArthurs.ExpenseTracker.model.ExpectedCategoryDistribution;
import com.JacobArthurs.ExpenseTracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpectedCategoryDistributionRepository extends JpaRepository<ExpectedCategoryDistribution, Long> {
    List<ExpectedCategoryDistribution> findAllByCreatedBy(User createdBy, Sort sort);
    Page<ExpectedCategoryDistribution> findAll(Specification<ExpectedCategoryDistribution> spec, Pageable pageable);
}
