package service;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.BoardDAO;
import model.LikeDAO;
import model.MemberDTO;
import model.ReplyDAO;

public class MypageCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		MemberDTO member = session == null ? null : (MemberDTO) session.getAttribute("loginmember");
		if (member == null) {
			response.sendRedirect(request.getContextPath() + "/member/login");
			return false;
		}
		String memberId = member.getId();
		request.setAttribute("myPosts", new BoardDAO().selectListBtWriter(memberId));
		request.setAttribute("myReplies", new ReplyDAO().selectMyReplies(memberId));
		request.setAttribute("myLikes", new LikeDAO().selectLikedList(memberId));
		return true;
	}

}
