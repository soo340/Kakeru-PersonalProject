<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ja">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>デザインを編集 - KAKERU</title>


<link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/css/common.css">


<link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/css/mystyle.css">


</head>


<body>


<%@ include file="/include/header.jsp" %>


<main class="write-wrap">


    <p class="section-label">EDIT DESIGN</p>
    <h2 class="write-gap-title">デザインを編集</h2>


    <!-- 에러 메시지 -->
    <c:if test="${not empty errMsg}">

        <div class="error-message">
            ${errMsg}
        </div>

    </c:if>


    <form
        name="my"
        id="writeForm"
        class="write-form"
        method="post"
        enctype="multipart/form-data"
        action="${pageContext.request.contextPath}/blog/update">


        <!-- 게시글 번호 -->
        <input
            type="hidden"
            name="bno"
            value="${board.bno}">


        <!-- 이미지 削除 여부 -->
        <input
            type="hidden"
            name="removeImg"
            id="removeImg"
            value="false">


        <!-- 投稿者 -->
        <div class="form-row">

            <label>
                投稿者
            </label>

            <p class="write-author">
                ${board.writer}
            </p>

        </div>


        <!-- 제목 -->
        <div class="form-row">

            <label for="title">
                デザインタイトル
            </label>


            <input
                type="text"
                id="title"
                name="title"
                value="${board.title}"
                placeholder="デザインタイトルを入力してください"
                class="form-input"
                required>

        </div>


        <!-- 대표 이미지 -->
        <div class="form-row">

            <label>

                デザインプレビュー

                <span class="form-option">
                    (800 × 600 推奨)
                </span>

            </label>


            <div class="image-upload-box">


                <c:choose>

    <c:when test="${not empty board.imgPath}">

        <img
            id="imgPreview"
            src="${pageContext.request.contextPath}/imgs/${board.imgPath}"
            alt="画像プレビュー"
            class="img-preview">

    </c:when>

    <c:otherwise>

        <img
            id="imgPreview"
            src="${pageContext.request.contextPath}/images/default-preview.svg"
            alt="画像プレビュー"
            class="img-preview">

    </c:otherwise>

</c:choose>


                <div class="image-upload-btns">


                    <label
                        for="imgFile"
                        class="btn btn-outline file-label">

                        画像を選択

                    </label>


                    <input
                        type="file"
                        id="imgFile"
                        name="imgFile"
                        accept="image/*"
                        class="file-input">


                    <button
                        type="button"
                        id="imgRemoveBtn"
                        class="btn btn-outline">

                        画像を削除

                    </button>


                </div>


            </div>


        </div>


        <!-- 내용 -->
        <div class="form-row">


            <label for="content">
                説明とコード
            </label>


            <textarea
                rows="12"
                id="content"
                name="content"
                placeholder="デザインの説明と使用したHTML・CSSコードを入力してください。"
                class="form-textarea"
                required>${board.content}</textarea>


        </div>


        <!-- 버튼 -->
        <div class="write-btns">


            <a
                href="${pageContext.request.contextPath}/blog/view?bno=${board.bno}"
                class="btn btn-outline">

                キャンセル

            </a>


            <button
                type="submit"
                class="btn btn-primary">

                    変更を保存

            </button>


        </div>


    </form>


</main>


<script
    src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js">
</script>


<script>

$(function() {


    /*
     * 이미지 선택
     */
    $("#imgFile").on(
        "change",
        function(event) {


            const file =
                event.target.files[0];


            if (!file) {
                return;
            }


            /*
             * 새 이미지 선택하면
             * 이미지 削除 상태 해제
             */
            $("#removeImg").val(
                "false"
            );


            const reader =
                new FileReader();


            reader.onload =
                function(e) {

                    $("#imgPreview")
                        .attr(
                            "src",
                            e.target.result
                        );
                };


            reader.readAsDataURL(
                file
            );

        }
    );


    /*
     * 画像を削除 버튼
     */
    $("#imgRemoveBtn").on(
        "click",
        function() {


            /*
             * 선택된 새 파일 제거
             */
            $("#imgFile").val("");


            /*
             * 서버에 이미지 削除 요청
             */
            $("#removeImg").val(
                "true"
            );


            /*
             * 기본 이미지 표시
             */
            $("#imgPreview")
                .attr(
                    "src",
                    "${pageContext.request.contextPath}/images/default-preview.svg"
                );

        }
    );


});

</script>


<%@ include file="/include/footer.jsp" %>


</body>

</html>
