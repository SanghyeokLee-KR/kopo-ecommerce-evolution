package kr.co.shop.common.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 로컬 파일 업로드 모듈
 */
@Component
public class LocalFileStorage implements FileStorage {

    @Value("${file.upload-path}")
    private String baseDir;

    // 파일 업로드
    @Override
    public String upload(MultipartFile file) {
        try {
            Path uploadPath = Path.of(baseDir);

            // 폴더 없으면 생성
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String ext = getExtension(file.getOriginalFilename());
            String savedName = UUID.randomUUID() + "." + ext;

            Path targetPath = uploadPath.resolve(savedName);

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return savedName;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 확장자 추출
    private String getExtension(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }
}