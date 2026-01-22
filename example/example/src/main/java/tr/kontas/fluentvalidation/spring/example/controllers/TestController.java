package tr.kontas.fluentvalidation.spring.example.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.kontas.fluentvalidation.spring.example.dtos.CreateUserRequest;
import tr.kontas.fluentvalidation.spring.example.entity.User;
import tr.kontas.fluentvalidation.spring.example.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final UserRepository repository;

    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());

        return repository.save(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody CreateUserRequest request
    ) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());

        repository.save(user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
