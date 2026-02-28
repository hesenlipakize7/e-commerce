package eCommerce.configuration;

import eCommerce.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/api/categories/**").permitAll()
                                .requestMatchers("/api/products/**").permitAll()
                                .requestMatchers("/api/addresses/**").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/addresses").hasRole("USER")
                                .requestMatchers(HttpMethod.PUT,"/api/addresses/**").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE,"/api/addresses/**").hasRole("USER")
                                .requestMatchers("/api/cart/**").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/cart/**").hasRole("USER")
                                .requestMatchers(HttpMethod.PUT,"/api/products/**").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE,"/api/products/**").hasRole("USER")
                                .requestMatchers("/api/favorites/**").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/favorites/**").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE,"/api/favorites/**").hasRole("USER")
                                .requestMatchers("/api/orders/**").authenticated()
                                .requestMatchers("/api/payments/**").hasRole("USER")
                                .requestMatchers("/api/users/**").authenticated()
                                .requestMatchers(HttpMethod.PUT,"/api/users/**").hasRole("USER")

                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) ->
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN)
                        )
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
