package by.demo.telegram.service;

import by.demo.telegram.model.Task;
import by.demo.telegram.repository.TaskRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addTask(String description, Long chatId) {
        Task task = new Task();
        task.setDescription(description);
        task.setTaskId((long) getUserTasks(chatId).size() + 1);
        task.setChatId(chatId);
        task.setCompleted(false); // По умолчанию задача не выполнена
        taskRepository.save(task);
    }

    public List<Task> getUserTasks(Long chatId) {
        return taskRepository.findByChatId(chatId);
    }

    public void deleteTask(Long taskId, long chatId) {
        taskRepository.deleteByTaskIdAndChatId(taskId, chatId);
    }

    public void completeTask(Long taskId, Long chatId) {
        Task task = taskRepository.findByTaskIdAndChatId(taskId, chatId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));
        task.setCompleted(true);
        taskRepository.save(task);
    }
}
