package com.codegym.demo16.controllers;

import com.codegym.demo16.dto.CreateUserDTO;
import com.codegym.demo16.dto.DepartmentDTO;
import com.codegym.demo16.dto.EditUserDTO;
import com.codegym.demo16.dto.UserDTO;
import com.codegym.demo16.dto.RoleDTO;
import com.codegym.demo16.dto.response.ListDepartmentResponse;
import com.codegym.demo16.services.DepartmentService;
import com.codegym.demo16.services.RoleService;
import com.codegym.demo16.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final DepartmentService departmentService;
    private final RoleService roleService;

    public UserController(UserService userService, DepartmentService departmentService, RoleService roleService) {
        this.userService = userService;
        this.departmentService = departmentService;
        this.roleService = roleService;
    }


    @GetMapping
    public String listUsers(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                            Model model) {
        if (page < 1) {
            page = 1; // Ensure page number is at least 1
        } else {
            page -= 1; // Convert to zero-based index for pagination
        }
        ListDepartmentResponse listUserResponse = userService.getAllUsers(page, size);
        List<UserDTO> users = listUserResponse.getUsers();
        // Logic to list users
        model.addAttribute("users", users);
        model.addAttribute("totalPages", listUserResponse.getTotalPage());
        return "users/list";
    }

    @GetMapping("/create")
    public String createUser(Model model) {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        List<RoleDTO> roles = roleService.getAllRoles(); // ✅ sửa thành RoleDTO

        model.addAttribute("departments", departments);
        model.addAttribute("roles", roles); // ✅ dùng "roles" giống edit
        model.addAttribute("user", createUserDTO);

        return "users/create";
    }


    @GetMapping("/{id}/detail")
    public String userDetail(@PathVariable("id") String id,
                             Model model) {
        model.addAttribute("id", id);
        return "users/detail"; // This will resolve to /WEB-INF/views/users/detail.html
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") int id) {
        // Logic to delete a user by ID
        userService.deleteById(id);
        // For now, just redirect to the list of users
        return "redirect:/users";
    }
//

    //
    @PostMapping("/create")
    public String storeUser(@Validated @ModelAttribute("user") CreateUserDTO
                                    createUserDTO, BindingResult result, Model model ) throws IOException {
        if (result.hasErrors()){
            List<DepartmentDTO> departments = departmentService.getAllDepartments();
            model.addAttribute("departments", departments);
            return "users/create";
        }
        // Logic to store a new user
        userService.storeUser(createUserDTO);
        return "redirect:/users";
    }
    //

    @GetMapping("/{id}/edit")
    public String showFormEdit(@PathVariable("id") int id, Model model) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/users";
        }

        EditUserDTO editUserDTO = new EditUserDTO(
                Math.toIntExact(user.getId()),
                user.getUsername(),
                user.getEmail(),
                user.getPhone()
        );
        editUserDTO.setDepartmentId(user.getDepartmentId());
        editUserDTO.setRoleId(user.getRoleId()); // 🔥 Sửa ở đây

        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        List<RoleDTO> roles = roleService.getAllRoles();

        model.addAttribute("user", editUserDTO);
        model.addAttribute("departments", departments);
        model.addAttribute("roles", roles);

        return "users/edit";
    }

    //
    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable("id") int id,
                             @Validated @ModelAttribute("user") EditUserDTO editUserDTO,
                             BindingResult result,
                             Model model) throws IOException {
        UserDTO existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return "redirect:/users"; // Redirect if user not found
        }

        if (result.hasErrors()) {
            // Nếu có lỗi, nạp lại các dữ liệu cần thiết cho form
            List<DepartmentDTO> departments = departmentService.getAllDepartments();
            List<RoleDTO> roles = roleService.getAllRoles();
            model.addAttribute("departments", departments);
            model.addAttribute("roles", roles);
            return "users/edit"; // Trả lại view edit với các lỗi validation
        }

        userService.updateUser(id, editUserDTO);
        return "redirect:/users";
    }

}