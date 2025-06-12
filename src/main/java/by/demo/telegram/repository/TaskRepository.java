package by.demo.telegram.repository;

import by.demo.telegram.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByChatId(Long chatId);

    @Transactional
    void deleteByTaskIdAndChatId(Long taskId, long chatId);

    Optional<Task> findByTaskIdAndChatId(Long taskId, Long chatId);
}
