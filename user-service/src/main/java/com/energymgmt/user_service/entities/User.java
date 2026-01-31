package com.energymgmt.user_service.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID ID;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "password", nullable = false)
    private String password; //this will be hashed

    public User(){
    }
    public User(String username, String name, int age, String address) {
        this.username = username;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public User(String name, String address, int age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
