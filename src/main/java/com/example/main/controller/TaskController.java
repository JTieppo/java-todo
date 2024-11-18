package com.example.main.controller;

import com.example.main.models.Tasks;
import com.example.main.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    
    @PostMapping
    public ResponseEntity<Tasks> createTask(@RequestBody Tasks task) {
        Tasks createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    
    @GetMapping
    public ResponseEntity<Map<String, List<Tasks>>> getAllTasksByStatus() {
        Map<String, List<Tasks>> tasksByStatus = taskService.getAllTasksOrganizedByStatus();
        return ResponseEntity.ok(tasksByStatus);
    }

    
    @PutMapping("/{id}/move")
    public ResponseEntity<Tasks> moveTask(@PathVariable Long id) {
        Tasks movedTask = taskService.moveTask(id);
        return ResponseEntity.ok(movedTask);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Tasks> updateTask(@PathVariable Long id, @RequestBody Tasks updatedTask) {
        Tasks task = taskService.updateTask(id, updatedTask);
        return ResponseEntity.ok(task);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> generateReport() {
        Map<String, Object> report = taskService.generateReport();
        return ResponseEntity.ok(report);
    }

    
    @GetMapping("/sorted")
    public ResponseEntity<List<Tasks>> getTasksSortedByPriority(@RequestParam Tasks.Status status) {
        List<Tasks> sortedTasks = taskService.getTasksSortedByPriority(status);
        return ResponseEntity.ok(sortedTasks);
    }

    
    @GetMapping("/filter")
    public ResponseEntity<List<Tasks>> filterTasksByPriorityAndDueDate(
            @RequestParam Tasks.Priority priority,
            @RequestParam String dueDate) {
        List<Tasks> filteredTasks = taskService.filterTasksByPriorityAndDueDate(
                priority, java.time.LocalDate.parse(dueDate));
        return ResponseEntity.ok(filteredTasks);
    }
}
