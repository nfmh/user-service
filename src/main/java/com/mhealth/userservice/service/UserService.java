package com.mhealth.userservice.service;

import com.mhealth.userservice.dto.AppUserDTO;
import com.mhealth.userservice.dto.UpdatePasswordDTO;
import com.mhealth.userservice.entity.AppUser;

import java.util.List;

public interface UserService {
    AppUser createUser(AppUserDTO userDTO);
    AppUser getUserById(Long userId);
    boolean updateUserPassword(Long userId, UpdatePasswordDTO passwordDTO);
    boolean deleteUser(Long userId);
    List<AppUser> getAllUsers();
}
