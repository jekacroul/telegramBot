package by.demo.telegram.controller.api;

import by.demo.telegram.dto.ReviewRequest;
import by.demo.telegram.service.ReviewNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewNotificationService reviewNotificationService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> notifyNewReview(@RequestBody ReviewRequest reviewRequest) {
        int delivered = reviewNotificationService.notifyNewReview(reviewRequest.toTelegramText());
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "delivered", delivered
        ));
    }
}
