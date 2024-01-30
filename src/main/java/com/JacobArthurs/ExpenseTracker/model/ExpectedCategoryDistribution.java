package com.JacobArthurs.ExpenseTracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "expected_category_distribution")
@Getter
@Setter
@NoArgsConstructor
public class ExpectedCategoryDistribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    private Category category;

    @Column(name = "distribution", nullable = false)
    private int distribution;

    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Column(name = "last_updated_date", nullable = false)
    private Timestamp lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User createdBy;

    public ExpectedCategoryDistribution(Category category, int distribution, Timestamp createdDate, Timestamp lastUpdatedDate, User createdBy) {
        this.category = category;
        this.distribution = distribution;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.createdBy = createdBy;
    }
}