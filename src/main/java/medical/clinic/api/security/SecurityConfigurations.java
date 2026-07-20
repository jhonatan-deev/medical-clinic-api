package medical.clinic.api.security;

import medical.clinic.api.security.handler.CustomAccessDeniedHandler;
import medical.clinic.api.security.handler.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfigurations(SecurityFilter securityFilter, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler accessDeniedHandler) {

        this.securityFilter = securityFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)).authorizeHttpRequests(auth -> auth

                        .requestMatchers("/", "/api/v1/auth/login", "/api/v1/auth/reset-password", "/api/v1/auth/forgot-password", "/api/v1/auth/confirmar-conta", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // PACIENTE (ATENDENTE e ADMIN herdam essas permissões)
                        .requestMatchers(HttpMethod.GET, "/api/v1/medicos/**").hasRole("PACIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/consultas/**").hasRole("PACIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/consultas/**").hasRole("PACIENTE")

                        // MÉDICO (ATENDENTE e ADMIN herdam essas permissões)
                        .requestMatchers(HttpMethod.GET, "/api/v1/consultas/**").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/consultas/**").hasRole("MEDICO")

                        // ATENDENTE (ADMIN herda essas permissões)
                        .requestMatchers(HttpMethod.POST, "/api/v1/medicos/**").hasRole("ATENDENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/medicos/**").hasRole("ATENDENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/medicos/**").hasRole("ATENDENTE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/pacientes/**").hasRole("ATENDENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/pacientes/**").hasRole("ATENDENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/pacientes/**").hasRole("ATENDENTE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/pacientes/**").permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}