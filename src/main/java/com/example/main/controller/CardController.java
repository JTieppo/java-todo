package com.example.main.controller;

import com.example.main.models.Card;
import com.example.main.service.CardeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Card")
public class CardController {
    private final Cardervice Cardervice;

    public TaskController(CardService cardService) {
        this.cardService = cardService;
    }

    
    @PostMapping
    public ResponseEntity<Card> createTask(@RequestBody Card card) {
        Card createdTask = cardService.createTask(card);
        return ResponseEntity.ok(createdTask);
    }

    
    @GetMapping
    public ResponseEntity<Map<String, List<Card>>> getAllCardByStatus() {
        Map<String, List<Card>> cardsByStatus = cardService.getAllCardOrganizedByStatus();
        return ResponseEntity.ok(cardsByStatus);
    }

    
    @PutMapping("/{id}/move")
    public ResponseEntity<Card> moveTask(@PathVariable Long id) {
        Card movedTask = cardService.moveTask(id);
        return ResponseEntity.ok(movedTask);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateTask(@PathVariable Long id, @RequestBody Card updatedTask) {
        Card card = Cardervice.updateTask(id, updatedTask);
        return ResponseEntity.ok(task);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Cardervice.deleteTask(id);
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
