package Spring.Book.domain.admin;

import Spring.Book.domain.admin.member.AdminMemberService;
import Spring.Book.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminMemberService adminMemberService;

    @GetMapping("/admin")
    public String admin(Model model){

        List<UserDto> userList = adminMemberService.findall();

        model.addAttribute("userList", userList);

        return "admin/member";
    }
}
