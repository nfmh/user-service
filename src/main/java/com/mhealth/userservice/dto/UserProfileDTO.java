package com.mhealth.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class UserProfileDTO {

    @NotNull(message = "User ID is required")
    private long userId;

    @NotNull(message = "Age is required")
    @Positive(message = "Age must be a positive number")
    private Integer age;

    @NotNull(message = "Gender is required")
    private String gender;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be a positive number")
    private Double weight;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be a positive number")
    private Double height;

    @NotBlank(message = "Activity level is required")
    private String activityLevel;

    @NotEmpty(message = "At least one dietary preference must be selected")
    private Set<String> dietaryPreferences;

    @NotEmpty(message = "At least one fitness goal must be selected")
    private Set<String> fitnessGoals;

}
