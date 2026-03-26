package com.campus.lostandfound.repository;

import com.campus.lostandfound.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByStatus(String status);
    List<Item> findByReporterId(Long reporterId);
    List<Item> findByCategory(String category);
}
