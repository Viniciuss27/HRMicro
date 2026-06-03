package vinix.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Flux;

@Configuration
@EnableWebFluxSecurity // gateway usa WebFlux, não WebMvc
public class ResourceServerConfig {

	@Autowired
	private SecretKey jwtSecretKey;

	// rota publica, todos acessam
	private static final String[] PUBLIC = { "/hr-oauth/oauth/token" };

	// rotas privada para trabaladores
	private static final String[] OPERATOR = { "/hr-worker/**" };

	// rota privada para ADM e Usuarios
	private static final String[] ADMIN = { "/hr-payroll/**", "/hr-user/**" };

	@Bean
	ReactiveJwtDecoder jwtDecoder() {
		return NimbusReactiveJwtDecoder.withSecretKey(jwtSecretKey).build();
	}

	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeExchange(auth -> auth
	            .pathMatchers(PUBLIC).permitAll()//público
	            .pathMatchers(HttpMethod.GET, OPERATOR).hasAnyRole("OPERATOR", "ADMIN") //operator e adm so get
	            .pathMatchers(ADMIN).hasRole("ADMIN")//admin
	            .anyExchange().authenticated()//qualquer outro exige autorização
	        )
	        .oauth2ResourceServer(oauth2 -> oauth2
	            .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
	        );

	    return http.build();
	}

	// converte o campo "roles" do token JWT para authorities do Spring Security
	@Bean
	ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
		converter.setAuthoritiesClaimName("roles"); // campo "roles" do nosso token
		converter.setAuthorityPrefix(""); // adiciona ROLE_ automaticamente

		ReactiveJwtAuthenticationConverter reactiveConverter = new ReactiveJwtAuthenticationConverter();
		reactiveConverter.setJwtGrantedAuthoritiesConverter(jwt -> Flux.fromIterable(converter.convert(jwt)));
		return reactiveConverter;
	}
}