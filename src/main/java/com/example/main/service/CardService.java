package com.example.main.service;

import com.example.main.models.Card;
import com.example.main.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

   
    public Card create(Card card) {
        card.setStatus(Card.Status.todo); 
        return cardRepository.save(card);
    }

    
    public Map<String, List<Card>> getCardsByStatus() {
        List<Card> allCards = cardRepository.findAll();

        List<Card> todoCards = new ArrayList<>();
        List<Card> doingCards = new ArrayList<>();
        List<Card> doneCards = new ArrayList<>();

        for (Card card : allCards) {
            if (card.getStatus() == Card.Status.todo) {
                todoCards.add(card);
            } else if (card.getStatus() == Card.Status.doing) {
                doingCards.add(card);
            } else if (card.getStatus() == Card.Status.done) {
                doneCards.add(card);
            }
        }

        Map<String, List<Card>> cardsByStatus = new HashMap<>();
        cardsByStatus.put("todo", todoCards);
        cardsByStatus.put("doing", doingCards);
        cardsByStatus.put("done", doneCards);

        return cardsByStatus;
    }

    
    public Card move(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (card.getStatus() == Card.Status.todo) {
            card.setStatus(Card.Status.doing);
        } else if (card.getStatus() == Card.Status.doing) {
            card.setStatus(Card.Status.done);
        } else if (card.getStatus() == Card.Status.done) {
            throw new RuntimeException("The card is already completed");
        }

        return cardRepository.save(card);
    }

    
    public Card update(Long id, Card updatedCard) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setTitle(updatedCard.getTitle());
        card.setDescription(updatedCard.getDescription());
        card.setPriority(updatedCard.getPriority());
        card.setDueDate(updatedCard.getDueDate());

        return cardRepository.save(card);
    }

    
    public void delete(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new RuntimeException("Card not found");
        }
        cardRepository.deleteById(id);
    }

    
    public List<Card> getCardsByPriority(Card.Status status) {
        List<Card> allCards = cardRepository.findAll();

        List<Card> filteredCards = new ArrayList<>();
        for (Card card : allCards) {
            if (card.getStatus() == status) {
                filteredCards.add(card);
            }
        }

        filteredCards.sort((t1, t2) -> t1.getPriority().compareTo(t2.getPriority()));
        return filteredCards;
    }

    
    public List<Card> filterCardsPriorityDate(Card.Priority priority, LocalDate dueDate) {
        List<Card> allCards = cardRepository.findAll();

        List<Card> filteredCards = new ArrayList<>();
        for (Card card : allCards) {
            if (card.getPriority() == priority && card.getDueDate() != null && card.getDueDate().isBefore(dueDate)) {
                filteredCards.add(card);
            }
        }

        return filteredCards;
    }

    
    public Map<String, Object> report() {
        List<Card> allCards = cardRepository.findAll();

        List<Card> todoCards = new ArrayList<>();
        List<Card> doingCards = new ArrayList<>();
        List<Card> doneCards = new ArrayList<>();
        List<Card> overdueCards = new ArrayList<>();

        for (Card card : allCards) {
            if (card.getStatus() == Card.Status.todo) {
                todoCards.add(card);
            } else if (card.getStatus() == Card.Status.doing) {
                doingCards.add(card);
            } else if (card.getStatus() == Card.Status.done) {
                doneCards.add(card);
            }

            if (card.getDueDate() != null && card.getDueDate().isBefore(LocalDate.now()) && card.getStatus() != Card.Status.done) {
                overdueCards.add(card);
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("todo", todoCards);
        report.put("doing", doingCards);
        report.put("done", doneCards);
        report.put("overdue", overdueCards);

        return report;
    }
}
