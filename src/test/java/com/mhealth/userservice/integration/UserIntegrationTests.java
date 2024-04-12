package com.mhealth.userservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhealth.userservice.controller.UserController;
import com.mhealth.userservice.dto.AppUserDTO;
import com.mhealth.userservice.dto.UpdatePasswordDTO;
import com.mhealth.userservice.entity.AppUser;
import com.mhealth.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.mockito.ArgumentMatchers;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
 class UserIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void createUser_ReturnsCreated() throws Exception {
        AppUserDTO userDTO = new AppUserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");

        AppUser user = new AppUser();
        user.setId(1L); // Set the id in the user object

        when(userService.createUser(ArgumentMatchers.any(AppUserDTO.class))).thenReturn(user); // Use ArgumentMatchers.any() here

        mockMvc.perform(post("/auth/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists()); // Check if the id field exists in the response
    }

    @Test
    void getUserById_ReturnsUser() throws Exception {
        long userId = 1L;
        AppUser user = new AppUser();
        user.setId(userId);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/auth/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }


    @Test
    void getAllUsers_ReturnsListOfUsers() throws Exception {
        // Mocking userService to return a list of users
        List<AppUser> users = new ArrayList<>();
        AppUser user1 = new AppUser();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password123");

        AppUser user2 = new AppUser();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password456");

        users.add(user1);
        users.add(user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[0].username").value(user1.getUsername()))
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                .andExpect(jsonPath("$[1].username").value(user2.getUsername()));
    }
}
