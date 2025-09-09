package co.kr.toybackend.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import co.kr.toybackend.common.TimeAuditingBaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends TimeAuditingBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "nick_name")
    private String nickName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Builder
    public Member(String loginId, String password, String nickName, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.role = role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
