package co.kr.toybackend.security.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import co.kr.toybackend.security.domain.JwtToken;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static co.kr.toybackend.security.domain.QJwtToken.jwtToken;

@Repository
public class JwtTokenRepository {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public JwtTokenRepository(EntityManager em, JPAQueryFactory jpaQueryFactory) {
        this.em = em;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Long save(JwtToken jwtToken) {
        em.persist(jwtToken);
        em.flush();
        em.clear();
        return jwtToken.getId();
    }

    public Long expireTokenByLoginId(String loginId) {
        return jpaQueryFactory
                .update(jwtToken)
                .set(jwtToken.isExpired, true)
                .set(jwtToken.updatedAt, LocalDateTime.now())
                .where(jwtToken.loginId.eq(loginId).and(jwtToken.isExpired.eq(false)))
                .execute();
    }

    public Long updateTokenByRefreshToken(String accessToken, String refreshToken) {
        return jpaQueryFactory
                .update(jwtToken)
                .set(jwtToken.accessToken, accessToken)
                .set(jwtToken.updatedAt, LocalDateTime.now())
                .where(jwtToken.refreshToken.eq(refreshToken))
                .execute();
    }

    public JwtToken getTokenByLoginId(String loginId) {
        return jpaQueryFactory
                .selectFrom(jwtToken)
                .where(
                        jwtToken.loginId.eq(loginId)
                        .and(jwtToken.isExpired.eq(false))
                )
                .fetchOne();
    }

    public Long expireRefreshToken(String refreshToken) {
        return jpaQueryFactory
                .update(jwtToken)
                .set(jwtToken.isExpired, true)
                .set(jwtToken.updatedAt, LocalDateTime.now())
                .where(jwtToken.refreshToken.eq(refreshToken))
                .execute();
    }

}
