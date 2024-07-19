package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.entity.Product;

import java.util.List;

public interface ProductService {
    
    //Method to Search for products with certain criteria, returns a list of products
    List<Product> productsSearch(String name, String category, Double minPrice, Double maxPrice);
}