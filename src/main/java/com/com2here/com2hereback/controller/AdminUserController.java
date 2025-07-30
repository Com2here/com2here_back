package com.com2here.com2hereback.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;
import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.AdminUserRegisterReqDto;
import com.com2here.com2hereback.dto.AdminUserUpdateReqDto;
import com.com2here.com2hereback.dto.UserShowRespDto;
import com.com2here.com2hereback.service.AdminUserService;
import com.com2here.com2hereback.vo.UserShowVO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/user")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public CMResponse<Void> createUserByAdmin(@Valid @RequestBody AdminUserRegisterReqDto registerDto) {
        adminUserService.createUserByAdmin(registerDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CMResponse<Page<UserShowVO>> getAllUsers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserShowRespDto> users = adminUserService.getAllUsers(pageable);
        Page<UserShowVO> result = users.map(UserShowVO::from);

        return CMResponse.success(BaseResponseStatus.SUCCESS, result);
    }

    @PatchMapping(value = "/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public CMResponse<Void> updateUser(@PathVariable("uuid") String uuid,
                                    @Valid @ModelAttribute AdminUserUpdateReqDto updateDto) {
        adminUserService.updateUserByAdmin(uuid, updateDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public CMResponse<Void> deleteUser(@PathVariable("uuid") String uuid) {
        adminUserService.deleteUserByAdmin(uuid);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }
}
