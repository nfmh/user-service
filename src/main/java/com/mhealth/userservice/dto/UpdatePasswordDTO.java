package com.mhealth.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordDTO {

    @NotBlank(message = "Password is required")
    @Size(min = 10, message = "Password should have at least 10 characters")
    private String password;
}
