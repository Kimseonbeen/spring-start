package hello.core.lifecycle;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class NetworkClient  {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect() {
        System.out.println("connect : " + url);
    }

    public void call(String message) {
        System.out.println("call : " + url + " message : " + message);
    }

    // 서비스 종료 시
    public void disconnect() {
        System.out.println("close : " + url);
    }

    
    // implement 받은 경우 : afterPropertiesSet : 의존 관계 주입이 끝나면 호출 해주겠다.

    // @PostConstruct, @PreDestroy : 최신 스프링에서 가장 권장하는 방법
    // 스프링에 종속적 기술이 아니라 자바 표준이다. 따라서 스프링이 아닌 다른 컨테이너에서도 동작한다.
    // 컴포넌트 스캔과 잘 어울린다
    // 유일한 단점 : 외부라이브러리에 적용하지 못 한다..
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }
    @PreDestroy
    // 빈이 종료될 때 호출 된다.
    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
