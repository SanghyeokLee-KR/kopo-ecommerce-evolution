package kr.co.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 업로드 파일 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "tb_content")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SequenceGenerator(name = "tb_content_seq_generator", sequenceName = "seq_tb_content", allocationSize = 1)
public class Content {

    // 파일 식별번호(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_content_seq_generator")
    @Column(name = "nb_file")
    private Long nbFile;

    // 원본 파일명
    @Column(name = "nm_org_file", length = 200)
    private String nmOrgFile;

    // 저장 파일명
    @Column(name = "nm_save_file", length = 200)
    private String nmSaveFile;

    // 저장 경로
    @Column(name = "nm_file_path", length = 200)
    private String nmFilePath;

    // MIME 타입
    @Column(name = "nm_content_type", length = 20)
    private String nmContentType;

    // 파일 크기
    @Column(name = "qt_file_size")
    private Long qtFileSize;

    // 파일 확장자
    @Column(name = "nm_file_ext", nullable = false, length = 10)
    private String nmFileExt;

    // 생성 일시
    @Column(name = "da_create_at", nullable = false)
    private LocalDateTime daCreateAt;

    // 원본 파일 식별번호(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_org_file")
    private Content orgFile;
}