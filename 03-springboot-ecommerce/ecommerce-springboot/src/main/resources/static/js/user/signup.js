// 페이지 로드 후 실행
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('joinForm');

    const fields = {
        idUser: document.getElementById('idUser'),
        nmUser: document.getElementById('nmUser'),
        nmEmail: document.getElementById('nmEmail'),
        nmPaswd: document.getElementById('nmPaswd'),
        confirmPassword: document.getElementById('confirmPassword'),
        noMobile: document.getElementById('noMobile')
    };

    const checkAll = document.getElementById('checkAll');
    const termChecks = Array.from(document.querySelectorAll('.term-check'));

    // 아이디 검증
    function validateIdUser() {
        const value = fields.idUser.value.trim();

        if (!value) {
            setError(fields.idUser, '아이디를 입력해주세요.');
            return false;
        }

        if (!authRules.userId.test(value)) {
            setError(fields.idUser, '아이디는 영문 또는 숫자 5~15자리로 입력해주세요.');
            return false;
        }

        clearMessage(fields.idUser);
        return true;
    }

    // 이름 검증
    function validateNmUser() {
        const value = fields.nmUser.value.trim();

        if (!value) {
            setError(fields.nmUser, '이름을 입력해주세요.');
            return false;
        }

        if (!authRules.name.test(value)) {
            setError(fields.nmUser, '이름은 한글 또는 영문으로 입력해주세요.');
            return false;
        }

        clearMessage(fields.nmUser);
        return true;
    }

    // 이메일 검증
    function validateNmEmail() {
        const value = fields.nmEmail.value.trim();

        if (!value) {
            setError(fields.nmEmail, '이메일을 입력해주세요.');
            return false;
        }

        if (!authRules.email.test(value)) {
            setError(fields.nmEmail, '올바른 이메일 형식으로 입력해주세요.');
            return false;
        }

        clearMessage(fields.nmEmail);
        return true;
    }

    // 비밀번호 검증
    function validateNmPaswd() {
        const value = fields.nmPaswd.value;

        if (!value) {
            setError(fields.nmPaswd, '비밀번호를 입력해주세요.');
            return false;
        }

        if (!authRules.password.test(value)) {
            setError(fields.nmPaswd, '비밀번호는 대문자, 소문자, 숫자를 포함한 5~15자리여야 합니다.');
            return false;
        }

        clearMessage(fields.nmPaswd);
        return true;
    }

    // 비밀번호 확인 검증
    function validateConfirmPassword() {
        const value = fields.confirmPassword.value;

        if (!value) {
            setError(fields.confirmPassword, '비밀번호 확인을 입력해주세요.');
            return false;
        }

        if (value !== fields.nmPaswd.value) {
            setError(fields.confirmPassword, '비밀번호가 일치하지 않습니다.');
            return false;
        }

        clearMessage(fields.confirmPassword);
        return true;
    }

    // 휴대전화 검증
    function validateNoMobile() {
        const value = fields.noMobile.value.trim();

        if (!value) {
            setError(fields.noMobile, '휴대전화를 입력해주세요.');
            return false;
        }

        if (!authRules.phone.test(value)) {
            setError(fields.noMobile, '휴대전화는 010-0000-0000 형식으로 입력해주세요.');
            return false;
        }

        clearMessage(fields.noMobile);
        return true;
    }

    // 약관 검증
    function validateTerms() {
        const termsError = document.getElementById('termsError');
        const requiredTerms = termChecks.filter(check => check.required);
        const isValid = requiredTerms.every(check => check.checked);

        if (!isValid) {
            termsError.textContent = '필수 약관에 모두 동의해주세요.';
            return false;
        }

        termsError.textContent = '';
        return true;
    }

    // 전체 검증
    function validateForm() {
        return [
            validateIdUser(),
            validateNmUser(),
            validateNmEmail(),
            validateNmPaswd(),
            validateConfirmPassword(),
            validateNoMobile(),
            validateTerms()
        ].every(Boolean);
    }

    // 휴대전화 자동 하이픈
    function formatPhoneNumber(value) {
        const numbers = value.replace(/\D/g, '').slice(0, 11);

        if (numbers.length <= 3) {
            return numbers;
        }

        if (numbers.length <= 7) {
            return `${numbers.slice(0, 3)}-${numbers.slice(3)}`;
        }

        return `${numbers.slice(0, 3)}-${numbers.slice(3, 7)}-${numbers.slice(7)}`;
    }

    // 입력 검증 이벤트
    function bindValidationEvents() {
        fields.idUser.addEventListener('blur', validateIdUser);
        fields.nmUser.addEventListener('blur', validateNmUser);
        fields.nmEmail.addEventListener('blur', validateNmEmail);
        fields.nmPaswd.addEventListener('blur', validateNmPaswd);
        fields.confirmPassword.addEventListener('blur', validateConfirmPassword);
        fields.noMobile.addEventListener('blur', validateNoMobile);

        fields.noMobile.addEventListener('input', (event) => {
            event.target.value = formatPhoneNumber(event.target.value);
        });

        fields.nmPaswd.addEventListener('input', () => {
            if (fields.confirmPassword.value) {
                validateConfirmPassword();
            }
        });
    }

    // 약관 체크 이벤트
    function bindTermsEvents() {
        checkAll.addEventListener('change', (event) => {
            termChecks.forEach(check => {
                check.checked = event.target.checked;
            });

            validateTerms();
        });

        termChecks.forEach(check => {
            check.addEventListener('change', () => {
                checkAll.checked = termChecks.every(check => check.checked);
                validateTerms();
            });
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
        bindTermsEvents();
        bindPasswordToggleEvents();
        bindSubmitEvent();
    }

    init();
});