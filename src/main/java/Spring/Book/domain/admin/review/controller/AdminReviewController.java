package Spring.Book.domain.admin.review.controller;

import Spring.Book.domain.admin.review.dto.ReviewStatusCount;
import Spring.Book.domain.admin.review.service.AdminReviewService;
import Spring.Book.domain.review.dto.ReviewDto;
import Spring.Book.domain.user.dto.UserStatusCount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    @GetMapping("/review")
    public String reviewList(Model model){

        ReviewStatusCount statusCount = adminReviewService.countReviewStatus();

        model.addAttribute("AnsweredCount", statusCount.getAnsweredCount());
        model.addAttribute("UnansweredCount", statusCount.getUnansweredCount());

        List<ReviewDto> reviewList = adminReviewService.findall();

        model.addAttribute("totalReview", reviewList.size());
        model.addAttribute("reviewList", reviewList);
    return "/admin/review";

    }

    @GetMapping("/review/BtnStatus")
    @ResponseBody
    public ResponseEntity<List<ReviewDto>> getFilteredReviews(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String date,
            @RequestParam(required = false, defaultValue = "") String query) {

        List<ReviewDto> reviews = adminReviewService.findReviewsByFilter(status, date, query);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/review/answer/{id}")
    public String addAnswer(@PathVariable Long id,
                            @RequestParam("answer") String answer) {
        adminReviewService.addAnswer(id, answer);

        return "redirect:/admin/review";
    }
}
