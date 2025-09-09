package co.kr.toybackend.security.domain;

import jakarta.persistence.*;
import co.kr.toybackend.common.TimeAuditingBaseEntity;
import co.kr.toybackend.config.converter.BooleanConverter;

@Entity
@Table(name = "jwt_expiration")
public class JwtToken extends TimeAuditingBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "access_token", columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "is_expired")
    @Convert(converter = BooleanConverter.class)
    private Boolean isExpired;

    public JwtToken(String loginId, String accessToken, String refreshToken, Boolean isExpired) {
        this.loginId = loginId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isExpired = isExpired;
    }

    public JwtToken() {

    }

    public Long getId() {
        return id;
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
