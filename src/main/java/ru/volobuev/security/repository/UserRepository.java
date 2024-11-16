package ru.volobuev.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.volobuev.security.models.User;

import java.util.Optional;

@Repository
@Component
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>  findByEmail(String email);
}