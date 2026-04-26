package by.demo.telegram.repository;

import by.demo.telegram.model.TaskArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskArchiveRepository extends JpaRepository<TaskArchive, Long> {
    List<TaskArchive> findByChatId(Long chatId);
}
