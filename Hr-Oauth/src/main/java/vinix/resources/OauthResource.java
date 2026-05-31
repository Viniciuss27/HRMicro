package vinix.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vinix.entities.User;
import vinix.feignclients.UserFeignClient;

@RestController
@RequestMapping(value = "/oauth")
public class OauthResource {

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