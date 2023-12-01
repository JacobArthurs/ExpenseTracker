package com.JacobArthurs.ExpenseTracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "minimum_distribution", nullable = false)
    private int minimumDistribution;

    @Column(name = "maximum_distribution", nullable = false)
    private int maximumDistribution;
}