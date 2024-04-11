package com.mhealth.userservice.controller;

import com.mhealth.userservice.dto.UserProfileDTO;
import com.mhealth.userservice.dto.UserProfileUpdateDTO;
import com.mhealth.userservice.entity.UserProfile;
import com.mhealth.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfileById(@PathVariable Long id) {
        UserProfile userProfile = userProfileService.getUserProfileById(id);
        if (userProfile == null) {
            return ResponseEntity.notFound().build();
        }
        UserProfileDTO userProfileDTO = convertToDTO(userProfile);
        return ResponseEntity.ok(userProfileDTO);
    }

    @PostMapping
    public ResponseEntity<UserProfile> createUserProfile(@Valid @RequestBody UserProfileDTO userProfileDTO) {
        UserProfile userProfile = convertToEntity(userProfileDTO);
        UserProfile createdUserProfile = userProfileService.createUserProfile(userProfile);
        return new ResponseEntity<>(createdUserProfile, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable Long id, @Validated @RequestBody UserProfileUpdateDTO userProfileUpdateDTO) {
        UserProfile userProfile = convertToEntity(userProfileUpdateDTO);
        userProfile.setId(id);
        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userProfile);
        return ResponseEntity.ok(updatedUserProfile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserProfile(@PathVariable Long id) {
        userProfileService.deleteUserProfile(id);
        return ResponseEntity.ok("User profile deleted successfully");
    }

    private UserProfileDTO convertToDTO(UserProfile userProfile) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUserId(userProfile.getUserId());
        userProfileDTO.setAge(userProfile.getAge());
        userProfileDTO.setGender(userProfile.getGender() != null ? userProfile.getGender().toString() : null);
        userProfileDTO.setWeight(userProfile.getWeight());
        userProfileDTO.setHeight(userProfile.getHeight());
        userProfileDTO.setActivityLevel(userProfile.getActivityLevel() != null ? userProfile.getActivityLevel().name() : null);
        userProfileDTO.setDietaryPreferences(userProfile.getDietaryPreferences().stream().map(Enum::name).collect(Collectors.toSet()));
        userProfileDTO.setFitnessGoals(userProfile.getFitnessGoals().stream().map(Enum::name).collect(Collectors.toSet()));
        return userProfileDTO;
    }

    private UserProfile convertToEntity(UserProfileUpdateDTO userProfileUpdateDTO) {
        UserProfile userProfile = new UserProfile();
        userProfile.setAge(userProfileUpdateDTO.getAge());
        userProfile.setGender(userProfileUpdateDTO.getGender() != null ? UserProfile.Gender.valueOf(userProfileUpdateDTO.getGender().toUpperCase()) : null);
        userProfile.setWeight(userProfileUpdateDTO.getWeight());
        userProfile.setHeight(userProfileUpdateDTO.getHeight());
        userProfile.setActivityLevel(userProfileUpdateDTO.getActivityLevel() != null ? UserProfile.ActivityLevel.valueOf(userProfileUpdateDTO.getActivityLevel().toUpperCase()) : null);

        // Convert dietary preferences from String to DietaryPreference enum
        Set<UserProfile.DietaryPreference> dietaryPreferences = userProfileUpdateDTO.getDietaryPreferences().stream()
                .map(UserProfile.DietaryPreference::valueOf)
                .collect(Collectors.toSet());
        userProfile.setDietaryPreferences(dietaryPreferences);

        // Convert fitness goals from String to FitnessGoal enum
        Set<UserProfile.FitnessGoal> fitnessGoals = userProfileUpdateDTO.getFitnessGoals().stream()
                .map(UserProfile.FitnessGoal::valueOf)
                .collect(Collectors.toSet());
        userProfile.setFitnessGoals(fitnessGoals);

        return userProfile;
    }

    private UserProfile convertToEntity(UserProfileDTO userProfileDTO) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userProfileDTO.getUserId());
        userProfile.setAge(userProfileDTO.getAge());
        userProfile.setGender(userProfileDTO.getGender() != null ? UserProfile.Gender.valueOf(userProfileDTO.getGender().toUpperCase()) : null);
        userProfile.setWeight(userProfileDTO.getWeight());
        userProfile.setHeight(userProfileDTO.getHeight());
        userProfile.setActivityLevel(userProfileDTO.getActivityLevel() != null ? UserProfile.ActivityLevel.valueOf(userProfileDTO.getActivityLevel().toUpperCase()) : null);

        // Convert dietary preferences from String to DietaryPreference enum
        Set<UserProfile.DietaryPreference> dietaryPreferences = userProfileDTO.getDietaryPreferences().stream()
                .map(UserProfile.DietaryPreference::valueOf)
                .collect(Collectors.toSet());
        userProfile.setDietaryPreferences(dietaryPreferences);

        // Convert fitness goals from String to FitnessGoal enum
        Set<UserProfile.FitnessGoal> fitnessGoals = userProfileDTO.getFitnessGoals().stream()
                .map(UserProfile.FitnessGoal::valueOf)
                .collect(Collectors.toSet());
        userProfile.setFitnessGoals(fitnessGoals);

        return userProfile;
    }
}
