package com.example.demom3erp.entity;

import com.example.demom3erp.config.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Entity
@Table(name = "ORDER_ITEM_TABLE")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE order_items SET deleted_flag = true, deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_flag = false")
public class OrderItem extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;

//    @Column(name = "is_deleted", columnDefinition = "TINYINT(1) DEFAULT 0")
//    private boolean isDeleted;
//    @Column(name = "deleted_flag", nullable = false)
//    private boolean deletedFlag = false;
//
//    @Column(name = "deleted_at")
//    private LocalDateTime deletedAt;
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }

    public OrderItem(Order order, Product product, int quantity, BigDecimal price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

}

