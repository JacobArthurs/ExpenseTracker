package com.JacobArthurs.ExpenseTracker.repository;

import com.JacobArthurs.ExpenseTracker.model.ExpectedCategoryDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpectedCategoryDistributionRepository extends JpaRepository<ExpectedCategoryDistribution, Long> {
}
