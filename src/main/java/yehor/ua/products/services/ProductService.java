package yehor.ua.products.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yehor.ua.products.dto.ProductDto;
import yehor.ua.products.dto.RecordsDto;
import yehor.ua.products.models.ProductEntity;
import yehor.ua.products.repositories.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductEntity> saveAllProducts(RecordsDto records) {
        List<ProductEntity> products = convertListOfDtosToListOfEntities(records.records());

        return productRepository.saveAll(products);
    }

    private static List<ProductEntity> convertListOfDtosToListOfEntities(List<ProductDto> productDtos) {
        return productDtos.stream()
                .map(ProductService::createProductEntityFromDto)
                .toList();
    }

    private static ProductEntity createProductEntityFromDto(ProductDto productDto) {
        return new ProductEntity(
                productDto.entryDate(),
                productDto.itemCode(),
                productDto.itemName(),
                productDto.itemQuantity(),
                productDto.status());
    }
}
