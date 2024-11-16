package ru.volobuev.security.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.volobuev.security.dto.UserDTO;
import ru.volobuev.security.models.Role;
import ru.volobuev.security.models.User;
import ru.volobuev.security.repository.RoleRepository;
import ru.volobuev.security.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public UserDTO getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.getByEmail(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new UserDTO(
                    user.getId(), user.getFirstName(), user.getLastName(), user.getAge(), user.getEmail(),
                    user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        }else{
            return new UserDTO();
        }
    }
    public UserDTO findUserById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new UserDTO(
                    user.getId(), user.getFirstName(), user.getLastName(), user.getAge(), user.getEmail(),
                    user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        }else{
            return new UserDTO();
        }
    }

    public void updateUser(UserDTO userDTO) {
        System.out.println();
        User userToUpdate = em.find(User.class, userDTO.getId());
        Set<Role> roles = new HashSet<>();
        for (String roleName : userDTO.getRoles()) {
            Role role = roleRepository.findByName(roleName);
            if (role != null) {
                roles.add(role);
            }
        }
        userToUpdate.setFirstName(userDTO.getFirstName());
        userToUpdate.setLastName(userDTO.getLastName());
        userToUpdate.setEmail(userDTO.getEmail());
        userToUpdate.setAge(userDTO.getAge());
        userToUpdate.setRoles(roles);
        userRepository.save(userToUpdate);
    }

//    public void updateUser(long id, User user, String[] roleNames) {
//        System.out.println();
//        User userToUpdate = em.find(User.class, id);
//        Set<Role> roles = new HashSet<>();
//        for (String roleName : roleNames) {
//            Role role = roleRepository.findByName(roleName);
//            if (role != null) {
//                roles.add(role);
//            }
//        }
//        userToUpdate.setFirstName(user.getFirstName());
//        userToUpdate.setLastName(user.getLastName());
//        userToUpdate.setEmail(user.getEmail());
//        userToUpdate.setAge(user.getAge());
//        userToUpdate.setRoles(roles);
//        userRepository.save(userToUpdate);
//    }

    public List<UserDTO> allUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user ->
                        new UserDTO(user.getId(), user.getFirstName(), user.getLastName(),
                                user.getAge(), user.getEmail(),
                                user.getRoles().stream().map(role -> role.getName()).toList()))
                .toList();
    }

    public boolean saveUser(User user, String[] roleNames) {
        User userFromDB = userRepository.findByEmail(user.getEmail());

        if (userFromDB != null) {
            return false;
        }
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName);
            if (role != null) {
                roles.add(role);
            }
        }
        user.setRoles(roles);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<User> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }
}
