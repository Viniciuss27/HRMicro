package vinix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import vinix.config.JwtUtil;
import vinix.entities.User;
import vinix.feignclients.UserFeignClient;

@Service
public class AuthService implements UserDetailsService {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticate(String clientId, String clientSecret,
                               String email, String password) {
        if (!this.clientId.equals(clientId) ||
            !passwordEncoder.matches(clientSecret, passwordEncoder.encode(this.clientSecret))) {
            throw new RuntimeException("Client inválido");
        }

        User user = userFeignClient.findByEmail(email).getBody();

        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        return jwtUtil.generateToken(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userFeignClient.findByEmail(username).getBody();

        if (user == null) {
            throw new UsernameNotFoundException("Email não encontrado: " + username);
        }

        return user; // retorna direto pois User já implementa UserDetails
    }
}