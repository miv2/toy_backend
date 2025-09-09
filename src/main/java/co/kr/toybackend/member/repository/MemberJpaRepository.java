package co.kr.toybackend.member.repository;

import co.kr.toybackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Boolean existsByLoginId(String loginId);
}
