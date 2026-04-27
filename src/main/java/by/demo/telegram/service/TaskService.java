package by.demo.telegram.service;

import by.demo.telegram.model.Task;
import by.demo.telegram.model.TaskArchive;
import by.demo.telegram.repository.TaskArchiveRepository;
import by.demo.telegram.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskArchiveRepository taskArchiveRepository;

    public TaskService(TaskRepository taskRepository, TaskArchiveRepository taskArchiveRepository) {
        this.taskRepository = taskRepository;
        this.taskArchiveRepository = taskArchiveRepository;
    }

    public void addTask(String description, Long chatId) {
        Task task = new Task();
        task.setDescription(description);
        task.setTaskId(getNextTaskId(chatId));
        task.setChatId(chatId);
        task.setCompleted(false);
        taskRepository.save(task);
    }

    private Long getNextTaskId(Long chatId) {
        return taskRepository.findByChatId(chatId)
                .stream()
                .map(Task::getTaskId)
                .filter(taskId -> taskId != null && taskId > 0)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    public List<Task> getUserTasks(Long chatId) {
        return taskRepository.findByChatIdAndIsCompletedFalse(chatId);
    }

    public List<TaskArchive> getUserArchiveTasks(Long chatId) {
        return taskArchiveRepository.findByChatId(chatId);
    }

    public void deleteTask(Long taskId, long chatId) {
        Task task = taskRepository.findByTaskIdAndChatIdAndIsCompletedFalse(taskId, chatId)
                .orElseThrow(() -> new RuntimeException("Задача с номером " + taskId + " не найдена"));

        taskRepository.delete(task);
    }

    @Transactional
    public void completeTask(Long taskId, Long chatId) {
        Task task = taskRepository.findByTaskIdAndChatIdAndIsCompletedFalse(taskId, chatId)
                .orElseThrow(() -> new RuntimeException("Задача с номером " + taskId + " не найдена"));

        TaskArchive archiveTask = new TaskArchive();
        archiveTask.setTaskId(task.getTaskId());
        archiveTask.setDescription(task.getDescription());
        archiveTask.setChatId(task.getChatId());
        taskArchiveRepository.save(archiveTask);

        taskRepository.delete(task);
    }
}
