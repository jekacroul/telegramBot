package by.demo.telegram.repository;

import by.demo.telegram.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAllByChatId(Long chatId);
    void deleteByChatIdAndTaskId(Long chatId, Long taskId);

}
