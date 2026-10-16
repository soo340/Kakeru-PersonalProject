$(function () {
    $('#loginForm').on('submit', function (e) {
        if ($('#id').val().trim() === '') {
            alert('ユーザーIDを入力してください。');
            e.preventDefault();
            return;
        }
        if ($('#pwd').val().trim() === '') {
            alert('パスワードを入力してください。');
            e.preventDefault();
            return;
        }
        // 여기까지 통과하면 실제 폼이 /member/login 으로 제출됨
    });
});
