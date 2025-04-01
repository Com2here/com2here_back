package com.com2here.com2hereback.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CMResponse<T> {
    private int code;
    private String message;
    private T data;

    // public 생성자
    public CMResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // BaseResponseStatus를 인자로 받는 생성자
    public CMResponse(BaseResponseStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = null; // 기본값은 null
    }

    // 성공 : 메시지 + 데이터 (반환값 O)
    public static <T> CMResponse<T> success(int code,BaseResponseStatus status, T data) {
        return new CMResponse<>(code,status.getMessage(), data);
    }

    // 성공 : 메시지 + 데이터 (반환값 X)
    public static <T> CMResponse<T> success(int code, BaseResponseStatus status) {
        return new CMResponse<>(code, status.getMessage(), null);
    }

    // 실패 : 코드 + 메시지 (반환값 O)
    public static <T> CMResponse<T> fail(int code,BaseResponseStatus status, T data) {
        return new CMResponse<>(code, status.getMessage(), data);
    }

    // 실패 : 코드 +  + 메시지 (반환값 X)
    public static <T> CMResponse<T> fail(int code, BaseResponseStatus status) {
        return new CMResponse<>(code, status.getMessage(), null);
    }
}
