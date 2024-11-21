package com.example.main.controller;

import com.example.main.models.Card;
import com.example.main.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Card")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    
    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        Card createdCard = cardService.createCard(card);
        return ResponseEntity.ok(createdCard);
    }

    
    @GetMapping
    public ResponseEntity<Map<String, List<Card>>> getAllCardByStatus() {
        Map<String, List<Card>> cardsByStatus = cardService.getAllCardOrganizedByStatus();
        return ResponseEntity.ok(cardsByStatus);
    }

    
    @PutMapping("/{id}/move")
    public ResponseEntity<Card> moveCard(@PathVariable Long id) {
        Card movedCard = cardService.moveCard(id);
        return ResponseEntity.ok(movedCard);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable Long id, @RequestBody Card updatedCard) {
        Card card = cardService.updateCard(id, updatedCard);
        return ResponseEntity.ok(card);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> generateReport() {
        Map<String, Object> report = cardService.generateReport();
        return ResponseEntity.ok(report);
    }

    
    @GetMapping("/sorted")
    public ResponseEntity<List<Card>> getCardSortedByPriority(@RequestParam Card.Status status) {
        List<Card> sortedCard = cardService.getCardSortedByPriority(status);
        return ResponseEntity.ok(sortedCard);
    }

    
    @GetMapping("/filter")
    public ResponseEntity<List<Card>> filterCardByPriorityAndDueDate(
            @RequestParam Card.Priority priority,
            @RequestParam String dueDate) {
        List<Card> filteredCard = cardService.filterCardByPriorityAndDueDate(
                priority, java.time.LocalDate.parse(dueDate));
        return ResponseEntity.ok(filteredCard);
    }
}
