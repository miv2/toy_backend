package co.kr.toybackend.security.dto.request;

public class TokenRequest {
    private String loginId;
    private String accessToken;
    private String refreshToken;
    private Boolean isExpired;

    public TokenRequest(String loginId, String accessToken, String refreshToken, Boolean isExpired) {
        this.loginId = loginId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isExpired = isExpired;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Boolean getExpired() {
        return isExpired;
    }
}
