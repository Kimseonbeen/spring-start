package hello.core.scope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);

    }

    @Test
    void singletonClientPrototype() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);
    }

    @Scope("singleton")
    static class ClientBean {
        // singleton 빈안에서 prototype 빈 사용시..
        // 생성 시점에 주입
        // 그러므로 prototype 빈의 같은 인스턴스를 사용하게 됌
        // private final PrototypeBean prototypeBean;

        // prototypeBeanProvider.getObject() 을 통해서 항상 새로운 프로토타입 빈이 생성되는 것을 확인 할 수 있다.
        // ObejctProvider의 getObject를 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다. (DL)
        // 스프링이 제공하느 기능을 사용하지만, 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기는 훨씬 쉬워진다.

        // 자바 표준 Provider :
        // get() 메서드 하나로 기능이 매우 단순
        // 별도의 라이브러리가 필요
        // 자바 표준이므로 스프링이 아닌 다른 컨테이너에서도 사용할 수 있다.
        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        // public ClientBean(PrototypeBean prototypeBean) {
        //     this.prototypeBean = prototypeBean;
        // }
        public int logic() {
            // prototypeBeanProvider.get() : !!!!
            // 스프링 컨터이너를 통해서 조회하기 때문에.
            // 필요한 의존관계 주입과 초기화의 도움을 받을 수 있다.
            // 그래서 get 할때 마다 새로운 인스턴스 객체가 생기는것...!
            // 정리 : Provider.get() 호출하면, 호출할 때 마다 계속 새로운 PrototypeBean이 생성
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);

        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
