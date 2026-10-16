
package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.BoardDelete;
import service.BoardList;
import service.BoardModify;
import service.BoardView;
import service.BoardWrite;
import service.Command;
import service.LikeCommand;
import service.PopularCommand;
import service.ReplyWrite;
import service.ReplyDelete;
import service.ReplyModify;

@WebServlet(
    urlPatterns = "/blog/*",
    loadOnStartup = 1
)
@MultipartConfig(
    maxFileSize = 10 * 1024 * 1024,
    maxRequestSize = 20 * 1024 * 1024
)
public class BoardController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public BoardController() {
        super();
    }

    @Override
    public void init() throws ServletException {

        super.init();

        System.out.println(
            "★★★ BoardController 등록 성공 ★★★"
        );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doAction(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doAction(request, response);
    }

    protected void doAction(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        System.out.println(
            "BoardController pathInfo = " + pathInfo
        );

        // 기본 주소 접속 시 목록 이동
        if (pathInfo == null || "/".equals(pathInfo)) {

            response.sendRedirect(
                request.getContextPath() + "/blog/list"
            );

            return;
        }

        String page = null;
        Command command = null;

        switch (pathInfo) {

        // ============================
        // 게시글 목록
        // ============================
        case "/list":

            command = new BoardList();
            page = "/board/list.jsp";

            break;

        // ============================
        // 게시글 작성
        // ============================
        case "/write":

            command = new BoardWrite();
            page = "/board/write.jsp";

            break;

        // ============================
        // 게시글 상세보기
        // ============================
        case "/view":

            command = new BoardView();
            page = "/board/view.jsp";

            break;

        // ============================
        // 게시글 삭제
        // ============================
        case "/delete":

            command = new BoardDelete();

            break;

        // ============================
        // 게시글 수정
        // ============================
        case "/update":

            command = new BoardModify();
            page = "/board/modify.jsp";

            break;

        // ============================
        // 좋아요 등록 및 취소 AJAX
        // ============================
        case "/like":

            if (!"POST".equalsIgnoreCase(request.getMethod())) {

                response.sendError(
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED
                );

                return;
            }

            command = new LikeCommand();

            break;

        // ============================
        // 댓글 등록 AJAX
        // ============================
        case "/reply/write":

            if (!"POST".equalsIgnoreCase(request.getMethod())) {

                response.sendError(
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED
                );

                return;
            }
            
            
            command = new ReplyWrite();

            break;

        // ============================
        // 댓글 삭제 AJAX
        // ============================
        case "/reply/delete":

            if (!"POST".equalsIgnoreCase(request.getMethod())) {

                response.sendError(
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED
                );

                return;
            }

            command = new ReplyDelete();

            break;

        // ============================
        // 댓글 수정 AJAX
        // ============================
        case "/reply/edit":

            if (!"POST".equalsIgnoreCase(request.getMethod())) {

                response.sendError(
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED
                );

                return;
            }

            command = new ReplyModify();

            break;
            
        case "/popular":
        	command = new PopularCommand();
        	page = "/board/popular.jsp";
        	break;
        // ============================
        // 존재하지 않는 주소
        // ============================
        default:

            response.sendError(
                HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }
        

        // ============================
        // Command 실행
        // ============================
        if (command != null) {

            boolean needForward =
                command.doCommand(request, response);

            if (needForward && page != null) {

                request
                    .getRequestDispatcher(page)
                    .forward(request, response);
            }

            return;
        }

        // ============================
        // Command 없이 JSP로 이동
        // ============================
        if (page != null) {

            request
                .getRequestDispatcher(page)
                .forward(request, response);
        }
    }
}
