package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.BoardDAO;
import model.BoardDTO;
import model.ReplyDAO;
import model.ReplyDTO;

public class BoardView implements Command {

    @Override
    public boolean doCommand(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. 게시글 번호 확인
        int bno;

        try {
            bno = Integer.parseInt(request.getParameter("bno"));

            if (bno <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "投稿番号が正しくありません。"
            );
            return false;
        }

        // 2. 게시글 조회
        BoardDAO boardDAO = new BoardDAO();
        
        boardDAO.increViewCnt(bno);

        BoardDTO dto = boardDAO.selectOne(bno);

        if (dto == null) {
            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "投稿が見つかりません。"
            );
            return false;
        }

        request.setAttribute("view", dto);

        // 3. 댓글 목록 조회
        ReplyDAO replyDAO = new ReplyDAO();

        List<ReplyDTO> replyList = replyDAO.list(bno);

        // 4. JSP에 댓글 목록 전달
        request.setAttribute("replyList", replyList);

        System.out.println(
            "게시글 번호 = " + bno
            + ", 댓글 개수 = " + replyList.size()
        );

        return true;
    }
}