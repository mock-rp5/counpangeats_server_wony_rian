package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import com.example.demo.src.user.model.Req.PatchAddressReq;
import com.example.demo.src.user.model.Req.PostLoginReq;
import com.example.demo.src.user.model.Req.PostUserReq;
import com.example.demo.src.user.model.Res.*;
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

    public MyEatsInfo getMyEats(int userIdx){
        String getMyEatsQuery = "select user_name,user_phone from User where user_id = ?";
        int getMyEatsParams = userIdx;
        return this.jdbcTemplate.queryForObject(getMyEatsQuery,
                (rs, rowNum) -> new MyEatsInfo(
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

    public String isExistUserStatus(String email){
        String isExistUserStatusQuery="select status\n" +
                "from User\n" +
                "where user_email=?";
        String isExistUserStatusParam=email;
        return this.jdbcTemplate.queryForObject(isExistUserStatusQuery,
                String.class,
                isExistUserStatusParam
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

    public GetUserEmailRes findUserEmail(String user_name, String user_phone){
        String getUserEmailQuery = "select user_email from User\n" +
                "where user_name=? and user_phone=?;";
        Object[]  getUserEmailParams= new Object[]{user_name,user_phone};
        return this.jdbcTemplate.queryForObject(getUserEmailQuery,
                (rs, rowNum) -> new GetUserEmailRes(
                        rs.getString("user_email")),
                getUserEmailParams);
    }

    public GetUserPasswordRes findUserPassword(String user_name, String user_email){
        String getUserPasswordQuery = "select user_password from User\n" +
                "where user_name=? and user_email=?;";
        Object[]  getUserPasswordParams= new Object[]{user_name, user_email};
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

//    public void modifyStatusToE(int userIdx, int addressIdx){
//        String modifyHtoEQuery = "update Address set status='E'\n" +
//                "where user_id=? and address_id=?";
//        Object[] modifyHtoEParams=new Object[]{userIdx,addressIdx};
//        this.jdbcTemplate.update(modifyHtoEQuery,modifyHtoEParams);
//    }

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
                "where user_id=? and status != 'N';";
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

    public GetAddressRes getAddressOne(int addressIdx){
        String getAddressesOneQuery = "select main_address, detail_address, address_guide, status, address_name\n" +
                "from Address\n" +
                "where address_id=?;";
        int getAddressesOneParams = addressIdx;
        return this.jdbcTemplate.queryForObject(getAddressesOneQuery,
                (rs, rowNum) -> new GetAddressRes(
                        rs.getString("main_address"),
                        rs.getString("detail_address"),
                        rs.getString("address_guide"),
                        rs.getString("status"),
                        rs.getString("address_name")),
                getAddressesOneParams);
    }

    public String getAddressStatus(int addressIdx){
        String getAddressStatusQuery="select status\n" +
                "from Address\n" +
                "where address_id=?;";
        int getAddressStatusParam=addressIdx;
        return this.jdbcTemplate.queryForObject(getAddressStatusQuery,String.class,getAddressStatusParam);
    }

    public int createBookmark(int userIdx, int storeIdx){
        String postBookmarkQuery="insert into Book_Mark(user_id,store_id) values(?,?);";
        Object[] postBookmarkParams=new Object[]{userIdx,storeIdx};
        this.jdbcTemplate.update(postBookmarkQuery,postBookmarkParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int deleteBookmark(int userIdx, int storeIdx){
        String deleteBookmarkQuery = "update Book_Mark set status='N'\n" +
                "where user_id=? and store_id=?;";
        Object[] deleteBookmarkParams = new Object[]{userIdx, storeIdx};
        this.jdbcTemplate.update(deleteBookmarkQuery,deleteBookmarkParams);

        String bookmarkIdxQuery="select bookmark_id from Book_Mark where user_id =? and store_id=?";
        return this.jdbcTemplate.queryForObject(bookmarkIdxQuery,int.class,userIdx,storeIdx);
    }

    public String getBookmarkStatus(int userIdx, int storeIdx){
        String getBookmarkStatusQuery="select status\n" +
                "from Book_Mark\n" +
                "where user_id=? and store_id=?;";
        Object[] getBookmarkStatusParam=new Object[]{userIdx,storeIdx};
        return this.jdbcTemplate.queryForObject(getBookmarkStatusQuery,String.class,getBookmarkStatusParam);
    }

    public int isAlreadyCreate(int userIdx,int storeIdx){
        String getIsAlreadyCreateQuery="select exists(\n" +
                "    select *from Book_Mark\n" +
                "    where user_id=? and store_id=? and status='Y');";
        Object[] getIsAlreadyCreateParams=new Object[]{userIdx,storeIdx};
        return this.jdbcTemplate.queryForObject(getIsAlreadyCreateQuery,int.class,getIsAlreadyCreateParams);
    }
    // 주의 rs.getString("") -> 괄호안에 들어가는거 DTO이름이 아니라 mysql 컬럼명 이름이다!!
    public GetBookmarkRes getBookmarkList(int userIdx){
        String getCountBookmarkQuery="select count(*) as bookmark_count from Book_Mark where user_id=?;";
        String getStoreSimpleQuery="select S.store_main_image_url, S.store_name, S.is_cheetah_delivery,SD.delivery_time,SD.start_delivery_fee,S.is_takeout\n" +
                "from (select * from Book_Mark\n" +
                "where user_id=?) BM\n" +
                "inner join Store S on S.store_id=BM.store_id\n" +
                "inner join Store_Delivery SD on SD.store_id=BM.store_id;";
        String getReviewSimpleQuery="select round(avg(review_star),1) as star, count(R.review_id) as review_count\n" +
                "from (select * from Book_Mark\n" +
                "where user_id=?) BM\n" +
                "inner join Review R on BM.store_id=R.store_id\n" +
                "group by BM.store_id;";

        return this.jdbcTemplate.queryForObject(getCountBookmarkQuery,
                (rs, rowNum) -> new GetBookmarkRes(
                        rs.getInt("bookmark_count"),
                        this.jdbcTemplate.query(getStoreSimpleQuery,
                                (rs1, rowNum1) -> {
                            String store_main_image_url=rs1.getString("store_main_image_url");
                                    String store_name=rs1.getString("store_name");
                                    String is_cheetah_delivery=rs1.getString("is_cheetah_delivery");
                                    int start_delivery_fee=rs1.getInt("start_delivery_fee");
                                    String is_takeout=rs1.getString("is_takeout");
                                    return new StoreSimple(store_main_image_url,store_name,is_cheetah_delivery,start_delivery_fee,is_takeout);
                                },userIdx),
                        this.jdbcTemplate.query(getReviewSimpleQuery,
                                (rs2,rowNum2)-> {
                                    Float review_avg = rs2.getFloat("star");
                                    int review_cnt = rs2.getInt("review_count");
                                    return new ReviewSimple(review_avg, review_cnt);
                                },userIdx)
                ), userIdx);
    }
}
