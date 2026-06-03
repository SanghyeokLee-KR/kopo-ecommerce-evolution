// 에러 메시지 영역 찾기
function getErrorElement(input) {
    return document.getElementById(`${input.id}Error`);
}

// 에러 표시
function setError(input, message) {
    const group = input.closest('.input-group');
    const errorElement = getErrorElement(input);

    group?.classList.add('has-error');
    input.setAttribute('aria-invalid', 'true');

    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.color = '';
    }
}

// 성공 메시지 표시
function setSuccess(input, message) {
    const group = input.closest('.input-group');
    const errorElement = getErrorElement(input);

    group?.classList.remove('has-error');
    input.removeAttribute('aria-invalid');

    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.color = '#2e7d32';
    }
}

// 메시지 초기화
function clearMessage(input) {
    const group = input.closest('.input-group');
    const errorElement = getErrorElement(input);

    group?.classList.remove('has-error');
    input.removeAttribute('aria-invalid');

    if (errorElement) {
        errorElement.textContent = '';
        errorElement.style.color = '';
    }
}

// 첫 번째 에러 입력창으로 이동
function focusFirstInvalid(form) {
    const firstInvalid = form.querySelector('[aria-invalid="true"]');

    if (firstInvalid) {
        firstInvalid.focus();
    }
}

// 비밀번호 보기/숨기기
function bindPasswordToggleEvents() {
    const passwordToggleButtons = document.querySelectorAll('.btn-password-toggle');

    passwordToggleButtons.forEach(button => {
        button.addEventListener('click', () => {
            const targetInput = document.getElementById(button.dataset.target);

            if (!targetInput) return;

            const isHidden = targetInput.type === 'password';

            targetInput.type = isHidden ? 'text' : 'password';
            button.setAttribute('aria-pressed', String(isHidden));
            button.setAttribute(
                'aria-label',
                isHidden ? '비밀번호 숨기기' : '비밀번호 표시'
            );
        });
    });
}

// 공통 정규식
const authRules = {
    userId: /^[a-zA-Z0-9]{5,15}$/,
    name: /^[가-힣a-zA-Z]{1,20}$/,
    email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
    password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{5,15}$/,
    phone: /^010-\d{4}-\d{4}$/
};