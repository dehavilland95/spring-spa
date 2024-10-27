package ru.volobuev.security.controllers;

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
import ru.volobuev.security.service.UserService;
import ru.volobuev.security.utils.PersonValidator;

import java.util.List;
import java.util.Set;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private PersonValidator personValidator;
    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam int age,
            @RequestParam String password,
            @RequestParam(required = false) String[] roles,
            Model model) {
        System.out.println();
        User user = new User(firstName, lastName, email, age);
        user.setPassword(password);

        //personValidator.validate(user, bindingResult);
//        if(bindingResult.hasErrors()) {
//            return "registration";
//        }
        if (!userService.saveUser(user, roles)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }
        Set<Role> usersRoles = user.getRoles();
        if (usersRoles.stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            return "redirect:/admin"; // Если есть ROLE_ADMIN, перенаправляем на /admin
        } else {
            return "redirect:/user"; // Иначе перенаправляем на /user
        }
    }
}
