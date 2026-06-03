package kr.co.shop.common.file;

import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 Interface
 */
public interface FileStorage {

    // 파일 업로드
    String upload(MultipartFile file);
}