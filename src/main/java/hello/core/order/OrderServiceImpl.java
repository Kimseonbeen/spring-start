package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

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
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
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
