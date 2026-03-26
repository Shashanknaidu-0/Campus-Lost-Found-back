package com.campus.lostandfound.service;

import com.campus.lostandfound.model.Item;
import com.campus.lostandfound.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AiTaggingService aiTaggingService;

    public Item saveItem(Item item) {
        // Mock AI tags generation
        String tags = aiTaggingService.generateTags(item.getTitle(), item.getDescription());
        item.setAiTags(tags);
        
        // Find opposite status items for mock probability updates
        Item savedItem = itemRepository.save(item);
        calculateAndSetProbabilities(savedItem);
        return savedItem;
    }

    private void calculateAndSetProbabilities(Item newItem) {
        String targetStatus = newItem.getStatus().equalsIgnoreCase("LOST") ? "FOUND" : "LOST";
        List<Item> oppositeItems = itemRepository.findByStatus(targetStatus);
        
        double maxProb = 0.0;
        for (Item existingItem : oppositeItems) {
            double prob = aiTaggingService.calculateMatchProbability(newItem.getAiTags(), existingItem.getAiTags());
            if (prob > maxProb) {
                maxProb = prob;
            }
        }
        newItem.setMatchProbability(maxProb);
        itemRepository.save(newItem);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
