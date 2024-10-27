package ru.volobuev.security.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.volobuev.security.models.Role;
import ru.volobuev.security.repository.RoleRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        addRoleIfNotExists("ROLE_USER");
        addRoleIfNotExists("ROLE_ADMIN");
    }

    private void addRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role(roleName);
            roleRepository.save(role);
        }
    }
}