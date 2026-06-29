package medical.clinic.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(csrf -> csrf.disable()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth -> auth

                        // Públicas
                        .requestMatchers("/", "/api/v1/auth/login", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Médicos
                        .requestMatchers(HttpMethod.GET, "/api/v1/medicos/**").hasAnyRole("PACIENTE", "ATENDENTE")

                        .requestMatchers(HttpMethod.POST, "/api/v1/medicos/**").hasRole("ATENDENTE")

                        .requestMatchers(HttpMethod.PUT, "/api/v1/medicos/**").hasRole("ATENDENTE")

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/medicos/**").hasRole("ATENDENTE")

                        // Pacientes
                        .requestMatchers(HttpMethod.GET, "/api/v1/pacientes/**").hasRole("ATENDENTE")

                        .requestMatchers(HttpMethod.POST, "/api/v1/pacientes/**").hasRole("ATENDENTE")

                        .requestMatchers(HttpMethod.PUT, "/api/v1/pacientes/**").hasRole("ATENDENTE")

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/pacientes/**").hasRole("ATENDENTE")

                        // Consultas
                        .requestMatchers(HttpMethod.GET, "/api/v1/consultas/**").hasAnyRole("MEDICO", "PACIENTE", "ATENDENTE")

                        .requestMatchers(HttpMethod.POST, "/api/v1/consultas/**").hasAnyRole("PACIENTE", "ATENDENTE")

                        .requestMatchers(HttpMethod.PUT, "/api/v1/consultas/**").hasAnyRole("PACIENTE", "ATENDENTE")

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/consultas/**").hasAnyRole("MEDICO", "PACIENTE", "ATENDENTE")

                        // Qualquer outra rota precisa estar autenticada
                        .anyRequest().authenticated())

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
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