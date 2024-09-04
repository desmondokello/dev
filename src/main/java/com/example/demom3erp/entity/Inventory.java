package com.example.demom3erp.entity;


import com.example.demom3erp.config.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "INVENTORY_TABLE")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE inventories SET deleted_flag = true, deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_flag = false")
public class Inventory extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
//    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String location;

//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
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

    public Inventory(Product product, int quantity, String location) {
        this.product = product;
        this.quantity = quantity;
        this.location = location;
    }


}
