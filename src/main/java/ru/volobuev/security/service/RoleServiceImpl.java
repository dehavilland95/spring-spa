package ru.volobuev.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.volobuev.security.dto.UserDTO;
import ru.volobuev.security.models.Role;
import ru.volobuev.security.models.User;
import ru.volobuev.security.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}