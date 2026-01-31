package com.energymgmt.auth_service.dtos;

import java.io.Serializable;
import java.util.UUID;

public class UserSyncDTO implements Serializable {
    private UUID id;
    private String username;
    private String name;
    private String address;
    private Integer age;
    private String role;
    private String action;

    public UserSyncDTO(UUID id, String username, String name, String address, Integer age, String role, String action) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.address = address;
        this.age = age;
        this.role = role;
        this.action = action;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}