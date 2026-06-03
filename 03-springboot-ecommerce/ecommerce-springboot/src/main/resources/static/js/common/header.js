document.addEventListener("DOMContentLoaded", () => {
    const header = document.querySelector("header");
    const searchToggle = document.querySelector(".header-search-toggle");
    const searchPanel = document.querySelector(".header-search-panel");
    const searchInput = document.querySelector(".header-search-panel-form input");

    const handleHeaderScroll = () => {
        if (!header) return;

        if (window.scrollY > 50) {
            header.classList.add("scrolled");
        } else {
            header.classList.remove("scrolled");
        }
    };

    handleHeaderScroll();
    window.addEventListener("scroll", handleHeaderScroll, { passive: true });

    if (searchToggle && searchPanel) {
        searchToggle.addEventListener("click", () => {
            searchPanel.classList.toggle("active");

            if (searchPanel.classList.contains("active") && searchInput) {
                searchInput.focus();
            }
        });
    }
});