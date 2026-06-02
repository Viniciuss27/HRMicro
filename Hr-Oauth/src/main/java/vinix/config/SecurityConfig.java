package vinix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity    // ativa o módulo de segurança do Spring Security
public class SecurityConfig {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    // injeta o bean de criptografia declarado no AppConfig
    // usado para comparar a senha digitada com o hash salvo no banco

    @Autowired
    private UserDetailsService userDetailsService;
    // injeta o serviço que busca o usuário no banco pelo email
    // o Spring Security usa ele para carregar o usuário na autenticação

    @Bean //Autenticador
    DaoAuthenticationProvider authenticationProvider() {
        //componente que faz a autenticação, ou seja, busca o usuário no banco
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        // diz ao provider COMO buscar o usuário (pelo UserDetailsService)
        provider.setPasswordEncoder(passwordEncoder);
        // diz ao provider COMO comparar a senha (BCrypt)
        return provider;
        // fluxo: recebe email+senha → busca usuário → compara senha → autentica
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
        // AuthenticationManager é o orquestrador da autenticação
        // ele usa o DaoAuthenticationProvider que configuramos acima
        // o AuthService vai injetar e usar ele para autenticar o usuário
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            // desativa proteção CSRF pois a API é stateless (usa JWT, não sessão)

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // STATELESS = o servidor não guarda sessão do usuário
            // cada requisição precisa trazer o token JWT para se identificar

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/oauth/token").permitAll()
                // libera o endpoint de login sem precisar de token
                // é aqui que o usuário vai enviar email+senha e receber o JWT
                .anyRequest().authenticated()
                // todos os outros endpoints exigem token JWT válido
            );

        return http.build();
    }
}