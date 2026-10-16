package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBUtil;


public class MemberDAO {


    // 아이디 중복 확인
    public boolean idCheck(String id)
    {
        String sql =
                "SELECT ID FROM MEMBER WHERE ID = ?";

        boolean isDuplicate = false;


        try (
            Connection con = DBUtil.getInstance();

            PreparedStatement pstmt =
                    con.prepareStatement(sql)
        )
        {
            pstmt.setString(1, id);


            try (
                ResultSet rs = pstmt.executeQuery()
            )
            {
                isDuplicate = rs.next();
            }

        }
        catch(Exception e)
        {
            System.out.println(
                    "아이디 중복확인 DB 오류"
            );

            e.printStackTrace();
        }


        return isDuplicate;
    }



    // 회원가입
    public int memberInsert(MemberDTO dto)
    {
        String sql =
                "INSERT INTO MEMBER "
              + "(ID, PWD, NICKNAME, EMAIL) "
              + "VALUES (?, ?, ?, ?)";


        int result = 0;


        try (
            Connection con = DBUtil.getInstance();

            PreparedStatement pstmt =
                    con.prepareStatement(sql)
        )
        {

            pstmt.setString(
                    1,
                    dto.getId()
            );

            pstmt.setString(
                    2,
                    dto.getPwd()
            );

            pstmt.setString(
                    3,
                    dto.getNickname()
            );

            pstmt.setString(
                    4,
                    dto.getEmail()
            );


            result = pstmt.executeUpdate();


            System.out.println(
                    "회원가입 INSERT 결과 = "
                    + result
            );

        }
        catch(Exception e)
        {
            System.out.println(
                    "회원가입 INSERT 오류 발생"
            );

            e.printStackTrace();
        }


        return result;
    }
    public MemberDTO login(String id)
    {
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	String sql = "select id,pwd,nickname,email,to_char(join_date,'YYYY-MM-DD') as join_date "
    			+ "from member where id=?";
    	
    	MemberDTO dto = null;
    	try
    	{
    		con = DBUtil.getInstance();
    		pstmt = con.prepareStatement(sql);
    		pstmt.setString(1, id);
    		rs = pstmt.executeQuery();
    		if(rs.next())
    		{
    			dto = new MemberDTO();
    			dto.setEmail(rs.getString("email"));
    			dto.setId(rs.getString("id"));
    			dto.setJoin_date(rs.getString("join_date"));
    			dto.setNickname(rs.getString("nickname"));
    			dto.setPwd(rs.getString("pwd"));
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
    			if(rs != null) rs.close();
    			if(pstmt != null) pstmt.close();
    			if(con != null)con.close();
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    	return dto;
    }
}