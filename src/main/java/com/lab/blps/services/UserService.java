package com.lab.blps.services;

import com.lab.blps.exceptions.UserWithThisUsernameAlreadyExists;
import com.lab.blps.models.applications.User;
import com.lab.blps.repositories.applications.UserRepository;
import com.lab.blps.xml.XmlUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final XmlUserService xmlUserService;

    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    @Transactional
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserWithThisUsernameAlreadyExists("Пользователь с таким именем уже существует");
        }
        xmlUserService.save(user);
        return save(user);
    }


    public User getByUsername(String username) {
        return repository.findByUsername(username);
    }


    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = getCurrentUsername();
        return getByUsername(username);
    }


    public String getCurrentUsername() {
        // Получение имени пользователя из контекста Spring Security
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
