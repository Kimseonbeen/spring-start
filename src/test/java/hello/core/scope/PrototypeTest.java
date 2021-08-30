package hello.core.scope;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.assertThat;

public class PrototypeTest {

    // 프로토타입 빈의 특징
    // 1. 스프링 컨테이너에 요청할 때 마다 새로 생성된다.
    // 2. 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입 긜고 초기화까지만 관여한다.
    // 3. 종료 메서드가 호출되지 않는다.
    // 4. 그래서 프로토타입 빈은 프로토타입 빈을 조회한 클라이언트가 관리해야 한다.
    //    종료 메서드에 대한 호출도 클라이언트가 직접 해야한다.

    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);
        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

        ac.close();


    }

    @Scope("prototype")
    static class PrototypeBean {
        @PostConstruct
        public void init() {
            System.out.println("prototypeBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("prototypeBean.destroy");
        }

    }
}
