package yehor.ua.products.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import yehor.ua.products.services.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHENTICATION_HEADER_START = "Bearer ";

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        processToken(request);

        filterChain.doFilter(request, response);
    }

    private void processToken(HttpServletRequest request) {
        final String authenticationHeader = request.getHeader("Authorization");

        if (isHeaderInvalid(authenticationHeader)) {
            return;
        }

        String jwtToken = extractJwtToken(authenticationHeader);
        String username = extractUsernameFromToken(jwtToken);

        if (username != null && isAuthenticationContextEmpty()) {
            setCreatedTokenIfValid(request, jwtToken, username);
        }
    }

    private boolean isHeaderInvalid(String authenticationHeader) {
        return authenticationHeader == null || !authenticationHeader.startsWith(AUTHENTICATION_HEADER_START);
    }

    private String extractJwtToken(String authenticationHeader) {
        return authenticationHeader.substring(AUTHENTICATION_HEADER_START.length());
    }

    private String extractUsernameFromToken(String jwtToken) {
        return jwtService.extractUsername(jwtToken);
    }

    private boolean isAuthenticationContextEmpty() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void setCreatedTokenIfValid(HttpServletRequest request, String jwtToken, String login) {
        UserDetails userDetails = loadUserDetails(login);

        if (isTokenValid(jwtToken, userDetails)) {
            setCreatedToken(request, userDetails);
        }
    }

    private UserDetails loadUserDetails(String login) {
        return userDetailsService.loadUserByUsername(login);
    }

    private boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        return jwtService.isTokenValid(jwtToken, userDetails);
    }

    private void setCreatedToken(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationToken(userDetails, request);
        setAuthenticationToken(authenticationToken);
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(UserDetails userDetails,
                                                                          HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    private void setAuthenticationToken(UsernamePasswordAuthenticationToken authenticationToken) {
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}