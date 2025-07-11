package com.com2here.com2hereback.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

//    private final String uploadDir = "uploads/";  // 프로젝트 루트 기준 상대 경로
    private final String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
    @Override
    public String upload(MultipartFile file) {
//        System.out.println("========== [파일 업로드 시작] ==========");
//        System.out.println("현재 작업 디렉토리: " + System.getProperty("user.dir"));

        if (file == null) {
//            System.out.println("파일 객체 자체가 null입니다.");
            throw new RuntimeException("파일이 null입니다.");
        }

        if (file.isEmpty()) {
//            System.out.println("업로드된 파일이 비어 있습니다.");
            throw new RuntimeException("업로드된 파일이 비어있습니다.");
        }

//        System.out.println("요청된 파일 이름: " + file.getOriginalFilename());
//        System.out.println("파일 크기 (bytes): " + file.getSize());

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(uploadDir + filename);
        File dir = dest.getParentFile();

        if (!dir.exists()) {
            boolean created = dir.mkdirs();
//            System.out.println("업로드 디렉토리 생성 여부: " + created);
        } else {
//            System.out.println("업로드 디렉토리 이미 존재함: " + dir.getAbsolutePath());
        }

        try {
//            System.out.println("▶▶ file.transferTo 실행 직전");
            file.transferTo(dest);
            System.out.println("✅ 파일 저장 성공! 경로: " + dest.getAbsolutePath());
            System.out.println("========== [파일 업로드 완료] ==========\n");
            return "/uploads/" + filename;
        } catch (IOException e) {
            System.err.println("❌ 파일 저장 중 예외 발생!");
            e.printStackTrace();
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
