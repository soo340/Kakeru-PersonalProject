
package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/imgs/*")
public class ImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // 기존 이미지 저장 위치
    private static final Path UPLOAD_DIR =
            Paths.get("C:/upload").toAbsolutePath().normalize();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // 이미지 파일명 가져오기
        String fileName = request.getPathInfo();

        if (fileName == null || fileName.length() <= 1) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 앞의 / 제거
        fileName = fileName.substring(1);

        // 경로 조작 방지
        if (fileName.isBlank()
                || fileName.equals(".")
                || fileName.equals("..")
                || fileName.contains("/")
                || fileName.contains("\\")) {

            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 실제 이미지 파일 경로
        Path imagePath =
                UPLOAD_DIR.resolve(fileName).normalize();

        if (!imagePath.startsWith(UPLOAD_DIR)
                || !Files.isRegularFile(imagePath)
                || !Files.isReadable(imagePath)) {

            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 이미지 확장자 확인
        String lowerName = fileName.toLowerCase(Locale.ROOT);
        String contentType;

        if (lowerName.endsWith(".jpg")
                || lowerName.endsWith(".jpeg")) {
            contentType = "image/jpeg";

        } else if (lowerName.endsWith(".png")) {
            contentType = "image/png";

        } else if (lowerName.endsWith(".gif")) {
            contentType = "image/gif";

        } else if (lowerName.endsWith(".webp")) {
            contentType = "image/webp";

        } else {
            response.sendError(
                    HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        // 이미지 반환
        response.setContentType(contentType);
        response.setContentLengthLong(Files.size(imagePath));
        response.setHeader("X-Content-Type-Options", "nosniff");

        try (java.io.InputStream input =
                     Files.newInputStream(imagePath)) {

            input.transferTo(response.getOutputStream());
        }
    }
}
