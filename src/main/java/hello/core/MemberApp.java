package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
        
        // AppConfig의 설정정보를 스프링이 다 객체를 생성하여 관리를 해줌
        // 스프링컨테이너에 Bean으로 된것들을 등록
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        // Ctrl + Alt + V
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        // soutv 하면 변수 찾을 수 있당.. !
        System.out.println("new member = " + member.getName());
        System.out.println("findMember = " + findMember.getName());
    }
}
