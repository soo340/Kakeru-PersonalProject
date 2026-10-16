package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BoardDAO;
import model.BoardDTO;

public class BoardList implements Command {

    private static final int PAGE_SIZE = 6;
    private static final int PAGE_BLOCK_SIZE = 5;

    @Override
    public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");
        int currentPage = parsePositiveInt(request.getParameter("page"), 1);

        BoardDAO dao = new BoardDAO();
        int totalCount = dao.selectCount(searchType, keyword);
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
        }

        int startRow = (currentPage - 1) * PAGE_SIZE + 1;
        int endRow = currentPage * PAGE_SIZE;
        List<BoardDTO> list = dao.selectList(searchType, keyword, startRow, endRow);

        int startPage = ((currentPage - 1) / PAGE_BLOCK_SIZE) * PAGE_BLOCK_SIZE + 1;
        int endPage = Math.min(startPage + PAGE_BLOCK_SIZE - 1, totalPages);

        request.setAttribute("boardList", list);
        request.setAttribute("searchType", searchType);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        return true;
    }

    private int parsePositiveInt(String value, int defaultValue) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
