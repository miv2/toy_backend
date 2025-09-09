package co.kr.toybackend.security.config;

import co.kr.toybackend.security.filter.JwtAccessDeniedHandler;
import co.kr.toybackend.security.filter.JwtAuthenticationEntryPoint;
import co.kr.toybackend.security.filter.JwtAuthenticationFilter;
import co.kr.toybackend.security.provider.JwtTokenProvider;
import co.kr.toybackend.security.service.JwtTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, JwtTokenService jwtTokenService, JwtAccessDeniedHandler jwtAccessDeniedHandler, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenService = jwtTokenService;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/test/**").permitAll()
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/api/v1/member/**").permitAll()

                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource())
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, jwtTokenService),
                        UsernamePasswordAuthenticationFilter.class);
        return http.getOrBuild();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/favicon.ico"
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("http://localhost:8081");
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

}
