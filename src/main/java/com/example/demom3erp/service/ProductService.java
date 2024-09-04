package com.example.demom3erp.service;


import com.example.demom3erp.dto.ProductDto;
import com.example.demom3erp.entity.Product;
import com.example.demom3erp.exception.InvalidProductException;
import com.example.demom3erp.exception.ResourceNotFoundException;
import com.example.demom3erp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    // Convert Product entity to ProductDto
    private ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }

    // Convert ProductDto to Product entity
    private Product convertToEntity(ProductDto productDto) {
        return new Product(productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getStock());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long productId) {
        logger.info("Fetching product with id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return convertToDto(product);
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        logger.info("Creating new product: {}", productDto.getName());
        Product product = convertToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        logger.info("Updating product with id: {}", productId);
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (productDto.getPrice().compareTo(BigDecimal.ZERO) < 0 || productDto.getStock() < 0) {
            throw new InvalidProductException("Product price and stock must be non-negative");
        }

        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStock(productDto.getStock());

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDto(updatedProduct);
    }


    @Transactional
    public void deleteProduct(Long productId) {
        logger.info("Deleting product with id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        product.setDeletedFlag(true);
        productRepository.save(product);
    }

}

