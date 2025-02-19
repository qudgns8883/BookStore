package Spring.global.aspect;

import Spring.Book.domain.notification.service.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LogAspect {

    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;
    private final Map<String, LogParameterExtractor> parameterExtractors;

    @Around("@annotation(loggable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        long start = System.currentTimeMillis();

        Map<String, Object> logData = new HashMap<>();
        logData.put("event", loggable.value());
        logData.put("timestamp", Instant.now().toString());

        //실제 비즈니스 로직 실행
        Object result = joinPoint.proceed();

        try {
            //먼저 메서드 인자 분석
            Object[] args = joinPoint.getArgs();
            log.info("🔹 AOP 실행 - 전달된 args: {}", Arrays.toString(args));

            if (args.length > 0) {
                LogParameterExtractor extractor = parameterExtractors.get(loggable.value());

                if (extractor != null) {
                    // 인자 추출
                    String extractedParams = objectMapper.writeValueAsString(extractor.extractParameters(args));
                    log.info("📌 추출된 parameters: {}", extractedParams);
                    logData.put("parameters", extractedParams);
                } else {
                    log.warn("⚠️ No extractor found for event: {}", loggable.value());
                    logData.put("parameters", Arrays.toString(args));
                }
            }



            //실행 후 처리
            long executionTime = System.currentTimeMillis() - start;
            logData.put("executionTime", executionTime + "ms");

            log.info("🔹 AOP 실행 - 메서드 실행 후, 반환값: {}", result);
            log.info("⏳ 실행 시간: {}ms", executionTime);

            //Kafka로 최종 전송 데이터 확인
            String finalLog = objectMapper.writeValueAsString(logData);
            log.info("🚀 Kafka로 전송할 최종 로그 데이터: {}", finalLog);
            sendLogAsync(finalLog);

            return result;

        } catch (Exception e) {
            logError(joinPoint, loggable, e);  // 예외 발생 시 로그 처리
            throw e;
        }
    }

    // 비동기 방식으로 Kafka로 로그 전송
    @Async
    public void sendLogAsync(String logMessage) {
        kafkaProducer.sendLog(logMessage);
    }

    // 에러 처리 및 로그 전송
    private void logError(ProceedingJoinPoint joinPoint, Loggable loggable, Exception e) {
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("event", loggable.value());
            logData.put("timestamp", Instant.now().toString());
            logData.put("errorMessage", e.getMessage());
            logData.put("stackTrace", Arrays.toString(e.getStackTrace()));
            logData.put("parameters", Arrays.toString(joinPoint.getArgs()));

            // Kafka로 에러 로그 전송
            sendLogAsync(objectMapper.writeValueAsString(logData));
        } catch (Exception logEx) {
            log.error("Error while logging error: {}", logEx.getMessage(), logEx);
        }
    }
}