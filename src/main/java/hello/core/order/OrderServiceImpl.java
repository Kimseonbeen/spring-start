package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
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
}
