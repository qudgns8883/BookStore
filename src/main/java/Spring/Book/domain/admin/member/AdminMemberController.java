package Spring.Book.domain.admin.member;

import Spring.Book.domain.user.dto.UserDto;
import Spring.Book.domain.user.dto.UserStatusCount;
import Spring.Book.domain.user.entity.Status;
import Spring.Book.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping("/member")
    public String memberList(Model model){
        UserStatusCount statusCount = adminMemberService.countUserStatus();

        model.addAttribute("activeMembers", statusCount.getActiveCount());
        model.addAttribute("inactiveMembers", statusCount.getInactiveCount());

        List<UserDto> userList = adminMemberService.findall();

        model.addAttribute("totalMembers", userList.size());
        model.addAttribute("userList", userList);

        return "admin/member";
    }

    @GetMapping("/searchMembers")
    @ResponseBody
    public List<UserDto> searchMembers(@RequestParam("status") String status,
                                       @RequestParam("searchBy") String searchBy,
                                       @RequestParam("query") String query) {
        return adminMemberService.searchMembers(status, searchBy, query);
    }
}
