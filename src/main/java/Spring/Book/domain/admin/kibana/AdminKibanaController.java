package Spring.Book.domain.admin.kibana;

import Spring.Book.domain.admin.review.service.AdminReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminKibanaController {

    private final AdminReviewService adminReviewService;

    @GetMapping("/log")
    public String LogView() {

        return "/admin/logPage";
    }

    @GetMapping("/dashboard")
    public String Dashboard() {

        return "/admin/dashboard";
    }
}
