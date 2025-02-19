package Spring.Book.domain.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(60 * 10000L);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> removeEmitter(userId));
        emitter.onTimeout(() -> removeEmitter(userId));
        emitter.onError((e) -> {
            System.err.println("SSE 연결 오류: " + e.getMessage());
            removeEmitter(userId);
        });

        // 클라이언트가 연결되었음을 알리는 더미 데이터 전송
        try {
            emitter.send(SseEmitter.event().name("connect").data("연결 성공"));
        } catch (IOException e) {
            removeEmitter(userId);
        }

        return emitter;
    }

    public void sendMessageToUser(String message) {
        List<Long> expiredEmitters = new ArrayList<>();

        for (Map.Entry<Long, SseEmitter> entry : emitters.entrySet()) {
            Long userId = entry.getKey();
            SseEmitter emitter = entry.getValue();

            System.out.println("📤 SSE 메시지 전송 중... 대상 userId: " + userId);

            try {
                emitter.send(SseEmitter.event().name("notification").data(message));
                System.out.println("✅ SSE 메시지 전송 성공: " + message);
            } catch (IOException | IllegalStateException e) {
                System.err.println("SSE 전송 실패, Emitter 제거: " + e.getMessage());
                expiredEmitters.add(userId);
            }
        }

        for (Long userId : expiredEmitters) {
            removeEmitter(userId);
        }
    }

    private void removeEmitter(Long userId) {
        emitters.remove(userId);
    }
}