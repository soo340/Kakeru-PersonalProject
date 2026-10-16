
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title><c:out value="${view.title}"/> - KAKERU</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/mystyle.css" rel="stylesheet">

<style>
.like-btn.liked {
    color: #ff5c7a !important;
    border-color: #ff5c7a !important;
}
.reply-wrap { padding: 24px 28px; }
.reply-title { margin: 0 0 16px; }
.reply-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
}
.reply-list .reply-item {
    min-height: 0;
    height: auto;
    padding: 12px 16px;
    margin: 0;
    border: 1px solid rgba(255,255,255,.12);
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    gap: 6px;
}
.reply-info {
    display: flex;
    align-items: center;
    gap: 10px;
    margin: 0;
    font-size: 13px;
}
.reply-content {
    min-height: 0;
    height: auto;
    padding: 0;
    margin: 0;
    line-height: 1.5;
    white-space: pre-wrap;
    overflow-wrap: anywhere;
}
.reply-btns {
    display: flex;
    gap: 6px;
    margin-top: 4px;
    padding: 0;
}
.reply-btns .btn {
    min-height: 30px;
    padding: 5px 12px;
    font-size: 12px;
}
.reply-form {
    display: flex;
    gap: 10px;
    margin-top: 12px;
    align-items: flex-start;
}
.reply-input {
    flex: 1;
    min-height: 65px;
    padding: 12px;
    border: 1px solid #ccc;
    border-radius: 8px;
    resize: vertical;
    font: inherit;
    box-sizing: border-box;
}
.reply-submit {
    min-width: 90px;
    height: 45px;
    cursor: pointer;
}
.reply-submit:disabled,
.reply-delete-btn:disabled,
.reply-edit-save:disabled {
    opacity: .5;
    cursor: not-allowed;
}
.reply-message {
    margin-top: 10px;
    font-size: 14px;
}
.reply-write-wrap { margin-top: 20px; }
.reply-delete-form { display: inline; }
</style>
</head>

<body>

<%@ include file="/include/header.jsp" %>

