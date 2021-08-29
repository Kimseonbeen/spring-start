package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component

public class OrderServiceImpl implements OrderService {

    // @RequiredArgsConstructor 란?
    // 현재 내 객체 내의 final이 붙은 필드를 모아서 생성자를 자동으로 만들어준다. ctrl + f12로 확인가능

    // 생성자 주입을 사용하면 필드에 'final' 키워드를 사용가능
    // 그래서 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에서 막아준다.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // 필드 주입
    // 이름 그대로 필드에 바로 주입하는 방법
    // 코드가 간결해서 좋아보이지만, 외부에서 변경이 불가능해서 테스트 하기 힘들다는 치명적인 단점이 있다.
    // DI 프레임워크가 없으면 아무것도 할 수 없다.
    // 사용하지 말자 !
//    @Autowired private  MemberRepository memberRepository;
//    @Autowired private  DiscountPolicy discountPolicy;

    // setter(수정자) 주입의 특성
    // 선택적이고, 변경 가능성이 있는 의존관계에 사용
//   @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        System.out.println("memberRepository = " + memberRepository);
//        this.memberRepository = memberRepository;
//    }
//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
//        System.out.println("discountPolicy = " + discountPolicy);
//        this.discountPolicy = discountPolicy;
//    }

    // 생성자 주입
    // 생성자를 통해서 의존 관계를 주입 받는 방법
    // 특징
    // 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다
    // 불변, 필수 의존관계 사용
    // 만약, 생성자가 하나 일경우 @Autowired를 생략해도 생성자 주입이 일어난다.
    // 생성자 주입은 수정자 주입과는 다르게 빈이 생성과 동시에 주입이 일어남
    // 수정자 주입은 빈이 생성되고 그 뒤에 일어난다.
    // Lombok을 사용하면 @RequiredArgsConstructor로 해결가능

    // 문제 !
    // 조회 빈이 2개 이상 일 시
    // @Qualifier는 추가 구분자를 붙여주는 방법이다. 주입 시 추가적인 방법을 제공하는 것이지
    // 빈 이름을 변경하는것은 아니다 !!
    // ex ) @Qualifier("mainDiscountPolicy")

    // 우선순위 !
    // @Primary는 기본값 처럼 동작하는 것이고 @Qualifier는 매우 상세하게 동작하는 것이다.
    // 스프링은 자동보다는 수동이, 넓은 범위 보다는 좁은 범위의 선택권이 우선 순위가 높다
    // 따라서 여기서도 @Qualifierrk 우선권이 높다.. !
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        System.out.println("OrderServiceImpl.OrderServiceImpl");
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    // DIP = 구체에 의존하지 말고 추상에만 의존하라
    // 객체지향 설계 원칙중 DIP를 준수했다 ?
    // 클래스 의존관계를 분석해보면 추상(인터페이스) 뿐만 아니라 구체(구현)클래스에도 의존하고 있다.
    // 구현객체는 몰라야한다 ?
    // 추상에도 의존하고 구체에도 의존 하고 있는 상태.
    // 중요 ! 정책변경시 FixDiscountPolicy를 RateDiscountPolicy 변경하는 순간
    // OrderServiceImpl의 소스코드도 함께 변경해야한다. !-- OCP위반 --!
    // private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    // 그러면 구체클래스에는 의존하지 않도록 변경해보자
    // 해결방안 : 누군가가 클라이언트인 OrderServiceImpl에 DsicountPolicy의 구현 객체를 대신 생성하고 주입해주어야만 한다..!



    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return  new Order(memberId, itemName, itemPrice, discountPrice);
    }
    
    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
