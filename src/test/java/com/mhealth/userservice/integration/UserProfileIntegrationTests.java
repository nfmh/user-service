package com.mhealth.userservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhealth.userservice.controller.UserProfileController;
import com.mhealth.userservice.dto.UserProfileDTO;
import com.mhealth.userservice.entity.UserProfile;
import com.mhealth.userservice.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserProfileController.class)
@AutoConfigureMockMvc
 class UserProfileIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserProfileService userProfileService;

    @Test
    void createUserProfile_ReturnsCreated() throws Exception {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUserId(1L);
        userProfileDTO.setAge(30);
        userProfileDTO.setGender("MALE");
        userProfileDTO.setWeight(70.5);
        userProfileDTO.setHeight(175.0);
        userProfileDTO.setActivityLevel("MODERATELY_ACTIVE");
        userProfileDTO.setDietaryPreferences(Set.of("VEGETARIAN"));
        userProfileDTO.setFitnessGoals(Set.of("WEIGHT_LOSS"));

        UserProfile userProfile = new UserProfile();
        // Set userProfile properties

        when(userProfileService.createUserProfile(userProfile)).thenReturn(userProfile);

        mockMvc.perform(post("/auth/user-profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userProfileDTO)))
                .andExpect(status().isCreated());
    }
}
