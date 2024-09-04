package com.example.demom3erp.entity;

import com.example.demom3erp.config.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;


@Entity
@Table(name = "CUSTOMER_TABLE")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE customers SET deleted_flag = true, deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_flag = false")
public class Customer extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
////    @Column(name = "is_deleted", columnDefinition = "TINYINT(1) DEFAULT 0")
////    private boolean isDeleted;
//    @Column(name = "deleted_flag", nullable = false)
//    private boolean deletedFlag = false;
//
//    @Column(name = "deleted_at")
//    private LocalDateTime deletedAt;

//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

}

