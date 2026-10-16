
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil;

public class LikeDAO {

    // 마이페이지 - 내가 좋아요한 글
    public List<BoardDTO> selectLikedList(String memberId) {
        List<BoardDTO> list = new ArrayList<>();
        String sql = "select b.bno, b.title, b.content, m.nickname as writer_nickname, "
                   + "b.like_cnt, b.img_path "
                   + "from board_like l "
                   + "join board b on l.bno = b.bno "
                   + "join member m on b.writer = m.id "
                   + "where l.member_id = ? "
                   + "order by l.like_date desc";
        try (Connection con = DBUtil.getInstance();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BoardDTO dto = new BoardDTO();
                    dto.setBno(rs.getInt("bno"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setWriterNickname(rs.getString("writer_nickname"));
                    dto.setLikeCnt(rs.getInt("like_cnt"));
                    dto.setImgPath(rs.getString("img_path"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ============================
    // 좋아요 여부 확인
    // ============================
    public boolean isLiked(int bno, String memberId) {

        String sql =
            "SELECT 1 FROM board_like "
          + "WHERE bno = ? AND member_id = ?";

        try (
            Connection con = DBUtil.getInstance();
            PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setInt(1, bno);
            pstmt.setString(2, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }

    // ============================
    // 좋아요 토글
    // 좋아요가 없으면 추가
    // 좋아요가 있으면 취소
    // ============================
    public void toggle(int bno, String memberId) {

        String checkSql =
            "SELECT 1 FROM board_like "
          + "WHERE bno = ? AND member_id = ?";

        String insertSql =
            "INSERT INTO board_like (bno, member_id) "
          + "VALUES (?, ?)";

        String deleteSql =
            "DELETE FROM board_like "
          + "WHERE bno = ? AND member_id = ?";

        String plusSql =
            "UPDATE board "
          + "SET like_cnt = like_cnt + 1 "
          + "WHERE bno = ?";

        String minusSql =
            "UPDATE board "
          + "SET like_cnt = like_cnt - 1 "
          + "WHERE bno = ?";

        try (Connection con = DBUtil.getInstance()) {

            // 트랜잭션 시작
            con.setAutoCommit(false);

            try {

                // 1. 이미 좋아요를 눌렀는지 확인
                boolean exists;

                try (PreparedStatement pstmt =
                        con.prepareStatement(checkSql)) {

                    pstmt.setInt(1, bno);
                    pstmt.setString(2, memberId);

                    try (ResultSet rs = pstmt.executeQuery()) {
                        exists = rs.next();
                    }
                }

                // 2. 좋아요 추가 또는 삭제
                String likeSql =
                    exists ? deleteSql : insertSql;

                int changed;

                try (PreparedStatement pstmt =
                        con.prepareStatement(likeSql)) {

                    pstmt.setInt(1, bno);
                    pstmt.setString(2, memberId);

                    changed = pstmt.executeUpdate();
                }

                if (changed != 1) {
                    throw new SQLException(
                        "좋아요 변경에 실패했습니다."
                    );
                }

                // 3. 게시글 좋아요 개수 변경
                String countSql =
                    exists ? minusSql : plusSql;

                try (PreparedStatement pstmt =
                        con.prepareStatement(countSql)) {

                    pstmt.setInt(1, bno);

                    int updated = pstmt.executeUpdate();

                    if (updated != 1) {
                        throw new SQLException(
                            "게시글 좋아요 개수 변경 실패"
                        );
                    }
                }

                // 4. 두 작업 모두 성공하면 커밋
                con.commit();

                System.out.println(
                    exists
                    ? "좋아요 취소 성공"
                    : "좋아요 등록 성공"
                );

            } catch (Exception e) {

                // 오류 발생 시 전체 롤백
                try {
                    con.rollback();

                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }

        } catch (Exception e) {

            System.err.println(
                "LikeDAO 좋아요 토글 오류"
            );

            e.printStackTrace();
        }
    }

    // ============================
    // 현재 좋아요 개수 조회
    // ============================
    public int selectLikeCnt(int bno) {

        String sql =
            "SELECT like_cnt FROM board "
          + "WHERE bno = ?";

        try (
            Connection con = DBUtil.getInstance();
            PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setInt(1, bno);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("like_cnt");
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return 0;
    }
}
