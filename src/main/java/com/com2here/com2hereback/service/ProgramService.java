package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProgramRequestDto;
import java.util.Map;

public interface ProgramService {
    void addProgram(ProgramRequestDto programRequestDto);
    Map<String, Object> getProgram(int offset, int limit, String search, String purpose);
    void updateProgram(ProgramRequestDto programRequestDto);
    void deleteProgram(ProgramRequestDto programRequestDto);
}
