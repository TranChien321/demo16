package com.codegym.demo16.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // ADMIN, USER, MANAGER...

    private String description;

    // Quan hệ N Role - N Users
    @ManyToMany(mappedBy = "roles")
    private java.util.Set<User> users;

    public Role() {
    }

    public Role(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.util.Set<User> getUsers() {
        return users;
    }

    public void setUsers(java.util.Set<User> users) {
        this.users = users;
    }
}