package com.codegym.demo16.validations;


import com.codegym.demo16.validations.custom.ValidImage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private final List<String> allowedTypes = Arrays.asList(
            "image/png",
            "image/jpeg",
            "image/jpg"
    );

    private final long MAX_SIZE = 2 * 1024 * 1024; // 2MB

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // Không bắt buộc upload ảnh, muốn bắt buộc thì return false
        }

        // ✅ Check type
        if (!allowedTypes.contains(file.getContentType())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Chỉ chấp nhận PNG, JPG, JPEG")
                    .addConstraintViolation();
            return false;
        }

        // ✅ Check size
        if (file.getSize() > MAX_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Kích thước tối đa là 2MB")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
