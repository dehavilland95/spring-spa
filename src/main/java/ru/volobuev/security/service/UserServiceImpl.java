package ru.volobuev.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.volobuev.security.dto.UserDTO;
import ru.volobuev.security.models.Role;
import ru.volobuev.security.models.User;
import ru.volobuev.security.repository.UserRepository;
import ru.volobuev.security.repository.RoleRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private UserDTO userToDTO(User user) {
        return new UserDTO(
                user.getId(), user.getFirstName(), user.getLastName(), user.getAge(), user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.get();
    }

    @Override
    public UserDTO findByUsername(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(this::userToDTO).orElseGet(UserDTO::new);
    }


    @Transactional(readOnly = true)
    public UserDTO findById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.map(this::userToDTO).orElseGet(UserDTO::new);
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
    private Set<Role> createRolesSet(String[] roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName);
            if (role != null) {
                roles.add(role);
            }
        }
        return roles;
    }

    private User DTOToUser(UserDTO userDTO) {
        return new User(
                userDTO.getId(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getAge(), userDTO.getEmail(),
                createRolesSet(userDTO.getRoles()));
    }

    @Transactional
    public void update(UserDTO userDTO) {
        User user = DTOToUser(userDTO);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(this::userToDTO).collect(Collectors.toList());
    }

    @Transactional
    public void save(UserDTO userDTO) {
        Optional<User> userFromDB = userRepository.findByEmail(userDTO.getEmail());
        if (userFromDB.isPresent()) {
            throw  new RuntimeException("User already exists");
        }
        User user = DTOToUser(userDTO);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public boolean save(User user, String[] usersRoles) {
        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isPresent()) {
           return false;
        }
        user.setRoles(createRolesSet(usersRoles));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
