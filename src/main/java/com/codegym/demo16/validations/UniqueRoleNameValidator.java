package com.codegym.demo16.validations;

import com.codegym.demo16.repositories.IRoleRepository;
import com.codegym.demo16.validations.custom.UniqueRoleName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueRoleNameValidator implements ConstraintValidator<UniqueRoleName, String> {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) return true;
    return roleRepository.findByName(value).isEmpty();}
}
