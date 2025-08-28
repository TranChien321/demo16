package com.codegym.demo16.repositories;

import com.codegym.demo16.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    // Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);

    // Tìm theo department (có phân trang)
    Page<User> findByDepartmentId(Long departmentId, Pageable pageable);


    // Tìm theo tên, email hoặc phone
    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
            String name, String email, String phone, Pageable pageable);
}
