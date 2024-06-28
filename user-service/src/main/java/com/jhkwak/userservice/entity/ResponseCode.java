package com.jhkwak.userservice.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ResponseCode {

    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, false, "잘못된 요청입니다."),

    // 403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, false, "권한이 없습니다."),

    // 404 Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, false, "사용자를 찾을 수 없습니다."),
    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, false, "허용되지 않은 메소드입니다."),

    // 409 Conflict
    USER_EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 가입된 이메일 입니다."),
    USER_PHONE_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 가입된 핸드폰 번호 입니다."),
    USER_PASSWORD_WRONG(HttpStatus.CONFLICT, false, "비밀번호가 일치하지 않습니다."),
    RE_VERIFICATION_EMAIL(HttpStatus.CONFLICT, false, "인증이 만료되었습니다. 메일을 확인하여 인증을 진행해주세요."),
    REQUIRE_VERIFICATION_EMAIL(HttpStatus.CONFLICT, false, "메일을 확인하여 인증을 완료해주세요."),
    TOKEN_DO_NOT_MATCH(HttpStatus.CONFLICT, false, "메일이 올바르지 않습니다. 다시 확인해주세요!"),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, "서버에 오류가 발생하였습니다."),

    // 200 OK
    USER_READ_SUCCESS(HttpStatus.OK, true, "사용자 정보 조회 성공"),
    USER_UPDATE_SUCCESS(HttpStatus.OK, true, "사용자 정보 수정 성공"),
    USER_LOGIN_SUCCESS(HttpStatus.OK, true, "사용자 로그인 성공"),
    EMAIL_VERIFICATION_SUCCESS(HttpStatus.OK, true, "이메일 인증 성공"),

    // 201 Created
    USER_CREATE_SUCCESS(HttpStatus.CREATED, true, "회원가입이 완료되었습니다. 이메일을 인증을 진행해주세요.");

    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

}
