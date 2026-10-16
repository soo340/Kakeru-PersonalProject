
package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.Command;
import service.ReplyWrite;
import service.ReplyDelete;
import service.ReplyModify;

@WebServlet("/blog/reply/*")
public class ReplyController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        System.out.println(
                "ReplyController pathInfo = " + pathInfo
        );

        Command command;

        if ("/write".equals(pathInfo)) {

            // 댓글 등록
            command = new ReplyWrite();

        } else if ("/delete".equals(pathInfo)) {

            // 댓글 삭제
            command = new ReplyDelete();

        } else if ("/edit".equals(pathInfo)
                || "/modify".equals(pathInfo)) {

            // 댓글 수정
            command = new ReplyModify();

        } else {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );
            return;
        }

        // 서비스 실행
        command.doCommand(request, response);
    }
}
