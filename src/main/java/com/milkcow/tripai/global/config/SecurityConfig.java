package com.milkcow.tripai.global.config;


import com.milkcow.tripai.jwt.JwtService;
import com.milkcow.tripai.jwt.handler.JwtAccessDeniedHandler;
import com.milkcow.tripai.jwt.handler.JwtAuthenticationEntryPoint;
import com.milkcow.tripai.member.repository.MemberRepository;
import com.milkcow.tripai.security.filter.CustomAuthenticationFilter;
import com.milkcow.tripai.security.filter.CustomAuthenticationProvider;
import com.milkcow.tripai.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


/**
 * Spring Security 환경 설정을 구성하기 위한 클래스입니다. 웹 서비스가 로드 될때 Spring Container 의해 관리가 되는 클래스이며 사용자에 대한 ‘인증’과 ‘인가’에 대한 구성을 Bean
 * 메서드로 주입을 한다.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final MemberRepository memberRepository;

    private final JwtService jwtService;


    /**
     * 1. 정적 자원(Resource)에 대해서 인증된 사용자가  정적 자원의 접근에 대해 ‘인가’에 대한 설정을 담당하는 메서드이다.
     *
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 자원에 대해서 Security를 적용하지 않음으로 설정
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 2. HTTP에 대해서 ‘인증’과 ‘인가’를 담당하는 메서드이며 필터를 통해 인증 방식과 인증 절차에 대해서 등록하며 설정을 담당하는 메서드이다.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("SecurityConfig Start !!! ");

        http
                .csrf().disable()
                .httpBasic().disable() // httpBasic 사용 X
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/signup/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin().disable()
                .headers().frameOptions().disable()

                .and()
                .addFilterBefore(customAuthFilter(), UsernamePasswordAuthenticationFilter.class) // 인증 필터 추가
                .addFilterBefore(new CustomAuthorizationFilter(authenticationManager(), memberRepository, jwtService),
                        BasicAuthenticationFilter.class) // 권한 필터 추가
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // 인증 예외 처리 핸들러
                .accessDeniedHandler(new JwtAccessDeniedHandler()); // 인가 예외 처리 핸들러

        return http.build();
    }


    /**
     * 3. authenticate 의 인증 메서드를 제공하는 매니져로'Provider'의 인터페이스를 의미합니다. - 과정: CustomAuthenticationFilter →
     * AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
     *
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    /**
     * 4. '인증' 제공자로 사용자의 이름과 비밀번호가 요구됩니다. - 과정: CustomAuthenticationFilter → AuthenticationManager(interface) →
     * CustomAuthenticationProvider(implements)
     *
     * @return CustomAuthenticationProvider
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(bCryptPasswordEncoder());
    }

    /**
     * 5. 비밀번호를 암호화하기 위한 BCrypt 인코딩을 통하여 비밀번호에 대한 암호화를 수행합니다.
     *
     * @return BCryptPasswordEncoder
     */
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 6. 커스텀한 '인증' 필터로 접근 URL, 데이터 전달방식(form) 등 인증 과정 및 인증 후 처리에 대한 설정을 구성하는 메서드입니다.
     *
     * @return CustomAuthenticationFilter
     */
    @Bean
    public CustomAuthenticationFilter customAuthFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(),
                jwtService);
        customAuthenticationFilter.setFilterProcessesUrl("/login");     // 접근 URL
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }


}
