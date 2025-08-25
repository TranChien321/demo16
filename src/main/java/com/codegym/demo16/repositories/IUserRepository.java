package com.codegym.demo16.repositories;

import com.codegym.demo16.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends PagingAndSortingRepository<User , Long> {
    Optional<User> findById(Long id);
    void delete(User user);
    void save(User user);
    boolean existsByEmail(String email);

    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
            String name, String email, String phone);
}