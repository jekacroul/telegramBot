package by.demo.telegram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserStateProcessor {

    private final UserStateService userStateService;

    private final TaskService taskService;

    public static final String AWAITING_TASK_DESCRIPTION = "AWAITING_TASK_DESCRIPTION";
    public static final String COMPLETE_TASK_DESCRIPTION = "COMPLETE_TASK_DESCRIPTION";
    public static final String DELETE_TASK_DESCRIPTION = "DELETE_TASK_DESCRIPTION";

    public boolean isUserHasState(long userId) {
        return userStateService.getUserState(userId) != null;
    }

    public SendMessage process(long chatId, String messageText) {
        return process(chatId, messageText, userStateService.getUserState(chatId));
    }

    public SendMessage process(long chatId, String messageText, String userState) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        if (AWAITING_TASK_DESCRIPTION.equals(userState)) {
            taskService.addTask(messageText, chatId);
            message.setText("✅ Задача добавлена: \"" + messageText + "\"");
            userStateService.clearUserState(chatId); // Сбрасываем состояние
        }else if (DELETE_TASK_DESCRIPTION.equals(userState)) {
            try {
                if (messageText.startsWith("/")) {
                    userStateService.clearUserState(chatId);
                    message.setText("Ввод номера задачи отменён. Повтори команду.");
                    return message;
                }
                if (!messageText.matches("\\d+")) {
                    message.setText("Нужен номер задачи (только число). Попробуй снова.");
                    return message;
                }
                Long taskId = Long.valueOf(messageText);
                taskService.deleteTask(taskId, chatId);
                message.setText("🗑 Задача удалена!");
                userStateService.clearUserState(chatId);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                message.setText(e.getMessage());
            }
        }else if (COMPLETE_TASK_DESCRIPTION.equals(userState)) {
            try {
                if (messageText.startsWith("/")) {
                    userStateService.clearUserState(chatId);
                    message.setText("Ввод номера задачи отменён. Повтори команду.");
                    return message;
                }
                if (!messageText.matches("\\d+")) {
                    message.setText("Нужен номер задачи (только число). Попробуй снова.");
                    return message;
                }
                Long taskId = Long.valueOf(messageText);
                taskService.completeTask(taskId, chatId);
                message.setText("✅ Задача выполнена!");
                userStateService.clearUserState(chatId);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                message.setText(e.getMessage());
            }
        }
        return message;
    }

    public void setAwaitingTaskForUser(long chatId) {
        userStateService.setUserState(chatId, AWAITING_TASK_DESCRIPTION);
    }

    public void setCompleteTaskForUser(long chatId) {
        userStateService.setUserState(chatId, COMPLETE_TASK_DESCRIPTION);
    }

    public void setDeleteTaskForUser(long chatId) {
        userStateService.setUserState(chatId, DELETE_TASK_DESCRIPTION);
    }

    public void clearUserState(long chatId) {
        userStateService.clearUserState(chatId);
    }
}
