package by.demo.telegram.controller;

import by.demo.telegram.model.Task;
import by.demo.telegram.service.TaskService;
import by.demo.telegram.service.UserStateProcessor;
import by.demo.telegram.service.UserStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BotController extends TelegramLongPollingBot {

    private final TaskService taskService;

    private final UserStateProcessor userStateProcessor;


    @Value("${bot.token}")
    private String TOKEN;

    @Value("${spring.application.name}")
    private String APP_NAME;


    @Override
    public String getBotUsername() {
        return APP_NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            log.debug("chatId {}", chatId);
            log.debug("messageIn {}", messageText);

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));

            if(userStateProcessor.isUserHasState(chatId)) {
                message = userStateProcessor.process(chatId, messageText);
                userStateProcessor.clearUserState(chatId);
            }else if (messageText.equals("/start")) {
                message.setText("Привет! Я твой личный органайзер. Используй /help для списка команд.");
            } else if (messageText.equals("/help")) {
                message.setText("Доступные команды:\n" +
                        "/addtask - Добавить задачу\n" +
                        "/deletetask [описание] - Удалить задачу\n" +
                        "/completetask [описание] - Выполнить задачу\n" +
                        "/mytasks - Показать мои задачи\n" +
                        "/habits - Трекер привычек");
            } else if (messageText.startsWith("/addtask")) {
                userStateProcessor.setAwaitingTaskForUser(chatId);
                message.setText("Введи описание задачи:");
            } else if (messageText.equals("/mytasks")) {
                List<Task> tasks = taskService.getUserTasks(chatId);
                if (tasks.isEmpty()) {
                    message.setText("У тебя нет задач. Добавь новую через /addtask");
                } else {
                    for (Task task : tasks) {
                        SendMessage taskMessage = new SendMessage();
                        taskMessage.setChatId(String.valueOf(chatId));
                        String statusText = task.isCompleted() ? "✅ Выполнена" : "🕒 В процессе";
                        taskMessage.setText("🔢 Номер: " + task.getTaskId() + "\n"
                                + "📝 Текст: " + task.getDescription() + "\n"
                                + "📌 Статус: " + statusText);
                        try {
                            execute(taskMessage);
                        } catch (TelegramApiException e) {
                            log.error("Ошибка отправки задачи {} пользователю {}", task.getTaskId(), chatId, e);
                        }
                    }
                    return;
                }
            }else if (messageText.startsWith("/deletetask")) {
                userStateProcessor.setDeleteTaskForUser(chatId);
                message.setText("Введи номер задачи которую необходимо удалить:");
            }else if (messageText.startsWith("/completetask")) {
                userStateProcessor.setCompleteTaskForUser(chatId);
                message.setText("Введи номер задачи для выполнения:");
            }else {
                message.setText("Неизвестная команда. Используй /help");
            }

            try {
                log.debug("message {}", message);
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
