package tr.kontas.fluentvalidation.spring.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.kontas.fluentvalidation.spring.example.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}