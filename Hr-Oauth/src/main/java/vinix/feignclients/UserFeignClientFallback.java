package vinix.feignclients;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import vinix.entities.User;

@Component
public class UserFeignClientFallback implements UserFeignClient {

	@Override
	public ResponseEntity<User> findByEmail(String email) {
		User user = new User(null, "Fallback" , email, null);
		return ResponseEntity.ok(user);
	}
}
