package com.example.demo.src.orders;

import com.example.demo.src.orders.model.GetCartRes;
import com.example.demo.src.orders.model.PatchCartReq;
import com.example.demo.src.orders.model.PostCartReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class OrderDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //카트 생성
    public int createCart(int user_id, int store_id, int menu_id, PostCartReq postCartReq) {
        String createCartQuery = "insert into Cart (user_id, store_id, menu_id, menu_count, total_price) VALUES (?,?,?,?,?)";
        Object[] createCartParams = new Object[]{user_id, store_id, menu_id, postCartReq.getMenu_count(),
                postCartReq.getTotal_price()};
        this.jdbcTemplate.update(createCartQuery, createCartParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    //카트 조회
    public GetCartRes getCart(int user_id) {
        GetCartRes getCartRes = new GetCartRes();
        return getCartRes;
    }

    //카트 수량 수정
    public int modifyCart(int user_id, int store_id, int menu_id, PatchCartReq patchCartReq) {
        String UpdateCountQuery = "UPDATE Cart SET menu_count=? WHERE cart_id=?;";
        Object[] updateCartParams = new Object[]{user_id, store_id, menu_id, patchCartReq.getCount()};
        this.jdbcTemplate.update(UpdateCountQuery, updateCartParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }
}
