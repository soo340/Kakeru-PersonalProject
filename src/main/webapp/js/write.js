$(function() {

    // 홈 체험 에디터에서 가져온 코드를 새 글 본문에 한 번만 채웁니다.
    try {
        const homeDraft = sessionStorage.getItem("codekongDraft");
        if (homeDraft && !$("#content").val().trim()) {
            $("#content").val(homeDraft);
            sessionStorage.removeItem("codekongDraft");
        }
    } catch (e) {
        // 저장소를 사용할 수 없어도 일반 글쓰기는 그대로 동작합니다.
    }

    // 기본 이미지 경로 저장
    const defaultImg = $("#imgPreview").attr("src");


    // =========================
    // 이미지 선택
    // =========================
    $("#imgFile").on("change", function() {

        const file = this.files[0];

        if (!file) {
            return;
        }

        // 선택한 이미지 임시 URL 생성
        const previewUrl = URL.createObjectURL(file);

        console.log(previewUrl);

        // 미리보기 이미지 변경
        $("#imgPreview").attr("src", previewUrl);

    });


    // =========================
    // 画像を削除
    // =========================
    $("#imgRemoveBtn").on("click", function() {

        $("#imgFile").val("");

        $("#imgPreview").attr("src", defaultImg);

    });


    // =========================
    // 글쓰기 폼 검사
    // =========================
    $("#writeForm").on("submit", function(e) {

        const title = $("#title").val().trim();
        const content = $("#content").val().trim();


        // 제목 검사
        if (title === "") {

            alert("タイトルを入力してください！");

            e.preventDefault();

            $("#title").focus();

            return;
        }


        // 내용 검사
        if (content === "") {

            alert("内容を入力してください！");

            e.preventDefault();

            $("#content").focus();

            return;
        }

    });

});
