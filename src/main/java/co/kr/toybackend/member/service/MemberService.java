package co.kr.toybackend.member.service;

import jakarta.persistence.EntityNotFoundException;
import co.kr.toybackend.member.domain.Member;
import co.kr.toybackend.member.domain.Role;
import co.kr.toybackend.member.enumeration.RoleType;
import co.kr.toybackend.member.repository.MemberJpaRepository;
import co.kr.toybackend.member.repository.MemberRepository;
import co.kr.toybackend.member.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private static final String NOT_EXISTS_MEMBER = "존재하지 않은 회원입니다.";

    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final RoleRepository roleRepository;

    public MemberService(MemberRepository memberRepository, MemberJpaRepository memberJpaRepository, RoleRepository roleRepository) {
        this.memberRepository = memberRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<Member> getMemberOrNullByLoginId(String loginId) {
        return memberRepository.findByLoginIdOrNull(loginId);
    }

    public Member getMemberByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId);
        if (member == null) {
            throw new EntityNotFoundException(NOT_EXISTS_MEMBER);
        }
        return member;
    }

    public Role getRoleByType(RoleType roleType) {
        Role role = roleRepository.findRoleByType(roleType);
        if (role == null) {
            throw new EntityNotFoundException("해당 타입의 권한이 없습니다.");
        }
        return role;
    }

    @Transactional
    public Member save(Member member) {
        Member saveMember = memberJpaRepository.save(member);
        return saveMember;
    }

    public Boolean existsLoginId(String loginId) {
        if (loginId.isEmpty() || loginId.trim().isBlank()) {
            throw new IllegalArgumentException("빈 값(공백)은 입력할 수 없습니다.");
        }
        return memberJpaRepository.existsByLoginId(loginId);
    }


}
