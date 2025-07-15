package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudflareStorageServiceImpl implements FileStorageService {

    private final S3Client s3Client;

    @Value("${cloudflare.bucket.name}")
    private String bucketName;

    @Value("${cloudflare.public.url}")
    private String publicUrl;

    @Override
    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .contentType(file.getContentType())
                    .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            throw new BaseException(BaseResponseStatus.FILE_UPLOAD_FAILED);
        }

        return publicUrl + "/" + filename;
    }
}
