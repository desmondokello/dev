package com.example.demom3erp.repository;

import com.example.demom3erp.entity.Inventory;
import com.example.demom3erp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
//    int findAvailableStockByProductId(Long productId);

    @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM Inventory i WHERE i.product.id = :productId")
    int findAvailableStockByProductId(@Param("productId") Long productId);

    Optional<Inventory> findByProductId(Long productId);

    Optional<Inventory> findByProduct(Product product);


    // Custom query to find total available stock for a product
    int countByProduct(Product product);
}
