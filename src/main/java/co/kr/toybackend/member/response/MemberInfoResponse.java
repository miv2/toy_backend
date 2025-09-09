package co.kr.toybackend.member.response;

import co.kr.toybackend.member.domain.Member;

public class MemberInfoResponse {
    private Long id;
    private String loginId;
    private String nickName;
    private String role;

    public MemberInfoResponse(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.nickName = member.getNickName();
        this.role = member.getRole().getRoleName();
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getRole() {
        return role;
    }

}
