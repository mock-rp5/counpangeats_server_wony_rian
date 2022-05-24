package com.example.demo.src.orders;

import com.example.demo.src.orders.model.CartMenu;
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
        String getPriceQuery = "select (M.menu_price+MO.option_price) as order_price\n" +
                "                from Menu M \n" +
                "                inner join  Menu_Option as MO\n" +
                "                on M.menu_id = MO.menu_id\n" +
                "                inner join Cart as C\n" +
                "                on C.menu_id = M.menu_id\n" +
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
        int store_id = this.jdbcTemplate.queryForObject(getCartQuery, int.class, user_id);

        String storeQuery = "select S.store_name, S.is_cheetah_delivery\n" +
                "from Store as S\n" +
                "inner join Cart as C\n" +
                "on C.store_id = S.store_id\n" +
                "where S.store_id = ? and S.status = 'Y' \n" +
                "group by C.user_id";

        String menuQuery = "select MO.option_name, M.menu_name, MO.option_price, (C.order_price)*C.menu_count as price\n" +
                "from Cart as C \n" +
                "inner join Menu_Option as MO\n" +
                "on MO.menu_option_id = C.menu_option_id\n" +
                "inner join Menu as M \n" +
                "on M.menu_id = C.menu_id " +
                "where C.user_id = ? and C.status = 'Y'";

        return this.jdbcTemplate.queryForObject(storeQuery,
                (rs, rowNum) -> new GetCartRes(
                        rs.getString("store_name"),
                        rs.getString("is_cheetah_delivery"),
                        this.jdbcTemplate.query(menuQuery,
                                (rs1, rowNum1) -> new CartMenu(
                                        rs1.getString("option_name"),
                                        rs1.getString("menu_name"),
                                        rs1.getInt("option_price"),
                                        rs1.getInt("price")
                                ), user_id)
                        ), store_id);
    }

    //카트 수량 수정
    public int modifyCart(int store_id, int cart_id, PatchCartReq patchCartReq) {
        String UpdateCountQuery = "UPDATE Cart SET menu_count=? WHERE cart_id=? and store_id=?";
        Object[] updateCartParams = new Object[]{patchCartReq.getCount(), cart_id, store_id};
        return this.jdbcTemplate.update(UpdateCountQuery, updateCartParams);
    }

    //카트 삭제
    public int deleteCart(int user_id, int cart_id) {
        String Query = "UPDATE Cart SET status='N' WHERE cart_id=? AND user_id=?";
        return this.jdbcTemplate.update(Query, cart_id, user_id);
    }

    //카트에 담긴 가게 확인
    public int checkCartStore(int user_id) {
        String checkQuery = "select exists(select * from Cart where user_id = ? and status = \"Y\")";
        Integer store_id = this.jdbcTemplate.queryForObject(checkQuery, int.class, user_id);

        if(store_id == 0) return store_id;

        String getCartQuery = "SELECT store_id FROM Cart WHERE status='Y' AND user_id=? LIMIT 1";
        return this.jdbcTemplate.queryForObject(getCartQuery, int.class, user_id);
    }

    public int checkCartExists(int cart_id){
        String checkQuery = "select exists( select * from Cart where cart_id = ? and status = 'Y')";
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, cart_id);
    }
}
