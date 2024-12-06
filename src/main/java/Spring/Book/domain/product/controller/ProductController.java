package Spring.Book.domain.product.controller;

import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        ProductDto product = productService.findById(id);
        model.addAttribute("product", product);

        return "product/productDetail";
    }

}
