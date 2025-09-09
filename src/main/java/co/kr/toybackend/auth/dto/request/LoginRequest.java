package co.kr.toybackend.auth.dto.request;

public class LoginRequest {
    private String loginId;
    private String password;

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }
}
