package com.JacobArthurs.ExpenseTracker.repository;

import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByCreatedBy(User createdBy, Sort sort);
    Long countByCreatedBy(User createdBy);
    Page<Category> findAll(Specification<Category> spec, Pageable pageable);
}
