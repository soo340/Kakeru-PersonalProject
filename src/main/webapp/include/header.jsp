<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<header class="site-header"><div class="header-inner">
    <h1 class="site-logo"><a href="${pageContext.request.contextPath}/" aria-label="KAKERU ホーム"><img src="${pageContext.request.contextPath}/images/kakeru-logo.svg" alt="KAKERU"></a></h1>
    <nav class="gnb" aria-label="メインメニュー"><ul><li><a href="${pageContext.request.contextPath}/blog/list">デザイン一覧</a></li><li><a href="${pageContext.request.contextPath}/blog/popular">人気デザイン</a></li></ul></nav>
    <div class="member-menu"><c:choose><c:when test="${empty sessionScope.loginmember}"><a href="${pageContext.request.contextPath}/member/login">ログイン</a><a href="${pageContext.request.contextPath}/member/join">新規登録</a></c:when><c:otherwise><span class="member-name"><c:out value="${sessionScope.loginmember.nickname}"/>さん</span><a href="${pageContext.request.contextPath}/member/mypage">マイページ</a><a href="${pageContext.request.contextPath}/member/logout">ログアウト</a><a class="header-write-btn" href="${pageContext.request.contextPath}/blog/write">デザインを投稿</a></c:otherwise></c:choose></div>
</div></header>
