package Spring.global.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // 메서드에만 붙일 수 있도록 설정
@Retention(RetentionPolicy.RUNTIME)  // 런타임까지 유지되어야 AOP에서 감지 가능
public @interface Loggable {

    String value();
}