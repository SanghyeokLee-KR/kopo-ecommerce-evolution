package kr.co.shop.service;

import kr.co.shop.common.enums.ProductStatus;
import kr.co.shop.common.exception.BusinessException;
import kr.co.shop.common.exception.ErrorCode;
import kr.co.shop.common.file.FileStorage;
import kr.co.shop.domain.Category;
import kr.co.shop.domain.CategoryProductMapping;
import kr.co.shop.domain.Content;
import kr.co.shop.domain.Product;
import kr.co.shop.dto.product.request.ProductRequest;
import kr.co.shop.dto.product.response.ProductDetailResponse;
import kr.co.shop.dto.product.response.ProductListResponse;
import kr.co.shop.repository.jpa.CategoryProductMappingRepository;
import kr.co.shop.repository.jpa.CategoryRepository;
import kr.co.shop.repository.jpa.ContentRepository;
import kr.co.shop.repository.jpa.ProductRepository;
import kr.co.shop.repository.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryProductMappingRepository categoryProductMappingRepository;
    private final ContentRepository contentRepository;
    private final FileStorage fileStorage;
    private final ProductMapper productMapper;

    @Override
    public List<ProductListResponse> findAll() {
        return productMapper.findAllProducts();
    }

    @Override
    public Page<ProductListResponse> findAdminProducts(int page) {
        int pageNum = Math.max(page - 1, 0);
        int size = 6;
        int offset = pageNum * size;

        List<ProductListResponse> products = productMapper.findAdminProducts(offset, size);
        int total = productMapper.countAdminProducts();

        return new PageImpl<>(products, PageRequest.of(pageNum, size), total);
    }

    @Override
    public Page<ProductListResponse> findDisplayProducts(Long nbCategory, String keyword, int page) {
        int pageNum = Math.max(page - 1, 0);
        int size = 8;
        int offset = pageNum * size;

        // 공백만 있는 검색어는 전체 조회로 처리
        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        List<ProductListResponse> products;
        int total;

        if (nbCategory == null) {
            products = productMapper.findDisplayProducts(keyword, offset, size);
            total = productMapper.countDisplayProducts(keyword);
        } else {
            products = productMapper.findProductsByCategory(nbCategory, keyword, offset, size);
            total = productMapper.countProductsByCategory(nbCategory, keyword);
        }

        return new PageImpl<>(products, PageRequest.of(pageNum, size), total);
    }

    @Override
    public ProductDetailResponse findById(Long nbProduct) {
        Product product = productRepository.findById(nbProduct)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        // 상품-카테고리 매핑이 없는 경우도 있을 수 있어서 null 허용
        CategoryProductMapping mapping = categoryProductMappingRepository
                .findByProduct_NbProduct(nbProduct)
                .orElse(null);

        Long nbCategory = (mapping != null && mapping.getCategory() != null)
                ? mapping.getCategory().getNbCategory()
                : null;

        return new ProductDetailResponse(
                product.getNbProduct(),
                product.getNmProduct(),
                product.getNmDetailExplain(),
                product.getQtSalePrice(),
                product.getQtStock(),
                product.getDtStartDate(),
                getProductStatus(product),
                getImagePath(product),
                nbCategory
        );
    }

    @Override
    @Transactional
    public void create(ProductRequest request) {
        Category category = categoryRepository.findById(request.getNbCategory())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Content content = saveContent(request.getProductImage());

        // 상품 번호 포맷: PRD-생성일자-UUID 앞 8자리 (대문자)
        // FIXME: 시간 남으면 수정
        String noProduct = "PRD-"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-"
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Product product = Product.builder()
                .noProduct(noProduct)
                .nmProduct(request.getNmProduct())
                .nmDetailExplain(request.getDcProduct())
                .content(content)
                .dtStartDate(request.getDtSellStart().format(DATE_FORMATTER))
                .dtEndDate(null)
                .qtSalePrice(request.getQtPrice())
                .qtStock(request.getQtStock())
                .build();

        productRepository.save(product);

        categoryProductMappingRepository.save(
                CategoryProductMapping.builder()
                        .category(category)
                        .product(product)
                        .cnOrder(1)
                        .build()
        );

        log.info("상품 등록 완료 - nbProduct={}, nmProduct={}, noProduct={}",
                product.getNbProduct(), product.getNmProduct(), product.getNoProduct());
    }

    @Override
    @Transactional
    public void update(Long nbProduct, ProductRequest request) {
        Product product = productRepository.findById(nbProduct)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        Category category = categoryRepository.findById(request.getNbCategory())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        product.updateProduct(
                request.getNmProduct(),
                request.getDcProduct(),
                request.getDtSellStart().format(DATE_FORMATTER),
                null,
                request.getQtPrice(),
                request.getQtStock()
        );

        // 이미지 없이 저장하면 기존 이미지 유지됨
        if (request.getProductImage() != null && !request.getProductImage().isEmpty()) {
            // FIXME: 고아 이미지 삭졔 예정
            product.changeContent(saveContent(request.getProductImage()));
        }

        categoryProductMappingRepository
                .findByProduct_NbProduct(nbProduct)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND))
                .changeCategory(category);

        log.info("상품 수정 완료 - nbProduct={}, nmProduct={}", nbProduct, request.getNmProduct());
    }

    @Override
    @Transactional
    public void delete(Long nbProduct) {
        Product product = productRepository.findById(nbProduct)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        // TODO: 소프트 딜리트로 전환 필요. (DB에 주문 내역에서 상품명 조회 안 됨)
        categoryProductMappingRepository.deleteByProduct_NbProduct(nbProduct);
        productRepository.delete(product);

        log.info("상품 삭제 완료 - nbProduct={}", nbProduct);
    }

    private String getImagePath(Product product) {
        if (product.getContent() == null) {
            return null;
        }
        return "/files/" + product.getContent().getNmSaveFile();
    }

    private Content saveContent(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String savedName;
        try {
            savedName = fileStorage.upload(file);
        } catch (Exception e) {
            log.error("파일 업로드 실패 - 원본파일명={}, contentType={}", file.getOriginalFilename(), file.getContentType(), e);
            throw e;
        }

        return contentRepository.save(
                Content.builder()
                        .nmOrgFile(file.getOriginalFilename())
                        .nmSaveFile(savedName)
                        .nmFilePath("uploads/products")
                        .nmContentType(file.getContentType())
                        .qtFileSize(file.getSize())
                        .nmFileExt(StringUtils.getFilenameExtension(file.getOriginalFilename()))
                        .daCreateAt(LocalDateTime.now())
                        .orgFile(null)
                        .build()
        );
    }

    // 재고가 없으면 품절 처리. DB에 별도 상태 컬럼은 없고 재고 수량으로만 판단
    private String getProductStatus(Product product) {
        if (product.getQtStock() != null && product.getQtStock() <= 0) {
            return ProductStatus.SOLD_OUT.getCode();
        }
        return ProductStatus.ON_SALE.getCode();
    }

    @Override
    public List<ProductListResponse> findLatestProducts(int limit) {
        return productMapper.findLatestProducts(limit);
    }

}