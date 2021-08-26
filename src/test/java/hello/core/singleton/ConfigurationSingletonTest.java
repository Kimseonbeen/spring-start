package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        // Impl로 꺼내야 테스트 용도 로직을 실행할 수 있다.
        // 원래는 구체타입으로 꺼내면 안 좋음.
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();
        //....? 두번 new로 객체 생성을 하여도 같은 주소값(인스턴스)을 같는다..
        System.out.println("memberService -> memberRepository = " + memberRepository1);
        System.out.println("orderService -> memberRepository = " + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);

        // 확인 해보면 memberRepository 인스턴스는 모두 같은 인스턴스가 공유되어 사용된다.
        Assertions.assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        Assertions.assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);

    }

    @Test
    void configurationDeep() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);
        // 순수한 클래스라면 class hello.core.AppConfig이 출력 돠어야 함
        // 그런데 예상과 다르게 복잡하게 뒤에 붙어있음
        // 이것은 내가 만든 클래스가 아니라 스프링이 CGLIB라는 바이트 코드 조작 라이브러리를 사용해서 AppConfig 클래스를
        // 상속받은 임의의 다른 클래스를 만들고, 그 다른 클래스를 스프링 빈으로 등록한 것이다!
        // AppConig@CGLIB 조회가 된 이유는 AppConfig의 자식 타입이라 조회가 된것
        System.out.println("bean = " + bean.getClass());
    }
}
