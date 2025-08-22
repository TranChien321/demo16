package com.codegym.demo16.controllers;

import com.codegym.demo16.dto.CreateUserDTO;
import com.codegym.demo16.dto.DepartmentDTO;
import com.codegym.demo16.dto.EditUserDTO;
import com.codegym.demo16.dto.UserDTO;
import com.codegym.demo16.dto.response.ListUserResponse;
import com.codegym.demo16.services.DepartmentService;
import com.codegym.demo16.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final DepartmentService departmentService;

    public UserController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }
    // This controller can handle user-related requests
    // Add methods to handle user operations like listing, creating, updating, and deleting users

    @GetMapping
    public String listUsers(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                            Model model) {
        if (page < 1) {
            page = 1; // Ensure page number is at least 1
        } else {
            page -= 1; // Convert to zero-based index for pagination
        }
        ListUserResponse listUserResponse = userService.getAllUsers(page, size);
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
        model.addAttribute("departments", departments);
        model.addAttribute("user", createUserDTO);
        // Logic to create a new user
        return "users/create"; // This will resolve to /WEB-INF/views/users/create.html
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
    @PostMapping("/store")
    public String storeUser(@ModelAttribute("user") CreateUserDTO
                                    createUserDTO) throws IOException {
        // Logic to store a new user
        userService.storeUser(createUserDTO);
        return "redirect:/users";
    }
    //
    @GetMapping("/{id}/edit")
    public String showFormEdit(@PathVariable("id") int id, Model model) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/users"; // Redirect if user not found
        }

        // Prepare the EditUserDTO with the user's current details
        EditUserDTO editUserDTO = new EditUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone()
        );
        editUserDTO.setDepartmentId(user.getDepartmentId());

        List<DepartmentDTO> departments = departmentService.getAllDepartments();

        // Add the EditUserDTO to the model
        model.addAttribute("user", editUserDTO);
        model.addAttribute("departments", departments);

        return "users/edit"; // This will resolve to /WEB-INF/views/users/edit.html
    }
    //
    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable("id") int id,
                             @ModelAttribute("user") EditUserDTO editUserDTO) throws IOException {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/users"; // Redirect if user not found
        }
        userService.updateUser(id, editUserDTO);
        return "redirect:/users";
    }
}