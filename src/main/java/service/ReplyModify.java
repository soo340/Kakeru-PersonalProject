
package service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.MemberDTO;
import model.ReplyDAO;

public class ReplyModify implements Command {

    @Override
    public boolean doCommand(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        // 1. 로그인 확인
        HttpSession session = request.getSession(false);

        MemberDTO dto = null;

        if (session != null) {
            dto = (MemberDTO) session.getAttribute("loginmember");
        }

        if (dto == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            result.put("success", false);
            result.put("message", "ログインが必要です。");

            sendJson(response, result);
            return false;
        }

        // 2. 댓글 번호와 내용 가져오기
        String rnoParam = request.getParameter("rno");
        String content = request.getParameter("content");

        if (rnoParam == null || content == null) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            result.put("success", false);
            result.put("message", "コメント情報が正しくありません。");

            sendJson(response, result);
            return false;
        }

        content = content.trim();

        if (content.isEmpty() || content.length() > 1000) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            result.put("success", false);
            result.put("message", "コメントは1〜1000文字で入力してください。");

            sendJson(response, result);
            return false;
        }

        // 3. 댓글 번호 변환
        int rno;

        try {
            rno = Integer.parseInt(rnoParam);

            if (rno <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            result.put("success", false);
            result.put("message", "コメント番号が正しくありません。");

            sendJson(response, result);
            return false;
        }

        // 4. 댓글 수정
        try {

            ReplyDAO dao = new ReplyDAO();

            int updateCount = dao.update(
                    rno,
                    content,
                    dto.getId()
            );

            if (updateCount > 0) {

                result.put("success", true);
                result.put("message", "コメントを更新しました。");

            } else {

                result.put("success", false);
                result.put("message",
                        "編集するコメントがないか、投稿者が一致しません。");
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            result.put("success", false);
            result.put("message", "コメントの編集中にサーバーエラーが発生しました。");
        }

        // 5. JSON 응답 전송
        sendJson(response, result);

        return false;
    }

    // AJAX JSON 응답 메서드
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
