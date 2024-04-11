package com.mhealth.userservice.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_profile")
@Data
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "weight")
    private Double weight; // in kilograms

    @Column(name = "height")
    private Double height; // in centimeters

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level")
    private ActivityLevel activityLevel;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_dietary_preferences")
    @Column(name = "dietary_preference")
    private Set<DietaryPreference> dietaryPreferences;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_fitness_goals")
    @Column(name = "fitness_goal")
    private Set<FitnessGoal> fitnessGoals;

    // Define enums here

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public enum ActivityLevel {
        SEDENTARY,
        LIGHTLY_ACTIVE,
        MODERATELY_ACTIVE,
        VERY_ACTIVE,
        EXTRA_ACTIVE
    }

    public enum DietaryPreference {
        VEGETARIAN,
        VEGAN,
        KETOGENIC,
        PALEO,
        GLUTEN_FREE,
        DAIRY_FREE
    }

    public enum FitnessGoal {
        WEIGHT_LOSS,
        MUSCLE_GAIN,
        MAINTENANCE,
        IMPROVE_ENDURANCE,
        IMPROVE_STRENGTH
    }
}
