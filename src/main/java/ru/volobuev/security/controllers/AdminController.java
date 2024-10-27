package ru.volobuev.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.volobuev.security.models.Role;
import ru.volobuev.security.models.User;
import ru.volobuev.security.service.UserService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public String index(Model model) {
        List<User> users = userService.allUsers();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserDetails user = userService.loadUserByUsername(email);
        List<Role> roles = userService.getAllRoles();
        model.addAttribute("allRoles", roles);
        if(user != null) {
            model.addAttribute("myInfo", (User) user);
        }
        model.addAttribute("users", users);
        model.addAttribute("user", new User());
        return "admin/index";
    }
    @GetMapping(params = "id")
    public String show(
            @RequestParam(name = "id", required = false, defaultValue = "0") long id,
            Model model){
        model.addAttribute("user", userService.findUserById(id));
        return "admin/show";
    }
    @GetMapping("/new")
    public String newUser(Model model){
        List<Role> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("user", new User());
        return "admin/new";
    }
    @PostMapping()
    public String create(@ModelAttribute("user") User user,  @RequestParam(required = false) String[] usersRoles){
        System.out.println();
        userService.saveUser(user, usersRoles);
        return "redirect:/admin";
    }
    @GetMapping(value = "/edit", params = "id")
    public String edit(
            @RequestParam(name = "id", required = false, defaultValue = "0") long id,
            Model model){
        List<Role> roles = userService.getAllRoles();
        model.addAttribute("allRoles", roles);
        model.addAttribute("user", userService.findUserById(id));
        return "admin/edit";
    }
    @PatchMapping()
    public String update(
            @RequestParam(name = "id", required = false, defaultValue = "0") long id,
            @ModelAttribute("user") User user,
            @RequestParam(required = false) String[] usersRoles){
        System.out.println();
        userService.updateUser(id, user, usersRoles);
        return "redirect:/admin";
    }
    @DeleteMapping(params = "id")
    public String delete(@RequestParam(name = "id", required = false, defaultValue = "0") long id){
        System.out.println();
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
