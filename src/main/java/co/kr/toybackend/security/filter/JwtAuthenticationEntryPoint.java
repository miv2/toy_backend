package co.kr.toybackend.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import co.kr.toybackend.common.response.ApplicationResponse;
import co.kr.toybackend.common.response.ApplicationResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String responseJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                new ApplicationResponse(ApplicationResult.unauthorized("미인증"), ""));
        response.getOutputStream().write(responseJson.getBytes(StandardCharsets.UTF_8));
    }
}
