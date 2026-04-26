package by.demo.telegram.service;

import by.demo.telegram.model.Task;
import by.demo.telegram.model.TaskArchive;
import by.demo.telegram.repository.TaskArchiveRepository;
import by.demo.telegram.repository.TaskRepository;
import org.springframework.stereotype.Service;

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
        task.setTaskId((long) getUserTasks(chatId).size() + 1);
        task.setChatId(chatId);
        task.setCompleted(false);
        taskRepository.save(task);
    }

    public List<Task> getUserTasks(Long chatId) {
        return taskRepository.findByChatIdAndIsCompletedFalse(chatId);
    }

    public List<TaskArchive> getUserArchiveTasks(Long chatId) {
        return taskArchiveRepository.findByChatId(chatId);
    }

    public void deleteTask(Long taskId, long chatId) {
        taskRepository.deleteByTaskIdAndChatId(taskId, chatId);
    }

    public void completeTask(Long taskId, Long chatId) {
        Task task = taskRepository.findByTaskIdAndChatId(taskId, chatId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        if (task.isCompleted()) {
            return;
        }

        task.setCompleted(true);
        taskRepository.save(task);

        TaskArchive archiveTask = new TaskArchive();
        archiveTask.setTaskId(task.getTaskId());
        archiveTask.setDescription(task.getDescription());
        archiveTask.setChatId(task.getChatId());
        taskArchiveRepository.save(archiveTask);
    }
}
