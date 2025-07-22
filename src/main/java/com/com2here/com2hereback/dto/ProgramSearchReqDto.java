package com.com2here.com2hereback.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProgramSearchReqDto {

    @Min(value = 1, message = "page는 1 이상이어야 합니다.")
    private int page = 1;

    @Min(value = 1, message = "limit은 1 이상이어야 합니다.")
    private int limit = 10;

    private String search;

    private String purpose;
}
