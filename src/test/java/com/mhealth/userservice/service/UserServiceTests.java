package com.mhealth.userservice.service;

import com.mhealth.userservice.controller.UserController;
import com.mhealth.userservice.dto.AppUserDTO;
import com.mhealth.userservice.dto.UpdatePasswordDTO;
import com.mhealth.userservice.entity.AppUser;
import com.mhealth.userservice.repository.AppUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    @InjectMocks
    private UserController userController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAppUserInitialization() {
        // Arrange
        String username = "testUser";
        String email = "test@example.com";
        String password = "password";

        // Act
        AppUser user = new AppUser(username, email, password);

        // Assert
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    void testGetUserById() {
        // Arrange
        long userId = 1L;
        AppUser user = new AppUser("testUser", "test@example.com", "password");
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        AppUser result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }
    void testCreateUserController() {
        // Arrange
        AppUserDTO userDTO = new AppUserDTO();
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        AppUser user = new AppUser("testUser", "test@example.com", "password");
        when(userService.createUser(userDTO)).thenReturn(user);

        // Act
        ResponseEntity<AppUser> response = userController.createUser(userDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
    @Test
    void testCreateUser() {
        // Arrange
        AppUserDTO userDTO = new AppUserDTO();
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        AppUser user = new AppUser("testUser", "test@example.com", "password");
        user.setId(1L);

        when(userRepository.save(any(AppUser.class))).thenReturn(user);

        // Act
        AppUser createdUser = userService.createUser(userDTO);

        // Assert
        assertNotNull(createdUser);
        assertEquals(user.getUsername(), createdUser.getUsername());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getPassword(), createdUser.getPassword());
    }

    @Test
    void testUpdateUserPassword_UserNotFound() {
        // Arrange
        long userId = 1L;
        UpdatePasswordDTO passwordDTO = new UpdatePasswordDTO();
        passwordDTO.setPassword("newPassword");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        boolean updated = userService.updateUserPassword(userId, passwordDTO);

        // Assert
        Assertions.assertFalse(updated);
    }

    @Test
    void testUpdateUserPassword_NullNewPassword() {
        // Arrange
        long userId = 1L;
        UpdatePasswordDTO passwordDTO = new UpdatePasswordDTO();
        passwordDTO.setPassword(null);
        AppUser user = new AppUser("testUser", "test@example.com", "oldPassword");
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUserPassword(userId, passwordDTO));
        assertEquals("New password cannot be null", exception.getMessage());
    }



    @Test
    void testDeleteUser() {
        // Arrange
        long userId = 1L;
        AppUser user = new AppUser("testUser", "test@example.com", "password");
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(userId);

        // Act
        boolean deleted = userService.deleteUser(userId);

        // Assert
        Assertions.assertTrue(deleted);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<AppUser> userList = new ArrayList<>();
        userList.add(new AppUser("testUser1", "test1@example.com", "password1"));
        userList.add(new AppUser("testUser2", "test2@example.com", "password2"));
        userList.add(new AppUser("testUser3", "test3@example.com", "password3"));

        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<AppUser> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(userList.size(), result.size());
    }
}
