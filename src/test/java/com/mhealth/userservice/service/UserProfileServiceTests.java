package com.mhealth.userservice.service;

import com.mhealth.userservice.entity.UserProfile;
import com.mhealth.userservice.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserProfileServiceTests {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserProfile() {
        // Arrange
        UserProfile userProfile = createUserProfile();
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        // Act
        UserProfile createdUserProfile = userProfileService.createUserProfile(userProfile);

        // Assert
        assertNotNull(createdUserProfile);
        assertEquals(userProfile.getUserId(), createdUserProfile.getUserId());
        assertEquals(userProfile.getAge(), createdUserProfile.getAge());
        // Add more assertions as needed
    }

    @Test
    void testGetUserProfileById() {
        // Arrange
        long userId = 1L;
        UserProfile userProfile = createUserProfile();
        userProfile.setId(userId);
        when(userProfileRepository.findById(userId)).thenReturn(Optional.of(userProfile));

        // Act
        UserProfile result = userProfileService.getUserProfileById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(userProfile.getUserId(), result.getUserId());
        assertEquals(userProfile.getAge(), result.getAge());
        // Add more assertions as needed
    }

    @Test
    void testGetUserProfileById_UserProfileNotFound() {
        // Arrange
        long userProfileId = 1L;
        when(userProfileRepository.findById(userProfileId)).thenReturn(Optional.empty());

        // Act
        UserProfile result = userProfileService.getUserProfileById(userProfileId);

        // Assert
        assertNull(result);
    }

    @Test
    void testUpdateUserProfile() {
        // Arrange
        long userId = 1L;
        UserProfile userProfile = createUpdatedUserProfile();
        userProfile.setId(userId);
        when(userProfileRepository.findById(userId)).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        // Act
        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userProfile);

        // Assert
        assertNotNull(updatedUserProfile);
        assertEquals(userProfile.getUserId(), updatedUserProfile.getUserId());
        assertEquals(userProfile.getAge(), updatedUserProfile.getAge());
        // Add more assertions as needed
    }

    @Test
    void testDeleteUserProfile() {
        // Arrange
        long userId = 1L;

        // Act
        userProfileService.deleteUserProfile(userId);

        // Assert
        verify(userProfileRepository, times(1)).deleteById(userId);
    }

    @Test
    void testCreateUserProfile_WithEmptyProfile() {
        // Arrange
        UserProfile emptyProfile = new UserProfile(); // Create an empty profile
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(emptyProfile);

        // Act
        UserProfile createdEmptyProfile = userProfileService.createUserProfile(emptyProfile);

        // Assert
        assertNotNull(createdEmptyProfile);
        // Add more assertions if needed
    }

    @Test
    void testUpdateUserProfile_WithNullValues() {
        // Arrange
        long userId = 1L;
        UserProfile userProfile = createUpdatedUserProfileWithNullValues();
        userProfile.setId(userId);
        when(userProfileRepository.findById(userId)).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        // Act
        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userProfile);

        // Assert
        assertNotNull(updatedUserProfile);
        // Add assertions to verify behavior with null values
    }

    @Test
    void testCreateUserProfile_RepositorySaveError() {
        // Arrange
        UserProfile userProfile = createUserProfile();
        when(userProfileRepository.save(any(UserProfile.class))).thenThrow(new RuntimeException("Unable to save"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userProfileService.createUserProfile(userProfile));
        // Add more assertions as needed
    }

    private UserProfile createUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(123L);
        userProfile.setAge(30);
        // Set other properties as needed
        return userProfile;
    }

    private UserProfile createUpdatedUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(123L);
        userProfile.setAge(35); // Update the age for testing purposes
        // Set other properties as needed
        return userProfile;
    }

    private UserProfile createUpdatedUserProfileWithNullValues() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(123L);
        userProfile.setAge(null);
        // Set other properties as needed
        return userProfile;
    }
}
