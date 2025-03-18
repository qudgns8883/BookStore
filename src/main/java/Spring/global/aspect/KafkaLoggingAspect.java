package Spring.global.aspect;

import Spring.Book.domain.notification.dto.KafkaMessageDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class KafkaLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(KafkaLoggingAspect.class);

    @Around("execution(* Spring.Book.domain.notification.service.KafkaProducer.sendNotification(..))")
    public Object logKafkaMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof KafkaMessageDto message) {

            logger.info("Kafka Message Sent: {}", message);
        }
        return joinPoint.proceed();
    }
}
