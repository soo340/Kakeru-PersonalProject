$(function () {
    $('.mypage-tab-btn').on('click', function () {
        const target = $(this).data('tab');

        $('.mypage-tab-btn').removeClass('active');
        $(this).addClass('active');

        $('.mypage-panel').removeClass('active');
        $('#' + target).addClass('active');
    });
});
