package com.codegym.demo16.controllers;


import com.codegym.demo16.models.Product;
import com.codegym.demo16.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showProducts(Model model){
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products); // dùng biến products đã lấy
        return "products/list"; // file Thymeleaf: src/main/resources/templates/products/list.html
    }
}
