package com.codegym.demo16.dto;

import com.codegym.demo16.validations.custom.UniqueEmail;
import com.codegym.demo16.validations.custom.ValidImage;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CreateUserDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @UniqueEmail(message = "Email address already exists")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @ValidImage(message = "Only PNG, JPG, JPEG are allowed")
    private MultipartFile image;

    private Long departmentId;

    @NotEmpty(message = "Phai chon it nhat 1 vai tro")
    private List<Long> roleIds;

    public CreateUserDTO() {
    }

    public CreateUserDTO(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public @NotEmpty(message = "Phải chọn ít nhất 1 role") List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(@NotEmpty(message = "Phải chọn ít nhất 1 role") List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}