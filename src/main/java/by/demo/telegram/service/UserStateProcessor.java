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

    private final String awaitingTaskDescription = "AWAITING_TASK_DESCRIPTION";
    private final String completeTaskDescription = "COMPLETE_TASK_DESCRIPTION";
    private final String deleteTaskDescription = "DELETE_TASK_DESCRIPTION";

    public boolean isUserHasState(long userId) {
        return userStateService.getUserState(userId) != null;
    }

    public SendMessage process(long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        String userState = userStateService.getUserState(chatId);
        if (awaitingTaskDescription.equals(userState)) {
            taskService.addTask(messageText, chatId);
            message.setText("✅ Задача добавлена: \"" + messageText + "\"");
            userStateService.clearUserState(chatId); // Сбрасываем состояние
        }else if (deleteTaskDescription.equals(userState)) {
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
                message.setText("Ошибка. Укажи ID задачи: /deletetask [число]");
            }
        }else if (completeTaskDescription.equals(userState)) {
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
                message.setText("Ошибка. Укажи ID задачи: /completetask [число]");
            }
        }
        return message;
    }

    public void setAwaitingTaskForUser(long chatId) {
        userStateService.setUserState(chatId, awaitingTaskDescription);
    }

    public void setCompleteTaskForUser(long chatId) {
        userStateService.setUserState(chatId, completeTaskDescription);
    }

    public void setDeleteTaskForUser(long chatId) {
        userStateService.setUserState(chatId, deleteTaskDescription);
    }

    public void clearUserState(long chatId) {
        userStateService.clearUserState(chatId);
    }
}
