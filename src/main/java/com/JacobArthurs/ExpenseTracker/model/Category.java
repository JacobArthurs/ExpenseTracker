package com.JacobArthurs.ExpenseTracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

import com.JacobArthurs.ExpenseTracker.dto.CategoryRequestDto;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Column(name = "last_updated_date", nullable = false)
    private Timestamp lastUpdatedDate;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Expense> expenses;

    @OneToOne(mappedBy = "category", cascade = CascadeType.REMOVE)
    private ExpectedCategoryDistribution expectedCategoryDistribution;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User createdBy;

    public Category(String title, String description, Timestamp createdDate, Timestamp lastUpdatedDate, User createdBy) {
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.createdBy = createdBy;
    }

    public Category(CategoryRequestDto request, User createdBy) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.createdDate = new Timestamp(System.currentTimeMillis());
        this.lastUpdatedDate = new Timestamp(System.currentTimeMillis());
        this.createdBy = createdBy;
    }
}