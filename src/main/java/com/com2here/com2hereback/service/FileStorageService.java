package com.com2here.com2hereback.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String upload(MultipartFile file);
    // void delete(String fileUrl); // 필요하면 삭제도 지원
}
