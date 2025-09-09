package co.kr.toybackend.auth.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private String loginId;
    private String password;
    private String nickName;
    private int age;

    public void setPassword(String password) {
        this.password = password;
    }
}
