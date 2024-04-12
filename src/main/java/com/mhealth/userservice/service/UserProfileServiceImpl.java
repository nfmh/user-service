package com.mhealth.userservice.service;

import com.mhealth.userservice.entity.UserProfile;
import com.mhealth.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfile createUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile getUserProfileById(Long userProfileId) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(userProfileId);
        return userProfileOptional.orElse(null);
    }

    @Override
    public UserProfile updateUserProfile(UserProfile userProfile) {
        // Ensure the user profile has a non-null ID
        if (userProfile.getId() == null) {
            throw new IllegalArgumentException("User profile ID cannot be null");
        }
        // Get the existing user profile from the database
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(userProfile.getId());
        if (userProfileOptional.isPresent()) {
            UserProfile existingProfile = userProfileOptional.get();
            updateFields(existingProfile, userProfile);
            // Save the updated user profile
            return userProfileRepository.save(existingProfile);
        } else {
            // Handle the case where the user profile does not exist
            throw new IllegalArgumentException("User profile with ID " + userProfile.getId() + " does not exist");
        }
    }

    private void updateFields(UserProfile existingProfile, UserProfile updatedProfile) {
        if (updatedProfile.getAge() != null) {
            existingProfile.setAge(updatedProfile.getAge());
        }
        if (updatedProfile.getGender() != null) {
            existingProfile.setGender(updatedProfile.getGender());
        }
        if (updatedProfile.getWeight() != null) {
            existingProfile.setWeight(updatedProfile.getWeight());
        }
        if (updatedProfile.getHeight() != null) {
            existingProfile.setHeight(updatedProfile.getHeight());
        }
        if (updatedProfile.getActivityLevel() != null) {
            existingProfile.setActivityLevel(updatedProfile.getActivityLevel());
        }
        // Only update dietaryPreferences if the field is not null and not empty in the updated profile
        if (updatedProfile.getDietaryPreferences() != null && !updatedProfile.getDietaryPreferences().isEmpty()) {
            existingProfile.setDietaryPreferences(updatedProfile.getDietaryPreferences());
        }
        // Only update fitnessGoals if the field is not null and not empty in the updated profile
        if (updatedProfile.getFitnessGoals() != null && !updatedProfile.getFitnessGoals().isEmpty()) {
            existingProfile.setFitnessGoals(updatedProfile.getFitnessGoals());
        }
    }


    @Override
    public boolean deleteUserProfile(Long userProfileId) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(userProfileId);
        if (userProfileOptional.isPresent()) {
            userProfileRepository.deleteById(userProfileId);
            return true;
        }
        return false;
    }
}
