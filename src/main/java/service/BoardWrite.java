package service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import model.BoardDAO;
import model.BoardDTO;
import model.MemberDTO;

public class BoardWrite implements Command {

    private static final String UPLOAD_DIR = "C:/upload";

    @Override
    public boolean doCommand(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException {


        /*
         * ============================
         * 1. 로그인 확인
         * ============================
         */

        HttpSession session = request.getSession();

        MemberDTO memberdto =
                (MemberDTO) session.getAttribute("loginmember");


        if (memberdto == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/member/login"
            );

            return false;
        }


        /*
         * ============================
         * 2. GET 요청
         * 글쓰기 화면만 보여준다.
         * ============================
         */

        if ("GET".equalsIgnoreCase(
                request.getMethod())) {

            return true;
        }


        /*
         * ============================
         * 3. POST 요청
         * 입력값 받기
         * ============================
         */

        String title =
                request.getParameter("title");

        String content =
                request.getParameter("content");


        // 확인용
        System.out.println(
                "title = " + title
        );

        System.out.println(
                "content = " + content
        );

        System.out.println(
                "writer = " + memberdto.getId()
        );


        /*
         * 제목 검증
         */
        if (title == null ||
            title.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "タイトルを入力してください。"
            );

            return true;
        }


        /*
         * 내용 검증
         */
        if (content == null ||
            content.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "内容を入力してください。"
            );

            return true;
        }


        /*
         * ============================
         * 4. BoardDTO 생성
         * ============================
         */

        BoardDTO dto =
                new BoardDTO();

        dto.setTitle(title.trim());

        dto.setContent(content);

        /*
         * 작성자는 form에서 받지 않고
         * 로그인한 회원의 ID를 사용한다.
         */
        dto.setWriter(
                memberdto.getId()
        );


        /*
         * ============================
         * 5. 이미지 파일 처리
         * ============================
         */

        Part filePart =
                request.getPart("imgFile");


        if (filePart != null &&
            filePart.getSize() > 0) {

            String saveFileName =
                    saveFile(filePart);

            dto.setImgPath(
                    saveFileName
            );
        }


        /*
         * ============================
         * 6. DB INSERT
         * ============================
         */

        BoardDAO dao =
                new BoardDAO();

        dao.insert(dto);


        /*
         * ============================
         * 7. 등록 후 목록으로 이동
         * ============================
         */

        response.sendRedirect(
                request.getContextPath()
                + "/blog/list"
        );


        /*
         * 이미 redirect 했으므로
         * Controller에서 forward 하지 않는다.
         */
        return false;
    }



    /*
     * ============================
     * 파일 저장 메서드
     * ============================
     */

    public String saveFile(Part filePart)
            throws IOException {


        /*
         * 원본 파일명
         */
        String oriFileName =
                filePart.getSubmittedFileName();


        /*
         * 혹시 경로 정보가 같이 넘어오더라도
         * 실제 파일명만 추출
         */
        oriFileName =
                Paths.get(oriFileName)
                     .getFileName()
                     .toString();


        System.out.println(
                "original file : "
                + oriFileName
        );


        /*
         * UUID를 사용해서
         * 중복되지 않는 파일명 생성
         */
        String saveFileName =
                UUID.randomUUID()
                + "_"
                + oriFileName;


        System.out.println(
                "save file : "
                + saveFileName
        );


        /*
         * F:/upload
         */
        Path uploadDir =
                Paths.get(UPLOAD_DIR);


        /*
         * upload 폴더가 없으면 생성
         */
        if (!Files.exists(uploadDir)) {

            Files.createDirectories(
                    uploadDir
            );
        }


        /*
         * 최종 파일 경로
         *
         * F:/upload/UUID_파일명.jpg
         */
        Path targetPath =
                uploadDir.resolve(
                        saveFileName
                );


        /*
         * 실제 파일 저장
         */
        try (InputStream input =
                     filePart.getInputStream()) {

            Files.copy(
                    input,
                    targetPath
            );
        }


        /*
         * 중요:
         * null이 아니라 저장된 파일명 반환
         */
        return saveFileName;
    }
}