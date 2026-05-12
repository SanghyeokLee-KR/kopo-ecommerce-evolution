/**
 * DINK GALLERY 회원가입 클라이언트 스크립트
 * 역할: 데이터 수집, 이름 통합, 백엔드 전송 및 리다이렉트 처리
 */
document.addEventListener('DOMContentLoaded', () => {
    const joinForm = document.getElementById('joinForm');
    const checkAll = document.getElementById('checkAll');
    const termChecks = document.querySelectorAll('.term-check');

    // 비밀번호 확인을 위한 요소
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');

    /**
     * 1. 약관 동의 UI 로직
     */
    if (checkAll) {
        // 전체 동의 클릭 시 모든 개별 체크박스 상태 변경
        checkAll.addEventListener('change', (e) => {
            termChecks.forEach(check => {
                check.checked = e.target.checked;
            });
        });

        // 개별 체크박스 변경 시 전체 동의 상태 업데이트
        termChecks.forEach(check => {
            check.addEventListener('change', () => {
                const allChecked = Array.from(termChecks).every(c => c.checked);
                checkAll.checked = allChecked;
            });
        });
    }

    /**
     * 2. 회원가입 데이터 전송 로직
     */
    if (joinForm) {
        joinForm.addEventListener('submit', async (e) => {
            e.preventDefault(); // 기본 폼 제출 동작 방지

            // [클라이언트 검증] 비밀번호 일치 여부만 확인 (UX용)
            if (password.value !== confirmPassword.value) {
                alert('비밀번호가 일치하지 않습니다.');
                confirmPassword.focus();
                return;
            }

            // [데이터 가공] DTO 규격에 맞게 수집
            const formData = new FormData(joinForm);

            // UI상의 성(lastName)과 이름(firstName)을 합쳐서 DTO의 userName 필드로 추가
            const lastName = document.getElementById('lastName').value.trim();
            const firstName = document.getElementById('firstName').value.trim();
            formData.append('userName', lastName + firstName);

            // UserController가 @RequestBody를 사용하지 않으므로 Form Data 형식으로 변환
            const params = new URLSearchParams(formData);

            try {
                // 서버로 POST 요청 전송
                const response = await fetch('/users/join', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: params
                });

                if (response.redirected) {
                    alert('가입이 완료되었습니다!');
                    window.location.href = response.url; // 서버가 보낸 /users/login 주소로 이동
                } else {
                    alert('가입 실패: 입력하신 정보를 다시 확인해주세요.\n(비밀번호: 대소문자/숫자 포함 5~15자 필수)');
                }

            } catch (error) {
                console.error('Error:', error);
                alert('서버와 통신 중 오류가 발생했습니다.');
            }
        });
    }
});