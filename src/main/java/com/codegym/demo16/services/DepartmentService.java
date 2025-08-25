
package com.codegym.demo16.services;

import com.codegym.demo16.dto.DepartmentDTO;
import com.codegym.demo16.models.Department;
import com.codegym.demo16.repositories.IDepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {
    private IDepartmentRepository departmentRepository;

    public DepartmentService(IDepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDTO> list = new ArrayList<>();
        for (Department department : departments) {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(department.getId());
            departmentDTO.setName(department.getName());
            list.add(departmentDTO);
        }
        return list;
    }

    //xoá department by id
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        if (!department.getUsers().isEmpty()) {
            throw new RuntimeException("Cannot delete department because it has users!");
        }

        departmentRepository.deleteById(id);
    }

    //Edit department
    public void editDepartment(DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(departmentDTO.getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        department.setName(departmentDTO.getName());
        departmentRepository.save(department);
    }


    //Create department
    public void createDepartment(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setName(departmentDTO.getName());
        departmentRepository.save(department);
    }
}