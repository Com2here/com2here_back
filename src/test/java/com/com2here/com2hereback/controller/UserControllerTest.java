package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.config.jwt.TokenProvider;
import com.com2here.com2hereback.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void registerUser_ShouldReturnValidationError_WhenRequestInvalid() throws Exception {
        // 빈 요청 → DTO @Valid 실패 유도
        mockMvc.perform(post("/api/v1/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(2400))  // VALIDATION_ERROR 코드
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void registerUser_ShouldReturnConflict_WhenDuplicateEmail() throws Exception {
        // 서비스에서 DUPLICATE_EMAIL BaseException 던지도록 Stub
        doThrow(new BaseException(BaseResponseStatus.DUPLICATE_EMAIL))
            .when(userService).RegisterUser(any());

        String validRequest = objectMapper.writeValueAsString(
            new TestUserRegisterReq("tester", "tester@test.com", "Test123!", "Test123!")
        );

        mockMvc.perform(post("/api/v1/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequest))
            .andExpect(status().isConflict())  // 409
            .andExpect(jsonPath("$.code").value(2100))
            .andExpect(jsonPath("$.message").value("사용중인 이메일입니다."));
    }

    @Test
    void registerUser_ShouldReturnInternalServerError_WhenUnexpectedExceptionOccurs() throws Exception {
        // 서비스에서 RuntimeException 발생하도록 Stub
        doThrow(new RuntimeException("unexpected error"))
            .when(userService).RegisterUser(any());

        String validRequest = objectMapper.writeValueAsString(
            new TestUserRegisterReq("tester", "tester@test.com", "Test123!", "Test123!")
        );

        mockMvc.perform(post("/api/v1/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequest))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value(900))
            .andExpect(jsonPath("$.message").value("Internal server error"));
    }

    // 테스트용 DTO (실제 UserRegisterReqDto와 동일한 필드만 사용)
    static class TestUserRegisterReq {
        public String nickname;
        public String email;
        public String password;
        public String confirmPassword;

        public TestUserRegisterReq(String nickname, String email, String password, String confirmPassword) {
            this.nickname = nickname;
            this.email = email;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }
    }
}
