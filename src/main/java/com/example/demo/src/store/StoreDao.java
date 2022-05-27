package com.example.demo.src.store;

import com.example.demo.src.store.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StoreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetStoreHomeRes> getHome() {
        String getHomeQuery = "select Store.store_name, Store.is_cheetah_delivery, Store_Takeout.status as take_out, Store_Delivery.delivery_time, Store.store_main_image_url, J.Cnt, J.RAvg\n" +
                "                from Store\n" +
                "                inner join (select OI.store_id, count(Review.review_id) as Cnt, avg(Review.review_star) as RAvg\n" +
                "                from Order_Info as OI \n" +
                "                inner join Review \n" +
                "                on OI.order_info_id = Review.order_info_id \n" +
                "                group by OI.store_id\n" +
                "                ) J \n" +
                "                on J.store_id = Store.store_id\n" +
                "                inner join Store_Delivery\n" +
                "                on Store_Delivery.store_id = Store.store_id\n" +
                "                inner join Store_Takeout\n" +
                "                on Store_Takeout.store_id = Store.store_id\n" +
                "                ";

        List<GetStoreHomeRes> result = this.jdbcTemplate.query(getHomeQuery,
                (rs, rowNum) -> new GetStoreHomeRes(
                        rs.getString("store_name"),
                        rs.getString("is_cheetah_delivery"),
                        rs.getString("take_out"),
                        rs.getString("delivery_time"),
                        rs.getString("store_main_image_url"),
                        rs.getInt("Cnt"),
                        rs.getFloat("RAvg")
                )
        );
        return result;
    }
    public GetStoreOneRes storeOne(int store_id, int userIdx){
        String storeQuery = "select S.store_name, S.is_cheetah_delivery, S.store_main_image_url, SD.delivery_time, SD.start_delivery_fee, SD.minimum_price, count(R.review_id) as cnt, avg(R.review_star) as average\n" +
                "from Store S\n" +
                "inner join Store_Delivery SD\n" +
                "on SD.store_id = S.store_id\n" +
                "inner join Review R\n" +
                "on R.store_id = S.store_id\n" +
                "where S.store_id = ?  \n" +
                "group by S.store_id \n";
        String reviewQuery = "select R.review_star, R.review_image_url, R.review_content\n" +
                "from Review R\n" +
                "inner join Store\n" +
                "on Store.store_id = R.store_id\n" +
                "where Store.store_id = ? and R.review_image_url is not null \n" +
                "order by R.created_at desc\n" +
                "limit 3";

        String isBookmarkQuery = "SELECT EXISTS(SELECT * FROM Book_Mark WHERE user_id= ? and store_id=? and status= 'Y');";
        Integer isBookmark = this.jdbcTemplate.queryForObject(isBookmarkQuery, int.class, userIdx, store_id);

        String menuKeywordQuery = "select MK.menu_keyword_name, MK.type\n" +
                "from Menu_Keyword MK\n" +
                "where MK.store_id = ? and MK.status = 'Y'";
        String menuDetailQuery = "select M.menu_name, M.menu_img_url, M.menu_description, M.menu_price\n" +
                "from Menu_Keyword MK\n" +
                "inner join Menu M\n" +
                "on M.menu_id = MK.menu_id\n" +
                "where MK.store_id = ? and MK.type = ? and MK.status = 'Y';";

        List<MenuCategory> menuCategoryList = this.jdbcTemplate.query(menuKeywordQuery,
                (rs, rowNum) -> new MenuCategory(
                        rs.getString("menu_keyword_name"),
                        this.jdbcTemplate.query(menuDetailQuery,
                                (rs1, rowNum1) -> new MenuDetail(
                                        rs1.getString("menu_name"),
                                        rs1.getString("menu_img_url"),
                                        rs1.getString("menu_description"),
                                        rs1.getInt("menu_price")
                                ), store_id, rs.getInt("type"))
                ), store_id);

        return this.jdbcTemplate.queryForObject(storeQuery,
                (rs, rowNum) -> new GetStoreOneRes(
                        rs.getString("store_name"),
                        rs.getString("is_cheetah_delivery"),
                        rs.getString("store_main_image_url"),
                        rs.getString("delivery_time"),
                        rs.getInt("start_delivery_fee"),
                        rs.getInt("minimum_price"),
                        rs.getInt("cnt"),
                        rs.getFloat("average"),
                        isBookmark,
                        this.jdbcTemplate.query(reviewQuery,
                                (rs1, rowNum1) -> new ReviewRes(
                                        rs1.getInt("review_star"),
                                        rs1.getString("review_image_url"),
                                        rs1.getString("review_content")
                                ), store_id),
                        menuCategoryList
                ), store_id
        );
    }
    public GetStoreInfoRes getStoreInfo(int storeIdx){
        String storeInfo = "select S.store_name, S.store_address, S.store_phone, S.store_ceo_name, S.business_number, S.search_business_name, S.find_store_tip, S.business_hours, S.store_description\n" +
                "from Store S\n" +
                "where S.store_id = ?";
        return this.jdbcTemplate.queryForObject(storeInfo,
                (rs, rowNum) -> new GetStoreInfoRes(
                        rs.getString("store_name"),
                        rs.getString("store_address"),
                        rs.getString("store_phone"),
                        rs.getString("store_ceo_name"),
                        rs.getString("business_number"),
                        rs.getString("search_business_name"),
                        rs.getString("find_store_tip"),
                        rs.getString("business_hours"),
                        rs.getString("store_description")
                ), storeIdx);
    }
}
