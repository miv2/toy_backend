package co.kr.toybackend.security.service;

import co.kr.toybackend.member.domain.Member;
import co.kr.toybackend.member.service.MemberService;
import co.kr.toybackend.security.domain.MemberDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailsService implements UserDetailsService {
    private final MemberService memberService;

    public MemberDetailsService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberService.getMemberByLoginId(username);
        return MemberDetails.of(member);
    }
}
