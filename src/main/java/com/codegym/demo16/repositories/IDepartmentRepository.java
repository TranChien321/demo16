package com.codegym.demo16.repositories;

import com.codegym.demo16.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDepartmentRepository extends JpaRepository<Department, Long> {
    // Additional query methods can be defined here if needed
}