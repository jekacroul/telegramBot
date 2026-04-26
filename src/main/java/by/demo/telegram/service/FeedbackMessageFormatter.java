package by.demo.telegram.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FeedbackMessageFormatter {

    private static final Pattern SCREENSHOT_LINE_PATTERN = Pattern.compile("^\\s*-\\s*(/uploads/\\S+)\\s*$");

    private final String filesBaseUrl;

    public FeedbackMessageFormatter(@Value("${feedback.files-base-url:}") String filesBaseUrl) {
        this.filesBaseUrl = normalizeBaseUrl(filesBaseUrl);
    }

    public FormattedMessage format(String sourceText) {
        if (sourceText == null || sourceText.isBlank()) {
            return new FormattedMessage(sourceText, List.of());
        }

        String[] lines = sourceText.split("\\n", -1);
        List<String> cleanedLines = new ArrayList<>();
        List<String> photoUrls = new ArrayList<>();

        for (String line : lines) {
            Matcher matcher = SCREENSHOT_LINE_PATTERN.matcher(line);
            if (matcher.matches() && !filesBaseUrl.isBlank()) {
                String relativePath = matcher.group(1);
                photoUrls.add(filesBaseUrl + relativePath);
                continue;
            }

            if (line.trim().equalsIgnoreCase("Скриншоты:")) {
                continue;
            }

            cleanedLines.add(line);
        }

        String cleanedText = String.join("\n", cleanedLines).trim();
        return new FormattedMessage(cleanedText, photoUrls);
    }

    private String normalizeBaseUrl(String url) {
        if (url == null || url.isBlank()) {
            return "";
        }

        String trimmed = url.trim();
        if (trimmed.endsWith("/")) {
            return trimmed.substring(0, trimmed.length() - 1);
        }

        return trimmed;
    }

    @Getter
    public static class FormattedMessage {
        private final String text;
        private final List<String> photoUrls;

        public FormattedMessage(String text, List<String> photoUrls) {
            this.text = text;
            this.photoUrls = photoUrls;
        }

        public boolean hasPhotoUrls() {
            return !photoUrls.isEmpty();
        }
    }
}
