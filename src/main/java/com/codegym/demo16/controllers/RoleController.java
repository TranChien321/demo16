package com.codegym.demo16.controllers;

import com.codegym.demo16.dto.RoleDTO;
import com.codegym.demo16.services.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Danh sách role
    @GetMapping
    public String listRoles(Model model) {
        List<RoleDTO> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "roles/list";
    }

    // Tạo role mới - Form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new RoleDTO());
        return "roles/create";
    }

    // Lưu role mới
    @PostMapping("/store")
    public String storeRole(@Validated @ModelAttribute("role") RoleDTO roleDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "roles/create";
        }
        roleService.createRole(roleDTO);
        return "redirect:/roles";
    }

    // Form sửa role
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        RoleDTO roleDTO = roleService.getRoleById(id);
        if (roleDTO == null) {
            return "redirect:/roles";
        }
        model.addAttribute("role", roleDTO);
        return "roles/edit";
    }

    // Cập nhật role
    @PostMapping("/{id}/update")
    public String updateRole(@PathVariable("id") Long id,
                             @Validated @ModelAttribute("role") RoleDTO roleDTO,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "roles/edit";
        }
        roleService.updateRole(id, roleDTO);
        return "redirect:/roles";
    }

    // Xoá role
    @GetMapping("/{id}/delete")
    public String deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return "redirect:/roles";
    }
}
