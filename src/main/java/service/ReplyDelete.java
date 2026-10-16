
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

public class ReplyDelete implements Command {

    @Override
    public boolean doCommand(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        // 1. 로그인 확인
        HttpSession session = request.getSession(false);

        MemberDTO loginMember = null;

        if (session != null) {
            loginMember = (MemberDTO)
                    session.getAttribute("loginmember");
        }

        if (loginMember == null) {

            response.setStatus(401);

            result.put("success", false);
            result.put("message", "ログインが必要です。");

            sendJson(response, result);
            return false;
        }

        // 2. 댓글 번호 확인
        String rnoStr = request.getParameter("rno");

        int rno;

        try {

            rno = Integer.parseInt(rnoStr);

            if (rno <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            response.setStatus(400);

            result.put("success", false);
            result.put("message", "コメント番号が正しくありません。");

            sendJson(response, result);
            return false;
        }

        // 3. 댓글 삭제
        try {

            ReplyDAO dao = new ReplyDAO();

            int cnt = dao.delete(
                    rno,
                    loginMember.getId()
            );

            System.out.println(
                    "댓글 삭제 결과 = " + cnt);

            // 4. 삭제 성공
            if (cnt > 0) {

                result.put("success", true);
                result.put("message", "コメントを削除しました。");

            } else {

                result.put("success", false);
                result.put(
                        "message",
                        "自分のコメントのみ削除できます。既に削除されている可能性があります。"
                );
            }

            sendJson(response, result);

        } catch (Exception e) {

            System.err.println("댓글 삭제 오류 발생");
            e.printStackTrace();

            response.setStatus(500);

            result.put("success", false);
            result.put(
                    "message",
                    "コメントの削除中にサーバーエラーが発生しました。"
            );

            sendJson(response, result);
        }

        return false;
    }

    // AJAX JSON 응답
    private void sendJson(
            HttpServletResponse response,
            Map<String, Object> result)
            throws IOException {

        response.setContentType(
                "application/json; charset=UTF-8");

        Gson gson = new Gson();

        response.getWriter().write(
                gson.toJson(result)
        );
    }
}
