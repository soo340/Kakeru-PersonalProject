
package service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.LikeDAO;
import model.MemberDTO;

public class LikeCommand implements Command {

    @Override
    public boolean doCommand(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        // ============================
        // 1. 로그인 확인
        // ============================
        HttpSession session = request.getSession(false);

        MemberDTO dto = null;

        if (session != null) {
            dto = (MemberDTO) session.getAttribute("loginmember");
        }

        if (dto == null) {

            response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
            );

            result.put("success", false);
            result.put("message", "ログインが必要です。");

            sendJson(response, result);
            return false;
        }

        // ============================
        // 2. POST 요청 확인
        // ============================
        if (!"POST".equalsIgnoreCase(request.getMethod())) {

            response.setStatus(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED
            );

            result.put("success", false);
            result.put("message", "許可されていないリクエストです。");

            sendJson(response, result);
            return false;
        }

        // ============================
        // 3. 게시글 번호 확인
        // ============================
        String bnoParam = request.getParameter("bno");

        int bno;

        try {

            bno = Integer.parseInt(bnoParam);

            if (bno <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            response.setStatus(
                HttpServletResponse.SC_BAD_REQUEST
            );

            result.put("success", false);
            result.put("message", "投稿番号が正しくありません。");

            sendJson(response, result);
            return false;
        }

        // ============================
        // 4. 좋아요 토글
        // ============================
        try {

            LikeDAO dao = new LikeDAO();

            // 좋아요 추가 또는 취소
            dao.toggle(bno, dto.getId());

            // 변경된 좋아요 상태
            boolean liked = dao.isLiked(
                bno,
                dto.getId()
            );

            // 변경된 좋아요 개수
            int likeCount = dao.selectLikeCnt(bno);

            result.put("success", true);
            result.put("liked", liked);
            result.put("likeCount", likeCount);

            result.put(
                "message",
                liked ? "「いいね」しました。" : "「いいね」を取り消しました。"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            result.put("success", false);
            result.put("message", "「いいね」の処理中にエラーが発生しました。");
        }

        // ============================
        // 5. JSON 응답
        // ============================
        sendJson(response, result);

        return false;
    }

    // ============================
    // AJAX JSON 응답 메서드
    // ============================
    private void sendJson(
            HttpServletResponse response,
            Map<String, Object> result)
            throws IOException {

        response.setContentType(
            "application/json; charset=UTF-8"
        );

        Gson gson = new Gson();

        response.getWriter().write(
            gson.toJson(result)
        );
    }
}
