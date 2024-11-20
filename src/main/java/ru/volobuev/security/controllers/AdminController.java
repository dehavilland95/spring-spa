package ru.volobuev.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.volobuev.security.dto.IdDTO;
import ru.volobuev.security.dto.UserDTO;
import ru.volobuev.security.models.Role;
import ru.volobuev.security.service.RoleService;
import ru.volobuev.security.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/getUsers")
    public List<UserDTO> getAllUsers(){
        return userService.getAll();
    }

    @PostMapping("/createUser")
    public void createUser(@RequestBody UserDTO userDTO){
        userService.save(userDTO);
    }

    @PostMapping("/deleteUser")
    public void deleteUser(@RequestBody IdDTO idDTO){
        userService.delete(idDTO.getId());
    }

    @PostMapping("/updateUser")
    public void updateUser(@RequestBody UserDTO userDTO){
        userService.update(userDTO);
    }

    @PostMapping("/getUserById")
    public UserDTO getUserById(@RequestBody IdDTO idDTO){
        return userService.findById(idDTO.getId());
    }

    @PostMapping("/getAllRoles")
    public List<String> getAllRoles(){
        List<Role> roles = roleService.getAll();
        return roles.stream().map(Role::getName).toList();
    }
}
