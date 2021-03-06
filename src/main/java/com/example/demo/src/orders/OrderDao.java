package com.example.demo.src.orders;

import com.example.demo.src.orders.model.CartMenu;
import com.example.demo.src.orders.model.OrderMenuList;
import com.example.demo.src.orders.model.Res.GetCartRes;
import com.example.demo.src.orders.model.OrderDetail;
import com.example.demo.src.orders.model.Req.PatchCartReq;
import com.example.demo.src.orders.model.Req.PostCartReq;
import com.example.demo.src.orders.model.Req.PostOrderReq;
import com.example.demo.src.orders.model.Res.GetUserOrder;
import com.example.demo.src.orders.model.Res.PostOrderRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

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
                "                from Menu as M \n" +
                "                inner join Menu_Option as MO\n" +
                "                on M.menu_id = MO.menu_id\n"+
                "where M.menu_id = ? and MO.menu_option_id = ? limit 1";
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

        String storeQuery = "select S.store_id, S.store_name, S.is_cheetah_delivery, SD.start_delivery_fee, SD.minimum_price\n" +
                "from Store as S \n" +
                "inner join Store_Delivery SD\n" +
                "on SD.store_id = S.store_id \n" +
                "inner join Cart as C\n" +
                "on C.store_id = S.store_id\n" +
                "where S.store_id = ? and C.user_id = ? and S.status = \"Y\" \n" +
                "group by C.user_id";

        String menuQuery = "select C.cart_id, C.menu_count, MO.menu_option_id, MO.option_name, M.menu_name, M.menu_price, MO.option_price, (C.order_price)*C.menu_count as price\n" +
                "from Cart as C \n" +
                "inner join Menu_Option as MO\n" +
                "on MO.menu_option_id = C.menu_option_id\n" +
                "inner join Menu as M \n" +
                "on M.menu_id = C.menu_id " +
                "where C.user_id = ? and C.status = \"Y\"";

        return this.jdbcTemplate.queryForObject(storeQuery,
                (rs, rowNum) -> new GetCartRes(
                        rs.getInt("store_id"),
                        rs.getString("store_name"),
                        rs.getString("is_cheetah_delivery"),
                        rs.getInt("start_delivery_fee"),
                        rs.getInt("minimum_price"),
                        this.jdbcTemplate.query(menuQuery,
                                (rs1, rowNum1) -> new CartMenu(
                                        rs1.getInt("cart_id"),
                                        rs1.getInt("menu_count"),
                                        rs1.getInt("menu_option_id"),
                                        rs1.getString("option_name"),
                                        rs1.getString("menu_name"),
                                        rs1.getInt("menu_price"),
                                        rs1.getInt("option_price"),
                                        rs1.getInt("price")
                                ), user_id)
                        ), store_id, user_id);
    }

    //카트 수량 수정
    public int modifyCart(int store_id, int cart_id, PatchCartReq patchCartReq) {
        String UpdateCountQuery = "UPDATE Cart SET menu_count=? WHERE cart_id=? and store_id=?";
        return this.jdbcTemplate.update(UpdateCountQuery, patchCartReq.getMenu_count(), cart_id, store_id);
    }

    //카트 삭제
    public int deleteCart(int user_id, int cart_id) {
        String Query = "UPDATE Cart SET status='N' WHERE cart_id=? AND user_id=?";
        return this.jdbcTemplate.update(Query, cart_id, user_id);
    }

    //카트 새로 담기
    public int restartCart(int user_id, int store_id){
        this.jdbcTemplate.update("set sql_safe_updates=0");
        String Query = "UPDATE Cart SET status='N' WHERE store_id=? AND user_id=?";
        int update = this.jdbcTemplate.update(Query, store_id, user_id);
        return this.jdbcTemplate.update(Query, store_id, user_id);
    }

    //재주문 새로 담기
    public int restartOrder(int user_id, int store_id){
        this.jdbcTemplate.update("set sql_safe_updates=0");
        String Query = "UPDATE Cart SET status='N' WHERE store_id=? AND user_id=?";
        int update = this.jdbcTemplate.update(Query, store_id, user_id);
        return this.jdbcTemplate.update(Query, store_id, user_id);
    }

    //주문 생성
    public int createOrder(int user_id, Integer[] cartList, PostOrderReq postOrderReq){
        String getOrderInfoQuery = "SELECT MAX(order_info_id) FROM Order_Info;";
        Integer orderInfoId = this.jdbcTemplate.queryForObject(getOrderInfoQuery, int.class);

        ArrayList<Integer> brr = new ArrayList<>();
        for(int k=0; k<cartList.length; k++){
            String checkQuery = "select menu_count*order_price as price\n" +
                    "from Cart\n" +
                    "where cart_id = ?";
            Integer integer = this.jdbcTemplate.queryForObject(checkQuery, int.class, cartList[k]);
            brr.add(integer);
        }

        String deleteCartQuery = "UPDATE Cart SET status='N' WHERE cart_id = ?";
        String insertOrderInfoQuery = "insert into Order_Info (user_id, store_id, total_price, payment_method_id, delivery_request, store_request, address_id) VALUES (?,?,?,?,?,?,?)";
        String insertOrderDetailQuery = "insert into Order_Detail (menu_id, menu_count, order_info_id, menu_option_id, user_id, store_id, cart_id) VALUES (?,?,?,?,?,?,?)";
        String selectCartMenuQuery = "select menu_id, menu_count, menu_option_id from Cart where cart_id = ?";
        String getTotalPriceQuery = "select menu_count*order_price as total_price from Cart where cart_id = ?";

        Integer sum = 0;
        int i = 0;
        for(Integer k : cartList){
            System.out.println("k = " + k);
            sum += this.jdbcTemplate.queryForObject(getTotalPriceQuery, int.class, k);
            OrderDetail orderDetail = this.jdbcTemplate.queryForObject(selectCartMenuQuery,
                    (rs, rowNum) -> new OrderDetail(
                            rs.getInt("menu_count"),
                            rs.getInt("menu_id"),
                            rs.getInt("menu_option_id")
                    ), k);
            this.jdbcTemplate.update(insertOrderDetailQuery, orderDetail.getMenu_id(), orderDetail.getMenu_count(), orderInfoId+1, orderDetail.getMenu_option_id(), user_id, postOrderReq.getStore_id(), brr.get(i++));
            this.jdbcTemplate.update(deleteCartQuery, k); //카트에서 삭제
        }
        System.out.println("selectCartMenuQuery = " + selectCartMenuQuery);
        String getDeliveryPriceQuery = "select SD.start_delivery_fee\n" +
                "from Store_Delivery SD\n" +
                "where SD.store_id = ?";
        int deliveryFee =  this.jdbcTemplate.queryForObject(getDeliveryPriceQuery, int.class, postOrderReq.getStore_id());
        return this.jdbcTemplate.update(insertOrderInfoQuery, user_id, postOrderReq.getStore_id(), sum + deliveryFee, postOrderReq.getPayment_method_id(), postOrderReq.getDelivery_request(), postOrderReq.getStore_request(), postOrderReq.getAddress_id());
    }

    //재주문
    public int reOrder(int user_id, int[] cartList){
        String createCartQuery = "insert into Cart (user_id, store_id, menu_id, menu_count, order_price, menu_option_id) \n" +
                "select user_id, store_id, menu_id, menu_count, order_price, menu_option_id from Cart where cart_id = ?";
        for(int k : cartList){
            this.jdbcTemplate.update(createCartQuery, k);
        }
        return 1;
    }
    //영수증
    public List<GetUserOrder> getUserOrder(int user_id, int orderIdx, int storeIdx){
        String getStoreQuery = "select S.store_name, OI.order_info_id, OI.created_at, OI.total_price, A.detail_address, SD.start_delivery_fee\n" +
                "from Order_Info OI\n" +
                "inner join Store_Delivery SD\n" +
                "on SD.store_id = OI.store_id\n" +
                "inner join Address A\n" +
                "on A.address_id = OI.address_id\n" +
                "inner join Store S\n" +
                "on S.store_id = OI.store_id\n" +
                "where OI.user_id = ? and OI.order_info_id = ? and S.Store_id = ? \n";
        String getMenuQuery = "select M.menu_name, MO.option_name, OD.menu_count, M.menu_price, MO.option_price\n" +
                "from Order_Detail OD\n" +
                "inner join Menu_Option MO\n" +
                "on MO.menu_option_id = OD.menu_option_id\n" +
                "inner join Menu M\n" +
                "on M.menu_id = OD.menu_id \n" +
                "where OD.order_info_id = ?";
        return this.jdbcTemplate.query(getStoreQuery,
                (rs, rowNum) -> new GetUserOrder(
                        rs.getString("store_name"),
                        rs.getInt("order_info_id"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("total_price"),
                        rs.getString("detail_address"),
                        rs.getInt("start_delivery_fee"),
                        this.jdbcTemplate.query(getMenuQuery,
                                (rs1, rowNum1) -> new OrderMenuList(
                                        rs1.getString("menu_name"),
                                        rs1.getString("option_name"),
                                        rs1.getInt("menu_count"),
                                        rs1.getInt("menu_price"),
                                        rs1.getInt("option_price")
                                ),orderIdx
                            )
                ), user_id, orderIdx, storeIdx);
    }

    // 회원의 주문 목록 조회
    public List<GetUserOrder> getUserOrderList(int user_id){
        String getStoreQuery = "select S.store_name, OI.order_info_id, OI.created_at, OI.total_price, A.detail_address, SD.start_delivery_fee\n" +
                "from Order_Info OI\n" +
                "inner join Store_Delivery SD\n" +
                "on SD.store_id = OI.store_id\n" +
                "inner join Address A\n" +
                "on A.address_id = OI.address_id\n" +
                "inner join Store S\n" +
                "on S.store_id = OI.store_id\n" +
                "where OI.user_id = ? and OI.delivery_status = 'Y' \n";
        String getMenuQuery = "select M.menu_name, MO.option_name, OD.menu_count, M.menu_price, MO.option_price \n" +
                "from Order_Detail OD\n" +
                "inner join Menu_Option MO\n" +
                "on MO.menu_option_id = OD.menu_option_id\n" +
                "inner join Menu M\n" +
                "on M.menu_id = OD.menu_id \n" +
                "where OD.order_info_id = ?";
        return this.jdbcTemplate.query(getStoreQuery,
                (rs, rowNum) -> new GetUserOrder(
                        rs.getString("store_name"),
                        rs.getInt("order_info_id"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("total_price"),
                        rs.getString("detail_address"),
                        rs.getInt("start_delivery_fee"),
                        this.jdbcTemplate.query(getMenuQuery,
                                (rs1, rowNum1) -> new OrderMenuList(
                                        rs1.getString("menu_name"),
                                        rs1.getString("option_name"),
                                        rs1.getInt("menu_count"),
                                        rs1.getInt("menu_price"),
                                        rs1.getInt("option_price")
                                ), rs.getInt("order_info_id")
                        )
                ), user_id);
    }

    public List<GetUserOrder> getSearchMenu(int user_id, String menuName){
        String getStoreQuery = "select S.store_name, OI.order_info_id, OI.created_at, OI.total_price, A.detail_address, SD.start_delivery_fee\n" +
                "from Order_Info OI\n" +
                "inner join Store_Delivery SD\n" +
                "on SD.store_id = OI.store_id\n" +
                "inner join Address A\n" +
                "on A.address_id = OI.address_id\n" +
                "inner join Store S\n" +
                "on S.store_id = OI.store_id\n" +
                "where OI.user_id = ? and OI.delivery_status = 'Y' \n";
        String getMenuQuery = "select M.menu_name, MO.option_name, OD.menu_count, M.menu_price, MO.option_price \n" +
                "from Order_Detail OD\n" +
                "inner join Menu_Option MO\n" +
                "on MO.menu_option_id = OD.menu_option_id\n" +
                "inner join Menu M\n" +
                "on M.menu_id = OD.menu_id \n" +
                "where OD.order_info_id = ? and menu_name like '%"+ menuName + "%'";
        return this.jdbcTemplate.query(getStoreQuery,
                (rs, rowNum) -> new GetUserOrder(
                        rs.getString("store_name"),
                        rs.getInt("order_info_id"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("total_price"),
                        rs.getString("detail_address"),
                        rs.getInt("start_delivery_fee"),
                        this.jdbcTemplate.query(getMenuQuery,
                                (rs1, rowNum1) -> new OrderMenuList(
                                        rs1.getString("menu_name"),
                                        rs1.getString("option_name"),
                                        rs1.getInt("menu_count"),
                                        rs1.getInt("menu_price"),
                                        rs1.getInt("option_price")
                                ), rs.getInt("order_info_id")
                        )
                ), user_id);
    }
    // 회원의 준비 중 주문 목록 조회
    public List<GetUserOrder> getUserReadyOrderList(int user_id){
        String getStoreQuery = "select S.store_name, OI.order_info_id, OI.created_at, OI.total_price, A.detail_address, SD.start_delivery_fee\n" +
                "from Order_Info OI\n" +
                "inner join Store_Delivery SD\n" +
                "on SD.store_id = OI.store_id\n" +
                "inner join Address A\n" +
                "on A.address_id = OI.address_id\n" +
                "inner join Store S\n" +
                "on S.store_id = OI.store_id\n" +
                "where OI.user_id = ? and OI.delivery_status = 'N' \n";
        String getMenuQuery = "select M.menu_name, MO.option_name, OD.menu_count, M.menu_price, MO.option_price \n" +
                "from Order_Detail OD\n" +
                "inner join Menu_Option MO\n" +
                "on MO.menu_option_id = OD.menu_option_id\n" +
                "inner join Menu M\n" +
                "on M.menu_id = OD.menu_id \n" +
                "where OD.order_info_id = ?";
        return this.jdbcTemplate.query(getStoreQuery,
                (rs, rowNum) -> new GetUserOrder(
                        rs.getString("store_name"),
                        rs.getInt("order_info_id"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("total_price"),
                        rs.getString("detail_address"),
                        rs.getInt("start_delivery_fee"),
                        this.jdbcTemplate.query(getMenuQuery,
                                (rs1, rowNum1) -> new OrderMenuList(
                                        rs1.getString("menu_name"),
                                        rs1.getString("option_name"),
                                        rs1.getInt("menu_count"),
                                        rs1.getInt("menu_price"),
                                        rs1.getInt("option_price")
                                ), rs.getInt("order_info_id")
                        )
                ), user_id);
    }
    //카트에 담긴 가게 확인
    public int checkCartStore(int user_id) {
        String checkQuery = "select exists(select * from Cart where user_id = ? and status = \"Y\")";
        Integer store_id = this.jdbcTemplate.queryForObject(checkQuery, int.class, user_id);

        if(store_id == 0) return store_id;

        String getCartQuery = "SELECT store_id FROM Cart WHERE status=\"Y\" AND user_id=? LIMIT 1";
        return this.jdbcTemplate.queryForObject(getCartQuery, int.class, user_id);
    }

    //카트에 동일한 메뉴 존재 확인
    public int checkCartMenu(int menu_id, int user_id, int storeIdx, PostCartReq postCartReq) {
        String checkQuery = "select exists(select menu_id from Cart where menu_id = ? and user_id = ? and menu_option_id = ? and store_id =? and status = \"Y\")";
        Integer menu_id_exist = this.jdbcTemplate.queryForObject(checkQuery, int.class, menu_id, user_id, postCartReq.getMenu_option_id(), storeIdx);
        if(menu_id_exist == 0) return menu_id_exist;

        String cartId = "select cart_id from Cart where menu_id = ? and user_id = ? and status = \"Y\" ";
        Integer cart_id = this.jdbcTemplate.queryForObject(cartId, int.class, menu_id, user_id);

        String orderCount = "SELECT menu_count FROM Cart WHERE cart_id=?";
        int menu_count = this.jdbcTemplate.queryForObject(orderCount, int.class, cart_id) + postCartReq.getMenu_count();

        String Query = "UPDATE Cart SET menu_count = ? WHERE cart_id = ?";
        return this.jdbcTemplate.update(Query, menu_count, cart_id);
    }

    public int checkCartExists(int cart_id){
        String checkQuery = "select exists( select * from Cart where cart_id = ? and status = \"Y\" )";
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, cart_id);
    }

    public List<PostOrderRes> checkCartExistsUser(int user_id){
        String cartQuery = " select cart_id from Cart where user_id = ? and status = \"Y\" ";
        return this.jdbcTemplate.query(cartQuery,
                (rs, rowNum) -> new PostOrderRes(
                        rs.getInt("cart_id")
                ), user_id);
    }

    public int checkMenuAndOption(int menu_id, int option_id){
        String cartQuery = "select menu_id from Menu_Option where menu_option_id = ? ";
        Integer check = this.jdbcTemplate.queryForObject(cartQuery, int.class, option_id);
        if(check == menu_id) return 1;
        else return 0;
    }
}
