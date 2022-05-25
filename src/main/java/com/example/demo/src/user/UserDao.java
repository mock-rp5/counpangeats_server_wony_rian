package com.example.demo.src.user;


import com.example.demo.src.user.model.Address;
import com.example.demo.src.user.model.Req.*;
import com.example.demo.src.user.model.Res.*;
import com.example.demo.src.user.model.User;
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

    public GetMyEatsRes getMyEats(int userIdx){
        String getMyEatsQuery = "select user_name,user_phone from User where user_id = ?";
        int getMyEatsParams = userIdx;
        return this.jdbcTemplate.queryForObject(getMyEatsQuery,
                (rs, rowNum) -> new GetMyEatsRes(
                        rs.getString("user_name"),
                        rs.getString("user_phone")),
                getMyEatsParams);
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
    public int modifyUserPassword(User user){
        String modifyUserPasswordQuery = "update User set user_password = ? where user_id = ? ";
        Object[] modifyUserPasswordParams = new Object[]{user.getUser_password(), user.getUser_id()};

        return this.jdbcTemplate.update(modifyUserPasswordQuery,modifyUserPasswordParams);
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

    public Boolean isExistEmail(String email){
        String isExistEmailQuery="select exists(\n" +
                "    select * from User\n" +
                "    where user_email=?\n" +
                "           ) as isExist;";
        String isExistEmailParam=email;
        return this.jdbcTemplate.queryForObject(isExistEmailQuery,
                Boolean.class,
                isExistEmailParam
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
    public String getUserPassword(int userId){
        String getPasswordQuery="select user_password from User where user_id=?";
        int getPasswordParams=userId;
        return this.jdbcTemplate.queryForObject(getPasswordQuery,
                String.class,
                getPasswordParams);
    }

    public int deleteUser(int userIdx){
        String deleteUserQuery = "update User set status = 'N' where user_id = ? ";
        int deleteUserParams=userIdx;
        return this.jdbcTemplate.update(deleteUserQuery,deleteUserParams);
    }

    public GetUserEmailRes findUserEmail(GetUserEmailReq getUserEmailReq){
        String getUserEmailQuery = "select user_email from User\n" +
                "where user_name=? and user_phone=?;";
        Object[]  getUserEmailParams= new Object[]{getUserEmailReq.getUser_name(),getUserEmailReq.getUser_phone()};
        return this.jdbcTemplate.queryForObject(getUserEmailQuery,
                (rs, rowNum) -> new GetUserEmailRes(
                        rs.getString("user_email")),
                getUserEmailParams);
    }

    public GetUserPasswordRes findUserPassword(GetUserPasswordReq getUserPasswordReq){
        String getUserPasswordQuery = "select user_password from User\n" +
                "where user_name=? and user_email=?;";
        Object[]  getUserPasswordParams= new Object[]{getUserPasswordReq.getUser_name(),getUserPasswordReq.getUser_email()};
        return this.jdbcTemplate.queryForObject(getUserPasswordQuery,
                 (rs, rowNum) -> new GetUserPasswordRes(
                        rs.getString("user_password")),
                getUserPasswordParams);
    }

    public int createAddress(Address address){
        System.out.println("main_address: "+address.getMain_address());
        System.out.println("detail_address: "+address.getDetail_address());
        System.out.println("address_guide: "+address.getAddress_guide());
        System.out.println("user_id: "+address.getUser_id());
        System.out.println("address_longitude: "+address.getLongitude());
        System.out.println("address_latitude: "+address.getLatitude());
        System.out.println("address_name: "+address.getAddress_name());
        System.out.println("address_address: "+address.getStatus());

        String createAddressQuery = "insert into Address( main_address, detail_address, address_guide, user_id, address_longitude, address_latitude, address_name,status)\n" +
                "values (?,?,?,?,?,?,?,?);";
        Object[] createAddressParams = new Object[]{
                address.getMain_address(),address.getDetail_address(),address.getAddress_guide(),address.getUser_id(),address.getLongitude(),address.getLatitude(),address.getAddress_name(),address.getStatus()
        };
        this.jdbcTemplate.update(createAddressQuery, createAddressParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int modifyAddress(int addressIdx, PatchAddressReq patchAddressReq){
        String modifyAddressQuery = "update Address\n" +
                "set detail_address=?, address_guide=?, status=?, address_name=?, address_longitude=?, address_latitude=?\n" +
                "where address_id=?;";
        Object[] modifyAddressParams = new Object[]{patchAddressReq.getDetail_address(),patchAddressReq.getAddress_guide(),
        patchAddressReq.getStatus(), patchAddressReq.getAddress_name(),patchAddressReq.getLongitude(), patchAddressReq.getLatitude(),addressIdx};

        return this.jdbcTemplate.update(modifyAddressQuery,modifyAddressParams);
    }

    public int deleteAddress(int addressIdx, int userIdx){
        String deleteAddressQuery = "update Address\n" +
                "set status='N'\n" +
                "where user_id=? and address_id=?;";
        Object[] deleteAddressParams = new Object[]{userIdx, addressIdx};

        return this.jdbcTemplate.update(deleteAddressQuery,deleteAddressParams);
    }

    public List<GetAddressSimpleRes> getAddress(int user_id){
        String getAddressesQuery = "select status, address_name, main_address, detail_address\n" +
                "from Address\n" +
                "where user_id=?;";
        int getAddressesParams = user_id;
        return this.jdbcTemplate.query(getAddressesQuery,
                (rs, rowNum) -> {
                    String status = rs.getString("status");
                    String address_name = rs.getString("address_name");
                    String main_address = rs.getString("main_address");
                    String detail_address=rs.getString("detail_address");
                    return new GetAddressSimpleRes(
                            address_name,main_address,detail_address,status);
                },
                getAddressesParams);
    }
}
