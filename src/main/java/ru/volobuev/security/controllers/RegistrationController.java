package ru.volobuev.security.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.volobuev.security.models.Role;
import ru.volobuev.security.models.User;
import ru.volobuev.security.service.RoleService;
import ru.volobuev.security.service.UserService;
import java.util.List;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/registration")
    public String registration(Model model) {
        List<Role> roles = roleService.getAll();
        model.addAttribute("allRoles", roles);
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            @RequestParam() String[] usersRoles,
            Model model) {
        if (result.hasErrors()) {
            return "registration";
        }
        if (!userService.save(user, usersRoles)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }
        return "redirect:/login";
    }
}
