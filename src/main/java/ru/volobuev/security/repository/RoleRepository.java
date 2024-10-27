package ru.volobuev.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.volobuev.security.models.Role;

@Repository
@Component
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
    boolean existsByName(String name);
}
