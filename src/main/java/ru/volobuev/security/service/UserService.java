package ru.volobuev.security.service;

import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.volobuev.security.dto.UserDTO;
import ru.volobuev.security.models.User;

import java.util.List;

public interface UserService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    UserDTO findByUsername(String username);
    UserDTO findById(Long userId);
    void update(UserDTO user);
    List<UserDTO> getAll();
    void save(UserDTO user);
    void delete(Long userId);
    boolean save(@Valid User user, String[] usersRoles);
}
