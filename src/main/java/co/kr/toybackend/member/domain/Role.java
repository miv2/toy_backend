package co.kr.toybackend.member.domain;

import jakarta.persistence.*;
import co.kr.toybackend.member.enumeration.RoleType;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_type")
    @Enumerated(value = EnumType.STRING)
    private RoleType roleType;

    public Long getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public RoleType getRoleType() {
        return roleType;
    }
}
