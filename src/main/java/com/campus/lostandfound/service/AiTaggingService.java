package com.campus.lostandfound.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AiTaggingService {

    // Mock AI: extracts 3+ letter words, lowercased, ignoring common stop words.
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "the", "and", "this", "that", "with", "from", "for", "was", "were"
    ));

    public String generateTags(String title, String description) {
        String text = (title + " " + description).toLowerCase().replaceAll("[^a-z\\s]", "");
        Set<String> tags = new HashSet<>();
        
        for (String word : text.split("\\s+")) {
            if (word.length() > 3 && !STOP_WORDS.contains(word)) {
                tags.add(word);
            }
        }
        return String.join(",", tags);
    }

    // Jaccard similarity: intersection / union
    public double calculateMatchProbability(String tags1, String tags2) {
        if (tags1 == null || tags2 == null || tags1.isEmpty() || tags2.isEmpty()) {
            return 0.0;
        }

        Set<String> set1 = new HashSet<>(Arrays.asList(tags1.split(",")));
        Set<String> set2 = new HashSet<>(Arrays.asList(tags2.split(",")));

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) return 0.0;

        return (double) intersection.size() / union.size() * 100.0;
    }
}
