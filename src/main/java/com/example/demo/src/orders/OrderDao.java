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
        String getPriceQuery = "select M.menu_price+MO.option_price as order_price\n" +
                "from Menu M\n" +
                "inner join  Menu_Option as MO\n" +
                "on M.menu_id = MO.menu_id\n" +
                "where M.menu_id = ? and MO.menu_option_id = ? ";
        Integer order_price = this.jdbcTemplate.queryForObject(getPriceQuery, int.class, menu_id, postCartReq.getMenu_option_id());

        String createCartQuery = "insert into Cart (user_id, store_id, menu_id, menu_count, order_price, menu_option_id) VALUES (?,?,?,?,?,?)";
        Object[] createCartParams = new Object[]{user_id, store_id, menu_id, postCartReq.getMenu_count(),
                order_price, postCartReq.getMenu_option_id()};
        this.jdbcTemplate.update(createCartQuery, createCartParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    //카트 조회
    public GetCartRes getCart(int user_id) {
        String getCartQuery = "SELECT store_id FROM Cart WHERE status='Y' AND user_id=? LIMIT 1";
        Integer store_id = this.jdbcTemplate.queryForObject(getCartQuery, int.class, user_id);

        String Query = "";
        GetCartRes getCartRes = new GetCartRes("dd","dd", 1);
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
