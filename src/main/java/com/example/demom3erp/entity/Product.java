package com.example.demom3erp.entity;

import com.example.demom3erp.config.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "PRODUCT_TABLE")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE PRODUCT_TABLE SET deleted_flag = true, deleted_at = NOW() WHERE id = ?")
//@DialectOverride.Wheres(value = "deleted_flag = false")
@Where(clause = "deleted_flag = false")
public class Product extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2) // Ensure correct use of DECIMAL
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;



    public Product(String name, String description, BigDecimal price, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }


}
