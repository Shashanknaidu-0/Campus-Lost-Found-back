package com.campus.lostandfound.controller;

import com.campus.lostandfound.model.Message;
import com.campus.lostandfound.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.sendMessage(message));
    }

    @GetMapping("/thread")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Message> getThread(@RequestParam Long itemId, @RequestParam Long user1, @RequestParam Long user2) {
        return messageService.getThread(itemId, user1, user2);
    }

    @GetMapping("/inbox")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Message> getInbox(@RequestParam Long userId) {
        return messageService.getUserMessages(userId);
    }
}
