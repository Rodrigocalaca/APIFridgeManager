package com.rodrigodev.FridgeManager.Controllers;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.rodrigodev.FridgeManager.Models.Product;
import com.rodrigodev.FridgeManager.Models.StorageType;
import com.rodrigodev.FridgeManager.Services.Interfaces.ProductService;

public abstract class BaseController {

    protected final ProductService service;
    protected final StorageType storageType;

    protected BaseController(ProductService service, StorageType storageType) {
        this.service = service;
        this.storageType = storageType;
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = service.getProduct(id);
        if (product == null || product.getStorageType() != storageType) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping("/items")
    public ResponseEntity<List<Product>> listItems() {
        return ResponseEntity.ok(service.listItems());
    }

    @PutMapping("/consume/{id}")
    public ResponseEntity<String> consumeOneUnity(@PathVariable Long id) {
        Product product = service.getProduct(id);

        if (product == null || product.getStorageType() != storageType) {
            return ResponseEntity.status(403).body("Invalid product for this endpoint.");
        }

        service.consumeOneUnity(id);
        return ResponseEntity.ok("One unit consumed.");
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        if (product.getStorageType() != storageType) {
            return ResponseEntity.badRequest().body("Invalid storage type for this endpoint.");
        }
        service.addProduct(product);
        return ResponseEntity.ok("Product added to " + storageType.name().toLowerCase() + ".");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product existing = service.getProduct(id);

        if (existing == null) {
            return ResponseEntity.status(404).body("Product not found.");
        }

        if (existing.getStorageType() != storageType || updatedProduct.getStorageType() != storageType) {
            return ResponseEntity.status(403).body("Invalid storage type for this endpoint.");
        }

        updatedProduct.setId(id); 
        service.addProduct(updatedProduct); 
        return ResponseEntity.ok("Product updated successfully.");
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable Long id) {
        Product product = service.getProduct(id);
        if (product == null || product.getStorageType() != storageType) {
            return ResponseEntity.badRequest().body("Product not found or not in " + storageType.name().toLowerCase() + ".");
        }
        service.removeProduct(id);
        return ResponseEntity.ok("Product removed from " + storageType.name().toLowerCase() + ".");
    }
}

