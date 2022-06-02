package com.example.demo.src.payment;

import com.example.demo.src.payment.Model.Payments;
import com.example.demo.src.payment.Model.Req.CashReq;
import com.example.demo.src.payment.Model.Req.PostCouponReq;
import com.example.demo.src.payment.Model.Req.PostPaymentReq;
import com.example.demo.src.payment.Model.Res.GetCouponRes;
import com.example.demo.src.payment.Model.Res.GetPaymentRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class WayDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //결제 방식 생성
    public int createPayment(int user_id, PostPaymentReq postPaymentReq) {
        String createPaymentQuery = "insert into Payment_Method (user_id, payment_name, payment_number, payment_type) VALUES (?,?,?,?)";
        Object[] createPaymentParams = new Object[]{user_id, postPaymentReq.getPayment_name(), postPaymentReq.getPayment_number(), postPaymentReq.getPayment_type()};
        this.jdbcTemplate.update(createPaymentQuery, createPaymentParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    //결제 방식 삭제
    public int deletePayment(int user_id, Integer payment_method_id) {
        String createPaymentQuery = "UPDATE Payment_Method SET status='N' WHERE payment_method_id=? AND user_id=?";
        Object[] createPaymentParams = new Object[]{payment_method_id, user_id};
        return this.jdbcTemplate.update(createPaymentQuery, createPaymentParams);
    }

    //결제 방식 조회
    public List<GetPaymentRes> getPayment(int user_id) {
        String checkQuery = "select exists(select cash_number from Cash  where user_id = ? and status = 'Y');";

        int checkCash = this.jdbcTemplate.queryForObject(checkQuery, int.class, user_id);
        String cash_number = null;
        if(checkCash == 1){
            String cashQuery = "select cash_number from Cash where user_id = ? and status = 'Y';";
            cash_number = this.jdbcTemplate.queryForObject(cashQuery, String.class, user_id);
        }

        String getPaymentQuery = "select payment_method_id, payment_name, payment_number, payment_type \n" +
        "from Payment_Method \n" +
        "where user_id = ? and status = 'Y';";

        List<Payments> query = this.jdbcTemplate.query(getPaymentQuery,
                (rs1, rowNum1) -> new Payments(
                        rs1.getInt("payment_method_id"),
                        rs1.getString("payment_name"),
                        rs1.getString("payment_number"),
                        rs1.getString("payment_type")
                ), user_id);

        String finalCash_number = cash_number;
        return this.jdbcTemplate.query(checkQuery,
                (rs, rowNum) -> new GetPaymentRes(
                        finalCash_number,
                    this.jdbcTemplate.query(getPaymentQuery,
                            (rs1, rowNum1)-> new Payments(
                                    rs1.getInt("payment_method_id"),
                                    rs1.getString("payment_name"),
                                    rs1.getString("payment_number"),
                                    rs1.getString("payment_type")
                            ), user_id)
                ), user_id);
    }

    //현금 영수증 생성
    public int postCash(int user_id, CashReq cashReq){
        String Query = "insert into Cash (cash_number, user_id, cash_type) VALUES (?,?,?)";
        return this.jdbcTemplate.update(Query, cashReq.getCash_number(), user_id, cashReq.getCash_type());
    }

    //현금영수증 수정
    public int patchCash(int user_id, CashReq cashReq){
        String Query1 = "UPDATE Cash SET cash_number = ? WHERE user_id=?";
        String Query2 = "UPDATE Cash SET cash_type = ? WHERE user_id=?";
        String Query3 = "UPDATE Cash SET status = 'Y' WHERE user_id=?";
        this.jdbcTemplate.update(Query1, cashReq.getCash_number(), user_id);
        this.jdbcTemplate.update(Query3, user_id);
        return this.jdbcTemplate.update(Query2, cashReq.getCash_type(), user_id);
    }

    //현금 영수증 삭제
    public int deleteCash(int user_id){
        String Query = "UPDATE Cash SET status = 'N' WHERE user_id=?";
        return this.jdbcTemplate.update(Query, user_id);
    }

    //쿠폰 생성
    public int createCoupon(int user_id, PostCouponReq postCouponReq){
        String couponIdQuery = "select coupon_id from Coupon where coupon_description = ?";
        Integer coupon_id = this.jdbcTemplate.queryForObject(couponIdQuery, int.class, postCouponReq.getCoupon_description());

        String createCouponQuery = "insert into Coupon_User (coupon_id, user_id) VALUES (?,?)";
        return this.jdbcTemplate.update(createCouponQuery, coupon_id, user_id);
    }

    //쿠폰 조회
    public List<GetCouponRes> getCoupon(int user_id) {
        String getCouponQuery = "select C.coupon_name, C.discount_price, C.discount_condition, C.expiration_date\n" +
                "from Coupon C\n" +
                "inner join Coupon_User U\n" +
                "on U.coupon_id = C.coupon_id\n" +
                "where U.user_id = ? and C.status = 'Y'";

        return this.jdbcTemplate.query(getCouponQuery,
                (rs, rowNum) -> new GetCouponRes(
                        rs.getString("coupon_name"),
                        rs.getInt("discount_price"),
                        rs.getString("discount_condition"),
                        rs.getTimestamp("expiration_date")
                ), user_id);
    }

    //현금 영수증 확인
    public int checkCash(int user_id){
        String checkQuery = "select exists(select * from Cash where user_id = ?)";
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, user_id);
    }

    //쿠폰 확인
    public int checkCoupon(String coupon_description){
        String Query = "select exists(select * from Coupon where coupon_description = ?)";
        return this.jdbcTemplate.queryForObject(Query, int.class, coupon_description);
    }

    //쿠폰 식별자 확인
    public int checkCouponStore(int coupon_id){
        String Query = "select exists(select * from Coupon where coupon_id = ?)";
        return this.jdbcTemplate.queryForObject(Query, int.class, coupon_id);
    }
    //쿠폰 유저한테 확인
    public int checkMeCoupon(int user_id, String coupon_description){
        String couponIdQuery = "select coupon_id from Coupon where coupon_description = ?";
        Integer coupon_id = this.jdbcTemplate.queryForObject(couponIdQuery, int.class, coupon_description);

        String Query = "select exists(select coupon_user_id from Coupon_User where coupon_id = ? and user_id = ?)";
        return this.jdbcTemplate.queryForObject(Query, int.class, coupon_id, user_id);
    }

}
