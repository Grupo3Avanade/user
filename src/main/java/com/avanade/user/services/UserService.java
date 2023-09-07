package com.avanade.user.services;

import com.avanade.user.entities.Address;
import com.avanade.user.entities.User;
import com.avanade.user.exceptions.DatabaseException;
import com.avanade.user.exceptions.ResourceAlreadyExistsException;
import com.avanade.user.exceptions.ResourceNotFoundException;
import com.avanade.user.payloads.request.RequestUser;
import com.avanade.user.payloads.response.ResponseUser;
import com.avanade.user.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<ResponseUser> findAll() {
        return repository.findAll().stream()
                .map(User::toResponse)
                .toList();
    }

    public ResponseUser findById(UUID id) {
        User user = findOrFailById(id);
        return user.toResponse();
    }

    public User findOrFailById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User could not be found"));
    }

    public ResponseUser create(RequestUser request) {
        User user = new User(request);
        checkIfEmailAlreadyExists(user.getEmail());
        saveOrFail(user);
        return user.toResponse();
    }

    public ResponseUser update(UUID id, RequestUser request) {
        Address address = new Address(request.address());
        User user = findOrFailById(id);

        user.setName(request.name());
        user.setBirthday(request.birthday());
        user.setAddress(address);

        setNewEmailIfNotExist(request, user);

        saveOrFail(user);
        return user.toResponse();
    }

    public void delete(UUID id) {
        User user = findOrFailById(id);
        repository.delete(user);
    }

    private void saveOrFail(User user) {
        try {
            repository.save(user);
        } catch (Exception e) {
            throw new DatabaseException("Error while saving user");
        }
    }

    private void setNewEmailIfNotExist(RequestUser request, User user) {
        if (!request.email().equals(user.getEmail())) {
            checkIfEmailAlreadyExists(request.email());
            user.setEmail(request.email());
        }
    }


    private void checkIfEmailAlreadyExists(String email) {
        boolean isUserExists = repository.existsByEmail(email);
        if (isUserExists) {
            throw new ResourceAlreadyExistsException("User with the informed email already exists");
        }
    }
}
