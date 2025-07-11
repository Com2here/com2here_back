package com.com2here.com2hereback.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String upload(MultipartFile file);
}