<main class="view-wrap">

    <!-- 게시글 정보 -->
    <div class="view-header">

        <h1 class="view-title">
            <c:out value="${view.title}"/>
        </h1>

        <div class="view-meta">
            投稿者
            <c:out value="${view.writerNickname}"/> |

            <c:out value="${view.regDate}"/> |

            閲覧数
            <c:out value="${view.viewCnt}"/> |

            いいね
            <span id="metaLikeCount">
                <c:out value="${view.likeCnt}"/>
            </span>
        </div>
    </div>

    <!-- 이미지 -->
    <c:if test="${not empty view.imgPath}">
        <div class="view-img">
            <img
                src="${pageContext.request.contextPath}/imgs/${view.imgPath}"
                alt="投稿画像">
        </div>
    </c:if>

    <!-- 게시글 본문 -->
    <div class="view-content">
        <c:out value="${view.content}"/>
    </div>

    <!-- 게시글 버튼 -->
    <div class="view-actions">

        <button
            type="button"
            id="likeBtn"
            class="btn btn-outline like-btn ${liked ? 'liked' : ''}"
            data-bno="${view.bno}"
            aria-pressed="${liked ? 'true' : 'false'}">
            ♥ このデザインが好き
            <span id="likeCount">
                <c:out value="${view.likeCnt}"/>
            </span>
        </button>

        <div class="view-actions-right">

            <a
                href="${pageContext.request.contextPath}/blog/list"
                class="btn btn-outline">一覧</a>

            <c:if test="${not empty sessionScope.loginmember and sessionScope.loginmember.id eq view.writer}">

                <a
                    href="${pageContext.request.contextPath}/blog/update?bno=${view.bno}"
                    class="btn btn-primary">編集</a>

                <form
                    action="${pageContext.request.contextPath}/blog/delete"
                    method="post"
                    style="display:inline"
                    onsubmit="return confirm('この投稿を削除しますか？');">

                    <input type="hidden" name="bno" value="${view.bno}">

                    <button type="submit" class="btn btn-outline">
                        削除
                    </button>
                </form>

            </c:if>
        </div>
    </div>

    <!-- 댓글 영역 -->
    <section class="reply-wrap">

        <h2 class="reply-title">
            デザインへのフィードバック
            <span id="replyCount">
                <c:out value="${empty replyList ? 0 : replyList.size()}"/>
            </span>
        </h2>

        <div class="reply-list" id="replyList">

            <c:choose>

                <c:when test="${not empty replyList}">

                    <c:forEach var="r" items="${replyList}">

                        <div class="reply-item" data-rno="${r.rno}">

                            <div class="reply-info">
                                <span class="reply-writer">
                                    <c:out value="${r.writerNickname}"/>
                                </span>
                                <span class="reply-date">
                                    <c:out value="${r.regDate}"/>
                                </span>
                            </div>

                            <div class="reply-content">
                                <c:out value="${r.content}"/>
                            </div>

                            <c:if test="${not empty sessionScope.loginmember and sessionScope.loginmember.id eq r.writer}">

                                <div class="reply-btns">

                                    <button
                                        type="button"
                                        class="btn btn-outline reply-edit-btn"
                                        data-bs-toggle="modal"
                                        data-bs-target="#replyModal">
                                        編集
                                    </button>

                                    <form
                                        class="reply-delete-form"
                                        action="${pageContext.request.contextPath}/blog/reply/delete"
                                        method="post">

                                        <input type="hidden"
                                               name="rno"
                                               value="${r.rno}">

                                        <input type="hidden"
                                               name="bno"
                                               value="${view.bno}">

                                        <button
                                            type="submit"
                                            class="btn btn-outline reply-delete-btn">
                                            削除
                                        </button>
                                    </form>

                                </div>

                            </c:if>

                        </div>

                    </c:forEach>

                </c:when>

                <c:otherwise>
                    <div class="reply-item">
                        まだフィードバックはありません。
                    </div>
                </c:otherwise>

            </c:choose>

        </div>

        <!-- 댓글 작성 -->
        <div class="reply-write-wrap">

            <h3>フィードバックを投稿</h3>

            <c:choose>

                <c:when test="${not empty sessionScope.loginmember}">

                    <form id="replyForm" class="reply-form">

                        <input type="hidden"
                               id="replyBno"
                               name="bno"
                               value="${view.bno}">

                        <textarea
                            id="replyContent"
                            name="content"
                            class="reply-input"
                            placeholder="デザインへの感想や改善アイデアを入力してください。"
                            maxlength="1000"
                            rows="3"
                            required></textarea>

                        <button
                            type="submit"
                            id="replySubmit"
                            class="btn btn-primary reply-submit">
                            フィードバックを送信
                        </button>

                    </form>

                    <div
                        id="replyMessage"
                        class="reply-message"
                        role="status"
                        aria-live="polite"></div>

                </c:when>

                <c:otherwise>
                    <div class="reply-item">
                        フィードバックを投稿するにはログインが必要です。
                        <a href="${pageContext.request.contextPath}/member/login">
                            ログイン
                        </a>
                    </div>
                </c:otherwise>

            </c:choose>

        </div>

    </section>

</main>

<!-- 댓글 수정 모달 -->
<div class="modal fade"
     id="replyModal"
     tabindex="-1"
     aria-labelledby="replyModalLabel"
     aria-hidden="true">

    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <h1 class="modal-title fs-5" id="replyModalLabel">
                    フィードバックを編集
                </h1>

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="閉じる"></button>
            </div>

            <div class="modal-body">

                <input type="hidden" id="modalRno">

                <textarea id="modalContent"
                          class="form-control"
                          rows="4"
                          maxlength="1000"
                          aria-label="編集するコメント"></textarea>

                <div id="modalMessage"
                     class="text-danger mt-2"
                     role="alert"></div>

            </div>

            <div class="modal-footer">

                <button type="button"
                        class="btn btn-secondary"
                        data-bs-dismiss="modal">
                    キャンセル
                </button>

                <button type="button"
                        id="replyEditSave"
                        class="btn btn-primary reply-edit-save">
                    変更を保存
                </button>

            </div>

        </div>
    </div>
