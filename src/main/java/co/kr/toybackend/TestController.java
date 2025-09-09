package co.kr.toybackend;

import co.kr.toybackend.common.response.ApplicationResponse;
import co.kr.toybackend.common.response.ApplicationResult;
import co.kr.toybackend.file.FileUploadService;
import co.kr.toybackend.member.service.MemberService;
import co.kr.toybackend.security.provider.JwtTokenProvider;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileUploadService fileUploadService;
    private final TestService testService;

    public TestController(MemberService memberService, JwtTokenProvider jwtTokenProvider, FileUploadService fileUploadService, TestService testService) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.fileUploadService = fileUploadService;
        this.testService = testService;
    }

    @GetMapping
    public ApplicationResponse getTest() {
        return ApplicationResponse.ok("Test API", "Payload Data");
    }

    @PostMapping("/upload")
    public ApplicationResponse test(@RequestPart("image") MultipartFile multipartFile) {
        String filePath = fileUploadService.fileUpload(multipartFile);
        System.out.println("File path : " + filePath);
        return new ApplicationResponse<>(ApplicationResult.ok("파일 업로드 완료"), "test");
    }

}
