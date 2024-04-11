package com.mhealth.userservice.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserProfileUpdateDTO {

    private Integer age;
    private String gender;
    private Double weight;
    private Double height;
    private String activityLevel;
    private Set<String> dietaryPreferences = new HashSet<>();
    private Set<String> fitnessGoals = new HashSet<>();

    public UserProfileUpdateDTO() {
        // Initialize dietaryPreferences and fitnessGoals to empty sets
    }

}
