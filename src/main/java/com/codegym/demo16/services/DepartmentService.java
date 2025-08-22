
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
}
