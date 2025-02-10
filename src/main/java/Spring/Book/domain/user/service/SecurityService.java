package Spring.Book.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SecurityService {

    private final CustomUserDetailsService customUserDetailsService;

    public void updateSecurityContext(String newUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails updatedUserDetails = customUserDetailsService.loadUserByUsername(newUsername);

        UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(
                        updatedUserDetails,
                        authentication.getCredentials(),
                        updatedUserDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
