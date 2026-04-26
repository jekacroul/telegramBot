package by.demo.telegram.dto;

public record ReviewRequest(
        String name,
        String email,
        String message,
        String screenshots
) {
    public String toTelegramText() {
        StringBuilder sb = new StringBuilder();
        appendLine(sb, "👤 Имя", name);
        appendLine(sb, "📧 Email", email);
        appendLine(sb, "💬 Сообщение", message);
        appendLine(sb, "🖼️ Скриншоты", screenshots);
        return sb.toString().trim();
    }

    private void appendLine(StringBuilder sb, String label, String value) {
        if (value != null && !value.isBlank()) {
            sb.append(label)
                    .append(": ")
                    .append(value.trim())
                    .append("\n");
        }
    }
}
