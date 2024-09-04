package com.example.demom3erp.entity;


import com.example.demom3erp.config.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDER_TABLE")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE orders SET deleted_flag = true, deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_flag = false")
public class Order extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();

//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
////
////    @Column(name = "is_deleted", columnDefinition = "TINYINT(1) DEFAULT 0")
////    private boolean isDeleted;
//
//    @Column(name = "deleted_flag", nullable = false)
//    private boolean deletedFlag = false;
//
//    @Column(name = "deleted_at")
//    private LocalDateTime deletedAt;

//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }
    public Order(Customer customer, List<OrderItem> items, BigDecimal total, String status) {
        this.customer = customer;
        this.orderItems = items;
        this.totalPrice = total;
        this.status = status;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }
}

