package by.demo.telegram.service;

import by.demo.telegram.controller.BotController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewNotificationService {

    private final BotController botController;

    @Value("${bot.review.chat-ids:}")
    private String reviewChatIds;

    public int notifyNewReview(String reviewText) {
        List<String> chatIds = Arrays.stream(reviewChatIds.split(","))
                .map(String::trim)
                .filter(chatId -> !chatId.isEmpty())
                .toList();

        if (chatIds.isEmpty()) {
            log.warn("Не настроены chat-id для отправки уведомления об отзыве (bot.review.chat-ids)");
            return 0;
        }

        String text = "🔔 У вас новый отзыв!\n\n" + reviewText;
        int delivered = 0;

        for (String chatId : chatIds) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text);

            try {
                botController.execute(message);
                delivered++;
            } catch (TelegramApiException e) {
                log.error("Не удалось отправить уведомление об отзыве в чат {}", chatId, e);
            }
        }

        return delivered;
    }
}
