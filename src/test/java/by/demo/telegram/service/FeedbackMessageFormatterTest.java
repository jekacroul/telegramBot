package by.demo.telegram.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeedbackMessageFormatterTest {

    @Test
    void shouldExtractScreenshotsAsPhotoUrls() {
        FeedbackMessageFormatter formatter = new FeedbackMessageFormatter("https://example.com");

        String message = "5 - Новый отзыв\nИмя: Yauheni Norkin\nСкриншоты:\n- /uploads/feedback/file-1.jpg\n- /uploads/feedback/file-2.jpg";

        FeedbackMessageFormatter.FormattedMessage result = formatter.format(message);

        assertTrue(result.hasPhotoUrls());
        assertEquals(2, result.getPhotoUrls().size());
        assertEquals("https://example.com/uploads/feedback/file-1.jpg", result.getPhotoUrls().get(0));
        assertEquals("https://example.com/uploads/feedback/file-2.jpg", result.getPhotoUrls().get(1));
        assertEquals("5 - Новый отзыв\nИмя: Yauheni Norkin", result.getText());
    }

    @Test
    void shouldReturnOriginalTextWhenBaseUrlIsMissing() {
        FeedbackMessageFormatter formatter = new FeedbackMessageFormatter("");
        String message = "Скриншоты:\n- /uploads/feedback/file-1.jpg";

        FeedbackMessageFormatter.FormattedMessage result = formatter.format(message);

        assertFalse(result.hasPhotoUrls());
        assertEquals(message, result.getText());
    }
}
