package co.kr.toybackend.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import co.kr.toybackend.member.domain.Member;
import co.kr.toybackend.security.dto.response.TokenResponse;
import co.kr.toybackend.security.service.MemberDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final MemberDetailsService memberDetailsService;
    private final String secretKey;
    private final long accessTokenTime;
    private final long refreshTokenTime;
    private Key key;

    public JwtTokenProvider(MemberDetailsService memberDetailsService) {
        this.memberDetailsService = memberDetailsService;
        this.secretKey = "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey";
        this.accessTokenTime = 10 * 60 * 1000;
        this.refreshTokenTime = 30 * 60 * 1000;
    }

    @PostConstruct
    public void init() {
        System.out.println("[init] JwtTokenProvider initialization started");
        // byte[] decodedSecretKey = Base64.getDecoder().decode(secretKey);
        byte[] decodedSecretKey = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(decodedSecretKey);
        System.out.println("[init] JwtTokenProvider initialization completed");
    }

    public Authentication getAuthentication(String token) {
        UserDetails loadUserByUsername = memberDetailsService.loadUserByUsername(this.getLoginId(token));
        return new UsernamePasswordAuthenticationToken(loadUserByUsername, "", loadUserByUsername.getAuthorities());
    }

    public String getLoginId(String token) {
        try {
/*            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return claimsJws.getBody().get("loginId", String.class);*/

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("loginId").toString();
        } catch (ExpiredJwtException e) {
            return e.getClaims().get("loginId", String.class);
        }
    }

    public String getRole(String token) {
        try {
/*            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return claimsJws.getBody().get("role", String.class);*/

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("role").toString();
        } catch (ExpiredJwtException e) {
            return e.getClaims().get("role", String.class);
        }
    }

    public TokenResponse createToken(Member member) {
        String loginId = member.getLoginId();
        String role = member.getRole() != null ? member.getRole().getRoleType().toString() : "";
        System.out.println("[createToken] 토큰 생성 loginId : " + loginId);
        System.out.println("[createToken] 토큰 생성 role : " + role);

        Date now = new Date();
        Date accessTokenExpiredTime = new Date(now.getTime() + accessTokenTime);
        Date refreshTokenExpiredTime = new Date(now.getTime() + refreshTokenTime);
        return tokenResponse(loginId, role, now, accessTokenExpiredTime, refreshTokenExpiredTime);
    }

    public TokenResponse createAccessTokenByRefreshToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Date refreshExpired = claims.getExpiration();

        String loginId = getLoginId(token);
        String role = getRole(token);

        Date now = new Date();
        Date accessTokenExpiredTime = new Date(now.getTime() + accessTokenTime);

        String newAccessToken = getToken(
                createClaims(loginId, role)
                ,now
                ,accessTokenExpiredTime);

        //return new TokenResponse(newAccessToken, token, LocalDateTime.now());
        return new TokenResponse(newAccessToken, token, refreshExpired.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    private TokenResponse tokenResponse(String loginId, String role, Date now,
                                        Date accessTokenExpiredTime, Date refreshExpired) {
        String accessToken = getToken(createClaims(loginId, role), now, accessTokenExpiredTime);
        String refreshToken = getToken(createClaims(loginId, role), now, refreshExpired);

        return new TokenResponse(
                accessToken,
                refreshToken,
                refreshExpired.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }

    private Claims createClaims(String loginId, String role) {
        Claims claims = Jwts.claims();
        claims.setSubject(UUID.randomUUID().toString());
        claims.put("loginId", loginId);
        claims.put("role", role);
        return claims;
    }

    private String getToken(Claims claims, Date currentTime, Date expiredTime) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(currentTime)
                .setExpiration(expiredTime)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
/*            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());*/

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());

        } catch (Exception e) {
            return false;
        }
    }

}
