package com.apinews.apiwebnews.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())) // ✅ Bật lại CORS
                .cors(withDefaults()) // 🔥 Sử dụng cấu hình CORS riêng
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ Cho phép đăng ký, đăng nhập
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/forgot-password", "/api/auth/reset-password").permitAll()
                        .requestMatchers("/images/**").permitAll()  // Cho phép truy cập ảnh không cần login

                        // ✅ News API
                        .requestMatchers("GET", "/api/news/**").permitAll()  // Ai cũng có thể xem
                        .requestMatchers("POST", "/api/news").hasRole("ADMIN")
                        .requestMatchers("PUT", "/api/news/**").hasRole("ADMIN")
                        .requestMatchers("DELETE", "/api/news/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll() // Cho phép truy cập ảnh

                        .requestMatchers(HttpMethod.GET, "/api/category/**").permitAll() // 🔥 Cho phép tất cả User GET danh mục và tin tức theo danh mục
                        .requestMatchers(HttpMethod.POST, "/api/category").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/category/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/category/**").hasRole("ADMIN")

                        // ✅ Comment API
                        .requestMatchers("GET", "/api/comment/**").permitAll()
                        .requestMatchers("POST", "/api/comment").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("PUT", "/api/comment/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("DELETE", "/api/comment/**").hasAnyRole("USER", "ADMIN")

                        // ✅ User API (CẬP NHẬT DẤU `/`)
                        .requestMatchers("/api/user/me").authenticated()
                        .requestMatchers("/api/user/update").authenticated()
                        .requestMatchers("/api/user/all").permitAll()
                        .requestMatchers("/api/user/{id}").authenticated()
                        .requestMatchers("/api/user/create").hasRole("ADMIN")
                        .requestMatchers("/api/user/update/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/user/delete/{id}").hasRole("ADMIN")

                        // ✅ Các API khác yêu cầu đăng nhập
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> cors.disable()) // ✅ Không cần cấu hình CORS trong SecurityConfig, vì đã có CORSConfig
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll() // 🔥 BỎ TOÀN BỘ PHÂN QUYỀN, CHO PHÉP TẤT CẢ REQUEST
//                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
