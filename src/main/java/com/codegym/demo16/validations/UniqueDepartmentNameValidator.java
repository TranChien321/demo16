package com.codegym.demo16.validations;

import com.codegym.demo16.repositories.IDepartmentRepository;
import com.codegym.demo16.validations.custom.UniqueDepartmentName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueDepartmentNameValidator implements ConstraintValidator<UniqueDepartmentName, String> {

    @Autowired
    private IDepartmentRepository departmentRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) return true;
        return !departmentRepository.existsByName(value.trim());
    }
}
