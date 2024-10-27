package ru.volobuev.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.volobuev.security.models.User;

@Repository
@Component
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}