<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>ログイン - KAKERU</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/common.css">

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/mystyle.css">

</head>

<body>

<%@ include file="/include/header.jsp" %>
<main class="login-wrap">

        <h2 class="login-page-title">ログイン</h2>
        <c:if test="${not empty loginFail}">
			<p class = "login-error-msg">${loginFail}</p>
        </c:if>
        
		
        <form id="loginForm" class="login-form"
              action="${pageContext.request.contextPath}/member/login" method="post">

            <div class="form-row">
                <label for="id">ユーザーID</label>
                <input type="text" id="id" name="id" placeholder="ユーザーIDを入力してください" class="form-input">
            </div>

            <div class="form-row">
                <label for="pwd">パスワード</label>
                <input type="password" id="pwd" name="pwd" placeholder="パスワードを入力してください" class="form-input">
            </div>

            <button type="submit" class="btn btn-primary login-submit-btn">ログイン</button>

            <p class="login-footer-link">
                アカウントをお持ちでないですか？
                <a href="${pageContext.request.contextPath}/member/join">新規登録</a>
            </p>

        </form>
        </main>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/login.js"></script>

<%@ include file="/include/footer.jsp" %>
</body>
</html>
