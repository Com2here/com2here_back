package com.com2here.com2hereback.common;

public enum ProgramPurpose {
    게임용,
    작업용,
    사무용,
    개발용;

    public static ProgramPurpose from(String input) {
        if (input == null) return null;
        for (ProgramPurpose purpose : ProgramPurpose.values()) {
            if (purpose.name().equals(input.trim())) {
                return purpose;
            }
        }
        return null;
    }
}
