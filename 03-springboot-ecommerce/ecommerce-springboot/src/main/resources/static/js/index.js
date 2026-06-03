document.addEventListener("DOMContentLoaded", () => {
    const anchorLinks = document.querySelectorAll('a[href^="#"]');
    const galleryItems = document.querySelectorAll(".grid-item");

    anchorLinks.forEach((link) => {
        link.addEventListener("click", (event) => {
            const targetId = link.getAttribute("href");

            if (!targetId || targetId === "#") return;

            const targetElement = document.querySelector(targetId);

            if (!targetElement) return;

            event.preventDefault();

            targetElement.scrollIntoView({
                behavior: "smooth",
                block: "start"
            });
        });
    });

    const observerOptions = {
        threshold: 0.18,
        rootMargin: "0px 0px -80px 0px"
    };

    const galleryObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach((entry) => {
            if (!entry.isIntersecting) return;

            entry.target.classList.add("is-visible");
            observer.unobserve(entry.target);
        });
    }, observerOptions);

    galleryItems.forEach((item, index) => {
        item.style.transitionDelay = `${index * 120}ms`;
        galleryObserver.observe(item);
    });
});