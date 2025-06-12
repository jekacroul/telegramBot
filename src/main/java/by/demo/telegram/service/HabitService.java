package by.demo.telegram.service;

import by.demo.telegram.model.Habit;
import by.demo.telegram.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {
    private final HabitRepository habitRepository;

    public List<Habit> getHabitsByChatId(Long chatId) {

        return habitRepository.findAllByChatId(chatId);
    }



    public void deleteHabitByChatIdAndTaskId(Long chatId, Long taskId) {
        habitRepository.deleteByChatIdAndTaskId(chatId, taskId);
    }
}
