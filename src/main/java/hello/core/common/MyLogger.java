package hello.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
// proxyMode = ScopedProxyMode.TARGET_CLASS 추가를 해주면
// 적용 대상이 인터페이스가 아닌 클래스 면 TARGET_CLASS
// 적용 대상이 인터페이스면 INTERFACES 선택
// 이렇게 하면 MyLogger의 가짜 프록시 클래스를 만들어두고 HTTP request와 상관 없이 가짜 프록시 클래스를 다른 빈이
// 미리 주입해 둘 수 있다.

// @Scope proxyMode = ScopedProxyMode.TARGET_CLASS를 설정 하면 스프링 컨테이너는 CGLIB라는 바이트코드 조작하는
// 라이브러리를 사용해서, MyLogger를 상속받은 가짜 프록시 객체를 생성한다.
// 결과를 확인 해보면 우리가 등록한 순수한 MyLogger 클래스가 아니라 $$CGLIB 클래스가 대신 등록된 것을 확인 할 수 있다.
// 그리고 스프링 컨테이너에 myLogger라는 이름으로 진짜 대신에 이 가짜 프록시 객체를 등록한다.
// 그래서 의존관계 주입도 이 가짜 프록시 객체가 주입된다.
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create: " + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close: " + this);
    }
}
