package Spring.Book.domain.cart.service;

import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.cart.dto.CartDto;
import Spring.Book.domain.cart.entity.CartEntity;
import Spring.Book.domain.cart.repository.CartRepository;
import Spring.Book.domain.product.repository.ProductRepository;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.repository.UserRepository;
import Spring.Book.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    public void addToCart(CartDto cartDTO) {
        UserEntity user = userService.getCurrentUser();

        ProductEntity product = productRepository.findById(cartDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        CartEntity existingCart = cartRepository.findByUserAndProduct(user, product);

        if (existingCart != null) {
            int newQuantity = existingCart.getQuantity() + cartDTO.getQuantity();
            existingCart.updateQuantity(newQuantity);
            cartRepository.save(existingCart);
        } else {
            CartEntity cart = CartEntity.builder()
                    .user(user)
                    .product(product)
                    .quantity(cartDTO.getQuantity())
                    .build();

            user.addCart(cart);
            cartRepository.save(cart);
        }
    }

    public int getCartItemCount() {
        UserEntity user = userService.getCurrentUser();

        if (user == null) {
            return 0;
        }

        List<CartEntity> items = cartRepository.findByUserId(user.getId());
        return (int) items.stream()
                .map(CartEntity::getProduct)
                .distinct()
                .count();
    }

    /**
     * 현재 사용자의 장바구니 항목 가져오기
     */
    public List<CartDto> getCartItems() {
        UserEntity user = userService.getCurrentUser();
        if (user == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        List<CartEntity> cartItems = cartRepository.findByUserIdWithProducts(user.getId());

        return cartItems.stream()
                .map(cart -> {
                    ProductDto productDto = ProductDto.builder()
                            .id(cart.getProduct().getId())
                            .productName(cart.getProduct().getProductName())
                            .author(cart.getProduct().getAuthor())
                            .description(cart.getProduct().getDescription())
                            .category(cart.getProduct().getCategory())
                            .price(cart.getProduct().getPrice())
                            .stock(cart.getProduct().getStock())
                            .productImage(cart.getProduct().getProductImage())
                            .productDetails(cart.getProduct().getProductDetails())
                            .productStatus(cart.getProduct().getStatus())
                            .createDate(cart.getProduct().getCreateDate().toString())
                            .build();

                    return CartDto.builder()
                            .productId(cart.getProduct().getId())
                            .product(productDto)
                            .quantity(cart.getQuantity())
                            .build();
                })
                .toList();
    }

    public boolean deleteCartItem(Long productId) {

        UserEntity user = userService.getCurrentUser();
        if (user == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Optional<CartEntity> cartItemOptional = cartRepository.findByUserIdAndProductId(user.getId(), productId);

        if (cartItemOptional.isPresent()) {
            cartRepository.delete(cartItemOptional.get());
            return true;
        }
        return false;
    }
}
