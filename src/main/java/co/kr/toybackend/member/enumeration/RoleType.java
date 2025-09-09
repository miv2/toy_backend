package co.kr.toybackend.member.enumeration;

public enum RoleType {

    ROLE_ADMIN("관리자"),
    ROLE_USER("유저");

    private final String value;

    RoleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
