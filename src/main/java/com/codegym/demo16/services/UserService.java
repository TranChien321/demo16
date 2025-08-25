
package com.codegym.demo16.services;

import com.codegym.demo16.dto.CreateUserDTO;
import com.codegym.demo16.dto.EditUserDTO;
import com.codegym.demo16.dto.UserDTO;
import com.codegym.demo16.dto.response.ListDepartmentResponse;
import com.codegym.demo16.models.Department;
import com.codegym.demo16.models.Role;
import com.codegym.demo16.models.User;
import com.codegym.demo16.repositories.IDepartmentRepository;
import com.codegym.demo16.repositories.IUserRepository;
import com.codegym.demo16.untils.FileManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.codegym.demo16.repositories.IRoleRepository;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {
    private static final String UPLOAD_DIR = "/Users/mac/Documents/demo16/uploads";
    private final IUserRepository userRepository;
    private final IDepartmentRepository departmentRepository;
    private final FileManager fileManager;
    private final IRoleRepository roleRepository;

    public UserService(IUserRepository userRepository, IDepartmentRepository departmentRepository, FileManager fileManager, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.fileManager = fileManager;
        this.roleRepository = roleRepository;
    }

    public ListDepartmentResponse getAllUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        Page<User> data = userRepository.findAll(pageable);
        System.out.println("Total page: " + data.getTotalPages());
        List<User> users = data.getContent();

        // map data Entity to DTO
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId().intValue());
            userDTO.setUsername(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());
            userDTO.setImageUrl(user.getImageUrl());

            String nameDepartment = user.getDepartment() != null ? user.getDepartment().getName() : "No Department";
            userDTO.setDepartmentName(nameDepartment);


            String roleName = user.getRole() != null ? user.getRole().getName() : "No Role";
            userDTO.setRoleName(roleName);


            userDTOs.add(userDTO);
        }

        ListDepartmentResponse listUserResponse = new ListDepartmentResponse();
        listUserResponse.setTotalPage(data.getTotalPages());
        listUserResponse.setCurrentPage(data.getNumber());
        listUserResponse.setUsers(userDTOs);

        return listUserResponse;
    }

    public void deleteById(int id){
        // Logic to delete a user by ID
        Optional<User> user = userRepository.findById((long)(id));
        if (user.isPresent()) {
            User currentUser = user.get();
            // delete image
            fileManager.deleteFile(UPLOAD_DIR + "/" + currentUser.getImageUrl());
            userRepository.delete(currentUser);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public void storeUser(CreateUserDTO createUserDTO) throws IOException {
        // Lấy dữ liệu từ DTO
        String username = createUserDTO.getUsername();
        String password = createUserDTO.getPassword();
        String email = createUserDTO.getEmail();
        String phone = createUserDTO.getPhone();
        Long departmentId = createUserDTO.getDepartmentId();
        Long roleId = createUserDTO.getRoleId();   // ✅ Lấy roleId từ DTO
        MultipartFile file = createUserDTO.getImage();

        // Tạo đối tượng User mới
        User newUser = new User();
        newUser.setName(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setPhone(phone);

        // Upload ảnh
        if (file != null && !file.isEmpty()) {
            String fileName = fileManager.uploadFile(UPLOAD_DIR, file);
            newUser.setImageUrl(fileName);
        }

        // Gán Department
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId).orElse(null);
            if (department != null) {
                newUser.setDepartment(department);
            }
        }

        // Gán Role (bắt buộc)
        if (roleId != null) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            newUser.setRole(role);

        } else {
            throw new RuntimeException("Role is required");
        }

        // Lưu vào DB
        userRepository.save(newUser);
    }


    public UserDTO getUserById(int id) {
        Optional<User> user = userRepository.findById((long) id);
        if (user.isPresent()) {
            User currentUser = user.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setId( currentUser.getId().intValue());
            userDTO.setUsername(currentUser.getName());
            userDTO.setEmail(currentUser.getEmail());
            userDTO.setPhone(currentUser.getPhone());
            userDTO.setImageUrl(currentUser.getImageUrl());
            userDTO.setDepartmentId(currentUser.getDepartment() != null ? currentUser.getDepartment().getId() : null);
            userDTO.setRoleId(String.valueOf(currentUser.getRole() != null ? currentUser.getRole().getId() : null));
            return userDTO;
        }
        return null;
    }

    //
    public void updateUser(int id, EditUserDTO editUserDTO) throws IOException {
        Optional<User> user = userRepository.findById((long) id);
        if (user.isPresent()) {
            // Update user details
            User currentUser = user.get();
            currentUser.setName(editUserDTO.getUsername());
            currentUser.setEmail(editUserDTO.getEmail());
            currentUser.setPhone(editUserDTO.getPhone());

            Long departmentId = editUserDTO.getDepartmentId();
            if (departmentId != null) {
                Department department = departmentRepository.findById(departmentId).orElse(null);
                if (department != null) {
                    currentUser.setDepartment(department);
                }
            }

            MultipartFile file = editUserDTO.getImage();
            if (!file.isEmpty()) {
                // 1. xoa anh cu
                fileManager.deleteFile(UPLOAD_DIR + "/" + currentUser.getImageUrl());
                String fileName = fileManager.uploadFile(UPLOAD_DIR, file);
                currentUser.setImageUrl(fileName); // Set the image URL
            }

            // Cập nhật Role (bắt buộc)
            Long roleID = editUserDTO.getRoleId();
            if (roleID != null) {
                Role role = roleRepository.findById(roleID).orElse(null);
                if (role != null) {
                    currentUser.setRole(role);
                }
            }

            userRepository.save(currentUser);
        }
    }
}
