package com.JacobArthurs.ExpenseTracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "expected_category_distribution")
@Getter
@Setter
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
}