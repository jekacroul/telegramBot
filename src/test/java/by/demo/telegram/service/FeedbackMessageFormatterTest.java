package by.demo.telegram.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeedbackMessageFormatterTest {

    @Test
    void shouldExtractScreenshotsAsPhotoSources() {
        FeedbackMessageFormatter formatter = new FeedbackMessageFormatter("https://example.com");

        String message = "5 - Новый отзыв\\nИмя: Yauheni Norkin\\nСкриншоты:\\n- /uploads/feedback/file-1.jpg\\n- https://cdn.example.com/file-2.jpg";

        FeedbackMessageFormatter.FormattedMessage result = formatter.format(message);

        assertTrue(result.hasPhotoSources());
        assertEquals(2, result.getPhotoSources().size());
        assertEquals("https://example.com/uploads/feedback/file-1.jpg", result.getPhotoSources().get(0));
        assertEquals("https://cdn.example.com/file-2.jpg", result.getPhotoSources().get(1));
        assertEquals("5 - Новый отзыв\nИмя: Yauheni Norkin", result.getText());
    }

    @Test
    void shouldKeepRelativePathWhenBaseUrlIsMissing() {
        FeedbackMessageFormatter formatter = new FeedbackMessageFormatter("");
        String message = "Скриншоты:\n- /uploads/feedback/file-1.jpg";

        FeedbackMessageFormatter.FormattedMessage result = formatter.format(message);

        assertTrue(result.hasPhotoSources());
        assertEquals("/uploads/feedback/file-1.jpg", result.getPhotoSources().get(0));
        assertEquals("", result.getText());
    }
}
