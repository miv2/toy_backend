package co.kr.toybackend.security.domain;

import co.kr.toybackend.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemberDetails implements UserDetails {

    private final Member member;

    public MemberDetails(Member member) {
        this.member = member;
    }

    public static MemberDetails of(Member member) {
        return new MemberDetails(member);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        String roleName = member.getRole() != null ? member.getRole().getRoleType().toString() : null;
        if (roleName != null) {
            authorities.add(new SimpleGrantedAuthority(roleName));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Member getMember() {
        return member;
    }
}
