package com.example.menulogapi.security; // ⚠️ご自身のプロジェクトのpackage名（例: com.example.menulogapi）に合わせてください

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF対策を無効化
                .httpBasic(basic -> basic.disable()) // 👈 これ！ポップアップ（Basic認証）を完全にオフにする
                .formLogin(form -> form.disable())   // フォームログインもオフ
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()        // 全てのリクエストを許可
                );
        return http.build();
    }
}