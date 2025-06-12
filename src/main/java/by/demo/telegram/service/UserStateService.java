package by.demo.telegram.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserStateService {
    private final Map<Long, String> userStates = new ConcurrentHashMap<>();

    public void setUserState(Long chatId, String state) {
        userStates.put(chatId, state);
    }

    public String getUserState(Long chatId) {
        return userStates.get(chatId);
    }

    public void clearUserState(Long chatId) {
        userStates.remove(chatId);
    }
}
