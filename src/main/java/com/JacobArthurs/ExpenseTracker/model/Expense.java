package com.JacobArthurs.ExpenseTracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.JacobArthurs.ExpenseTracker.dto.ExpenseRequestDto;

@Entity
@Table(name="expense")
@Getter
@Setter
@NoArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    private Category category;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Column(name = "last_updated_date", nullable = false)
    private Timestamp lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User createdBy;

    public Expense(ExpenseRequestDto request, Category category, User createdBy) {
        this.category = category;
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.amount = request.getAmount();
        this.createdDate = new Timestamp(System.currentTimeMillis());
        this.lastUpdatedDate = new Timestamp(System.currentTimeMillis());
        this.createdBy = createdBy;
    }
}
