package vinix.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vinix.entities.User;
import vinix.repositories.UserRepository;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserRepository repository;

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User obj = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id)); // 👈 evita NoSuchElementException
        return ResponseEntity.ok(obj);
    }

    @GetMapping(value = "/email/{email}") 
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        User obj = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + email));
        return ResponseEntity.ok(obj);
    }
}