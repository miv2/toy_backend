package co.kr.toybackend.security.service;

import co.kr.toybackend.security.domain.JwtToken;
import co.kr.toybackend.security.dto.request.TokenRequest;
import co.kr.toybackend.security.repository.JwtTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JwtTokenService {
    private final JwtTokenRepository jwtTokenRepository;

    public JwtTokenService(JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @Transactional
    public Long save(TokenRequest tokenRequest) {
        JwtToken jwtToken = new JwtToken(tokenRequest.getLoginId(), tokenRequest.getAccessToken(),
                tokenRequest.getRefreshToken(), tokenRequest.getExpired());

        return jwtTokenRepository.save(jwtToken);
    }

    @Transactional
    public Long updateAccessToken(String accessToken, String refreshToken) {
        return jwtTokenRepository.updateTokenByRefreshToken(accessToken, refreshToken);
    }

    @Transactional
    public Long expireTokenByLoginId(String loginId) {
        return jwtTokenRepository.expireTokenByLoginId(loginId);
    }

    public boolean checkRefreshTokenExpired(String loginId, String refreshToken) {
        System.out.println("checkRefreshTokenExpired loginId ::: " + loginId);
        JwtToken findToken = jwtTokenRepository.getTokenByLoginId(loginId);
        return findToken == null || findToken.getRefreshToken() != refreshToken;
    }

    public boolean checkAccessTokenExpired(String loginId, String accessToken) {
        JwtToken findToken = jwtTokenRepository.getTokenByLoginId(loginId);
        return findToken == null || findToken.getAccessToken() != accessToken;
    }

    @Transactional
    public Long expireRefreshToken(String refreshToken) {
        return jwtTokenRepository.expireRefreshToken(refreshToken);
    }



}
