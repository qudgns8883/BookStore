package Spring.Book.domain.admin.member;

import Spring.Book.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping("member")
    public String memberList(Model model){

        List<UserDto> userList = adminMemberService.findall();

        model.addAttribute("userList", userList);

        return "admin/member";
    }
}
