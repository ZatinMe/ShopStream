package com.shopstream.marketplace.repository;

import com.shopstream.marketplace.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for OutboxEvent entities.
 */
@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    
    /**
     * Find all unpublished events ordered by creation time.
     */
    @Query("SELECT e FROM OutboxEvent e WHERE e.published = false ORDER BY e.createdAt ASC")
    List<OutboxEvent> findUnpublishedEvents();
    
    /**
     * Find all unpublished events for a specific aggregate type.
     */
    @Query("SELECT e FROM OutboxEvent e WHERE e.published = false AND e.aggregateType = ?1 ORDER BY e.createdAt ASC")
    List<OutboxEvent> findUnpublishedEventsByAggregateType(String aggregateType);
}
