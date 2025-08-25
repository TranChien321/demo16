package com.codegym.demo16.controllers;

import com.codegym.demo16.dto.DepartmentDTO;
import com.codegym.demo16.services.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listDepartments(Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "departments/list"; // JSP
    }

    @GetMapping("/{id}/delete")
    public String deleteDepartment(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteDepartment(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phòng ban thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xoá phòng ban!");
        }
        return "redirect:/departments"; // quay lại danh sách
    }

    @GetMapping("/{id}/edit")
    public String showFormEdit(@PathVariable("id") Long id, Model model) {
        DepartmentDTO department = departmentService.getAllDepartments().stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Department not found"));
        model.addAttribute("department", department);
        return "departments/edit"; // JSP
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(
            @PathVariable("id") Long id,
            @Validated @ModelAttribute("department") DepartmentDTO departmentDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("department", departmentDTO);
            return "departments/edit"; // quay lại form edit nếu có lỗi
        }

        try {
            departmentDTO.setId(id);
            departmentService.editDepartment(departmentDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật phòng ban thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể cập nhật phòng ban!");
        }
        return "redirect:/departments";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new DepartmentDTO());
        return "departments/create"; // gọi tới JSP
    }

    @PostMapping("/create")
    public String createDepartment(
            @Validated @ModelAttribute("department") DepartmentDTO departmentDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("department", departmentDTO);
            return "departments/create"; // quay lại form nếu lỗi
        }
        try {
            departmentService.createDepartment(departmentDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm phòng ban thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể thêm phòng ban!");
        }
        return "redirect:/departments";
    }

}
