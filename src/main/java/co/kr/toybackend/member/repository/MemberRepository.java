package co.kr.toybackend.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import co.kr.toybackend.member.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static co.kr.toybackend.member.domain.QMember.member;

@Repository
public class MemberRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public MemberRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Member findByLoginId(String loginId) {
        return jpaQueryFactory.selectFrom(member)
                .join(member.role)
                .fetchJoin()
                .where(member.loginId.eq(loginId))
                .fetchOne();
    }

    public Member findByMemberId(Long memberId) {
        return jpaQueryFactory.selectFrom(member)
                .join(member.role)
                .fetchJoin()
                .where(member.id.eq(memberId))
                .fetchOne();
    }

    public Optional<Member> findByLoginIdOrNull(String loginId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(member)
                .join(member.role)
                .fetchJoin()
                .where(member.loginId.eq(loginId))
                .fetchOne());
    }

}
