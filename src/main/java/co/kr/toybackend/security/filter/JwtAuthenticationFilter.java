package co.kr.toybackend.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import co.kr.toybackend.security.provider.JwtTokenProvider;
import co.kr.toybackend.security.service.JwtTokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, JwtTokenService jwtTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);
        String refreshToken = getRefreshToken(request);
        logger.info(request.getHeader("X-ACCESS-TOKEN"));
        logger.info(request.getHeader("X-REFRESH-TOKEN"));

        if(!accessToken.isBlank() && jwtTokenProvider.validateToken(accessToken)) {
            String loginId = jwtTokenProvider.getLoginId(accessToken);
            Boolean checkAccessTokenExpired = jwtTokenService.checkAccessTokenExpired(loginId, accessToken);
            if(checkAccessTokenExpired) {
                SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(accessToken));
            }
        }

        // 리프레쉬 토큰 만료시 업데이트
        if(!refreshToken.isBlank() && !jwtTokenProvider.validateToken(refreshToken)) {
            String loginId = jwtTokenProvider.getLoginId(refreshToken);
            jwtTokenService.expireTokenByLoginId(loginId);
        }
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {
        return request.getHeader("X-ACCESS-TOKEN") != null ? request.getHeader("X-ACCESS-TOKEN") : "";
    }

    private String getRefreshToken(HttpServletRequest request) {
        return request.getHeader("X-REFRESH-TOKEN") != null ? request.getHeader("X-REFRESH-TOKEN") : "";
    }


}
