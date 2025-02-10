package Spring.global.aspect;

import Spring.global.filter.RequestIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
@Aspect
@Component
public class PerformanceAspect {

    private ThreadLocal<Integer> callDepth = ThreadLocal.withInitial(() -> 0);

    @Around("execution(public * Spring.Book..*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        int depth = callDepth.get();
        callDepth.set(depth + 1);
        HttpServletRequest request = null;
        String requestId = null;
        if (RequestContextHolder.getRequestAttributes() != null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            requestId = (String) request.getAttribute(RequestIdFilter.REQUEST_ID_HEADER);  // 요청 ID 가져오기
        }

        String httpRequestInfo = (request != null)
                ? " [HTTP " + request.getMethod() + " " + request.getRequestURI() + "]"
                : "";

        log.info("{}{} started execution", requestId != null ? "[" + requestId + "] " : "", "Depth[" + depth + "] " + "-> ".repeat(depth)  + joinPoint.getSignature().toShortString() + httpRequestInfo);

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            logExecutionDetails(joinPoint, executionTime, null, depth, request, requestId);

            return result;
        } catch (Throwable throwable) {
            long executionTime = System.currentTimeMillis() - start;
            logExecutionDetails(joinPoint, executionTime, throwable, depth, request, requestId);

            throw throwable;
        } finally {
            callDepth.set(depth);
        }
    }

    private void logExecutionDetails(ProceedingJoinPoint joinPoint, long executionTime, Throwable throwable, int depth, HttpServletRequest request, String requestId) {
        String methodInfo = joinPoint.getSignature().toShortString();
        String depthInfo = "Depth[" + depth + "] " + "<-  ".repeat(depth);
        String requestIdInfo = (requestId != null) ? "[" + requestId + "] " : "";
        String httpRequestInfo = (request != null)
                ? " [HTTP " + request.getMethod() + " " + request.getRequestURI() + "]"
                : "";
        if (throwable == null) {
            log.info("{}{} end executed in {} ms", requestIdInfo, depthInfo + methodInfo , executionTime);
        } else {
            log.error("{}{} failed in {} ms with exception: {}", requestIdInfo, depthInfo + methodInfo + httpRequestInfo, executionTime, throwable.getMessage());
        }
    }

}