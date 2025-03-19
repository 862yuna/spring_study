package com.gn.mvc.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 환경 설정 파일
@EnableWebSecurity // 스프링 시큐리티 쓸거다.
public class WebSecurityConfig {
	// 요청 중에 정적 리소스가 있는 경우 -> Security 비활성화
	@Bean // 시큐리티가 언제든 읽을 수 있게 만들어줌
	WebSecurityCustomizer configure() {
		// 람다식? 화살표 함수와 같다.
		// web은 webSecurity 를 써서 ignore하겠다.
		// requestMatchers는 특정 url을 정해줌(정적 파일 지정)
		// 스태틱 폴더 밑에 있는 파일들은 스프링 시큐리티
		return web -> web.ignoring()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());   
	}
	// 특정 요청이 들어왔을때 어떻게 처리할 것인가
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http,UserDetailsService customUserDetailService) throws Exception{
		// 시큐리티 필터 체인을 반환하겠다. http방식으로 들어온 요청을 필터링 해줌
		// "/login","/signup","/logout" 는 누구든 OK 나머지는 허락 X authenticated() 인증받은 사람만 접근 가능
		// invalidateHttpSession(true) 로그아웃 성공시 세션 정보 지우기.
		http.userDetailsService(customUserDetailService)
			.authorizeHttpRequests(request -> request
				.requestMatchers("/login","/signup","/logout","/").permitAll()
				.anyRequest().authenticated())
		.formLogin(login -> login.loginPage("/login")
							.successHandler(new MyLoginSuccessHandler())
							.failureHandler(new MyLoginFailureHandler()))
		.logout(logout -> logout.clearAuthentication(true)
								.logoutSuccessUrl("/login")
								.invalidateHttpSession(true));
		return http.build();
	}
	// 비밀번호 암호화에 사용될 Bean 등록
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // 비밀번호를 암호화 할 수 있는 메소드
	}
	
	
	// AuthenticationManager(인증 관리)
	@Bean
	// 매개변수로 configuration이 필요
	AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception{ 
		return authenticationConfiguration.getAuthenticationManager();
	}
}
