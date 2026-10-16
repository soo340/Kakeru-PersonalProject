
package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.BoardDAO;
import model.BoardDTO;

public class PopularCommand implements Command {

    @Override
    public boolean doCommand(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 좋아요 순으로 인기글 조회
        BoardDAO boardDAO = new BoardDAO();

        List<BoardDTO> list =
                boardDAO.selectPopularList();

        // JSP에 인기글 목록 전달
        request.setAttribute("poplist", list);

        System.out.println(
            "인기글 조회 개수 = " + list.size()
        );

        // true를 반환해야 Controller에서 JSP로 이동
        return true;
    }
}
