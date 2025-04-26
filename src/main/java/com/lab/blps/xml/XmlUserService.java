package com.lab.blps.xml;

import com.lab.blps.models.applications.User;
import com.lab.blps.models.applications.Users;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
public class XmlUserService {
    private List<User> users;
    private JAXBContext jaxbContext;
    private final Path writablePath = Paths.get("data/users.xml");

    @PostConstruct
    public void init() {
        try {
            JAXBContext context = JAXBContext.newInstance(Users.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            try (InputStream fileStream = Files.newInputStream(writablePath)) {
                Users wrapper = (Users) unmarshaller.unmarshal(fileStream);
                this.users = wrapper.getUsers();
            }

            // сохраняем контекст для записи
            this.jaxbContext = context;

        } catch (IOException | JAXBException e) {
            throw new RuntimeException("Ошибка при инициализации XML", e);
        }
    }


    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    @Transactional     // пометим, чтобы можно было откатывать, если что
    public User save(User user) {
        // если пользователь уже есть — обновляем, иначе добавляем
        findByUsername(user.getUsername())
                .ifPresent(users::remove);
        users.add(user);
        writeXml();      // переписываем файл
        return user;
    }

    private void writeXml() {
        try (OutputStream os = Files.newOutputStream(writablePath, CREATE, TRUNCATE_EXISTING)) {
            Marshaller m = jaxbContext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(new Users(users), os);
        } catch (IOException | JAXBException e) {
            throw new RuntimeException("Не удалось записать users.xml", e);
        }
    }


}

