package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.Command;
import service.IdCheckCommand;
import service.LoginCommand;
import service.LogoutCommand;
import service.MemberInsert;
import service.MypageCommand;


@WebServlet("/member/*")
public class Membercontroller extends HttpServlet {

    private static final long serialVersionUID = 1L;


    public Membercontroller() {
        super();
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


        String pathInfo =
                request.getPathInfo();


        String page = null;

        Command command = null;


        if (pathInfo == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }


        switch (pathInfo) {


        case "/join":

            command =
                    new MemberInsert();

            page =
                    "/mem/join.jsp";

            break;


        case "/idCheck":

            command =
                    new IdCheckCommand();

            break;


        case "/login":

            command =
                    new LoginCommand();

            page =
                    "/mem/login.jsp";

            break;


        case "/logout":

            command =
                    new LogoutCommand();

            break;
            
        case "/mypage":

        	command = 
        			new MypageCommand();

        	page=
        		"/mem/mypage.jsp";
        	
        	break;
        	
        default:

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }


        boolean needForward =
                command.doCommand(
                        request,
                        response
                );


        if (needForward
                && page != null) {

            RequestDispatcher rd =
                    request.getRequestDispatcher(
                            page
                    );

            rd.forward(
                    request,
                    response
            );
        }
    }
}