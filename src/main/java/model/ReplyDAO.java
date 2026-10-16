
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil;

public class ReplyDAO {

    // 마이페이지 - 내가 작성한 댓글
    public List<ReplyDTO> selectMyReplies(String memberId) {
        List<ReplyDTO> list = new ArrayList<>();
        String sql = "select r.rno, r.bno, b.title as board_title, r.content, "
                   + "to_char(r.reg_date, 'YYYY-MM-DD') as reg_date "
                   + "from reply r "
                   + "join board b on r.bno = b.bno "
                   + "where r.writer = ? "
                   + "order by r.bno desc";
        try (Connection con = DBUtil.getInstance();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ReplyDTO dto = new ReplyDTO();
                    dto.setRno(rs.getInt("rno"));
                    dto.setBno(rs.getInt("bno"));
                    dto.setContent(rs.getString("content"));
                    dto.setRegDate(rs.getString("reg_date"));
                    dto.setBoardTitle(rs.getString("board_title"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ============================
    // 댓글 등록
    // ============================
    public int insert(ReplyDTO dto) {

        int rno = -1;

        String sql =
            "INSERT INTO reply (rno, bno, writer, content) "
          + "VALUES (seq_reply.NEXTVAL, ?, ?, ?)";

        String seqSql =
            "SELECT seq_reply.CURRVAL FROM dual";

        Connection con = null;

        try {
            con = DBUtil.getInstance();

            try (PreparedStatement pstmt =
                    con.prepareStatement(sql)) {

                pstmt.setInt(1, dto.getBno());
                pstmt.setString(2, dto.getWriter());
                pstmt.setString(3, dto.getContent());

                int result = pstmt.executeUpdate();

                if (result > 0) {

                    try (PreparedStatement pstmt2 =
                            con.prepareStatement(seqSql);
                         ResultSet rs = pstmt2.executeQuery()) {

                        if (rs.next()) {
                            rno = rs.getInt(1);
                        }
                    }

                    if (rno <= 0) {
                        throw new java.sql.SQLException(
                            "댓글 번호 조회 실패"
                        );
                    }

                    if (!con.getAutoCommit()) {
                        con.commit();
                    }
                }
            }

            System.out.println(
                "댓글 등록 결과 rno = " + rno
            );

        } catch (Exception e) {

            System.err.println(
                "ReplyDAO 댓글 등록 오류"
            );

            e.printStackTrace();

            if (con != null) {
                try {
                    if (!con.getAutoCommit()) {
                        con.rollback();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            rno = -1;

        } finally {

            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return rno;
    }

    // ============================
    // 기존 댓글 목록 조회
    // ============================
    public List<ReplyDTO> list(int bno) {

        List<ReplyDTO> list = new ArrayList<>();

        String sql =
            "SELECT r.rno, r.bno, r.writer, r.content, "
          + "TO_CHAR(r.reg_date, 'YYYY-MM-DD') AS reg_date, "
          + "m.nickname AS writer_nickname "
          + "FROM reply r "
          + "LEFT JOIN member m ON r.writer = m.id "
          + "WHERE r.bno = ? "
          + "ORDER BY r.rno ASC";

        try (Connection con = DBUtil.getInstance();
             PreparedStatement pstmt =
                     con.prepareStatement(sql)) {

            pstmt.setInt(1, bno);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {

                    ReplyDTO dto = new ReplyDTO();

                    dto.setRno(rs.getInt("rno"));
                    dto.setBno(rs.getInt("bno"));
                    dto.setWriter(rs.getString("writer"));
                    dto.setContent(rs.getString("content"));
                    dto.setRegDate(rs.getString("reg_date"));

                    String nickname =
                        rs.getString("writer_nickname");

                    if (nickname == null ||
                        nickname.isBlank()) {

                        nickname =
                            rs.getString("writer");
                    }

                    dto.setWriterNickname(nickname);

                    list.add(dto);
                }
            }

            System.out.println(
                "댓글 조회 결과 = " + list.size() + "개"
            );

        } catch (Exception e) {

            System.err.println(
                "ReplyDAO list 조회 오류"
            );

            e.printStackTrace();
        }

        return list;
    }

    // ============================
    // 댓글 목록 조회
    // ============================
    public List<ReplyDTO> selectList(int bno) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<ReplyDTO> list = new ArrayList<>();

        String sql =
            "SELECT r.rno, r.bno, r.writer, "
          + "m.nickname AS writer_nickname, "
          + "r.content, "
          + "TO_CHAR(r.reg_date, 'YYYY-MM-DD') AS reg_date "
          + "FROM reply r "
          + "JOIN member m ON r.writer = m.id "
          + "WHERE r.bno = ? "
          + "ORDER BY r.rno ASC";

        try {

            con = DBUtil.getInstance();

            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, bno);

            rs = pstmt.executeQuery();

            while (rs.next()) {

                ReplyDTO dto = new ReplyDTO();

                dto.setBno(rs.getInt("bno"));
                dto.setContent(rs.getString("content"));
                dto.setRegDate(rs.getString("reg_date"));
                dto.setRno(rs.getInt("rno"));
                dto.setWriter(rs.getString("writer"));

                dto.setWriterNickname(
                    rs.getString("writer_nickname")
                );

                list.add(dto);
            }

            System.out.println(
                "selectList 댓글 조회 결과 = "
                + list.size() + "개"
            );

        } catch (Exception e) {

            System.err.println(
                "ReplyDAO selectList 조회 오류"
            );

            e.printStackTrace();

        } finally {

            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    // ============================
    // 댓글 삭제
    // ============================
    public int delete(int rno, String writer) {

        String sql =
            "DELETE FROM reply "
          + "WHERE rno = ? AND writer = ?";

        int result = 0;

        try (
            Connection con = DBUtil.getInstance();
            PreparedStatement pstmt =
                con.prepareStatement(sql)
        ) {

            pstmt.setInt(1, rno);
            pstmt.setString(2, writer);

            result = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // ============================
    // 댓글 수정
    // ============================
    public int update(
            int rno,
            String content,
            String writer) {

        String sql =
            "UPDATE reply "
          + "SET content = ? "
          + "WHERE rno = ? AND writer = ?";

        int result = 0;

        Connection con = null;

        try {

            con = DBUtil.getInstance();

            try (PreparedStatement pstmt =
                    con.prepareStatement(sql)) {

                pstmt.setString(1, content);
                pstmt.setInt(2, rno);
                pstmt.setString(3, writer);

                // 실제 수정된 행 수 저장
                result = pstmt.executeUpdate();

                // 수동 커밋 환경 처리
                if (result > 0 && !con.getAutoCommit()) {
                    con.commit();
                }

                System.out.println(
                    "댓글 수정 결과 = " + result
                );
            }

        } catch (Exception e) {

            System.err.println(
                "ReplyDAO 댓글 수정 오류"
            );

            e.printStackTrace();

            if (con != null) {
                try {
                    if (!con.getAutoCommit()) {
                        con.rollback();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            result = 0;

        } finally {

            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
    
}
