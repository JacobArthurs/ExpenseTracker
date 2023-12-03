package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.CategoryRequestDto;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.repository.CategoryRepository;
import com.JacobArthurs.ExpenseTracker.util.CategoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long  id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category createCategory(CategoryRequestDto request) {
        var category = CategoryUtils.convertRequestToObject(request);
        category.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryRequestDto request) {
        if (categoryRepository.existsById(id)) {
            var category = CategoryUtils.convertRequestToObject(request);
            category.setId(id);

            return categoryRepository.save(category);
        } else {
            return null;
        }
    }

    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
