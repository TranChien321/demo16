package com.codegym.demo16.services;

import com.codegym.demo16.models.Product;
import com.codegym.demo16.repositories.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final IProductRepository productRepository;

    // Constructor injection
    public ProductService( IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
