package co.kr.toybackend.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import co.kr.toybackend.auth.dto.request.LoginRequest;
import co.kr.toybackend.auth.dto.request.SignUpRequest;
import co.kr.toybackend.member.domain.Member;
import co.kr.toybackend.member.domain.Role;
import co.kr.toybackend.member.enumeration.RoleType;
import co.kr.toybackend.member.service.MemberService;
import co.kr.toybackend.security.domain.MemberDetails;
import co.kr.toybackend.security.dto.request.TokenRequest;
import co.kr.toybackend.security.dto.response.TokenResponse;
import co.kr.toybackend.security.exception.MemberAuthenticationException;
import co.kr.toybackend.security.provider.JwtTokenProvider;
import co.kr.toybackend.security.service.JwtTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {
    private static final String INVALID_PASSWORD = "아이디 or 비밀번호가 일치하지않습니다.";
    private static final String INVALID_REFRESH_TOKEN = "리프레쉬 토큰이 일치하지 않거나, 만료되었습니다. 재 로그인 하십시오.";
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationService(MemberService memberService, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Long signUp(SignUpRequest signUpRequest) {
        String loginId = signUpRequest.getLoginId();
        Boolean existsLoginId = memberService.existsLoginId(loginId);

        if(existsLoginId) {
            throw new IllegalArgumentException("아이디가 존재합니다.");
        }

        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Role userRole = memberService.getRoleByType(RoleType.ROLE_USER);

//        Member member = new Member(signUpRequest, userRole);

        Member member = Member.builder()
                .loginId(signUpRequest.getLoginId())
                .password(signUpRequest.getPassword())
                .role(userRole)
                .build();

        Member saveMember = memberService.save(member);

        return saveMember.getId();
    }

    public void authentication(LoginRequest loginRequest, HttpServletResponse response) {
        // 회원 찾기
        Member findMember = memberService.getMemberOrNullByLoginId(loginRequest.getLoginId())
                .orElseThrow(() -> new MemberAuthenticationException(INVALID_PASSWORD));

        // token member
        Member tokenMember = null;

        // password match
        if (!passwordEncoder.matches(loginRequest.getPassword(), findMember.getPassword())) {
            throw new MemberAuthenticationException(INVALID_PASSWORD);
        }

        tokenMember = findMember;

        // 기존 토큰 만료
        Long result = jwtTokenService.expireTokenByLoginId(loginRequest.getLoginId());
        System.out.println("기존 토큰 만료 : " + result);

        // 새로운 토큰 생성
        TokenResponse tokenResponse = jwtTokenProvider.createToken(tokenMember);

        jwtTokenService.save(new TokenRequest(
                loginRequest.getLoginId(),
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                false)
        );

        response.setHeader("X-ACCESS-TOKEN", tokenResponse.getAccessToken());
        response.setHeader("X-REFRESH-TOKEN", tokenResponse.getRefreshToken());
        response.setHeader("X-TOKEN-TIME", tokenResponse.getExpireRefreshTokenTime().toString());
    }

    public TokenResponse generateTokenByRefreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException(INVALID_REFRESH_TOKEN);
        }

        String loginId = jwtTokenProvider.getLoginId(refreshToken);
        boolean isTokenExpired = jwtTokenService.checkRefreshTokenExpired(loginId, refreshToken);
        if (isTokenExpired) {
            throw new IllegalArgumentException(INVALID_REFRESH_TOKEN);
        }

        TokenResponse tokenResponse = jwtTokenProvider.createAccessTokenByRefreshToken(refreshToken);

        jwtTokenService.updateAccessToken(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }

    public TokenResponse generateNewRefreshToken(String refreshToken) {
        // refresh 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException(INVALID_REFRESH_TOKEN);
        }

        String loginId = jwtTokenProvider.getLoginId(refreshToken);
        boolean expired = jwtTokenService.checkRefreshTokenExpired(loginId, refreshToken);

        if(expired) {
            throw new IllegalArgumentException(INVALID_REFRESH_TOKEN);
        }

        // 기존에 사용된 refresh token 만료 처리
        jwtTokenService.expireRefreshToken(refreshToken);

        //토큰 정보로 회원 찾기
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        Member member = memberDetails.getMember();

        TokenResponse tokenResponse = jwtTokenProvider.createToken(member);

        // 새로운 토큰 정보 저장
        jwtTokenService.save(new TokenRequest(member.getLoginId()
                ,tokenResponse.getAccessToken()
                ,tokenResponse.getRefreshToken()
                ,false));

        return tokenResponse;
    }
}
