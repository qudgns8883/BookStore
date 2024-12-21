package Spring.Book.domain.myPage.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageController {

    @GetMapping
    public String myPage(){
        return "myPage/myPage";
    }

}
