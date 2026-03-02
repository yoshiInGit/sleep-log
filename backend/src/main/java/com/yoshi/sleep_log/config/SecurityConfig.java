package com.yoshi.sleep_log.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 設定
 * 開発時は全エンドポイントをパブリックアクセス可能にする
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // 全てのリクエストを許可（開発用）
            )
            .csrf(csrf -> csrf.disable());  // CSRF保護を無効化（開発用）
        
        return http.build();
    }
}
