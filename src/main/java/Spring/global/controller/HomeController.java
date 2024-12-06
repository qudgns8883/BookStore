package Spring.global.controller;

import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.admin.product.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AdminProductService adminProductService;

    @GetMapping("/home")
    public String mainPage(Model model){

        List<ProductDto> productList = adminProductService.getAllProducts();

        model.addAttribute("productList", productList);

        return "home";
    }
}
