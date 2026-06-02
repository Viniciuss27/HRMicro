package vinix.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vinix.entities.User;
import vinix.feignclients.UserFeignClient;
import vinix.service.AuthService;

@RestController
@RequestMapping(value = "/oauth")
public class AuthResource {

    @Autowired
    private AuthService authService;

    // equivalente ao authorizedGrantTypes("password") 
    @PostMapping(value = "/token")
    public ResponseEntity<String> login(
            @RequestHeader("client-id") String clientId,          // myappname123
            @RequestHeader("client-secret") String clientSecret,  // myappsecret123
            @RequestParam String email,
            @RequestParam String password) {

        String token = authService.authenticate(clientId, clientSecret, email, password);
        return ResponseEntity.ok(token);
    }
    
    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping(value = "/user")
    public ResponseEntity<User> getUser(@RequestParam String email) {
        try {
            User user = userFeignClient.findByEmail(email).getBody();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            User fallback = new User(null, "Fallback", email, null);
            return ResponseEntity.ok(fallback);
        }
    }
}