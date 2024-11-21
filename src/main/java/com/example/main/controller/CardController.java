package com.example.main.controller;

import com.example.main.models.Card;
import com.example.main.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    
    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        Card createdCard = cardService.create(card);
        return ResponseEntity.ok(createdCard);
    }

    
    @GetMapping
    public ResponseEntity<Map<String, List<Card>>> getCardsByStatus() {
        Map<String, List<Card>> cardsByStatus = cardService.getCardsByStatus();
        return ResponseEntity.ok(cardsByStatus);
    }

    
    @PutMapping("/{id}/move")
    public ResponseEntity<Card> moveCard(@PathVariable Long id) {
        Card movedCard = cardService.move(id);
        return ResponseEntity.ok(movedCard);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable Long id, @RequestBody Card updatedCard) {
        Card card = cardService.update(id, updatedCard);
        return ResponseEntity.ok(card);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> report() {
        Map<String, Object> report = cardService.report();
        return ResponseEntity.ok(report);
    }

    
    @GetMapping("/sorted")
    public ResponseEntity<List<Card>> getCardSortedPriority(@RequestParam Card.Status status) {
        List<Card> sortedCard = cardService.getCardsByPriority(status);
        return ResponseEntity.ok(sortedCard);
    }

    
    @GetMapping("/filter")
    public ResponseEntity<List<Card>> filterCardsPriorityDate(
            @RequestParam Card.Priority priority,
            @RequestParam String dueDate) {
        List<Card> filteredCard = cardService.filterCardsPriorityDate(
                priority, java.time.LocalDate.parse(dueDate));
        return ResponseEntity.ok(filteredCard);
    }
}
