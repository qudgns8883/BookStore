package Spring.Book.domain.admin.product.service;

import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.admin.product.dto.ProductResponseDto;
import Spring.Book.domain.admin.product.dto.ProductStatusCount;
import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.admin.product.entity.ProductStatus;
import Spring.Book.domain.admin.product.entity.QProductEntity;
import Spring.Book.domain.admin.product.repository.AdminProductRepository;
import Spring.Book.domain.user.dto.CustomUserDetails;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.repository.UserRepository;
import Spring.Book.domain.user.service.UserService;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    private final JPAQueryFactory queryFactory;
    private final QProductEntity product = QProductEntity.productEntity;

    public AdminProductService(@Value("${file.upload-dir}") String uploadDir, UserService userService, AdminProductRepository productRepository, JPAQueryFactory queryFactory) {
        this.fileLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.userService = userService;
        this.productRepository = productRepository;
        this.queryFactory = queryFactory;
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
    public List<ProductResponseDto> searchProducts(String status, String category, String query) {
        BooleanExpression predicate = Expressions.asBoolean(true).isTrue()
                .and(eqStatus(status))
                .and(eqCategory(category))
                .and(containsQuery(query));

        return queryFactory
                .select(Projections.constructor(ProductResponseDto.class,
                        product.id,
                        product.productName,
                        product.category,
                        product.price,
                        product.stock,
                        product.status,
                        product.createDate
                ))
                .from(product)
                .where(predicate)
                .fetch();
    }

    private BooleanExpression eqStatus(String status) {
        return (StringUtils.hasText(status) && !"전체상품".equals(status))
                ? product.status.eq(ProductStatus.valueOf(status))
                : null;
    }

    private BooleanExpression eqCategory(String category) {
        return (StringUtils.hasText(category) && !"전체카테고리".equals(category))
                ? product.category.eq(category)
                : null;
    }

    private BooleanExpression containsQuery(String query) {
        return StringUtils.hasText(query)
                ? product.productName.containsIgnoreCase(query)
                : null;
    }



//    public List<ProductDto> getProductsByStatusAndCategory(String status, String category, String query) {
//        List<ProductEntity> productList = productRepository.findAll();
//
//        if (!"전체상품".equals(status)) {
//            productList = productList.stream()
//                    .filter(product -> product.getStatus().name().equals(status))
//                    .collect(Collectors.toList());
//        }
//
//        if (!"전체카테고리".equals(category)) {
//            productList = productList.stream()
//                    .filter(product -> product.getCategory().equals(category))
//                    .collect(Collectors.toList());
//        }
//
//        if (!query.isEmpty()) {
//            productList = productList.stream()
//                    .filter(product -> product.getProductName().toLowerCase().contains(query.toLowerCase()))
//                    .collect(Collectors.toList());
//        }
//
//        return productList.stream()
//                .map(product -> ProductDto.builder()
//                        .id(product.getId())
//                        .productName(product.getProductName())
//                        .author(product.getAuthor())
//                        .description(product.getDescription())
//                        .category(product.getCategory())
//                        .price(product.getPrice())
//                        .stock(product.getStock())
//                        .productStatus(product.getStatus())
//                        .createDate(product.getCreateDate())
//                        .build())
//                .collect(Collectors.toList());
//    }

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
