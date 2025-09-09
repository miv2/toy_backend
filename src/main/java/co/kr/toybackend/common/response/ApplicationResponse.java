package co.kr.toybackend.common.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 API 응답 포맷")
public record ApplicationResponse<T>(
        @Schema(description = "응답 결과 정보")
        ApplicationResult result,
        @Schema(description = "실제 응답 데이터", example = "응답 데이터가 여기에 포함됩니다")
        T payload) {

    public static <T> ApplicationResponse<T> ok(String message, T payload) {
        return new ApplicationResponse<>(ApplicationResult.ok(message), payload);
    }

    public static <T> ApplicationResponse<T> unauthorized(String message, T payload) {
        return new ApplicationResponse<>(ApplicationResult.unauthorized(message), payload);
    }

    public static <T> ApplicationResponse<T> forbidden(String message, T payload) {
        return new ApplicationResponse<>(ApplicationResult.forbidden(message), payload);
    }

    public static <T> ApplicationResponse<T> badRequest(String message, T payload) {
        return new ApplicationResponse<>(ApplicationResult.badRequest(message), payload);
    }

    public static <T> ApplicationResponse<T> server(String message, T payload) {
        return new ApplicationResponse<>(ApplicationResult.server(message), payload);
    }

    public static <T> ApplicationResponse<T> notFound(String message, T payload) {
        return new ApplicationResponse<>(ApplicationResult.notFound(message), payload);
    }
}
