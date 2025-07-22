package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProgramAddReqDto;
import com.com2here.com2hereback.dto.ProgramDeleteReqDto;
import com.com2here.com2hereback.dto.ProgramUpdateReqDto;
import java.util.Map;

public interface ProgramService {
    void addProgram(ProgramAddReqDto programAddReqDto);
    Map<String, Object> getProgram(int offset, int limit, String search, String purpose);
    void updateProgram(ProgramUpdateReqDto programUpdateReqDto);
    void deleteProgram(ProgramDeleteReqDto programDeleteReqDto);
}
