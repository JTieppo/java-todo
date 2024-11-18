package com.example.task_manager.service;

import com.example.task_manager.models.Tasks;
import com.example.task_manager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Create a new task
    public Tasks createTask(Tasks task) {
        task.setStatus(Tasks.Status.todo); // All new tasks start with "todo" status
        return taskRepository.save(task);
    }

    // Get all tasks organized by status (for GET /tasks)
    public Map<String, List<Tasks>> getAllTasksOrganizedByStatus() {
        List<Tasks> allTasks = taskRepository.findAll();

        List<Tasks> todoTasks = new ArrayList<>();
        List<Tasks> doingTasks = new ArrayList<>();
        List<Tasks> doneTasks = new ArrayList<>();

        for (Tasks task : allTasks) {
            if (task.getStatus() == Tasks.Status.todo) {
                todoTasks.add(task);
            } else if (task.getStatus() == Tasks.Status.doing) {
                doingTasks.add(task);
            } else if (task.getStatus() == Tasks.Status.done) {
                doneTasks.add(task);
            }
        }

        Map<String, List<Tasks>> tasksByStatus = new HashMap<>();
        tasksByStatus.put("todo", todoTasks);
        tasksByStatus.put("doing", doingTasks);
        tasksByStatus.put("done", doneTasks);

        return tasksByStatus;
    }

    // Move a task to the next column (for PUT /tasks/{id}/move)
    public Tasks moveTask(Long id) {
        Tasks task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getStatus() == Tasks.Status.todo) {
            task.setStatus(Tasks.Status.doing);
        } else if (task.getStatus() == Tasks.Status.doing) {
            task.setStatus(Tasks.Status.done);
        } else if (task.getStatus() == Tasks.Status.done) {
            throw new RuntimeException("The task is already completed");
        }

        return taskRepository.save(task);
    }

    // Update an existing task (for PUT /tasks/{id})
    public Tasks updateTask(Long id, Tasks updatedTask) {
        Tasks task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setPriority(updatedTask.getPriority());
        task.setDueDate(updatedTask.getDueDate());

        return taskRepository.save(task);
    }

    // Delete a task (for DELETE /tasks/{id})
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    // Get tasks sorted by priority within a column (for GET /tasks/sorted)
    public List<Tasks> getTasksSortedByPriority(Tasks.Status status) {
        List<Tasks> allTasks = taskRepository.findAll();

        List<Tasks> filteredTasks = new ArrayList<>();
        for (Tasks task : allTasks) {
            if (task.getStatus() == status) {
                filteredTasks.add(task);
            }
        }

        filteredTasks.sort((t1, t2) -> t1.getPriority().compareTo(t2.getPriority()));
        return filteredTasks;
    }

    // Filter tasks by priority and due date (for GET /tasks/filter)
    public List<Tasks> filterTasksByPriorityAndDueDate(Tasks.Priority priority, LocalDate dueDate) {
        List<Tasks> allTasks = taskRepository.findAll();

        List<Tasks> filteredTasks = new ArrayList<>();
        for (Tasks task : allTasks) {
            if (task.getPriority() == priority && task.getDueDate() != null && task.getDueDate().isBefore(dueDate)) {
                filteredTasks.add(task);
            }
        }

        return filteredTasks;
    }

    // Generate a report of tasks by column and highlight overdue tasks (for GET /tasks/report)
    public Map<String, Object> generateReport() {
        List<Tasks> allTasks = taskRepository.findAll();

        List<Tasks> todoTasks = new ArrayList<>();
        List<Tasks> doingTasks = new ArrayList<>();
        List<Tasks> doneTasks = new ArrayList<>();
        List<Tasks> overdueTasks = new ArrayList<>();

        for (Tasks task : allTasks) {
            if (task.getStatus() == Tasks.Status.todo) {
                todoTasks.add(task);
            } else if (task.getStatus() == Tasks.Status.doing) {
                doingTasks.add(task);
            } else if (task.getStatus() == Tasks.Status.done) {
                doneTasks.add(task);
            }

            if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now()) && task.getStatus() != Tasks.Status.done) {
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
