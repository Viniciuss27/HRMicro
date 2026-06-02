package vinix.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Configuration
public class AppConfig {

	@Value("${security.jwt.secret}")
	private String secret;
	// injeta a chave secreta do application.yml

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
		// cria o bean de criptografia de senha
		// BCrypt é um algoritmo de hash unidirecional
		// ou seja, você não consegue descriptografar, só comparar
		// usado em dois momentos:
		// 1. AuthService → para comparar senha digitada com hash do banco
		// 2. SecurityConfig → para o DaoAuthenticationProvider saber como comparar
	}

	@Bean
	SecretKey jwtSecretKey() {
		byte[] keyBytes = Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(secret.getBytes()));
		// pega a string do application.yml → converte para bytes → codifica em Base64
		// o Decoders.BASE64.decode converte de volta para bytes no formato correto
		// esse processo garante que a chave tenha o tamanho mínimo exigido pelo
		// HMAC-SHA

		return Keys.hmacShaKeyFor(keyBytes);
		// cria e retorna a chave criptográfica como bean do Spring
		// este bean é injetado no JwtUtil para assinar os tokens
		// e futuramente no gateway para validar os tokens
	}
}