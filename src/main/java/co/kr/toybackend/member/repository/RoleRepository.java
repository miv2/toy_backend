package co.kr.toybackend.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import co.kr.toybackend.member.domain.Role;
import co.kr.toybackend.member.enumeration.RoleType;
import org.springframework.stereotype.Repository;

import static co.kr.toybackend.member.domain.QRole.role;

@Repository
public class RoleRepository {
    private final JPAQueryFactory jpaqueryfactory;

    public RoleRepository(JPAQueryFactory jpaqueryfactory) {
        this.jpaqueryfactory = jpaqueryfactory;
    }

    public Role findRoleByType(RoleType type) {
        return jpaqueryfactory.selectFrom(role)
                .where(role.roleType.eq(type))
                .fetchOne();
    }

}
