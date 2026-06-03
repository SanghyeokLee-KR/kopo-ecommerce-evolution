// 페이지 로드 후 실행
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('loginForm');

    const fields = {
        idUser: document.getElementById('idUser'),
        nmPaswd: document.getElementById('nmPaswd')
    };

    // 아이디 검증
    function validateIdUser() {
        const value = fields.idUser.value.trim();

        if (!value) {
            setError(fields.idUser, '아이디를 입력해주세요.');
            return false;
        }

        clearMessage(fields.idUser);
        return true;
    }

    // 비밀번호 검증
    function validateNmPaswd() {
        const value = fields.nmPaswd.value;

        if (!value) {
            setError(fields.nmPaswd, '비밀번호를 입력해주세요.');
            return false;
        }

        clearMessage(fields.nmPaswd);
        return true;
    }

    // 전체 검증
    function validateForm() {
        return [
            validateIdUser(),
            validateNmPaswd()
        ].every(Boolean);
    }

    // 입력 검증 이벤트
    function bindValidationEvents() {
        fields.idUser.addEventListener('blur', validateIdUser);
        fields.nmPaswd.addEventListener('blur', validateNmPaswd);

        fields.idUser.addEventListener('input', () => {
            if (fields.idUser.getAttribute('aria-invalid') === 'true') {
                validateIdUser();
            }
        });

        fields.nmPaswd.addEventListener('input', () => {
            if (fields.nmPaswd.getAttribute('aria-invalid') === 'true') {
                validateNmPaswd();
            }
        });
    }

    // 폼 제출 이벤트
    function bindSubmitEvent() {
        form.addEventListener('submit', (event) => {
            if (!validateForm()) {
                event.preventDefault();
                focusFirstInvalid(form);
            }
        });
    }

    // 초기화
    function init() {
        bindValidationEvents();
        bindPasswordToggleEvents();
        bindSubmitEvent();
    }

    init();
});