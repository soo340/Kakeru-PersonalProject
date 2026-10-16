package service;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.MemberDAO;


public class IdCheckCommand implements Command {


    @Override
    public boolean doCommand(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException
    {

        String id =
                request.getParameter("id");


        System.out.println(
                "중복확인 id = " + id
        );


        MemberDAO dao =
                new MemberDAO();


        boolean isDuplicate =
                dao.idCheck(id);



        // JSON 응답
        response.setContentType(
                "application/json; charset=UTF-8"
        );


        PrintWriter out =
                response.getWriter();


        out.print(
                "{\"isDuplicate\":"
                + isDuplicate
                + "}"
        );


        out.flush();


        // AJAX 응답이므로 JSP forward 안 함
        return false;

    }

}