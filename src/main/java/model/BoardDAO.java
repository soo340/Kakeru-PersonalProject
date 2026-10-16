
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil;

public class BoardDAO {

    // 게시글 등록
    public int insert(BoardDTO dto) {

        String sql =
                "insert into board "
              + "(bno, title, content, writer, img_path) "
              + "values(seq_board.nextval, ?, ?, ?, ?)";

        int result = 0;

        try (
            Connection con = DBUtil.getInstance();
            PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());
            pstmt.setString(3, dto.getWriter());
            pstmt.setString(4, dto.getImgPath());

            result = pstmt.executeUpdate();

            System.out.println("게시글 등록 결과 = " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    //검색 조건에 맞는 where 절 만들기(selectCount, selectList 둘 다 같은 조건을 써야 해서 분리
    private String buildWhere(String searchType, String keyword)
    {
    	if(keyword == null || keyword.trim().isEmpty())
    	{
    		return "";
    	}
    	
    	if("title".equals(searchType))
    	{
    		return "where b.title like ?";
    	}
    	else if("content".equals(searchType))
    	{
    		return "where b.content like ?";
    	}
    	else if("writer".equals(searchType))
    	{
			return "where m.nickname like ?";
    	}
    	return "";
    }
    
    //검색 조건에 맞는 전체 글 개수
    public int selectCount(String searchType, String keyword)
    {
        String where = buildWhere(searchType, keyword);

        String sql = "SELECT COUNT(*) FROM board b "
                   + "JOIN member m ON b.writer = m.id "
                   + where;

        try (Connection con = DBUtil.getInstance();
             PreparedStatement pstmt = con.prepareStatement(sql))
        {
            if (!where.isEmpty())
            {
                pstmt.setString(1, "%" + keyword + "%");
            }

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }
    
    
    // 게시글 목록 및 검색
    public List<BoardDTO> selectList(
            String searchType,
            String keyword,
            int startRow,
            int endRow) {
    	String where = buildWhere(searchType, keyword);
        

        String sql = "select * from ("
                + "select rownum as rn, t.* from ("
                + "select b.bno, b.title, b.content, b.writer, "
                + "m.nickname as writer_nickname, "
                + "to_char(b.reg_date, 'YYYY-MM-DD') as reg_date, "
                + "b.view_cnt, b.like_cnt, b.img_path "
                + "from board b join member m on b.writer = m.id "
                + where
                + " order by b.bno desc"
                + ") t where rownum <= ?"
                + ") where rn >= ?";
        List<BoardDTO> list = new ArrayList<>();        
        
        try (Connection con = DBUtil.getInstance();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            int idx = 1;
            if (!where.isEmpty()) {
                pstmt.setString(
                        idx++,
                        "%" + keyword+ "%"
                );
            }
            pstmt.setInt(idx++, endRow);
            pstmt.setInt(idx, startRow);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {

                    BoardDTO dto = new BoardDTO();

                    dto.setBno(rs.getInt("bno"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setWriter(rs.getString("writer"));

                    dto.setWriterNickname(
                            rs.getString("writer_nickname")
                    );

                    dto.setRegDate(
                            rs.getString("reg_date")
                    );

                    dto.setViewCnt(
                            rs.getInt("view_cnt")
                    );

                    dto.setLikeCnt(
                            rs.getInt("like_cnt")
                    );

                    dto.setImgPath(
                            rs.getString("img_path")
                    );

                    list.add(dto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // 게시글 상세 조회
    public BoardDTO selectOne(int bno) {

        String sql =
                "select b.bno, "
              + "b.title, "
              + "b.content, "
              + "b.writer, "
              + "m.nickname as writer_nickname, "
              + "to_char(b.reg_date, 'YYYY-MM-DD') as reg_date, "
              + "b.view_cnt, "
              + "b.like_cnt, "
              + "b.img_path "
              + "from board b "
              + "join member m on b.writer = m.id "
              + "where b.bno = ?";

        BoardDTO dto = null;

        try (
            Connection con = DBUtil.getInstance();
            PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setInt(1, bno);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    dto = new BoardDTO();

                    dto.setBno(rs.getInt("bno"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setWriter(rs.getString("writer"));

                    dto.setWriterNickname(
                            rs.getString("writer_nickname")
                    );

                    dto.setRegDate(
                            rs.getString("reg_date")
                    );

                    dto.setViewCnt(
                            rs.getInt("view_cnt")
                    );

                    dto.setLikeCnt(
                            rs.getInt("like_cnt")
                    );

                    dto.setImgPath(
                            rs.getString("img_path")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }


    // 조회수 증가
    public void increViewCnt(int bno) {

        String sql =
                "update board "
              + "set view_cnt = view_cnt + 1 "
              + "where bno = ?";

        try (
            Connection con = DBUtil.getInstance();
            PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setInt(1, bno);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 게시글 삭제
    // 성공하면 기존 이미지 경로 반환
    // 실패하면 null 반환
    public String delete(int bno, String writer) {

        String selectSql =
                "select img_path "
              + "from board "
              + "where bno = ? and writer = ?";

        String deleteSql =
                "delete from board "
              + "where bno = ? and writer = ?";

        String imgPath = null;

        try (
            Connection con = DBUtil.getInstance()
        ) {

            try (
                PreparedStatement pstmt =
                        con.prepareStatement(selectSql)
            ) {

                pstmt.setInt(1, bno);
                pstmt.setString(2, writer);

                try (ResultSet rs = pstmt.executeQuery()) {

                    if (rs.next()) {
                        imgPath = rs.getString("img_path");
                    } else {
                        return null;
                    }
                }
            }

            try (
                PreparedStatement pstmt =
                        con.prepareStatement(deleteSql)
            ) {

                pstmt.setInt(1, bno);
                pstmt.setString(2, writer);

                int result = pstmt.executeUpdate();

                System.out.println(
                        "게시글 삭제 결과 = " + result
                );

                if (result > 0) {
                    return imgPath;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // 게시글 수정
    public int update(BoardDTO dto) {

        String sql =
                "update board "
              + "set title = ?, "
              + "content = ?, "
              + "img_path = ?, "
              + "mod_date = sysdate "
              + "where bno = ? "
              + "and writer = ?";

        int result = 0;

        try (
            Connection con = DBUtil.getInstance();
            PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());
            pstmt.setString(3, dto.getImgPath());
            pstmt.setInt(4, dto.getBno());
            pstmt.setString(5, dto.getWriter());

            result = pstmt.executeUpdate();

            System.out.println(
                    "게시글 수정 결과 = " + result
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public List<BoardDTO> selectPopularList()
    {
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	String sql = "select * from (select b.bno,b.title,b.content,m.nickname as writer_nickname,\r\n"
    			+ "    to_char(b.reg_date,'YYYY-MM-DD') as reg_date,\r\n"
    			+ "    b.view_cnt,b.like_cnt,b.img_path\r\n"
    			+ "    from board b\r\n"
    			+ "    join member m\r\n"
    			+ "    on b.writer = m.id\r\n"
    			+ "    order by b.like_cnt desc, b.view_cnt desc, b.bno desc)\r\n"
    			+ "where rownum <= 6";
    	
    	List<BoardDTO> list = new ArrayList<BoardDTO>();
    	
    	try
    	{
    		con = DBUtil.getInstance();
    		pstmt = con.prepareStatement(sql);
    		rs = pstmt.executeQuery();
    		while(rs.next())
    		{
    			BoardDTO dto = new BoardDTO();
    			dto.setBno(rs.getInt("bno"));
    			dto.setTitle(rs.getString("title"));
    			dto.setContent(rs.getString("content"));
    			dto.setWriterNickname(rs.getString("writer_nickname"));
    			dto.setRegDate(rs.getString("reg_date"));
    			dto.setViewCnt(rs.getInt("view_cnt"));
    			dto.setLikeCnt(rs.getInt("like_cnt"));
    			dto.setImgPath(rs.getString("img_path"));
    			list.add(dto);
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally
    	{
    		try
    		{
    		if(pstmt != null) pstmt.close();
    		if(con != null) con.close();
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    	return list;
    }

    // 마이페이지 - 내가 작성한 글
    public List<BoardDTO> selectListBtWriter(String memberId) {
        List<BoardDTO> list = new ArrayList<>();
        String sql = "select bno, title, content, to_char(reg_date, 'YYYY-MM-DD') as reg_date, "
                   + "view_cnt, like_cnt, img_path "
                   + "from board "
                   + "where writer = ? "
                   + "order by bno desc";
        try (Connection con = DBUtil.getInstance();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BoardDTO dto = new BoardDTO();
                    dto.setBno(rs.getInt("bno"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setRegDate(rs.getString("reg_date"));
                    dto.setViewCnt(rs.getInt("view_cnt"));
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
    
}
