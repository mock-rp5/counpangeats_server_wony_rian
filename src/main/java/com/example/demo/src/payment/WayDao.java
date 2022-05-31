package com.example.demo.src.payment;

import com.example.demo.src.payment.Model.Payments;
import com.example.demo.src.payment.Model.Req.PostCashReq;
import com.example.demo.src.payment.Model.Req.PostPaymentReq;
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
        String checkQuery = "select CASE WHEN status = 'Y' THEN cash_number else 0 END as cash_number\n" +
                "from Cash \n" +
                "where user_id = ?";

        String getPaymentQuery = "select payment_method_id, payment_name, payment_number, payment_type \n" +
        "from Payment_Method \n" +
        "where user_id = ? and status = 'Y';";

        return this.jdbcTemplate.query(checkQuery,
                (rs, rowNum) -> new GetPaymentRes(
                    rs.getString("cash_number"),
                    this.jdbcTemplate.query(getPaymentQuery,
                            (rs1, rowNum1)-> new Payments(
                                    rs1.getInt("payment_method_id"),
                                    rs1.getString("payment_name"),
                                    rs1.getString("payment_number"),
                                    rs1.getString("payment_type")
                            ), user_id)
                ), user_id);
    }

    //현금 영수증 생성 및 수정
    public int patchCash(int user_id, PostCashReq postCashReq){
        String checkQuery = "select exists(select * from Cash where user_id = ?)";
        Integer checkCash = this.jdbcTemplate.queryForObject(checkQuery, int.class, user_id);

        if(checkCash == 1){
            String Query1 = "UPDATE Cash SET cash_number = ? WHERE user_id=?";
            String Query2 = "UPDATE Cash SET status = ? WHERE user_id=?";
            this.jdbcTemplate.update(Query1, postCashReq.getCash_number(), user_id);
            return this.jdbcTemplate.update(Query2, postCashReq.getStatus(), user_id);
        }else {
            String Query = "insert into Cash (cash_number, user_id, status) VALUES (?,?,?)";
            return this.jdbcTemplate.update(Query, postCashReq.getCash_number(), user_id, postCashReq.getStatus());
        }
    }

    //현금 영수증 삭제
    public int deleteCash(int user_id){
        String Query = "UPDATE Cash SET status = 'N' WHERE user_id=?";
        return this.jdbcTemplate.update(Query, user_id);
    }

}
