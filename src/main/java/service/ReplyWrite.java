
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
import model.ReplyDTO;

public class ReplyWrite implements Command {

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

        // 2. 댓글 내용 가져오기
        String bnoStr = request.getParameter("bno");
        String content = request.getParameter("content");

        if (content == null || content.trim().isEmpty()) {
            response.setStatus(400);
            result.put("success", false);
            result.put("message", "コメントを入力してください。");
            sendJson(response, result);
            return false;
        }

        content = content.trim();

        if (content.length() > 1000) {
            response.setStatus(400);
            result.put("success", false);
            result.put("message", "コメントは1000文字以内で入力してください。");
            sendJson(response, result);
            return false;
        }

        // 3. 게시글 번호 확인
        int bno;

        try {
            bno = Integer.parseInt(bnoStr);

            if (bno <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            response.setStatus(400);
            result.put("success", false);
            result.put("message", "投稿番号が正しくありません。");
            sendJson(response, result);
            return false;
        }

        // 4. 댓글 DTO 생성
        ReplyDTO dto = new ReplyDTO();

        dto.setBno(bno);
        dto.setWriter(loginMember.getId());
        dto.setContent(content);

        // 5. DB에 댓글 저장
        try {
            ReplyDAO dao = new ReplyDAO();

            int rno = dao.insert(dto);

            System.out.println("댓글 등록 결과 = " + rno);

            if (rno <= 0) {
                response.setStatus(500);
                result.put("success", false);
                result.put("message", "コメントの投稿に失敗しました。");
                sendJson(response, result);
                return false;
            }

            // 6. 성공 JSON 반환
            result.put("success", true);
            result.put("rno", rno);
            result.put("writerNickname",
                    loginMember.getNickname());
            result.put("content", content);

            sendJson(response, result);

        } catch (Exception e) {

            System.err.println("댓글 등록 오류 발생");
            e.printStackTrace();

            response.setStatus(500);

            result.put("success", false);
            result.put("message", "コメントの保存中にサーバーエラーが発生しました。");

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
