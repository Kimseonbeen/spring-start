package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    // Provider 사용 시
    // mylogger를 찾을 수있는 대체자를 주입 받는것
    // private final ObjectProvider<MyLogger> myLoggerObjectProvider

    // 프록시 사용 시
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();

        // 결국 CGLIB라는 라이브러리로 내 클래스를 상속 받은 가짜 프록시 객체를 만들어서 주입한다.
        // 가짜 프록시 객체는 요청이 오면 그때 내부에서 진짜 빈을 요청하는 위임 로직이 들어있다.
        // 가짜 프록시 객체는 내부에 진짜 myLogger를 찾는 방법을 알고 있다.
        // 클라이언트가 myLogger.logic() 메서드를 호출하면 사실 가짜 프록시 객체의 메서드를 호출한 것이다.
        // 가짜 프록시 객체는 request 스코프의 진짜 myLogger.logic()을 호출한다.
        // 가짜 프록시 객체는 원본 클래스를 상속 받아서 만들어졌기 때문에 이 객체를 사용하는 클라이언트 입장에서는
        // 사실 원본인지 아닌지도 모르게, 동일하게 사용할 수 있다.(다형성)

        // 동작 정리
        // CGLIB라는 라이브러리로 내 클래스를 상속 받은 가짜 프록시 객체를 만들어서 주입힌다.
        // 이 가짜 프록시 객체는 실제 요청이 오면 그떄 내부에서 실제 빈을 요청하는 위임 로직이 들어있다.
        // 가짜 프록시 객체는 실제 request scope와는 관계가 없다.
        // 그냥 가짜이고, 내부에 단순한 위임 로직만 있고, 싱글톤 처럼 동작한다.

        // 특징 정리 !!!!
        // 프록시 객체 덕분에 클라이언트는 마치 싱글톤 빈을 사용하듯이 편리하게 request scope를 사용할 수 있다.
        // 사실 Provider를 사용하든, 프록시를 사용하든 핵심 아이디어는 진짜 객체 조회를 꼭 필요한 시점까지 지연처리 한다는 점 !!!!
        // 단지 애노테이션 설정 변경으로 원본 객체를 프록시 객체로 대체할 수 있다. 이것이 바로 다형성과 DI 컨테이너가 가진 큰 장점 !
        // 꼭 웹 스코프가 아니어도 프록시는 사용할 수 있다.
        System.out.println("myLogger = " + myLogger.getClass());
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "OK";
    }
}
