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

    public SseEmitter subscribe(Long adminId) {
        SseEmitter emitter = new SseEmitter(60 * 10000L);

        emitters.put(adminId, emitter);

        emitter.onCompletion(() -> removeEmitter(adminId));
        emitter.onTimeout(() -> removeEmitter(adminId));
        emitter.onError((e) -> {
            System.err.println("SSE 연결 오류: " + e.getMessage());
            removeEmitter(adminId);
        });

        return emitter;
    }

    public void sendNotificationToAllAdmins(String message) {
        for (Map.Entry<Long, SseEmitter> entry : emitters.entrySet()) {
            Long adminId = entry.getKey();
            SseEmitter emitter = entry.getValue();
            sendNotification(adminId, emitter, message);
        }
    }

    private void sendNotification(Long adminId, SseEmitter emitter, String message) {
        try {
            emitter.send(SseEmitter.event().name("notification").data(message));
        } catch (IOException e) {
            System.err.println("SSE 전송 실패, Emitter 제거: " + e.getMessage());
            removeEmitter(adminId);
        } catch (IllegalStateException e) {
            System.err.println("Emitter가 이미 완료됨: " + e.getMessage());
            removeEmitter(adminId);
        }
    }

    private void removeEmitter(Long adminId) {
        emitters.remove(adminId);
    }
}