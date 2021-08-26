package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
// @ComponentScan은 @Component가 붙은 모든 클래스를 스프링 빈으로 등록한다.
// 이때 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용한다.
@ComponentScan(
        // 탐색할 패키지의 시작 위치를 지정한다. 이 패키지를 포함해서 하위 패키지를 모두 탐색한다.
        // basePackages = "hello.core.member",
        // basePackageClasses = AutoAppConfig.class,
        // 만약, 지정하지 않으면 @ComponentScan이 붙은 설정 정보 클래스 패키지가 시작 위치가 된다 package hello.core;
        // 권장 방법 : 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것 package hello.core;

        // 컴포넌트스캔을 사용하면 @Configuration이 붙은 설정 정보도 자동으로 등록되기 때문에,
        // AppConfig, TestConfig 등 앞서 만들어두었던 설정 정보도 함께 등록되고, 실행되어 버린다.
        // 그래서 excludeFilters를 이용해서 설정정보는 컴포넌트 스캔 대상에서 제외했다.
        // 보통 설정 정보를 컴포넌트 스캔 대상에서 제외하지는 않지만, 기존 예제 코드를 최대한 남기고 유지하기 위해서 이 방법을 선택
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
}