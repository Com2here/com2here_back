package com.com2here.com2hereback.common.exception;

import com.com2here.com2hereback.common.BaseResponseStatus;

public class CustomException extends RuntimeException {
    private BaseResponseStatus status = null;

    public CustomException() {
        this.status = status;
    }


    public BaseResponseStatus getStatus() {
        return status;
    }

    public int getCode() {
        return status.getCode(); // 상태 코드 반환
    }

    public String getMessage() {
        return status.getMessage(); // 메시지 반환
    }
}
