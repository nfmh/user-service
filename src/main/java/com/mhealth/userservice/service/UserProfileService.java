package com.mhealth.userservice.service;
import com.mhealth.userservice.entity.UserProfile;


public interface UserProfileService {
    UserProfile createUserProfile(UserProfile userProfile);
    UserProfile getUserProfileById(Long userProfileId);
    UserProfile updateUserProfile(UserProfile userProfile);
    boolean deleteUserProfile(Long userProfileId);
}