<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>新規登録 - KAKERU</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/common.css">

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/mystyle.css">

</head>

<body>

<%@ include file="/include/header.jsp" %>


<main class="join-wrap">

    <h2 class="write-gap-title">新規登録</h2>

    <form id="joinForm"
          class="join-form"
          action="${pageContext.request.contextPath}/member/join"
          method="post">

        <!-- ユーザーID -->
        <div class="form-row">

            <label for="id">ユーザーID</label>

            <div class="id-check-group">

                <input type="text"
                       id="id"
                       name="id"
                       placeholder="半角英数字4〜12文字"
                       class="form-input">

                <button type="button"
                        id="idCheckBtn"
                        class="btn btn-outline id-check-btn">
                    重複確認
                </button>

            </div>

            <p id="idCheckMsg"
               class="field-msg"></p>

        </div>


        <!-- パスワード -->
        <div class="form-row">

            <label for="pwd">パスワード</label>

            <input type="password"
                   id="pwd"
                   name="pwd"
                   placeholder="パスワードを入力してください"
                   class="form-input">

        </div>


        <!-- パスワード（確認） -->
        <div class="form-row">

            <label for="pwdCheck">パスワード（確認）</label>

            <input type="password"
                   id="pwdCheck"
                   placeholder="パスワードをもう一度入力してください"
                   class="form-input">

            <p id="pwdCheckMsg"
               class="field-msg"></p>

        </div>


        <!-- ニックネーム -->
        <div class="form-row">

            <label for="nickname">ニックネーム</label>

            <input type="text"
                   id="nickname"
                   name="nickname"
                   placeholder="ニックネームを入力してください"
                   class="form-input">

        </div>


        <!-- メールアドレス -->
        <div class="form-row">

            <label for="email">メールアドレス</label>

            <input type="email"
                   id="email"
                   name="email"
                   placeholder="example@email.com"
                   class="form-input">

        </div>


        <!-- 버튼 -->
        <div class="join-btns">

            <a href="${pageContext.request.contextPath}/"
               class="btn btn-outline">
                キャンセル
            </a>

            <button type="submit"
                    class="btn btn-primary">
                登録する
            </button>

        </div>

    </form>

</main>


<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>


<script>

$(function(){

    let idChecked = false;


    // =========================
    // ユーザーID 重複確認
    // =========================
    $("#idCheckBtn").on("click", function(){

        const id = $("#id").val().trim();

        if(id === '')
        {
            alert("ユーザーIDを入力してください。");
            $("#id").focus();
            return;
        }


        $.ajax({

            url: '${pageContext.request.contextPath}/member/idCheck',

            type: 'get',

            data: {
                id: id
            },

            dataType: 'json',

            success: function(result)
            {
                console.log("重複確認 결과:", result);

                if(result.isDuplicate)
                {
                    $("#idCheckMsg")
                        .text("このユーザーIDは既に使用されています。")
                        .css("color", "#ff3b5c");

                    idChecked = false;
                }
                else
                {
                    $("#idCheckMsg")
                        .text("使用できるユーザーIDです。")
                        .css("color", "#3b6ff2");

                    idChecked = true;
                }
            },

            error: function(xhr, status, error)
            {
                console.log("HTTPステータス:", xhr.status);
                console.log("status:", status);
                console.log("error:", error);
                console.log("応答:", xhr.responseText);

                alert(
                    "通信エラーが発生しました！\n" +
                    "HTTPステータス: " + xhr.status + "\n" +
                    "status: " + status + "\n" +
                    "error: " + error + "\n" +
                    "応答: " + xhr.responseText
                );
            }

        });

    });



    // =========================
    // ユーザーID 수정하면
    // 다시 重複確認 필요
    // =========================
    $("#id").on("input", function(){

        idChecked = false;

        $("#idCheckMsg").text("");

    });



    // =========================
    // パスワード（確認）
    // =========================
    $("#pwdCheck, #pwd").on("input", function(){

        const pwd = $("#pwd").val();
        const pwdCheck = $("#pwdCheck").val();


        if(pwdCheck === '')
        {
            $("#pwdCheckMsg").text("");
        }
        else if(pwd === pwdCheck)
        {
            $("#pwdCheckMsg")
                .text("パスワードが一致しました。")
                .css("color", "#3b6ff2");
        }
        else
        {
            $("#pwdCheckMsg")
                .text("パスワードが一致しません。")
                .css("color", "#ff3b5c");
        }

    });



    // =========================
    // 회원가입 최종 확인
    // =========================
    $("#joinForm").on("submit", function(e){

        const id = $("#id").val().trim();
        const pwd = $("#pwd").val();
        const pwdCheck = $("#pwdCheck").val();
        const nickname = $("#nickname").val().trim();
        const email = $("#email").val().trim();


        if(id === '')
        {
            alert("ユーザーIDを入力してください。");
            $("#id").focus();

            e.preventDefault();
            return;
        }


        if(!idChecked)
        {
            alert("ユーザーIDの重複確認をしてください。");

            e.preventDefault();
            return;
        }


        if(pwd === '')
        {
            alert("パスワードを入力してください。");
            $("#pwd").focus();

            e.preventDefault();
            return;
        }


        if(pwd !== pwdCheck)
        {
            alert("パスワードが一致しません。");
            $("#pwdCheck").focus();

            e.preventDefault();
            return;
        }


        if(nickname === '')
        {
            alert("ニックネームを入力してください。");
            $("#nickname").focus();

            e.preventDefault();
            return;
        }


        if(email === '')
        {
            alert("メールアドレスを入力してください。");
            $("#email").focus();

            e.preventDefault();
            return;
        }

    });

});

</script>


<%@ include file="/include/footer.jsp" %>

</body>

</html>
