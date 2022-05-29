package com.example.demo.src.payment;

import com.example.demo.src.payment.Req.PostPaymentReq;
import com.example.demo.src.payment.Res.GetPaymentRes;
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
        String getPaymentQuery = "select payment_method_id, payment_name, payment_number, payment_type \n" +
                "from Payment_Method \n" +
                "where user_id = ? and status = 'Y';";

        return this.jdbcTemplate.query(getPaymentQuery,
                (rs, rowNum) -> new GetPaymentRes(
                        rs.getInt("payment_method_id"),
                        rs.getString("payment_name"),
                        rs.getString("payment_number"),
                        rs.getString("payment_type")
                ), user_id);
    }
}
