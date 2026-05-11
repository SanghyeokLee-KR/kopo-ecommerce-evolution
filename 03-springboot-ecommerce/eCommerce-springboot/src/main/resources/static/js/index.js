// 1. 메인 배너 슬라이드
const slides = document.querySelectorAll('.slide');
let currentSlide = 0;
function nextSlide() {
    slides[currentSlide].classList.remove('active');
    currentSlide = (currentSlide + 1) % slides.length;
    slides[currentSlide].classList.add('active');
}
setInterval(nextSlide, 7000);

// 2. 모바일 메뉴 토글
const menuBtn = document.querySelector('#menuBtn');
const mobileMenu = document.querySelector('#mobileMenu');
menuBtn.addEventListener('click', () => { mobileMenu.classList.toggle('active'); });

// 3. 카테고리 필터
const chips = document.querySelectorAll('.category-chip');
const cards = document.querySelectorAll('.product-card');
chips.forEach((chip) => {
    chip.addEventListener('click', () => {
        chips.forEach((item) => item.classList.remove('active'));
        chip.classList.add('active');
        const target = chip.getAttribute('data-target');

        cards.forEach(card => {
            const category = card.getAttribute('data-category');
            card.classList.remove('show');
            if (target === 'all' || target === category) {
                card.style.display = 'block';
                setTimeout(() => { card.classList.add('show'); }, 50);
            } else {
                card.style.display = 'none';
            }
        });
    });
});

// 4. 스크롤 애니메이션
const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
        if (entry.isIntersecting && entry.target.style.display !== 'none') {
            entry.target.classList.add('show');
        }
    });
}, { threshold: 0.1 });
cards.forEach((card) => observer.observe(card));

// 5. 장바구니 애니메이션
const cartCounts = document.querySelectorAll('.cart-count');
const quickAddButtons = document.querySelectorAll('.quick-add');
let cartCount = 0;

quickAddButtons.forEach((button) => {
    button.addEventListener('click', (e) => {
        e.preventDefault(); e.stopPropagation();
        cartCount++;
        cartCounts.forEach((count) => { count.textContent = cartCount; });
        const originalText = button.textContent;

        button.textContent = 'ADDED TO CART ✓';
        button.style.background = 'var(--black)';
        button.style.color = 'var(--white)';

        setTimeout(() => {
            button.textContent = originalText;
            button.style.background = 'rgba(255, 255, 255, 0.98)';
            button.style.color = 'var(--black)';
        }, 1200);
    });
});