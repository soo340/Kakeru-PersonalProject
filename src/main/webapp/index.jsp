<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>KAKERU - コードで描く、アイデアを残す。</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css?v=20261016-2">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mystyle.css?v=20261016-2">
</head>
<body>
<%@ include file="/include/header.jsp" %>

<main class="home-main">
    <section class="home-intro">
        <div class="home-intro-copy">
            <img class="home-brand-logo" src="${pageContext.request.contextPath}/images/kakeru-logo.svg" alt="KAKERU - CODE × DESIGN">
            <span class="home-eyebrow"><i></i> CODE TO DESIGN</span>
            <div class="kakeru-expression" aria-label="書ける、掛ける、翔ける">
                <span class="kanji-layer" aria-hidden="true">
                    <i><b>書</b><small>WRITE</small></i>
                    <i><b>掛</b><small>CONNECT</small></i>
                    <i><b>翔</b><small>SOAR</small></i>
                </span>
                <span class="keru-plus" aria-hidden="true">＋</span><b class="keru-text">ける</b>
            </div>
            <h2 class="home-tagline">小さなコードで、<em>未来を描く。</em></h2>
            <p>HTMLとCSSで作った画面をすぐにプレビューし、作品として記録・共有できるデザインアーカイブです。</p>
            <div class="home-intro-actions">
                <a class="btn btn-primary" href="#playground">今すぐ試す</a>
                <a class="btn btn-quiet" href="${pageContext.request.contextPath}/blog/list">デザインを見る <span>→</span></a>
            </div>
        </div>
        <div class="home-visual" aria-hidden="true">
            <div class="home-orbit orbit-one"></div><div class="home-orbit orbit-two"></div>
            <div class="sprout-card"><span class="sprout-code">{ }</span><span class="sprout-leaf leaf-left"></span><span class="sprout-leaf leaf-right"></span><b>code grows here</b><small>HTML · CSS · IDEA</small></div>
            <span class="float-tag tag-html">&lt;html&gt;</span><span class="float-tag tag-css">#style</span><span class="float-tag tag-share">share ↗</span>
        </div>
    </section>

    <section class="home-playground" id="playground">
        <div class="home-section-copy"><span class="home-step-label">TRY IT NOW</span><h2>コードを変えると、デザインもすぐ変わる</h2><p>左側のコードを編集してみてください。結果が右側にリアルタイムで表示されます。</p></div>
        <div class="playground-shell">
            <div class="playground-pane code-pane">
                <div class="pane-top"><span><i></i><i></i><i></i></span><b>design.html</b><small>LIVE</small></div>
                <textarea id="homeCode" spellcheck="false" aria-label="HTMLとCSSの体験コード"><!doctype html>
<html>
<head>
<style>
body {
  min-height: 100vh;
  margin: 0;
  display: grid;
  place-items: center;
  font-family: Arial;
  background: #eeeaff;
}
.hello-card {
  padding: 42px;
  text-align: center;
  background: white;
  border-radius: 24px;
  box-shadow: 0 20px 50px #6c5ce733;
}
.hello-card span { font-size: 38px; }
.hello-card h1 { margin: 10px 0 4px; }
.hello-card p { margin: 0; color: #80799b; }
</style>
</head>
<body>
  <div class="hello-card">
    <span>🌱</span>
    <h1>Hello, Code!</h1>
    <p>小さなコードがデザインになりました。</p>
  </div>
</body>
</html></textarea>
            </div>
            <div class="playground-arrow" aria-hidden="true">→</div>
            <div class="playground-pane result-pane">
                <div class="pane-top"><span><i></i><i></i><i></i></span><b>プレビュー</b><small>● LIVE</small></div>
                <iframe id="homePreview" sandbox title="コード実行結果"></iframe>
            </div>
        </div>
        <div class="playground-action"><p><span>気に入りましたか？</span> このコードをもとに、自分だけのデザインを作ってみましょう。</p><a id="useThisCode" class="btn btn-primary" href="${pageContext.request.contextPath}/blog/write">このデザインで投稿</a></div>
    </section>

    <section class="home-how">
        <div class="home-section-copy"><span class="home-step-label">HOW IT WORKS</span><h2>3ステップで簡単に記録</h2></div>
        <ol class="how-list"><li><span>01</span><div class="how-icon">⌨</div><h3>コードを書く</h3><p>作成したHTMLとCSSを本文に貼り付けます。</p></li><li><span>02</span><div class="how-icon">◫</div><h3>仕上がりを確認</h3><p>実際の表示が自動でサムネイルになります。</p></li><li><span>03</span><div class="how-icon">↗</div><h3>みんなに共有</h3><p>デザインを共有して、新しいアイデアに出会えます。</p></li></ol>
    </section>

    <section class="home-note"><div><span>FROM A TINY SEED</span><h2>今日作った小さな画面も<br>大切な作品になります。</h2><p>ボタンひとつ、カードひとつから気軽に残してみましょう。</p></div><a class="note-link" href="${pageContext.request.contextPath}/blog/write"><b>デザインを記録する</b><span>→</span></a></section>
</main>

<%@ include file="/include/footer.jsp" %>
<script src="${pageContext.request.contextPath}/js/home.js?v=20261016"></script>
</body></html>
