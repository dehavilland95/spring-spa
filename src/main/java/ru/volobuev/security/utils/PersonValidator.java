package ru.volobuev.security.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.volobuev.security.models.User;
import ru.volobuev.security.service.UserServiceImpl;

@Component
public class PersonValidator implements Validator {

    private final UserServiceImpl userServiceImpl;
    public PersonValidator(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            userServiceImpl.loadUserByUsername(user.getUsername());
        }catch (Exception e) {
            return;
        }
        errors.rejectValue("username", null, "Аккаунт с таким имейлом уже существует");
    }
}
