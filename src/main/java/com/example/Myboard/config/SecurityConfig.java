package com.example.Myboard.config;

import com.example.Myboard.service.Security.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOidcUserService customOidcUserService;

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 비로그인 허용
                        .requestMatchers("/", "/blog", "/login", "/join",
                                "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/articles", "/articles/*").permitAll() // 목록/상세는 공개

                        // 글작성/수정/삭제는 로그인 필요
                        .requestMatchers(HttpMethod.POST, "/articles").authenticated()          // 새 글 저장(폼 POST)
                        .requestMatchers("/articles/*/edit", "/articles/*/delete").authenticated()

                        // API는 보호
                        .requestMatchers("/api/**").authenticated()

                        .anyRequest().authenticated()
                )

                .formLogin(fl -> fl
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        // ✅ 항상 /blog 로
                        .defaultSuccessUrl("/blog", true)
                        .permitAll()
                )

                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(u -> u.oidcUserService(customOidcUserService))
                        // ✅ 구글 로그인도 항상 /blog 로
                        .defaultSuccessUrl("/blog", true)
                )

                .logout(lo -> lo
                        .logoutUrl("/logout")
                        // ✅ 로그아웃 뒤에도 /blog
                        .logoutSuccessUrl("/blog")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                );

        return http.build();
    }
}
