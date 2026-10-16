package service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import model.BoardDAO;
import model.BoardDTO;
import model.MemberDTO;

public class BoardModify implements Command {

    private static final String UPLOAD_DIR = "C:/upload";

    @Override
    public boolean doCommand(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");


        // 로그인 회원 확인
        HttpSession session = request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath() + "/member/login"
            );

            return false;
        }


        MemberDTO loginMember =
                (MemberDTO) session.getAttribute("loginmember");


        if (loginMember == null) {

            response.sendRedirect(
                    request.getContextPath() + "/member/login"
            );

            return false;
        }


        // 게시글 번호
        String bnoParam =
                request.getParameter("bno");


        if (bnoParam == null
                || bnoParam.trim().isEmpty()) {

            response.sendRedirect(
                    request.getContextPath() + "/blog/list"
            );

            return false;
        }


        int bno;

        try {

            bno = Integer.parseInt(bnoParam);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath() + "/blog/list"
            );

            return false;
        }


        BoardDAO dao = new BoardDAO();

        BoardDTO board =
                dao.selectOne(bno);


        // 존재하지 않는 글
        if (board == null) {

            response.sendRedirect(
                    request.getContextPath() + "/blog/list"
            );

            return false;
        }


        // 본인 글인지 확인
        if (!board.getWriter().equals(loginMember.getId())) {

            request.setAttribute(
                    "board",
                    board
            );

            request.setAttribute(
                    "errMsg",
                    "自分の投稿のみ編集できます。"
            );

            return true;
        }


        // ==================================================
        // GET : 수정 화면 출력
        // ==================================================

        if ("GET".equalsIgnoreCase(request.getMethod())) {

            request.setAttribute(
                    "board",
                    board
            );

            return true;
        }


        // ==================================================
        // POST : 실제 수정
        // ==================================================

        String title =
                request.getParameter("title");

        String content =
                request.getParameter("content");

        String removeImg =
                request.getParameter("removeImg");


        // 제목 검사
        if (title == null
                || title.trim().isEmpty()) {

            request.setAttribute(
                    "errMsg",
                    "タイトルを入力してください。"
            );

            request.setAttribute(
                    "board",
                    board
            );

            return true;
        }


        // 내용 검사
        if (content == null
                || content.trim().isEmpty()) {

            request.setAttribute(
                    "errMsg",
                    "内容を入力してください。"
            );

            request.setAttribute(
                    "board",
                    board
            );

            return true;
        }


        // 기존 이미지
        String oldImgPath =
                board.getImgPath();


        // 기본값은 기존 이미지 유지
        String finalImgPath =
                oldImgPath;


        Part filePart = null;


        try {

            filePart =
                    request.getPart("imgFile");

        } catch (Exception e) {

            e.printStackTrace();
        }


        // ==================================================
        // 새 이미지 선택
        // ==================================================

        if (filePart != null
                && filePart.getSize() > 0
                && filePart.getSubmittedFileName() != null
                && !filePart.getSubmittedFileName()
                            .trim()
                            .isEmpty()) {

            String newImgPath =
                    saveFile(filePart);

            // 새 이미지 저장 후
            // 기존 이미지 삭제
            deleteOldFile(oldImgPath);

            finalImgPath =
                    newImgPath;
        }


        // ==================================================
        // 기존 이미지 삭제
        // ==================================================

        else if ("true".equals(removeImg)) {

            deleteOldFile(oldImgPath);

            finalImgPath = null;
        }


        // ==================================================
        // 수정할 DTO
        // ==================================================

        BoardDTO updateDto =
                new BoardDTO();


        updateDto.setBno(bno);

        updateDto.setTitle(
                title.trim()
        );

        updateDto.setContent(
                content.trim()
        );

        updateDto.setWriter(
                loginMember.getId()
        );

        updateDto.setImgPath(
                finalImgPath
        );


        // ==================================================
        // DB 수정
        // ==================================================

        int result =
                dao.update(updateDto);


        if (result > 0) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/blog/view?bno="
                    + bno
            );

            return false;
        }


        // 수정 실패
        board.setTitle(title);

        board.setContent(content);

        board.setImgPath(finalImgPath);


        request.setAttribute(
                "board",
                board
        );


        request.setAttribute(
                "errMsg",
                "投稿の更新に失敗しました。"
        );


        return true;
    }


    // ==================================================
    // 이미지 저장
    // ==================================================

    private String saveFile(Part filePart)
            throws IOException {

        String oriFileName =
                filePart.getSubmittedFileName();


        oriFileName =
                Paths.get(oriFileName)
                     .getFileName()
                     .toString();


        String saveFileName =
                System.currentTimeMillis()
                + "-"
                + oriFileName;


        Path uploadDir =
                Paths.get(UPLOAD_DIR);


        if (!Files.exists(uploadDir)) {

            Files.createDirectories(
                    uploadDir
            );
        }


        Path targetPath =
                uploadDir.resolve(
                        saveFileName
                );


        try (InputStream input =
                     filePart.getInputStream()) {

            Files.copy(
                    input,
                    targetPath
            );
        }


        return saveFileName;
    }


    // ==================================================
    // 기존 이미지 삭제
    // ==================================================

    private void deleteOldFile(
            String fileName) {

        if (fileName == null
                || fileName.trim().isEmpty()) {

            return;
        }


        try {

            Path filePath =
                    Paths.get(
                            UPLOAD_DIR,
                            fileName
                    );


            Files.deleteIfExists(
                    filePath
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}