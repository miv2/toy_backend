package co.kr.toybackend.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import co.kr.toybackend.common.response.ApplicationResponse;
import co.kr.toybackend.member.domain.Member;
import co.kr.toybackend.member.response.MemberInfoResponse;
import co.kr.toybackend.security.domain.MemberDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 API", description = "사용자 정보 조회 및 관리")
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    @Operation(summary = "모든 사용자 조회", description = "시스템에 등록된 모든 사용자를 조회합니다.")
    @GetMapping
    public ApplicationResponse getMemberInfo(@AuthenticationPrincipal MemberDetails memberDetails) {
        Member member = memberDetails.getMember();

        MemberInfoResponse memberInfoResponse = new MemberInfoResponse(member);

        return ApplicationResponse.ok("내 정보 조회", memberInfoResponse);
    }

}