</div>

<%@ include file="/include/footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

<script>
document.addEventListener("DOMContentLoaded", function () {

    const contextPath = "${pageContext.request.contextPath}";
    const currentBno = "${view.bno}";

    // いいね
    const likeBtn = document.getElementById("likeBtn");
    const likeCount = document.getElementById("likeCount");
    const metaLikeCount = document.getElementById("metaLikeCount");

    // 댓글
    const replyList = document.getElementById("replyList");
    const replyCount = document.getElementById("replyCount");
    const replyForm = document.getElementById("replyForm");
    const replyContent = document.getElementById("replyContent");
    const replyBno = document.getElementById("replyBno");
    const replySubmit = document.getElementById("replySubmit");
    const replyMessage = document.getElementById("replyMessage");

    // 모달
    const modalElement = document.getElementById("replyModal");
    const modalRno = document.getElementById("modalRno");
    const modalContent = document.getElementById("modalContent");
    const modalMessage = document.getElementById("modalMessage");
    const replyEditSave = document.getElementById("replyEditSave");

    let liking = false;
    let submitting = false;
    let editing = false;

    // JSON 응답 처리
    async function readJsonResponse(response) {

        const contentType = response.headers.get("content-type") || "";

        if (!contentType.includes("application/json")) {
            throw new Error(
                "サーバーから正しい応答が返されませんでした。"
            );
        }

        const result = await response.json();

        if (!response.ok || result.success !== true) {
            throw new Error(
                result.message || "リクエストの処理に失敗しました。"
            );
        }

        return result;
    }

    // POST 요청
    function formParams(values) {

        const params = new URLSearchParams();

        for (const [key, value] of Object.entries(values)) {
            params.append(key, value);
        }

        return params.toString();
    }

    async function postForm(path, values) {

        const response = await fetch(contextPath + path, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
            },
            credentials: "same-origin",
            body: formParams(values)
        });

        return readJsonResponse(response);
    }

    // いいね 클릭
    if (likeBtn) {

        likeBtn.addEventListener("click", async function () {

            if (liking) return;

            const bno = likeBtn.dataset.bno;

            if (!bno) {
                alert("投稿番号がありません。");
                return;
            }

            liking = true;
            likeBtn.disabled = true;

            try {

                const result = await postForm("/blog/like", {bno});

                const count = Number(result.likeCount);

                if (!Number.isFinite(count) ||
                    typeof result.liked !== "boolean") {

                    throw new Error(
                        "「いいね」の応答データが正しくありません。"
                    );
                }

                likeCount.textContent = String(count);

                if (metaLikeCount) {
                    metaLikeCount.textContent = String(count);
                }

                likeBtn.classList.toggle("liked", result.liked);

                likeBtn.setAttribute(
                    "aria-pressed",
                    String(result.liked)
                );

            } catch (error) {

                console.error("「いいね」エラー:", error);
                alert(error.message);

            } finally {

                liking = false;
                likeBtn.disabled = false;
            }
        });
    }

    // 댓글 목록 갱신
    async function refreshReplyList() {

        const response = await fetch(
            contextPath + "/blog/view?bno=" +
            encodeURIComponent(currentBno),
            {
                method: "GET",
                credentials: "same-origin",
                cache: "no-store"
            }
        );

        if (!response.ok) {
            throw new Error("コメント一覧を読み込めませんでした。");
        }

        const html = await response.text();

        const doc = new DOMParser().parseFromString(
            html,
            "text/html"
        );

        const newReplyList = doc.getElementById("replyList");
        const newReplyCount = doc.getElementById("replyCount");

        if (!newReplyList || !newReplyCount) {
            throw new Error("コメント一覧の更新に失敗しました。");
        }

        replyList.replaceChildren(
            ...Array.from(newReplyList.childNodes)
                .map(node => document.importNode(node, true))
        );

        replyCount.textContent =
            newReplyCount.textContent.trim();
    }

    // 댓글 등록
    if (replyForm) {

        replyForm.addEventListener("submit", async function (event) {

            event.preventDefault();

            if (submitting) return;

            const content = replyContent.value.trim();

            if (!content || content.length > 1000) {
                alert("コメントは1〜1000文字で入力してください。");
                replyContent.focus();
                return;
            }

            submitting = true;
            replySubmit.disabled = true;
            replySubmit.textContent = "送信中...";
            replyMessage.textContent = "";

            let saved = false;

            try {

                await postForm("/blog/reply/write", {
                    bno: replyBno.value,
                    content
                });

                saved = true;

                await refreshReplyList();

                replyContent.value = "";
                replyMessage.textContent = "コメントを投稿しました。";

            } catch (error) {

                const message = saved
                    ? "コメントは投稿されましたが、一覧を更新できませんでした。"
                    : error.message;

                replyMessage.textContent = message;
                alert(message);

            } finally {

                submitting = false;
                replySubmit.disabled = false;
                replySubmit.textContent = "送信";
            }
        });
    }

    // 댓글 수정 모달
    modalElement.addEventListener("show.bs.modal", function (event) {

        const button = event.relatedTarget;
        const item = button && button.closest(".reply-item");
        const contentElement =
            item && item.querySelector(".reply-content");

        if (!item || !item.dataset.rno || !contentElement) {

            event.preventDefault();
            console.error("コメント情報が見つかりません。");
            return;
        }

        modalRno.value = item.dataset.rno;
        modalContent.value = contentElement.textContent;
        modalMessage.textContent = "";
    });

    modalElement.addEventListener("shown.bs.modal", function () {
        modalContent.focus();
    });

    // 댓글 変更を保存
    replyEditSave.addEventListener("click", async function () {

        if (editing) return;

        const rno = modalRno.value;
        const content = modalContent.value.trim();

        if (!rno || !content || content.length > 1000) {

            modalMessage.textContent = !rno
                ? "コメント番号がありません。"
                : "コメントは1〜1000文字で入力してください。";

            modalContent.focus();
            return;
        }

        editing = true;
        replyEditSave.disabled = true;
        replyEditSave.textContent = "保存中...";
        modalMessage.textContent = "";

        let saved = false;

        try {

            await postForm("/blog/reply/edit", {
                rno,
                bno: currentBno,
                content
            });

            saved = true;

            await refreshReplyList();

            bootstrap.Modal.getOrCreateInstance(modalElement).hide();

            modalRno.value = "";
            modalContent.value = "";

            if (replyMessage) {
                replyMessage.textContent = "コメントを更新しました。";
            }

        } catch (error) {

            modalMessage.textContent = saved
                ? "コメントは更新されましたが、一覧を更新できませんでした。"
                : error.message;

            console.error("コメント編集エラー:", error);

        } finally {

            editing = false;
            replyEditSave.disabled = false;
            replyEditSave.textContent = "変更を保存";
        }
    });

    // 댓글 削除
    replyList.addEventListener("submit", async function (event) {

        const form = event.target.closest(".reply-delete-form");

        if (!form) return;

        event.preventDefault();

        if (!confirm("コメントを削除しますか？")) return;

        const button = form.querySelector(".reply-delete-btn");

        if (button.disabled) return;

        const rno = form.elements["rno"].value;

        button.disabled = true;
        button.textContent = "削除中...";

        let deleted = false;

        try {

            await postForm("/blog/reply/delete", {
                rno,
                bno: currentBno
            });

            deleted = true;

            await refreshReplyList();

            if (replyMessage) {
                replyMessage.textContent = "コメントを削除しました。";
            }

        } catch (error) {

            const message = deleted
                ? "コメントは削除されましたが、一覧を更新できませんでした。"
                : error.message;

            if (replyMessage) {
                replyMessage.textContent = message;
            }

            alert(message);

        } finally {

            if (button.isConnected) {
                button.disabled = false;
                button.textContent = "削除";
            }
        }
    });

});
</script>

</body>
</html>
