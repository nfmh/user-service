package com.mhealth.userservice.service;

import com.mhealth.userservice.dto.AppUserDTO;
import com.mhealth.userservice.dto.UpdatePasswordDTO;
import com.mhealth.userservice.entity.AppUser;
import com.mhealth.userservice.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @SuppressWarnings("resource")
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        // Arrange
        long userId = 1L;
        AppUser user = new AppUser();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        AppUser result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void testCreateUser() {
        // Arrange
        AppUserDTO userDTO = new AppUserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("testpassword");

        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        when(userRepository.save(any(AppUser.class))).thenReturn(user);

        // Act
        AppUser createdUser = userService.createUser(userDTO);

        // Assert
        assertNotNull(createdUser);
        assertEquals(userDTO.getUsername(), createdUser.getUsername());
        assertEquals(userDTO.getEmail(), createdUser.getEmail());
        assertEquals(userDTO.getPassword(), createdUser.getPassword());
    }

    @Test
    void testDeleteUser() {
        // Arrange
        long userId = 1L;
        AppUser user = new AppUser();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        boolean deleted = userService.deleteUser(userId);

        // Assert
        assertTrue(deleted);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Arrange
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        boolean deleted = userService.deleteUser(userId);

        // Assert
        assertFalse(deleted);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void testUpdateUserPassword() {
        // Arrange
        long userId = 1L;
        UpdatePasswordDTO passwordDTO = new UpdatePasswordDTO();
        passwordDTO.setPassword("newpassword");

        AppUser user = new AppUser();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        boolean updated = userService.updateUserPassword(userId, passwordDTO);

        // Assert
        assertTrue(updated);
        assertEquals(passwordDTO.getPassword(), user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserPassword_UserNotFound() {
        // Arrange
        long userId = 1L;
        UpdatePasswordDTO passwordDTO = new UpdatePasswordDTO();
        passwordDTO.setPassword("newpassword");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        boolean updated = userService.updateUserPassword(userId, passwordDTO);

        // Assert
        assertFalse(updated);
        verify(userRepository, never()).save(any(AppUser.class));
    }
}
