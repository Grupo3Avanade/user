package com.avanade.user.controllers;

import com.avanade.user.entities.User;
import com.avanade.user.payloads.request.RequestAddress;
import com.avanade.user.payloads.request.RequestUser;
import com.avanade.user.payloads.response.ResponseUser;
import com.avanade.user.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService service;

    private RequestUser requestUser;

    @BeforeEach
    void setup() {
        RequestAddress requestAddress = new RequestAddress("12345", "Alguma rua", "Algum bairro", "Alguma cidade", "Algum estado", "Algum complemento", "42");
        requestUser = new RequestUser("Volnei", "volnei@email.com", LocalDate.of(1997, 7, 24), requestAddress);
    }

    @Test
    @DisplayName("Should be able to create a new user")
    void shouldBeAbleToCreateANewUser() throws Exception {
        // Given
        ResponseUser responseUser = new User(requestUser).toResponse();
        when(service.create(requestUser)).thenReturn(responseUser);

        // When / Act
        ResultActions response = mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestUser)));

        jsonPath("$.name", responseUser.name()).hasJsonPath();

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(responseUser.name()))
                .andExpect(jsonPath("$.email").value(responseUser.email()));
    }

    @Test
    @DisplayName("Should be able to find all users")
    void shouldBeAbleToFindAllUsers() throws Exception {
        // Given
        ResponseUser responseUser = new User(requestUser).toResponse();
        when(service.findAll()).thenReturn(List.of(responseUser));

        // When / Act
        ResultActions response = mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(responseUser.name()))
                .andExpect(jsonPath("$[0].email").value(responseUser.email()));
    }

    @Test
    @DisplayName("Should be able to find user by ID")
    void shouldBeAbleToFindUserById() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        ResponseUser responseUser = new User(requestUser).toResponse();
        when(service.findById(id)).thenReturn(responseUser);

        // When / Act
        ResultActions response = mockMvc.perform(get("/users/" + id).contentType(MediaType.APPLICATION_JSON));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(responseUser.name()))
                .andExpect(jsonPath("$.email").value(responseUser.email()));
    }

    @Test
    @DisplayName("Should be able to update a user")
    void shouldBeAbleToUpdateUser() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        ResponseUser responseUser = new User(requestUser).toResponse();
        when(service.update(id, requestUser)).thenReturn(responseUser);

        // When / Act
        ResultActions response = mockMvc.perform(put("/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestUser)));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(responseUser.name()))
                .andExpect(jsonPath("$.email").value(responseUser.email()));
    }

    @Test
    @DisplayName("Should be able to delete a user")
    void shouldBeAbleToDeleteUser() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        doNothing().when(service).delete(id);

        // When / Act
        ResultActions response = mockMvc.perform(delete("/users/" + id).contentType(MediaType.APPLICATION_JSON));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isNoContent());
    }
}
