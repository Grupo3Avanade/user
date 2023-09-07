package com.avanade.user.services;

import com.avanade.user.entities.User;
import com.avanade.user.exceptions.DatabaseException;
import com.avanade.user.exceptions.ResourceAlreadyExistsException;
import com.avanade.user.payloads.request.RequestAddress;
import com.avanade.user.payloads.request.RequestUser;
import com.avanade.user.payloads.response.ResponseUser;
import com.avanade.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private RequestUser requestUser;

    @BeforeEach
    void setup() {
        RequestAddress requestAddress = new RequestAddress("12345", "Alguma rua", "Algum bairro", "Alguma cidade", "Algum estado", "Algum complemento", "42");
        requestUser = new RequestUser("Volnei", "volnei@email.com", LocalDate.of(1997, 7, 24), requestAddress);
    }

    @Test
    @DisplayName("Should be able to create a new user")
    void shouldBeAbleToCreateANewUser() {

        // Given
        given(repository.existsByEmail(anyString())).willReturn(false);
        given(repository.save(any(User.class))).willReturn(new User(requestUser));

        // Act
        ResponseUser response = service.create(requestUser);

        // Assert
        assertNotNull(response);
        assertEquals("Volnei", response.name());
        assertEquals("volnei@email.com", response.email());
        assertEquals(LocalDate.of(1997, 7, 24), response.birthday());

        verify(repository, times(1)).existsByEmail(anyString());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw error when create a new user with existing email")
    void shouldThrowErrorWhenCreateANewUserWithExistingEmail() {

        // Given
        given(repository.existsByEmail(anyString())).willReturn(true);

        // Act
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            service.create(requestUser);
        });

        // Verify
        verify(repository, times(1)).existsByEmail(anyString());
    }

    @Test
    @DisplayName("Should throw DatabaseException when database error occurs")
    void shouldThrowDatabaseExceptionWhenDatabaseErrorOccurs() {

        // Given
        given(repository.existsByEmail(anyString())).willReturn(false);
        given(repository.save(any(User.class))).willThrow(DatabaseException.class);

        // Act
        assertThrows(DatabaseException.class, () -> {
            service.create(requestUser);
        });

        verify(repository, times(1)).existsByEmail(anyString());
        verify(repository, times(1)).save(any(User.class));
    }
}
