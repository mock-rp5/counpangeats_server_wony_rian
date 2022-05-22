package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import com.example.demo.src.user.model.Req.PostLoginReq;
import com.example.demo.src.user.model.Req.PostUserReq;
import com.example.demo.src.user.model.Res.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("user_id"),
                        rs.getString("user_email"),
                        rs.getString("user_password"),
                        rs.getString("user_name"),
                        rs.getString("user_phone"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from UserInfo where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUserParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User(user_email, user_password, user_name,user_phone) values (?,?,?,?);";
        Object[] createUserParams = new Object[]{postUserReq.getUser_email(), postUserReq.getUser_password(), postUserReq.getUser_name(), postUserReq.getUser_phone()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select user_email from User where user_email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserName(User user){
        String modifyUserNameQuery = "update User set user_name = ? where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{user.getUser_name(), user.getUser_id()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserEmail(User user){
        String modifyUserNameQuery = "update User set user_email = ? where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{user.getUser_email(), user.getUser_id()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserPhone(User user){
        String modifyUserPhoneQuery = "update User set user_phone = ? where user_id = ? ";
        Object[] modifyUserPhoneParams = new Object[]{user.getUser_phone(), user.getUser_id()};

        return this.jdbcTemplate.update(modifyUserPhoneQuery,modifyUserPhoneParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select user_id,user_email, user_password,user_name,user_phone from User where user_email=?";
        String getPwdParams = postLoginReq.getUser_email();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("user_id"),
                        rs.getString("user_email"),
                        rs.getString("user_password"),
                        rs.getString("user_name"),
                        rs.getString("user_phone")
                ),
                getPwdParams
                );

    }
    public String getUserEmail(int userId){
        String getEmailQuery="select user_email from User where user_id=?";
        int getEmailParams=userId;
        return this.jdbcTemplate.queryForObject(getEmailQuery,
                String.class,
                getEmailParams);
    }

    public String getUserName(int userId){
        String getNameQuery="select user_name from User where user_id=?";
        int getNameParams=userId;
        return this.jdbcTemplate.queryForObject(getNameQuery,
                String.class,
                getNameParams);
    }

    public String getUserPhone(int userId){
        String getPhoneQuery="select user_phone from User where user_id=?";
        int getPhoneParams=userId;
        return this.jdbcTemplate.queryForObject(getPhoneQuery,
                String.class,
                getPhoneParams);
    }

}
