package com.codegym.demo16.dto;

import com.codegym.demo16.validations.custom.UniqueDepartmentName;
import jakarta.validation.constraints.NotBlank;


public class DepartmentDTO {
    private Long id;

    @NotBlank(message = "Tên phòng ban không được để trống")
    @UniqueDepartmentName
    private String name;

    public DepartmentDTO() {
    }

    public DepartmentDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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

}