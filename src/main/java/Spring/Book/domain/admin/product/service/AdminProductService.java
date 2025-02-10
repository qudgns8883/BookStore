package Spring.Book.domain.admin.product.service;

import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.admin.product.dto.ProductStatusCount;
import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.admin.product.entity.ProductStatus;
import Spring.Book.domain.admin.product.repository.AdminProductRepository;
import Spring.Book.domain.user.dto.CustomUserDetails;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.repository.UserRepository;
import Spring.Book.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static Spring.Book.domain.admin.product.dto.ProductDto.fromProductEntity;

@Service
public class AdminProductService {

    private final Path fileLocation;
    private final AdminProductRepository productRepository;
    private final UserService userService;

    public AdminProductService(@Value("${file.upload-dir}") String uploadDir, UserService userService, AdminProductRepository productRepository) {
        this.fileLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.userService = userService;
        this.productRepository = productRepository;
        try {
            Files.createDirectories(this.fileLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + e.getMessage());
        }
    }

    @Transactional
    public void register(ProductDto productDto, String imageUrl) {

        UserEntity user = userService.getCurrentUser();

        ProductEntity product = ProductEntity.builder()
                .productName(productDto.getProductName())
                .author(productDto.getAuthor())
                .description(productDto.getDescription())
                .category(productDto.getCategory())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .productImage(imageUrl)
                .productDetails(productDto.getProductDetails())
                .status(ProductStatus.판매중)
                .user(user)
                .build();

        productRepository.save(product);

        user.addProduct(product);
    }

    public List<ProductDto> getProductsByStatusAndCategory(String status, String category, String query) {
        List<ProductEntity> productList = productRepository.findAll();

        if (!"전체상품".equals(status)) {
            productList = productList.stream()
                    .filter(product -> product.getStatus().name().equals(status))
                    .collect(Collectors.toList());
        }

        if (!"전체카테고리".equals(category)) {
            productList = productList.stream()
                    .filter(product -> product.getCategory().equals(category))
                    .collect(Collectors.toList());
        }

        if (!query.isEmpty()) {
            productList = productList.stream()
                    .filter(product -> product.getProductName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return productList.stream()
                .map(product -> ProductDto.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .author(product.getAuthor())
                        .description(product.getDescription())
                        .category(product.getCategory())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .productStatus(product.getStatus())
                        .createDate(product.getCreateDateAsString())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ProductDto> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();

        return productEntities.stream()
                .map(ProductDto::fromProductEntity)
                .collect(Collectors.toList());
    }

    public ProductStatusCount countProductStatus() {

        return productRepository.countProductStatus();
    }

    public String storeFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid file name: " + fileName);
            }

            Path targetLocation = fileLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file: " + e.getMessage());
        }
    }

    public ProductDto getProductById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        return fromProductEntity(product);
    }

    public void updateProduct(Long id, ProductDto productDto, String imageUrl) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        UserEntity user = userService.getCurrentUser();

        ProductEntity updatedProduct = ProductEntity.builder()
                .id(product.getId())
                .productName(productDto.getProductName())
                .author(productDto.getAuthor())
                .category(productDto.getCategory())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .status(productDto.getProductStatus())
                .description(productDto.getDescription())
                .productDetails(productDto.getProductDetails())
                .productImage(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : product.getProductImage())
                .user(user)
                .build();

        productRepository.save(updatedProduct);
    }
}
