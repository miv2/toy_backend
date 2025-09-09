package co.kr.toybackend.security.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireRefreshTokenTime;

    public TokenResponse() {
    }

    public TokenResponse(String accessToken, String refreshToken, LocalDateTime expireRefreshTokenTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expireRefreshTokenTime = expireRefreshTokenTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getExpireRefreshTokenTime() {
        return expireRefreshTokenTime;
    }

    public void setExpireRefreshTokenTime(LocalDateTime expireRefreshTokenTime) {
        this.expireRefreshTokenTime = expireRefreshTokenTime;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expireRefreshTokenTime=" + expireRefreshTokenTime +
                '}';
    }
}
