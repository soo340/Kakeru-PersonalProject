<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>デザインを投稿 - KAKERU</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/common.css">

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/mystyle.css">

</head>

<body>

<%@ include file="/include/header.jsp" %>


<main class="write-wrap">

    <p class="section-label">SHARE YOUR DESIGN</p>
    <h2 class="write-gap-title">デザインを投稿</h2>


    <form
        id="writeForm"
        class="write-form"
        method="post"
        enctype="multipart/form-data"
        action="${pageContext.request.contextPath}/blog/write">

        <!-- 작성자 -->
        <div class="form-row">

            <label>投稿者</label>

            <p class="write-author">
                ${sessionScope.loginmember.id}
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
                placeholder="例：CSSで作ったグラデーションボタン"
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

                <img
                    id="imgPreview"
                    src="${pageContext.request.contextPath}/images/default-preview.svg"
                    alt="画像プレビュー"
                    class="img-preview">


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

                        選択を解除

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
                required></textarea>

        </div>


        <div class="write-btns">

            <a
                href="${pageContext.request.contextPath}/blog/list"
                class="btn btn-outline">

                キャンセル

            </a>


            <button
                type="submit"
                class="btn btn-primary">

                投稿する

            </button>

        </div>

    </form>

</main>


<script
    src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js">
</script>

<script
    src="${pageContext.request.contextPath}/js/write.js">
</script>


<%@ include file="/include/footer.jsp" %>

</body>
</html>
