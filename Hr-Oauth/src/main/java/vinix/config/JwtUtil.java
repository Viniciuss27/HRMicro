package vinix.config;

import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import vinix.entities.User;

@Component
public class JwtUtil {

	@Value("${security.jwt.secret}")
	private String secret;
	// injeta a chave secreta do yaml: security.jwt.secret

	@Value("${security.jwt.expiration}")
	private Long expiration;
	// define quanto tempo o token é válido no yaml: security.jwt.expiration

	private SecretKey getSecretKey() {
		byte[] keyBytes = Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(secret.getBytes()));
		// converte a string do application.yml em bytes
		// depois decodifica de Base64 para o formato que o jjwt entende

		return Keys.hmacShaKeyFor(keyBytes);
		// cria uma chave criptográfica HMAC-SHA a partir dos bytes
		// HMAC = Hash-based Message Authentication Code
		// é o algoritmo usado para assinar o token JWT
	}

	public String generateToken(User user) {
		return Jwts.builder() // inicia a construção do token

				.subject(user.getEmail())
				// define o "dono" do token (quem está autenticado)
				// fica no campo "sub" do payload do JWT

				.claim("roles", user.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toList()))
				// adiciona as roles do usuário no payload do token
				// o gateway vai usar isso para verificar permissões

				.issuedAt(new Date())
				// registra quando o token foi gerado
				// fica no campo "iat" do payload do JWT

				.expiration(new Date(System.currentTimeMillis() + expiration))
				// define quando o token expira
				// System.currentTimeMillis() = agora em milissegundos
				// + expiration = agora + 1 dia
				// fica no campo "exp" do payload do JWT

				.signWith(getSecretKey())
				// assina o token com a chave secreta
				// garante que o token não foi alterado por terceiros

				.compact();
		// gera o token final no formato: xxxxx.yyyyy.zzzzz
		// header.payload.signature
	}
}