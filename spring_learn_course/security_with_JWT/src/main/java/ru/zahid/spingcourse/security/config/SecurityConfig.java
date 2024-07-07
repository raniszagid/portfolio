package ru.zahid.spingcourse.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.zahid.spingcourse.security.services.PersonDetailService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PersonDetailService userService;
    private final JWTFilter jwtFilter;
    private final AuthenticationConfiguration configuration;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth
                            //.requestMatchers("/admin").hasRole("ADMIN")
                            .requestMatchers("/auth/login",
                    "/auth/registration","/error")
                                 .permitAll()
                            //.anyRequest().authenticated()
                            .anyRequest().hasAnyRole("USER", "ADMIN")
                    ;
                 }).formLogin(formLogin ->
                         formLogin.loginPage("/auth/login")
                                 .loginProcessingUrl("/process_login")
                                 .defaultSuccessUrl("/hello", true)
                                 .failureUrl("/auth/login?error"))
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}














