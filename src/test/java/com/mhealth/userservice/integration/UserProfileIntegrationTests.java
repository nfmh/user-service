package com.mhealth.userservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhealth.userservice.dto.UserProfileDTO;
import com.mhealth.userservice.entity.UserProfile;
import com.mhealth.userservice.repository.UserProfileRepository;
import com.mhealth.userservice.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class UserProfileIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserProfileService userProfileService;

    @MockBean
    private UserProfileRepository userProfileRepository;

    private UserProfile userProfile;

    @BeforeEach
    public void setUp() {
        userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setUserId(123L);
        // Initialize other fields as needed
    }

    @Test
     void testGetUserProfileById() throws Exception {
        when(userProfileService.getUserProfileById(1L)).thenReturn(userProfile);

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/user-profiles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userProfile)));
    }

    @Test
     void testCreateUserProfile() throws Exception {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUserId(123L);
        // Set other fields in DTO as needed

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/user-profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userProfileDTO)))
                .andExpect(status().isCreated());
    }

    // Add more integration tests as needed
}
