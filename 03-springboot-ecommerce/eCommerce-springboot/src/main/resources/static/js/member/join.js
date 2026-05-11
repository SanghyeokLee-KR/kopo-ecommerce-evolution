document.addEventListener('DOMContentLoaded', function () {
    const joinForm = document.getElementById('joinForm');

    if (!joinForm) {
        return;
    }

    joinForm.addEventListener('submit', function (event) {
        const userId = document.getElementById('userId').value.trim();
        const userName = document.getElementById('userName').value.trim();
        const userPassword = document.getElementById('userPassword').value.trim();
        const userPhone = document.getElementById('userPhone').value.trim();
        const userEmail = document.getElementById('userEmail').value.trim();

        if (!userId || !userName || !userPassword || !userPhone || !userEmail) {
            alert('모든 항목을 입력해주세요.');
            event.preventDefault();
            return;
        }

        if (userId.length < 5 || userId.length > 15) {
            alert('아이디는 5~15자리로 입력해주세요.');
            event.preventDefault();
            return;
        }

        const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{5,15}$/;
        if (!passwordPattern.test(userPassword)) {
            alert('비밀번호는 대문자, 소문자, 숫자를 포함한 5~15자리여야 합니다.');
            event.preventDefault();
        }
    });
});