package yehor.ua.products.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yehor.ua.products.dto.RecordsDto;
import yehor.ua.products.models.ProductEntity;
import yehor.ua.products.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> foundProducts = productService.getAllProducts();

        return ResponseEntity.ok(foundProducts);
    }

    @PostMapping("/add")
    public ResponseEntity<List<ProductEntity>> saveAllProducts(@RequestBody RecordsDto records) {
        List<ProductEntity> savedProducts = productService.saveAllProducts(records);

        return new ResponseEntity<>(savedProducts, HttpStatus.CREATED);
    }
}
