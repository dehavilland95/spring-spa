package ru.volobuev.security.service;

import ru.volobuev.security.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAll();
}
