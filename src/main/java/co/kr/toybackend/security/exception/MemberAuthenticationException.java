package co.kr.toybackend.security.exception;

public class MemberAuthenticationException extends RuntimeException {
    public MemberAuthenticationException(String message) {
        super(message);
    }
}
