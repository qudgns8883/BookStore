package Spring.global.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.GrantedAuthority;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean userLoggedIn = authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String);

        String nickname = userLoggedIn ? authentication.getName() : null;

        String  role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);

        model.addAttribute("userLoggedIn", userLoggedIn);
        model.addAttribute("nickname", nickname);
        model.addAttribute("role", role);
    }
}
