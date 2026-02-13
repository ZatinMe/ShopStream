package com.shopstream.marketplace.repository;

import com.shopstream.marketplace.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    
    List<Product> findBySellerIdAndIsActiveTrue(Long sellerId);
    
    List<Product> findByCategoryAndIsActiveTrue(String category);
    
    List<Product> findByIsActiveTrue();
    
    @Query("{'name': {$regex: ?0, $options: 'i'}, 'isActive': true}")
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
    
    @Query("{'tags': {$in: [?0]}, 'isActive': true}")
    List<Product> findByTagsContainingAndIsActiveTrue(String tag);
    
    @Query("{'price': {$gte: ?0, $lte: ?1}, 'isActive': true}")
    List<Product> findByPriceBetweenAndIsActiveTrue(Double minPrice, Double maxPrice);
    
    Optional<Product> findByIdAndIsActiveTrue(String id);
}
