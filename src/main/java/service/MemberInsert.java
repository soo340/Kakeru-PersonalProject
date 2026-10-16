package service;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.MemberDAO;
import model.MemberDTO;


public class MemberInsert implements Command {


    @Override
    public boolean doCommand(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException
    {

        // ========================================
        // GET 요청
        // 회원가입 화면만 보여준다.
        // ========================================
        if("GET".equalsIgnoreCase(
                request.getMethod()))
        {

            return true;

        }



        // ========================================
        // POST 요청
        // 실제 회원가입 실행
        // ========================================

        request.setCharacterEncoding("UTF-8");


        String id =
                request.getParameter("id");


        String pwd =
                request.getParameter("pwd");

        String hashedpwd =
        		BCrypt.hashpw(pwd, BCrypt.gensalt());
        
        String nickname =
                request.getParameter("nickname");


        String email =
                request.getParameter("email");



        // ========================================
        // DTO 생성
        // ========================================

        MemberDTO dto =
                new MemberDTO();


        dto.setId(id);

        dto.setPwd(hashedpwd);

        dto.setNickname(nickname);

        dto.setEmail(email);



        // ========================================
        // DAO 호출
        // ========================================

        MemberDAO dao =
                new MemberDAO();


        int result =
                dao.memberInsert(dto);



        // ========================================
        // 회원가입 성공
        // ========================================

        if(result > 0)
        {

            System.out.println(
                    "회원가입 성공"
            );


            response.sendRedirect(
                    request.getContextPath()
                    + "/member/login"
            );


            // redirect 했으므로
            // forward 하지 않음
            return false;

        }



        // ========================================
        // 회원가입 실패
        // ========================================

        System.out.println(
                "회원가입 실패"
        );


        request.setAttribute(
                "errorMsg",
                "新規登録に失敗しました。"
        );


        // join.jsp로 다시 forward
        return true;

    }

}
