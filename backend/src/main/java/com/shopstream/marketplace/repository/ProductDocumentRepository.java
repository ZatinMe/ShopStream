package com.shopstream.marketplace.repository;

import com.shopstream.marketplace.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument, String> {
    
    /**
     * Find products by category
     */
    List<ProductDocument> findByCategory(String category);
    
    /**
     * Find products by seller ID
     */
    List<ProductDocument> findBySellerId(String sellerId);
    
    /**
     * Find active products
     */
    List<ProductDocument> findByIsActiveTrue();
    
    /**
     * Find products by category and active status
     */
    List<ProductDocument> findByCategoryAndIsActiveTrue(String category);
    
    /**
     * Find products by price range
     */
    List<ProductDocument> findByPriceBetweenAndIsActiveTrue(Double minPrice, Double maxPrice);
    
    /**
     * Find products by brand
     */
    List<ProductDocument> findByBrandAndIsActiveTrue(String brand);
    
    /**
     * Find products by tags
     */
    List<ProductDocument> findByTagsContainingAndIsActiveTrue(String tag);
    
    /**
     * Find products by seller ID and active status
     */
    List<ProductDocument> findBySellerIdAndIsActiveTrue(String sellerId);
    
    /**
     * Find products by title containing text and active status
     */
    List<ProductDocument> findByTitleContainingAndIsActiveTrue(String title);
}
