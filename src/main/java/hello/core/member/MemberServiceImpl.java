package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService {

    // 이제는 설계 변경으로 MemberServiceImpl은 MemoryMemberRepository를 의존하지 않는다.
    // 단지 MemberRepository 인터페이스만 의존한다.
    // MemberServiceImpl 입정에서 생성자를 통해 어떤 구현 객체가 들어오지(주입될지) 알 수 없다.
    // MemberServiceImpl의 생성자를 통해서 어떤 구현 객체를 주입할지는 오직 외부(AppConfig)에서 결정된다.
    // MemberServiceImpl은 이제부터 의존관계에 대한 고민은 외부에 맡기고 실행에만 집중한다.

    private final MemberRepository memberRepository;

    @Autowired  // ac.getBean(MemberRepository.class)
    // 자동으로 의존관계를 주입 해준다.
    // MemberRepository 타입에 맞는 memberRepository을 주입 해준다.

    // 생성자에 @Autowired를 지정하면, 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입힌다.
    // 이때 기본 조회 전략은 타입이 같은 빈을 찾아서 주입한다.
    // MemoryMemberRepository는 MemberRepository의 자식 객체이므로 타입이 동일
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
