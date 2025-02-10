package Spring.Book.domain.product.controller;

import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.product.service.ProductService;
import Spring.Book.domain.review.dto.ReviewDto;
import Spring.Book.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        ProductDto product = productService.findById(id);
        List<ReviewDto> reviews = reviewService.findReviewsByProductId(id);

        int reviewCount = reviews.size();
        double averageRating = reviews.stream()
                .mapToDouble(ReviewDto::getRating)
                .average()
                .orElse(0.0);

        averageRating = Math.round(averageRating * 10.0) / 10.0;

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("averageRating", averageRating);

        return "product/productDetail";
    }

}
