package com.mhealth.userservice.service;

import com.mhealth.userservice.entity.UserProfile;
import com.mhealth.userservice.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

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
        if (updatedProfile.getDietaryPreferences() != null) {
            existingProfile.setDietaryPreferences(updatedProfile.getDietaryPreferences());
        }
        if (updatedProfile.getFitnessGoals() != null) {
            existingProfile.setFitnessGoals(updatedProfile.getFitnessGoals());
        }
    }

    @Override
    public void deleteUserProfile(Long userProfileId) {
        userProfileRepository.deleteById(userProfileId);
    }
}
