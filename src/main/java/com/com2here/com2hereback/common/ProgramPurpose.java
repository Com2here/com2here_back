package com.com2here.com2hereback.common;

import lombok.Getter;

@Getter
public enum ProgramPurpose {
    GAME("게임용"),
    WORK("작업용"),
    OFFICE("사무용"),
    DEV("개발용");

    private final String displayName;

    ProgramPurpose(String displayName) {
        this.displayName = displayName;
    }

    public static ProgramPurpose from(String input) {
        if (input == null || input.isBlank()) return null;

        for (ProgramPurpose purpose : ProgramPurpose.values()) {
            if (purpose.name().equalsIgnoreCase(input.trim()) || 
                purpose.displayName.equals(input.trim())) {
                return purpose;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 ProgramPurpose 값: " + input);
    }
}
