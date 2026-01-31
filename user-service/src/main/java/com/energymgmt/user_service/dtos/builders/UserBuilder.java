package com.energymgmt.user_service.dtos.builders;

import com.energymgmt.user_service.dtos.UserDTO;
import com.energymgmt.user_service.dtos.UserDetailsDTO;
import com.energymgmt.user_service.entities.User;

public class UserBuilder {
    private UserBuilder(){
    }

    public static UserDTO toUserDTO(User user){
        return new UserDTO(user.getID(), user.getUsername(), user.getAge());
    }

    public static UserDetailsDTO toUserDetailsDTO(User user){
        return new UserDetailsDTO(user.getID(), user.getUsername(), "[PROTECTED]", user.getName(), user.getAddress(), user.getAge());
    }
    public static User toEntity(UserDetailsDTO dto){
        User user = new User();

        user.setID(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setAddress(dto.getAddress());
        user.setAge(dto.getAge());

        return user;
    }

}