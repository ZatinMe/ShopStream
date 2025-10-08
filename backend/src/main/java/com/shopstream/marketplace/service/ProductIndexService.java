package com.shopstream.marketplace.service;

import com.shopstream.marketplace.document.ProductDocument;
import com.shopstream.marketplace.model.Product;
import com.shopstream.marketplace.repository.ProductDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductIndexService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductIndexService.class);
    
    @Autowired
    private ProductDocumentRepository productDocumentRepository;
    
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    /**
     * Index a product in Elasticsearch
     */
    public void indexProduct(Product product) {
        try {
            logger.info("Indexing product: {}", product.getId());
            
            ProductDocument document = convertToDocument(product);
            productDocumentRepository.save(document);
            
            logger.info("Successfully indexed product: {} in Elasticsearch", product.getId());
        } catch (Exception e) {
            logger.error("Failed to index product: {}", product.getId(), e);
            throw new RuntimeException("Failed to index product: " + product.getId(), e);
        }
    }

    /**
     * Update an existing product in Elasticsearch
     */
    public void updateProduct(Product product) {
        try {
            logger.info("Updating product in index: {}", product.getId());
            
            ProductDocument document = convertToDocument(product);
            document.setUpdatedAt(LocalDateTime.now());
            
            productDocumentRepository.save(document);
            logger.info("Successfully updated product: {} in index", product.getId());
        } catch (Exception e) {
            logger.error("Failed to update product: {}", product.getId(), e);
            throw new RuntimeException("Failed to update product: " + product.getId(), e);
        }
    }

    /**
     * Delete a product from Elasticsearch
     */
    public void deleteProduct(String productId) {
        try {
            logger.info("Deleting product from index: {}", productId);
            
            productDocumentRepository.deleteById(productId);
            logger.info("Successfully deleted product: {} from index", productId);
        } catch (Exception e) {
            logger.error("Failed to delete product: {}", productId, e);
            throw new RuntimeException("Failed to delete product: " + productId, e);
        }
    }

    /**
     * Get a product document by ID
     */
    public Optional<ProductDocument> getProductDocument(String productId) {
        try {
            return productDocumentRepository.findById(productId);
        } catch (Exception e) {
            logger.error("Failed to get product document: {}", productId, e);
            return Optional.empty();
        }
    }

    /**
     * Search products by text query
     */
    public List<ProductDocument> searchProducts(String query) {
        try {
            // For now, simple text search - will be enhanced in Phase 3 Chunk 3
            return productDocumentRepository.findByTitleContainingAndIsActiveTrue(query);
        } catch (Exception e) {
            logger.error("Failed to search products with query: {}", query, e);
            return List.of();
        }
    }

    /**
     * Get all active products
     */
    public List<ProductDocument> getAllActiveProducts() {
        try {
            return productDocumentRepository.findByIsActiveTrue();
        } catch (Exception e) {
            logger.error("Failed to get all active products", e);
            return List.of();
        }
    }

    /**
     * Get products by category
     */
    public List<ProductDocument> getProductsByCategory(String category) {
        try {
            return productDocumentRepository.findByCategoryAndIsActiveTrue(category);
        } catch (Exception e) {
            logger.error("Failed to get products by category: {}", category, e);
            return List.of();
        }
    }

    /**
     * Get products by seller
     */
    public List<ProductDocument> getProductsBySeller(String sellerId) {
        try {
            return productDocumentRepository.findBySellerIdAndIsActiveTrue(sellerId);
        } catch (Exception e) {
            logger.error("Failed to get products by seller: {}", sellerId, e);
            return List.of();
        }
    }

    /**
     * Convert Product entity to ProductDocument
     */
    private ProductDocument convertToDocument(Product product) {
        ProductDocument document = new ProductDocument();
        
        document.setId(product.getId().toString());
        document.setTitle(product.getName());
        document.setDescription(product.getDescription());
        document.setPrice(product.getPrice().doubleValue());
        document.setCategory(product.getCategory());
        document.setCreatedAt(product.getCreatedAt());
        document.setUpdatedAt(LocalDateTime.now());
        document.setIsActive(true);
        
        // Set additional fields if available
        if (product.getTags() != null && !product.getTags().isEmpty()) {
            document.setTags(product.getTags());
        }
        
        // Mock seller information (in real app, this would come from user service)
        document.setSellerId("seller-" + product.getId());
        document.setSellerName("Seller " + product.getId());
        
        // Mock additional product attributes
        document.setStockQuantity(100); // Default stock
        document.setBrand("ShopStream Brand");
        document.setColor("Various");
        document.setSize("One Size");
        
        return document;
    }

    /**
     * Check if Elasticsearch is available
     */
    public boolean isElasticsearchAvailable() {
        try {
            elasticsearchOperations.indexOps(ProductDocument.class).exists();
            return true;
        } catch (Exception e) {
            logger.error("Elasticsearch is not available", e);
            return false;
        }
    }

    /**
     * Create the products index if it doesn't exist
     */
    public void createIndexIfNotExists() {
        try {
            if (!elasticsearchOperations.indexOps(ProductDocument.class).exists()) {
                elasticsearchOperations.indexOps(ProductDocument.class).create();
                logger.info("Created products_index_v1 index");
            } else {
                logger.info("Products index already exists");
            }
        } catch (Exception e) {
            logger.error("Failed to create products index", e);
            throw new RuntimeException("Failed to create products index", e);
        }
    }
}
