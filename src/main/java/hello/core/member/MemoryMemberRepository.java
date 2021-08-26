package hello.core.member;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MemoryMemberRepository  implements MemberRepository {

    // static을 사용한 이유
    // 애플리케이션이 시작되고 종료될 때 까지 Map은 단 한번 생성
    // 이 Map을 공동으로 사용 한다.
    // static을 사용하지 않게 되면 new MemberRepository() 를 작성할 때마다 각각의 인스턴스가 자기만의 Map 을 갖게 됩니다.
    // 이 Map은 인스턴스끼리 공유하지 않고 자기 혼자만 사용하는 Map이 됩니다.
    // static 이 붙은 필드나 메서드는, '인스턴스의 소유'가 아닌, '클래스의 소유'가 됩니다.
    private static Map<Long, Member> store = new HashMap<>();

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);

    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
