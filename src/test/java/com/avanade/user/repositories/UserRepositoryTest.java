package com.avanade.user.repositories;

import com.avanade.user.entities.User;
import com.avanade.user.payloads.request.RequestAddress;
import com.avanade.user.payloads.request.RequestUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {


    @Autowired
    private UserRepository repository;
    private User user;

    @BeforeEach
    void setup() {
        RequestAddress requestAddress = new RequestAddress("12345", "Alguma rua", "Algum bairro", "Alguma cidade", "Algum estado", "Algum complemento", "42");
        RequestUser requestUser = new RequestUser("Volnei", "volnei@email.com", LocalDate.of(1997, 7, 24), requestAddress);
        user = new User(requestUser);

    }

    @Test
    @DisplayName("Should successfully create and initialize a new user in the repository")
    void shouldSuccessfullyCreateAndInitializeNewUser() {

        User user = repository.saveAndFlush(this.user);

        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getEmail());
        assertNotNull(user.getName());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertNotNull(user.getAddress());
        assertEquals("Volnei", user.getName());
        assertEquals("volnei@email.com", user.getEmail());
        assertEquals(LocalDate.of(1997, 7, 24), user.getBirthday());
    }

    @Test
    @DisplayName("Should confirm that an existing email is recognized by the repository")
    void shouldConfirmExistingEmailIsRecognizedByRepository() {

        repository.saveAndFlush(user);

        Boolean existsByEmail = repository.existsByEmail(user.getEmail());

        assertTrue(existsByEmail);
    }

    @Test
    @DisplayName("ShouldRetrieveAllUsers")
    void shouldRetrieveAllUsers() {
        repository.saveAndFlush(user);

        List<User> users = repository.findAll();

        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    @DisplayName("ShouldRetrieveUserById")
    void shouldRetrieveUserById() {
        User savedUser = repository.saveAndFlush(user);

        Optional<User> foundUser = repository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    @DisplayName("ShouldNotRetrieveUserForNonExistentId")
    void shouldNotRetrieveUserForNonExistentId() {
        Optional<User> foundUser = repository.findById(UUID.randomUUID());

        assertFalse(foundUser.isPresent());
    }
}
