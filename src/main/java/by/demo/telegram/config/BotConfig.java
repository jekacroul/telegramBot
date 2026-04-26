package by.demo.telegram.config;


import by.demo.telegram.controller.BotController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class BotConfig {
    @Bean
    public TelegramBotsApi telegramBotsApi(BotController botController) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        if (StringUtils.hasText(botController.getBotToken()) && StringUtils.hasText(botController.getBotUsername())) {
            botsApi.registerBot(botController);
        } else {
            log.warn("Telegram bot registration skipped: set BOT_TOKEN (or botToken) and BOT_USERNAME (or botUsername)");
        }
        return botsApi;
    }
}
