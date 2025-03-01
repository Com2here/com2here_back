package com.com2here.com2hereback.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum BaseResponseStatus {
    /**
     * 200: 요청 성공
     **/
    SUCCESS(HttpStatus.OK, true, 200, "요청에 성공했습니다."),
    NONE_DATA(HttpStatus.OK, true, 200, "조회할 데이터가 없습니다."),

    // 회원 정보 조회
    USER_INFO_RETRIEVED(HttpStatus.OK, true, 200, "회원 정보를 조회했습니다."),

    // 회원 탈퇴
    MEMBER_DELETE_SUCCESS(HttpStatus.OK, true, 200, "회원 탈퇴가 완료되었습니다."),
    MEMBER_DELETE_FAILED(HttpStatus.BAD_REQUEST, false, 2201, "회원 탈퇴에 실패했습니다."),

    // 회원 정보 수정
    MEMBER_INFO_UPDATED(HttpStatus.OK, true, 200, "회원 정보가 수정되었습니다."),
    MEMBER_INFO_UPDATE_FAILED(HttpStatus.BAD_REQUEST, false, 2301, "회원 정보 수정에 실패했습니다."),

    /**
     * 400 : security 에러
     */
    WRONG_PAGE_NUM_MIN(HttpStatus.BAD_REQUEST, false, 400, "잘못된 페이지 번호입니다. (최소 1 이상)"),
    WRONG_PARAM(HttpStatus.BAD_REQUEST, false, 400, "잘못된 요청 (필수 값 누락 또는 잘못된 입력)"),
    WRONG_PAGE_NUM_MAX(HttpStatus.BAD_REQUEST, false, 400, "잘못된 size 값입니다. (1~100)"),
    WRONG_JWT_TOKEN(HttpStatus.UNAUTHORIZED, false, 401, "다시 로그인 해주세요"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, false, 401, "인증 실패 (로그인되지 않은 사용자)"),
    FORBIDDEN(HttpStatus.FORBIDDEN, false, 403, "권한이 없습니다."),
    INVALID_PASSWORD(HttpStatus.FORBIDDEN, false, 403, "비밀번호가 올바르지 않습니다."),
    NOT_APPROVAL_MEMBER(HttpStatus.UNAUTHORIZED, false, 403, "관리자에게 인가되지 않은 계정입니다."),
    NOT_FOUND_DATA(HttpStatus.NOT_FOUND, false, 404, "해당 프로젝트 홍보 게시물을 찾을 수 없음"),

    /**
     * 500 : security 에러
     */
    WRONG_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, false, 500, "서버 내부 오류가 발생"),

    /**
     * 900: 기타 에러
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 900, "Internal server error"),
    NO_EXIST_IMAGE(HttpStatus.NOT_FOUND, false, 901, "존재하지 않는 이미지 입니다"),

    /**
     * 2000 : members Service Error
     */

    // Token, Code
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, false, 2001, "토큰이 만료되었습니다."),
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, false, 2002, "토큰이 유효하지 않습니다."),
    TOKEN_NULL(HttpStatus.UNAUTHORIZED, false, 2003, "토큰이 존재하지 않습니다."),
    JWT_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, false, 2004, "토큰 생성에 실패했습니다."),
    JWT_VALID_FAILED(HttpStatus.UNAUTHORIZED, false, 2005, "토큰 검증에 실패했습니다."),
    EXPIRED_AUTH_CODE(HttpStatus.UNAUTHORIZED, false, 2006, "인증번호가 만료되었거나 존재하지 않는 멤버입니다."),
    WRONG_AUTH_CODE(HttpStatus.UNAUTHORIZED, false, 2007, "인증번호가 일치하지 않습니다."),
    LOGOUT_TOKEN(HttpStatus.UNAUTHORIZED, false, 2008, "로그아웃된 토큰입니다."),

    // Members
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, false, 2100, "사용중인 이메일입니다."),
    DUPLICATED_MEMBERS(HttpStatus.CONFLICT, false, 2101, "이미 가입된 멤버입니다."),
    MASSAGE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, false, 2102, "인증번호 전송에 실패했습니다."),
    MASSAGE_VALID_FAILED(HttpStatus.NOT_FOUND, false, 2103, "인증번호가 일치하지 않습니다."),
    FAILED_TO_LOGIN(HttpStatus.UNAUTHORIZED, false, 2104, "아이디 또는 패스워드를 다시 확인하세요."),
    FAILED_TO_PASSWORD(HttpStatus.UNAUTHORIZED, false, 2104, "비밀번호를 다시 한번 확인 해 주세요."),
    WITHDRAWAL_MEMBERS(HttpStatus.FORBIDDEN, false, 2105, "탈퇴한 회원입니다."),
    NO_EXIST_MEMBERS(HttpStatus.NOT_FOUND, false, 2106, "존재하지 않는 멤버 정보입니다."),
    MEMBERS_STATUS_IS_NOT_FOUND(HttpStatus.NOT_FOUND, false, 2107, "존재하지 않는 멤버 상태입니다."),
    PASSWORD_SAME_FAILED(HttpStatus.BAD_REQUEST, false, 2108, "현재 사용중인 비밀번호 입니다."),
    PASSWORD_CONTAIN_NUM_FAILED(HttpStatus.BAD_REQUEST, false, 2109, "휴대폰 번호를 포함한 비밀번호 입니다."),
    PASSWORD_CONTAIN_EMAIL_FAILED(HttpStatus.BAD_REQUEST, false, 2110, "이메일이 포함된 비밀번호 입니다."),
    NO_EXIST_AUTH(HttpStatus.NOT_FOUND, false, 2106, "인증 정보가 없습니다"),

    // 비밀번호
    PASSWORD_MISSING(HttpStatus.BAD_REQUEST, false, 2200, "비밀번호가 누락되었습니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, false, 2202, "비밀번호가 일치하지 않습니다."),

    DUPLICATE_SNS_MEMBERS(HttpStatus.CONFLICT, false, 2100, "이미 사용중인 SNS 회원입니다."),
    NO_EXIST_SNS_MEMBERS(HttpStatus.NOT_FOUND, false, 2106, "가입되지 않은 SNS 멤버 정보입니다."),

    // Address
    NO_EXIST_ADDRESS(HttpStatus.NOT_FOUND, false, 2300, "존재하지 않는 주소입니다.");


    private final HttpStatusCode httpStatusCode;
    private final boolean isSuccess;
    private final int code;
    private final String message;

}
