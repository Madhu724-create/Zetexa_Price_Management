package com.zetexa.entity.Reseller;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ResellerCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(nullable = false, unique = true, length = 250)
    private String categoryName;

    private String categoryImage;

    @Column(nullable = false)
    private Integer categoryDiscount;

    private Integer createdBy;
    private Integer updatedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
