package com.avanade.user.services;

import com.avanade.user.entities.User;
import com.avanade.user.exceptions.DatabaseException;
import com.avanade.user.exceptions.ResourceAlreadyExistsException;
import com.avanade.user.exceptions.ResourceNotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private RequestUser requestUser;
    private RequestAddress requestAddress;

    @BeforeEach
    void setup() {
        requestAddress = new RequestAddress("12345", "Alguma rua", "Algum bairro", "Alguma cidade", "Algum estado", "Algum complemento", "42");
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

    @Test
    @DisplayName("Should Retrieved all Users")
    void shouldRetrievedAllUsers() {
        // Given
        given(repository.findAll()).willReturn(new ArrayList<>());

        // Act
        List<ResponseUser> result = service.findAll();
        assertInstanceOf(List.class, result);
        assertNotNull(result);

        // Verify
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should Retrieve And Convert User By Id")
    void shouldRetrieveAndConvertUserById() {
        User mockUser = new User(requestUser);
        repository.save(mockUser);

        // Given
        given(repository.findById(mockUser.getId())).willReturn(Optional.of(mockUser));

        // Act
        ResponseUser result = service.findById(mockUser.getId());
        assertInstanceOf(ResponseUser.class, result);
        assertNotNull(result);

        // Verify
        verify(repository, times(1)).findById(mockUser.getId());
    }

    @Test
    @DisplayName("Should Throw ResourceNotFoundException When User Does Not Exist")
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
        UUID randomUUID = UUID.randomUUID();

        // Given
        given(repository.findById(randomUUID)).willReturn(Optional.empty());

        // Act
        assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(randomUUID);
        });

        // Verify
        verify(repository, times(1)).findById(randomUUID);
    }

    @Test
    @DisplayName("Should delete user when it's existing")
    void shouldDeleteUserWhenItsExists() {
        User mockUser = new User(requestUser);
        repository.save(mockUser);

        // Given
        given(repository.findById(mockUser.getId())).willReturn(Optional.of(mockUser));
        doNothing().when(repository).delete(mockUser);

        // Act
        service.delete(mockUser.getId());

        // Verify
        verify(repository, times(1)).findById(mockUser.getId());
        verify(repository, times(1)).delete(mockUser);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when attempting to delete non-existing user")
    void shouldThrowExceptionWhenDeleteNonExistingUser() {
        UUID randomUUID = UUID.randomUUID();

        // Given
        given(repository.findById(randomUUID)).willReturn(Optional.empty());

        // Act
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(randomUUID);
        });

        // Verify
        verify(repository, times(1)).findById(randomUUID);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when attempting to update non-existing user")
    void shouldThrowExceptionWhenUpdateNonExistingUser() {
        UUID randomUUID = UUID.randomUUID();

        // Given
        given(repository.findById(randomUUID)).willReturn(Optional.empty());

        // Act
        assertThrows(ResourceNotFoundException.class, () -> {
            service.update(randomUUID, requestUser);
        });

        // Verify
        verify(repository, times(1)).findById(randomUUID);
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Arrange (Given)
        UUID existingUserId = UUID.randomUUID();
        User existingUser = new User(requestUser);
        RequestUser updateUserRequest = new RequestUser("Teste", "test@email.com", LocalDate.of(1997, 7, 24), requestAddress);

        given(repository.findById(existingUserId)).willReturn(Optional.of(existingUser));
        given(repository.save(any(User.class))).willReturn(existingUser);

        // Act
        ResponseUser response = service.update(existingUserId, updateUserRequest);

        // Assert (Verify)
        assertNotNull(response);
        assertEquals(updateUserRequest.name(), response.name());
        assertEquals(updateUserRequest.email(), response.email());
        assertEquals(updateUserRequest.birthday(), response.birthday());

        verify(repository).findById(existingUserId);
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Should fail when email already exists")
    void shouldFailWhenEmailAlreadyExists() {
        // Given
        UUID existingUserId = UUID.randomUUID();
        User existingUser = new User(requestUser);
        RequestUser userRequest2 = new RequestUser("Teste", "test@email.com", LocalDate.of(1997, 7, 24), requestAddress);
        RequestUser updateUserRequest = new RequestUser("Teste", userRequest2.email(), LocalDate.of(1997, 7, 24), requestAddress);

        given(repository.findById(existingUserId)).willReturn(Optional.of(existingUser));
        given(repository.existsByEmail(userRequest2.email())).willReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> service.update(existingUserId, updateUserRequest));

        // Verify
        verify(repository).findById(existingUserId);
        verify(repository).existsByEmail(userRequest2.email());
    }
}