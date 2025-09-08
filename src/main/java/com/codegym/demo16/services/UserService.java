package com.codegym.demo16.services;

import com.codegym.demo16.dto.CreateUserDTO;
import com.codegym.demo16.dto.EditUserDTO;
import com.codegym.demo16.dto.UserDTO;
import com.codegym.demo16.dto.response.ListDepartmentResponse;
import com.codegym.demo16.dto.response.ListUserResponse;
import com.codegym.demo16.mappers.CreateUserMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.codegym.demo16.repositories.IRoleRepository;

import java.util.*;

@Service
public class UserService {
    private static final String UPLOAD_DIR = "/Users/mac/Documents/demo16/uploads";
    private final IUserRepository userRepository;
    private final IDepartmentRepository departmentRepository;
    private final FileManager fileManager;
    private final IRoleRepository roleRepository;
    private final CreateUserMapper createUserMapper;
    private final PasswordEncoder passwordEncoder;


    public UserService(IUserRepository userRepository, IDepartmentRepository departmentRepository, FileManager fileManager, IRoleRepository roleRepository, CreateUserMapper createUserMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.fileManager = fileManager;
        this.roleRepository = roleRepository;
        this.createUserMapper = createUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public ListUserResponse getAllUsers(int pageNumber, int pageSize) {
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

            List<String> nameRoles = new ArrayList<>();
            // get roleName
            for (Role role : user.getRoles()) {
                nameRoles.add(role.getName());
            }
            userDTO.setNameRoles(nameRoles);
            userDTOs.add(userDTO);
        }

        ListUserResponse listUserResponse = new ListUserResponse();
        listUserResponse.setTotalPage(data.getTotalPages());
        listUserResponse.setCurrentPage(data.getNumber());
        listUserResponse.setUsers(userDTOs);

        return listUserResponse;
    }

    //xoá user + file ảnh
    public void deleteUser(int id) {
        Optional<User> user = userRepository.findById((long) id);
        if (user.isPresent()) {
            User currentUser = user.get();
            // Xoá file ảnh
            String imageUrl = currentUser.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                fileManager.deleteFile(UPLOAD_DIR + "/" + imageUrl);
            }
            // Xoá user khỏi DB
            userRepository.deleteById((long) id);
        }
    }

    @Transactional
    public void storeUser(CreateUserDTO dto) {

        // 1) Upload ảnh (nếu có)
        String imageUrl = null;
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            imageUrl = fileManager.uploadFile(UPLOAD_DIR, dto.getImage());
        }

        // 2) Tìm Department (nếu có)
        Department department = null;
        if (dto.getDepartmentId() != null) {
            department = departmentRepository.findById(dto.getDepartmentId()).orElse(null);
        }

        // 3. Roles
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));

        // 4) Mã hoá mật khẩu
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User newUser = createUserMapper.toEntity(dto, encodedPassword, department, roles, imageUrl);
        userRepository.save(newUser);
        // Lưu vào DB
        userRepository.save(newUser);

    }


    public UserDTO getUserById(int id) {
        Optional<User> user = userRepository.findById((long) id);
        if (user.isPresent()) {
            User currentUser = user.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(currentUser.getId().intValue());
            userDTO.setUsername(currentUser.getName());
            userDTO.setEmail(currentUser.getEmail());
            userDTO.setPhone(currentUser.getPhone());
            userDTO.setImageUrl(currentUser.getImageUrl());
            userDTO.setDepartmentId(currentUser.getDepartment() != null ? currentUser.getDepartment().getId() : null);

            // Lấy tất cả role Id
            List<Long> roleIds = currentUser.getRoles().stream()
                    .map(Role::getId)
                    .toList();
            userDTO.setRoleIds(roleIds);

            return userDTO;
        }
        return null;
    }


    public void updateUser(int id, EditUserDTO editUserDTO) {
        Optional<User> user = userRepository.findById((long) id);
        if (user.isPresent()) {
            User currentUser = user.get();
            currentUser.setName(editUserDTO.getUsername());
            currentUser.setEmail(editUserDTO.getEmail());
            currentUser.setPhone(editUserDTO.getPhone());

            if (editUserDTO.getDepartmentId() != null) {
                Department department = departmentRepository.findById(editUserDTO.getDepartmentId()).orElse(null);
                currentUser.setDepartment(department);
            }

            MultipartFile file = editUserDTO.getImage();
            if (file != null && !file.isEmpty()) {
                fileManager.deleteFile(UPLOAD_DIR + "/" + currentUser.getImageUrl());
                String fileName = fileManager.uploadFile(UPLOAD_DIR, file);
                currentUser.setImageUrl(fileName);
            }

            // Cập nhật nhiều Role
            if (editUserDTO.getRoleIds() != null && !editUserDTO.getRoleIds().isEmpty()) {
                Set<Role> roles = new HashSet<>(roleRepository.findAllById(editUserDTO.getRoleIds()));
                currentUser.setRoles(roles);
            } else {
                currentUser.setRoles(new HashSet<>()); // nếu không chọn role → rỗng
            }

            userRepository.save(currentUser);
        }
    }


    public Page<UserDTO> searchUsers(String query, Pageable pageable) {
        Page<User> users = userRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                        query, query, query, pageable);

        return users.map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId().intValue());
            userDTO.setUsername(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());
            userDTO.setImageUrl(user.getImageUrl());

            String nameDepartment = user.getDepartment() != null ? user.getDepartment().getName() : "No Department";
            userDTO.setDepartmentName(nameDepartment);

            // Lấy tất cả role name thành List<String>
            List<String> nameRoles = new ArrayList<>();
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                for (Role role : user.getRoles()) {
                    nameRoles.add(role.getName());
                }
            }
            userDTO.setNameRoles(nameRoles);

            return userDTO;
        });
    }


    public List<UserDTO> filterUsersByDepartment(Long departmentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findByDepartmentId(departmentId, pageable);

        return users.stream().map(user -> {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId().intValue());
            dto.setUsername(user.getName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setImageUrl(user.getImageUrl());
            dto.setDepartmentName(user.getDepartment() != null ? user.getDepartment().getName() : "No Department");
            return dto;
        }).toList();
    }

}