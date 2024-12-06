package Spring.Book.domain.admin.product.controller;

import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.admin.product.dto.ProductStatusCount;
import Spring.Book.domain.admin.product.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @PostMapping("/register")
    public String registerProduct(@ModelAttribute ProductDto productDto,
                                  @RequestParam("productImageUrl") MultipartFile file) {

        String imageUrl = adminProductService.storeFile(file);
        adminProductService.register(productDto, imageUrl);
        return "redirect:/admin/product";
    }

    @GetMapping("/BtnStatus")
    @ResponseBody
    public ResponseEntity<List<ProductDto>> getProductsByStatusAndCategory(
            @RequestParam String status,
            @RequestParam String category,
            @RequestParam(required = false, defaultValue = "") String query) {
        List<ProductDto> products = adminProductService.getProductsByStatusAndCategory(status, category, query);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/productReg")
    public String productReg(@ModelAttribute("productDto") ProductDto productDto){

        return "/admin/productReg";
    }

    @GetMapping("/product")
    public String product(Model model) {
        List<ProductDto> productList = adminProductService.getAllProducts();

        int totalProduct = productList.size();
        ProductStatusCount statusCount = adminProductService.countProductStatus(); // 상태별 개수 가져오기

        model.addAttribute("totalProduct", totalProduct);
        model.addAttribute("activeProduct", statusCount.getOnSaleCount());
        model.addAttribute("inactiveProduct", statusCount.getStopSellingCount());
        model.addAttribute("outOfStockProduct", statusCount.getOutOfStockCount());
        model.addAttribute("productList", productList);

        return "/admin/product";
    }

    @PostMapping("/imageUpload")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("upload") MultipartFile file) {
        try {
            String imageUrl = adminProductService.storeFile(file);
            String url = "/uploads/" + imageUrl;

            Map<String, Object> response = new HashMap<>();
            response.put("uploaded", true);
            response.put("url", url); // 반환할 URL

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}



