package ru.volobuev.security.utils;

import org.springframework.stereotype.Component;
import ru.volobuev.security.dto.UserDTO;
import ru.volobuev.security.models.Role;
import ru.volobuev.security.models.User;
import ru.volobuev.security.repository.RoleRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DtoConverter {
    private final RoleRepository roleRepository;
    public DtoConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    private Set<Role> createRolesSet(List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName);
            if (role != null) {
                roles.add(role);
            }
        }
        return roles;
    }
    public UserDTO userToDTO(User user) {
        return new UserDTO(
                user.getId(), user.getFirstName(), user.getLastName(), user.getAge(), user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
    }

    public User dtoToUser(UserDTO userDTO) {
        return new User(
                userDTO.getId() == null ? 0 : userDTO.getId(), userDTO.getFirstName(), userDTO.getLastName(),
                userDTO.getAge(), userDTO.getEmail(), createRolesSet(userDTO.getRoles()));
    }
}
