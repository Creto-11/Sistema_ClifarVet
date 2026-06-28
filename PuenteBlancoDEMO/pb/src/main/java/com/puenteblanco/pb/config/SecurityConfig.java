package com.puenteblanco.pb.config;

import com.puenteblanco.pb.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
            "/css/**",
            "/js/**",
            "/images/**",
            "/img/**",
            "/favicon.ico",
            "/webjars/**",
            "/.well-known/**"
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/index.html",
                    "/css/**", "/js/**", "/img/**", "/images/**",
                    "/api/auth/**",
                    "/api/reniec/**",
                    "/api/recovery/**",
                    "/api/password/**",
                    "/vet/veterinarian_dashboard.html",
                    "/.well-known/**"
                ).permitAll()
                .requestMatchers(
                    "/dashboard",
                    "/calendar",
                    "/book-appointment",
                    "/cancel-appointment",
                    "/appointments/**",
                    "/logout",
                    "/pets/**",
                    "/api/payments/**",
                    "/payment-form",
                    "/api/client/**"
                ).hasRole("CLIENT")
                .requestMatchers(
                    "/vet/**",
                    "/api/vet/**",
                    "/api/vet/reports/**"
                ).hasRole("VETERINARIAN")
                .requestMatchers(
                    "/intern/**",
                    "/api/intern/**"
                ).hasRole("INTERN")
                .requestMatchers(
                    "/admin/**",
                    "/api/admin/**",
                    "/api/roles"
                ).hasRole("ADMIN")
                .anyRequest().authenticated())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
    }
}
