package by.demo.telegram.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long taskId;
    private String description;
    private Long chatId; // ID пользователя в Telegram
    private boolean isCompleted;
    private Long durationMinutes;
    private LocalDateTime startTime;
}
