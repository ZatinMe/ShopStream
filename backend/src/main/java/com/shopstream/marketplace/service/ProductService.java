package com.shopstream.marketplace.service;

import com.shopstream.marketplace.model.Product;
import com.shopstream.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    
    public Optional<Product> getProductById(String id) {
        return productRepository.findByIdAndIsActiveTrue(id);
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findByIsActiveTrue();
    }
    
    public List<Product> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerIdAndIsActiveTrue(sellerId);
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndIsActiveTrue(category);
    }
    
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
    }
    
    public List<Product> searchProductsByTag(String tag) {
        return productRepository.findByTagsContainingAndIsActiveTrue(tag);
    }
    
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetweenAndIsActiveTrue(minPrice, maxPrice);
    }
    
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteProduct(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            product.get().setIsActive(false);
            productRepository.save(product.get());
        }
    }
    
    public void updateProductStock(String productId, Integer newStock) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            product.get().setStock(newStock);
            productRepository.save(product.get());
        }
    }
}
