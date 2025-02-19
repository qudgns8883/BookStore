package Spring.global.aspect;

import java.util.Map;

public interface LogParameterExtractor {
    Map<String, Object> extractParameters(Object[] args);
}