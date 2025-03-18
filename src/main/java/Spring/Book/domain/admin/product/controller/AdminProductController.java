package Spring.Book.domain.admin.product.controller;

import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.admin.product.dto.ProductResponseDto;
import Spring.Book.domain.admin.product.dto.ProductStatusCount;
import Spring.Book.domain.admin.product.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String registerProduct(@ModelAttribute ProductDto productDto, BindingResult result,
                                  @RequestParam("productImageUrl") MultipartFile file) {
        if (result.hasErrors()) {
            return "user/signup";
        }

        String imageUrl = adminProductService.storeFile(file);
        adminProductService.register(productDto, imageUrl);
        return "redirect:/admin/product";
    }

    @GetMapping("/BtnStatus")
    @ResponseBody
    public ResponseEntity<List<ProductResponseDto>> getProductsByStatusAndCategory(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "") String query) {
        List<ProductResponseDto> products = adminProductService.searchProducts(status, category, query);
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
        ProductStatusCount statusCount = adminProductService.countProductStatus();

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

            String imageUrl = adminProductService.storeFile(file);
            String url = "/uploads/" + imageUrl;

            Map<String, Object> response = new HashMap<>();
            response.put("uploaded", true);
            response.put("url", url);

            return ResponseEntity.ok(response);
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        ProductDto productDto = adminProductService.getProductById(id);
        model.addAttribute("product", productDto);
        return "/admin/productEdit";
    }

    @PostMapping("/product/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute ProductDto productDto,
                                BindingResult result,
                                @RequestParam(value = "productImageUrl", required = false) MultipartFile file,
                                @RequestParam("existingImageUrl") String existingImageUrl) {
        if (result.hasErrors()) {
            return "/admin/productEdit";
        }

        String imageUrl = existingImageUrl;

        if (file != null && !file.isEmpty()) {
            imageUrl = adminProductService.storeFile(file);
        }

        adminProductService.updateProduct(id, productDto, imageUrl);
        return "redirect:/admin/product";
    }
}



