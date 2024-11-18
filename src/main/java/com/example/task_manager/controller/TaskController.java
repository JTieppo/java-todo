package com.example.task_manager.controller;

import com.example.task_manager.models.Tasks;
import com.example.task_manager.service.TaskService;
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

    // POST /tasks: Criar uma nova tarefa
    @PostMapping
    public ResponseEntity<Tasks> createTask(@RequestBody Tasks task) {
        Tasks createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    // GET /tasks: Listar todas as tarefas organizadas por coluna
    @GetMapping
    public ResponseEntity<Map<String, List<Tasks>>> getAllTasksByStatus() {
        Map<String, List<Tasks>> tasksByStatus = taskService.getAllTasksOrganizedByStatus();
        return ResponseEntity.ok(tasksByStatus);
    }

    // PUT /tasks/{id}/move: Mover uma tarefa para a próxima coluna
    @PutMapping("/{id}/move")
    public ResponseEntity<Tasks> moveTask(@PathVariable Long id) {
        Tasks movedTask = taskService.moveTask(id);
        return ResponseEntity.ok(movedTask);
    }

    // PUT /tasks/{id}: Atualizar uma tarefa (título, descrição, prioridade)
    @PutMapping("/{id}")
    public ResponseEntity<Tasks> updateTask(@PathVariable Long id, @RequestBody Tasks updatedTask) {
        Tasks task = taskService.updateTask(id, updatedTask);
        return ResponseEntity.ok(task);
    }

    // DELETE /tasks/{id}: Excluir uma tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // GET /tasks/report: Gerar relatório com tarefas por coluna e tarefas atrasadas
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> generateReport() {
        Map<String, Object> report = taskService.generateReport();
        return ResponseEntity.ok(report);
    }

    // GET /tasks/sorted: Ordenar tarefas dentro de cada coluna por prioridade
    @GetMapping("/sorted")
    public ResponseEntity<List<Tasks>> getTasksSortedByPriority(@RequestParam Tasks.Status status) {
        List<Tasks> sortedTasks = taskService.getTasksSortedByPriority(status);
        return ResponseEntity.ok(sortedTasks);
    }

    // GET /tasks/filter: Filtrar tarefas por prioridade e data limite
    @GetMapping("/filter")
    public ResponseEntity<List<Tasks>> filterTasksByPriorityAndDueDate(
            @RequestParam Tasks.Priority priority,
            @RequestParam String dueDate) {
        List<Tasks> filteredTasks = taskService.filterTasksByPriorityAndDueDate(
                priority, java.time.LocalDate.parse(dueDate));
        return ResponseEntity.ok(filteredTasks);
    }
}
