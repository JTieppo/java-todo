package com.example.main.service;

import com.example.main.models.Card;
import com.example.main.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardService {
    private final TaskRepository taskRepository;

    public CardService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

   
    public Card createTask(Card task) {
        task.setStatus(Card.Status.todo); 
        return taskRepository.save(task);
    }

    
    public Map<String, List<Card>> getAllTasksOrganizedByStatus() {
        List<Card> allTasks = taskRepository.findAll();

        List<Card> todoTasks = new ArrayList<>();
        List<Card> doingTasks = new ArrayList<>();
        List<Card> doneTasks = new ArrayList<>();

        for (Card task : allTasks) {
            if (task.getStatus() == Card.Status.todo) {
                todoTasks.add(task);
            } else if (task.getStatus() == Card.Status.doing) {
                doingTasks.add(task);
            } else if (task.getStatus() == Card.Status.done) {
                doneTasks.add(task);
            }
        }

        Map<String, List<Card>> tasksByStatus = new HashMap<>();
        tasksByStatus.put("todo", todoTasks);
        tasksByStatus.put("doing", doingTasks);
        tasksByStatus.put("done", doneTasks);

        return tasksByStatus;
    }

    
    public Card moveTask(Long id) {
        Card task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getStatus() == Card.Status.todo) {
            task.setStatus(Card.Status.doing);
        } else if (task.getStatus() == Card.Status.doing) {
            task.setStatus(Card.Status.done);
        } else if (task.getStatus() == Card.Status.done) {
            throw new RuntimeException("The task is already completed");
        }

        return taskRepository.save(task);
    }

    
    public Card updateTask(Long id, Card updatedTask) {
        Card task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setPriority(updatedTask.getPriority());
        task.setDueDate(updatedTask.getDueDate());

        return taskRepository.save(task);
    }

    
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    
    public List<Card> getTasksSortedByPriority(Card.Status status) {
        List<Card> allTasks = taskRepository.findAll();

        List<Card> filteredTasks = new ArrayList<>();
        for (Card task : allTasks) {
            if (task.getStatus() == status) {
                filteredTasks.add(task);
            }
        }

        filteredTasks.sort((t1, t2) -> t1.getPriority().compareTo(t2.getPriority()));
        return filteredTasks;
    }

    
    public List<Card> filterTasksByPriorityAndDueDate(Card.Priority priority, LocalDate dueDate) {
        List<Card> allTasks = taskRepository.findAll();

        List<Card> filteredTasks = new ArrayList<>();
        for (Card task : allTasks) {
            if (task.getPriority() == priority && task.getDueDate() != null && task.getDueDate().isBefore(dueDate)) {
                filteredTasks.add(task);
            }
        }

        return filteredTasks;
    }

    
    public Map<String, Object> generateReport() {
        List<Card> allTasks = taskRepository.findAll();

        List<Card> todoTasks = new ArrayList<>();
        List<Card> doingTasks = new ArrayList<>();
        List<Card> doneTasks = new ArrayList<>();
        List<Card> overdueTasks = new ArrayList<>();

        for (Card task : allTasks) {
            if (task.getStatus() == Card.Status.todo) {
                todoTasks.add(task);
            } else if (task.getStatus() == Card.Status.doing) {
                doingTasks.add(task);
            } else if (task.getStatus() == Card.Status.done) {
                doneTasks.add(task);
            }

            if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now()) && task.getStatus() != Card.Status.done) {
                overdueTasks.add(task);
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("todo", todoTasks);
        report.put("doing", doingTasks);
        report.put("done", doneTasks);
        report.put("overdue", overdueTasks);

        return report;
    }
}
