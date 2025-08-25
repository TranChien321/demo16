package com.codegym.demo16.services;

import com.codegym.demo16.dto.RoleDTO;
import com.codegym.demo16.models.Role;
import com.codegym.demo16.repositories.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RoleService {
    private final IRoleRepository roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleDTO> list = new ArrayList<>();
        for (Role role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setName(role.getName());
            list.add(roleDTO);
        }
        return list;
    }

    public void createRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        roleRepository.save(role);
    }

    public RoleDTO getRoleById(Long id) {
        return roleRepository.findById(id).map(role -> {
            RoleDTO dto = new RoleDTO();
            dto.setId(role.getId());
            dto.setName(role.getName());
            return dto;
        }).orElse(null);
    }

    public void updateRole(Long id, RoleDTO roleDTO) {
        roleRepository.findById(id).ifPresent(role -> {
            role.setName(roleDTO.getName());
            roleRepository.save(role);
        });
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
