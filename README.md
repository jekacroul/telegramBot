# Telegram bot backend (Spring Boot)

Чтобы бот начал отвечать в Telegram, обязательно передай **два** параметра из BotFather:

- `BOT_TOKEN` — токен бота;
- `BOT_USERNAME` — username бота (например `my_tasks_assistant_bot`).

## Быстрый старт

```bash
export BOT_TOKEN=123456:ABCDEF...
export BOT_USERNAME=my_tasks_assistant_bot
./mvnw spring-boot:run
```

После запуска открой Telegram, найди бота по username и отправь `/start`.

## Частые причины, почему бот «не работает»

1. Не передан `BOT_USERNAME` — бот не регистрируется в Telegram API.
2. Неверный `BOT_TOKEN` — Telegram возвращает ошибку авторизации.
3. Бот не запущен локально/на сервере.
4. Пишешь не своему боту (проверь username в BotFather).

## Если запускаешь в Docker

```bash
docker run --rm \
  -e BOT_TOKEN=123456:ABCDEF... \
  -e BOT_USERNAME=my_tasks_assistant_bot \
  -p 8080:8080 telegram-backend:latest
```

