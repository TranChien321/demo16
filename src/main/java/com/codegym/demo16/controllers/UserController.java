package com.codegym.demo16.controllers;

import com.codegym.demo16.dto.CreateUserDTO;
import com.codegym.demo16.dto.DepartmentDTO;
import com.codegym.demo16.dto.EditUserDTO;
import com.codegym.demo16.dto.UserDTO;
import com.codegym.demo16.dto.RoleDTO;
import com.codegym.demo16.dto.response.ListDepartmentResponse;
import com.codegym.demo16.dto.response.ListUserResponse;
import com.codegym.demo16.services.DepartmentService;
import com.codegym.demo16.services.RoleService;
import com.codegym.demo16.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;
    private final DepartmentService departmentService;
    private final RoleService roleService;
    private final HttpSession httpSession;


    public UserController(UserService userService, DepartmentService departmentService, RoleService roleService, HttpSession httpSession) {
        this.userService = userService;
        this.departmentService = departmentService;
        this.roleService = roleService;
        this.httpSession = httpSession;

    }


    @GetMapping
    public String listUsers(@CookieValue(value = "counter", defaultValue = "1") String counter,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                            @RequestParam(value = "departmentId", required = false) Long departmentId,
                            Model model,
                            HttpServletResponse response) {
        if (page < 1) {
            page = 1;
        } else {
            page -= 1;
        }

        Cookie myCookie = new Cookie("msg", "Hello");
        int total = Integer.parseInt(counter) + 1;
        Cookie counterViewPage = new Cookie("counter", total + "");
        myCookie.setMaxAge(60);
        counterViewPage.setMaxAge(60);
        response.addCookie(myCookie);
        response.addCookie(counterViewPage);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        httpSession.setAttribute("email", email);

        List<UserDTO> users;
        int totalPages;

        if (departmentId != null) {
            users = userService.filterUsersByDepartment(departmentId, page, size);
            totalPages = 1; // nếu lọc thì không cần nhiều trang, hoặc bạn tự tính lại
        } else {
            ListUserResponse listUserResponse = userService.getAllUsers(page, size);
            users = listUserResponse.getUsers();
            totalPages = listUserResponse.getTotalPage();
        }

        model.addAttribute("users", users);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalViewPage", counter);

        // Gửi list department xuống để vẽ dropdown lọc
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("selectedDepartmentId", departmentId);

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
        System.out.println(">>> Delete request for ID: " + id);
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            System.out.println(">>> User not found!");
            return "errors/404";
        }
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

//

    @PostMapping("/create")
    public String storeUser(@Validated @ModelAttribute("user") CreateUserDTO createUserDTO,
                            BindingResult result,
                            Model model) throws IOException {
        if (result.hasErrors()) {
            List<DepartmentDTO> departments = departmentService.getAllDepartments();
            List<RoleDTO> roles = roleService.getAllRoles();
            model.addAttribute("departments", departments);
            model.addAttribute("roles", roles);
            // Ensure the form reloads with the submitted data and errors
            model.addAttribute("user", createUserDTO);
            return "users/create";
        }

        // Nếu không có lỗi thì lưu user
        userService.storeUser(createUserDTO);
        return "redirect:/admin/users";
    }


    @GetMapping("/{id}/edit")
    public String showFormEdit(@PathVariable("id") int id, Model model) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return "errors/404";
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
            return "errors/404"; // Redirect if user not found
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
        return "redirect:/admin/users";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam(value = "query", required = false) String query,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              Model model) {

        if (query != null && !query.trim().isEmpty()) {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserDTO> usersPage = userService.searchUsers(query.trim(), pageable);
            model.addAttribute("users", usersPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", usersPage.getTotalPages());
        } else {
           ListUserResponse listResp = userService.getAllUsers(page, size);
            model.addAttribute("users", listResp.getUsers());
            model.addAttribute("currentPage", listResp.getCurrentPage());
            model.addAttribute("totalPages", listResp.getTotalPage());
        }

        model.addAttribute("query", query);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("roles", roleService.getAllRoles());

        return "users/list";
    }
}