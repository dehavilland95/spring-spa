package ru.volobuev.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.volobuev.security.dto.IdDTO;
import ru.volobuev.security.dto.UserDTO;
import ru.volobuev.security.models.Role;
import ru.volobuev.security.models.User;
import ru.volobuev.security.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @PostMapping("/getUsers")
    @ResponseBody
    public List<UserDTO> getAllUsers(){
        List<UserDTO> users = userService.allUsers();
        System.out.println();
        return users;
    }

    @PostMapping("/createUser")
    @ResponseBody
    public void createUser(@RequestBody UserDTO userDTO){
        userService.saveUser(
                new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                        userDTO.getAge(), userDTO.getPassword()), userDTO.getRoles().toArray(new String[0]));
    }

    @PostMapping("/deleteUser")
    @ResponseBody
    public void deleteUser(@RequestBody IdDTO idDTO){
        userService.deleteUser(idDTO.getId());
    }

    @PostMapping("/updateUser")
    @ResponseBody
    public void updateUser(@RequestBody UserDTO userDTO){
        System.out.println();
        userService.updateUser(userDTO);
    }

    @PostMapping("/getUserById")
    @ResponseBody
    public UserDTO getUserById(@RequestBody IdDTO idDTO){
        return userService.findUserById(idDTO.getId());
    }

    @PostMapping("/getAllRoles")
    @ResponseBody
    public List<String> getAllRoles(){
        List<Role> roles = userService.getAllRoles();
        return roles.stream().map(Role::getName).toList();
    }

    @GetMapping()
    public String index(Model model) {
        return "admin/index";
    }
    @GetMapping(params = "id")
    public String show(
            @RequestParam(name = "id", required = false, defaultValue = "0") long id,
            Model model){
        model.addAttribute("user", userService.findUserById(id));
        return "admin/show";
    }
//    @GetMapping("/new")
//    public String newUser(Model model){
//        List<Role> roles = userService.getAllRoles();
//        model.addAttribute("roles", roles);
//        model.addAttribute("user", new User());
//        return "admin/new";
//    }
//    @PostMapping()
//    public String create(@ModelAttribute("user") User user,  @RequestParam(required = false) String[] usersRoles){
//        System.out.println();
//        userService.saveUser(user, usersRoles);
//        return "redirect:/admin";
//    }
//    @GetMapping(value = "/edit", params = "id")
//    public String edit(
//            @RequestParam(name = "id", required = false, defaultValue = "0") long id,
//            Model model){
//        List<Role> roles = userService.getAllRoles();
//        model.addAttribute("allRoles", roles);
//        model.addAttribute("user", userService.findUserById(id));
//        return "admin/edit";
//    }
//    @PatchMapping()
//    public String update(
//            @RequestParam(name = "id", required = false, defaultValue = "0") long id,
//            @ModelAttribute("user") User user,
//            @RequestParam(required = false) String[] usersRoles){
//        System.out.println();
//        userService.updateUser(id, user, usersRoles);
//        return "redirect:/admin";
//    }
//    @DeleteMapping(params = "id")
//    public String delete(@RequestParam(name = "id", required = false, defaultValue = "0") long id){
//        System.out.println();
//        userService.deleteUser(id);
//        return "redirect:/admin";
//    }
}
