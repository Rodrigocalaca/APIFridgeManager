package com.rodrigodev.FridgeManager.Services;

import java.util.List;

import com.rodrigodev.FridgeManager.Models.Product;
import com.rodrigodev.FridgeManager.Models.StorageType;
import com.rodrigodev.FridgeManager.Repository.ProductRepository;
import com.rodrigodev.FridgeManager.Services.Interfaces.ProductService;

public abstract class BaseProductService implements ProductService {
    protected final ProductRepository productRepository;
    protected final StorageType storageType;

    protected BaseProductService(ProductRepository productRepository, StorageType storageType) {
        this.productRepository = productRepository;
        this.storageType = storageType;
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> listItems() {
        return productRepository.findByStorageType(storageType);
    }

    @Override
    public void addProduct(Product product) {
        product.setStorageType(storageType);
        productRepository.save(product);
    }

    @Override
    public void consumeOneUnity(Long id) {
    Product product = productRepository.findById(id).orElse(null);
        if (product != null && product.getStorageType() == storageType) {
            int updatedQty = product.getQuantity() - 1;

            if (updatedQty <= 0) {
                productRepository.deleteById(id);
            } else {
                product.setQuantity(updatedQty);
                productRepository.save(product);
            }
        }
    }

    @Override
    public void removeProduct(Long id) {
        productRepository.deleteById(id);
    }

}
