package com.codegym.demo16.dto;

import com.codegym.demo16.validations.custom.UniqueRoleName;
import jakarta.validation.constraints.NotBlank;

public class RoleDTO {
    private Long id;

    @NotBlank(message = "Role name is required")
    @UniqueRoleName
    private String name;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
