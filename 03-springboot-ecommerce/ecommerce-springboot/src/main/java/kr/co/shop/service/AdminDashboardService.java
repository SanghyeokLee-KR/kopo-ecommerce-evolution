package kr.co.shop.service;
 
import kr.co.shop.dto.admin.response.AdminDashboardResponse;
import kr.co.shop.repository.jpa.CategoryRepository;
import kr.co.shop.repository.jpa.OrderRepository;
import kr.co.shop.repository.jpa.ProductRepository;
import kr.co.shop.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardService {
 
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
 
    public AdminDashboardResponse getDashboard() {
        return new AdminDashboardResponse(
                userRepository.count(),
                categoryRepository.count(),
                productRepository.count(),
                orderRepository.count()
        );
    }
}