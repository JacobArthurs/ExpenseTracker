package com.JacobArthurs.ExpenseTracker.repository;

import com.JacobArthurs.ExpenseTracker.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findAll(Specification<Expense> spec, Pageable pageable);
}
