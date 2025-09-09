package co.kr.toybackend.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import co.kr.toybackend.common.response.ApplicationResponse;
import co.kr.toybackend.common.response.ApplicationResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String responseJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                new ApplicationResponse(ApplicationResult.forbidden("권한 없음"), ""));
        response.getOutputStream().write(responseJson.getBytes(StandardCharsets.UTF_8));
    }
}
