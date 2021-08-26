package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    // AppConfig는 애플리케이션의 실제 동작에 필요한 구현 객체를 생성한다.
    // AppConfig는 생성한 객체 인스턴스의 참조(래퍼런스)를 생성자를 통해서 주입(연결) 해준다.
    // AppConfig가 MemberServiceImpl, MemoryMemberRepository를 생성한다
    // 객체의 생성과 연결은 AppConfig가 담당한다.

    // AppConfig를 통해서 관심사를 확실히 분리했다.
    // 배역, 배우를 생각해보자
    // AppConfig는 공연 기획자이다.
    // AppConfig는 구체 클래스를 선택한다. 배역에 맞는 담당 배우를 선택한다.
    // 애플리케이션이 어떻게 동작해야 할지 전체 구성을 책임진다.
    // 이제 각 배우들은 담당 기능을 실행하는 책임만 지면 된다.

    // appConfig 객체는 MemoryMemberRepository 객체를 생성하고 그 참조값을 MemberServiceImpl을 생성하면서 생성자로 전달한다.
    // 클라이언트인 MemberServiceImpl 입장에서 보면 의존관계를 마치 외부에서 주입해주는 것 같다고 해서
    // DI 우리말로 의존관계 주입 또는 의존성 주입이라 한다.

    // 애플리케이션 실행 시점(런타임)에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제
    // 의존관계가 연결 되는 것을 의존관계 주입이라 한다.
    // 객체 인스턴스를 생성하고, 그 참조값을 전달해서 연결된다.

    // 스프링 컨테이너 !
    // ApplicationContext 를 스프링 컨테이너라 한다.
    // 기존에는 개발자가 AppConfig를 사용해서 직접 객체를 생성하고 DI를 했지만, 이제부터는 스프링 컨테이너를 통해 사용한다.
    // 스프링컨테이너는 @Configuration이 붙은 AppConfig를 설정(구성) 정보로 사용한다. 여기서 @Bean이라 적힌
    // 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다. 이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라 한다.
    // 스프링 빈은 @Bean이 붙은 메서드의 명을 스프링 빈의 이름으로 사용한다.


    // memberService()
    // @Bean memberService -> new MemoryMemberRepository()

    // orderService()
    // @Bean orderService -> new MemoryMemberRepository()

    // 이러면 singleton이 깨지는거 아닌가 ?
    // 테스트 결과 3개 MemoryMemberRepository가 모두 같은 인스턴스를 갖는다

    // 우리의 의도
    // call AppConfig.memberService
    // call AppConfig.memberRepository
    // call AppConfig.memberRepository
    // call AppConfig.orderService
    // call AppConfig.memberRepository

    // 테스트 결과 ConfigurationSingletoneTest
    // call AppConfig.memberService
    // call AppConfig.memberRepository
    // call AppConfig.orderService

    // 실제 스프링에 등록되는건 AppConfig가 아니라 AppConfig를 상속받은 AppConfig@CGLIB
    // @Bean이 붙은 메서드 마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고, 스프링 빈이 없으면
    // 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어진다.
    // 이 덕분에 싱글톤이 보장되는것
    
    // @Configuration 어노테이션을 사용하지 않는다면?
    // @Bean만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.
    // AppConfig를 상속받은 AppConfig@CGLIB 클래스가 생기지도 않고 스프링에 등록 되지도 않음
    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }
    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

}
