package by.demo.telegram.service;

import by.demo.telegram.model.Task;
import by.demo.telegram.model.TaskArchive;
import by.demo.telegram.repository.TaskArchiveRepository;
import by.demo.telegram.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskArchiveRepository taskArchiveRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void completeTask_shouldFindByEntityIdWhenCustomTaskIdMissing() {
        Long inputTaskNumber = 3L;
        Long chatId = 100L;

        Task task = new Task();
        task.setId(3L);
        task.setTaskId(null);
        task.setChatId(chatId);
        task.setDescription("task from external app");
        task.setCompleted(false);

        when(taskRepository.findByTaskIdAndChatId(inputTaskNumber, chatId)).thenReturn(Optional.empty());
        when(taskRepository.findByIdAndChatId(inputTaskNumber, chatId)).thenReturn(Optional.of(task));

        taskService.completeTask(inputTaskNumber, chatId);

        verify(taskRepository).save(task);
        ArgumentCaptor<TaskArchive> archiveCaptor = ArgumentCaptor.forClass(TaskArchive.class);
        verify(taskArchiveRepository).save(archiveCaptor.capture());
        assertEquals(3L, archiveCaptor.getValue().getTaskId());
    }

    @Test
    void completeTask_shouldThrowReadableErrorWhenTaskMissing() {
        Long inputTaskNumber = 3L;
        Long chatId = 100L;

        when(taskRepository.findByTaskIdAndChatId(inputTaskNumber, chatId)).thenReturn(Optional.empty());
        when(taskRepository.findByIdAndChatId(inputTaskNumber, chatId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> taskService.completeTask(inputTaskNumber, chatId));
        assertEquals("Задача с номером 3 не найдена", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }
}
