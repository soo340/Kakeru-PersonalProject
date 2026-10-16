package service;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MemberDAO;
import model.MemberDTO;

public class LoginCommand implements Command {

    @Override
    public boolean doCommand(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        request.setCharacterEncoding("utf-8");

        String id = request.getParameter("id");
        String pwd = request.getParameter("pwd");

        MemberDAO dao = new MemberDAO();

        MemberDTO dto = dao.login(id);

        if (dto != null && BCrypt.checkpw(pwd, dto.getPwd())) {

            HttpSession session = request.getSession();

            // 로그인 회원 정보 저장
            session.setAttribute("loginmember", dto);

            response.sendRedirect(
                    request.getContextPath() + "/"
            );

            return false;

        } else {

            request.setAttribute(
                    "loginFail",
                    "ユーザーIDまたはパスワードをご確認ください"
            );

            return true;
        }
    }
}