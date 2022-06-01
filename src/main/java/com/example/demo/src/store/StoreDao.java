package com.example.demo.src.store;

import com.example.demo.src.store.model.*;
import com.example.demo.src.store.model.Req.PatchHelpReq;
import com.example.demo.src.store.model.Req.PatchReviewReq;
import com.example.demo.src.store.model.Req.PostHelpReq;
import com.example.demo.src.store.model.Req.PostReviewReq;
import com.example.demo.src.store.model.Res.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class StoreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetStoreHomeRes getHome(String is_cheetah, int start_delivery_fee, int minimum_price, String is_Delivery) {
        String getAdQuery = "select ad_id, ad_image_url, link_url from Ad;";

        String getCategoryQuery = "select C.category_name, C.category_image_url from Category C";

        String getHomeQuery = "select Store.store_id, Store.store_name, Store.is_cheetah_delivery, Store_Takeout.status as take_out, SD.delivery_time, SD.start_delivery_fee, Store.store_main_image_url, J.Cnt, J.RAvg\n" +
                "from Store\n" +
                "left join (select OI.store_id, count(Review.review_id) as Cnt, avg(Review.review_star) as RAvg\n" +
                "\tfrom Order_Info as OI \n" +
                "\tleft join Review \n" +
                "\ton OI.order_info_id = Review.order_info_id \n" +
                "\tgroup by OI.store_id\n" +
                ") J \n" +
                "on J.store_id = Store.store_id\n" +
                "inner join Store_Delivery SD\n" +
                "on SD.store_id = Store.store_id\n" +
                "inner join Store_Takeout\n" +
                "on Store_Takeout.store_id = Store.store_id\n" +
                "where (Store.is_cheetah_delivery = ?) and SD.start_delivery_fee<= ? and minimum_price <= ? and (Store_Takeout.status  =?) ";

        List<Ad> adList = this.jdbcTemplate.query(getAdQuery,
                (rs, rowNum) -> new Ad(
                        rs.getInt("ad_id"),
                        rs.getString("ad_image_url"),
                        rs.getString("link_url")
                ));
        List<StoreCategory> categoryList = this.jdbcTemplate.query(getCategoryQuery,
                (rs, rowNum) -> new StoreCategory(
                        rs.getString("category_name"),
                        rs.getString("category_image_url")
                ));
        List<GetStoreRes> storeList = this.jdbcTemplate.query(getHomeQuery,
                (rs, rowNum) -> new GetStoreRes(
                        rs.getInt("store_id"),
                        rs.getString("store_name"),
                        rs.getString("is_cheetah_delivery"),
                        rs.getString("take_out"),
                        rs.getString("delivery_time"),
                        rs.getString("start_delivery_fee"),
                        rs.getString("store_main_image_url"),
                        rs.getInt("Cnt"),
                        rs.getFloat("RAvg")
                ), is_cheetah, start_delivery_fee, minimum_price, is_Delivery);
        return new GetStoreHomeRes(adList, categoryList, storeList);
    }
    public GetStoreOneRes storeOne(int store_id, int userIdx){
        String storeQuery = "select S.store_id, S.store_name, S.is_cheetah_delivery, S.store_main_image_url, SD.delivery_time, SD.start_delivery_fee, SD.minimum_price, count(R.review_id) as cnt, avg(R.review_star) as average\n" +
                "from Store S\n" +
                "inner join Store_Delivery SD\n" +
                "on SD.store_id = S.store_id\n" +
                "inner join Review R\n" +
                "on R.store_id = S.store_id\n" +
                "where S.store_id = ?  \n" +
                "group by S.store_id \n";
        String reviewQuery = "select R.review_id, R.review_star, R.review_image_url, R.review_content\n" +
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
                "where MK.store_id = ? and MK.status = 'Y'" +
                "group by MK.menu_keyword_name";
        String menuDetailQuery = "select M.menu_id, M.menu_name, M.menu_img_url, M.menu_description, M.menu_price\n" +
                "from Menu_Keyword MK\n" +
                "inner join Menu M\n" +
                "on M.menu_id = MK.menu_id\n" +
                "where MK.store_id = ? and MK.type = ? and MK.status = 'Y';";

        List<MenuCategory> menuCategoryList = this.jdbcTemplate.query(menuKeywordQuery,
                (rs, rowNum) -> new MenuCategory(
                        rs.getString("menu_keyword_name"),
                        this.jdbcTemplate.query(menuDetailQuery,
                                (rs1, rowNum1) -> new MenuDetail(
                                        rs1.getInt("menu_id"),
                                        rs1.getString("menu_name"),
                                        rs1.getString("menu_img_url"),
                                        rs1.getString("menu_description"),
                                        rs1.getInt("menu_price")
                                ), store_id, rs.getInt("type"))
                ), store_id);

        return this.jdbcTemplate.queryForObject(storeQuery,
                (rs, rowNum) -> new GetStoreOneRes(
                        rs.getInt("store_id"),
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
                                        rs1.getInt("review_id"),
                                        rs1.getInt("review_star"),
                                        rs1.getString("review_image_url"),
                                        rs1.getString("review_content")
                                ), store_id),
                        menuCategoryList
                ), store_id
        );
    }
    public GetStoreInfoRes getStoreInfo(int storeIdx){
        String storeInfo = "select S.store_id, S.store_name, S.store_address, S.store_phone, S.store_ceo_name, S.business_number, S.search_business_name, S.find_store_tip, S.business_hours, S.store_description\n" +
                "from Store S\n" +
                "where S.store_id = ?";
        return this.jdbcTemplate.queryForObject(storeInfo,
                (rs, rowNum) -> new GetStoreInfoRes(
                        rs.getInt("store_id"),
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

    public GetMenuRes menuInfo(int storeIdx, int menuIdx){
        String getMenu= "select M.menu_id, M.menu_name, M.menu_img_url, M.menu_price\n" +
                "from Menu M \n" +
                "inner join Store S \n" +
                "on S.store_id = M.store_id \n" +
                "where M.menu_id = ? and S.store_id = ? ";

        String getMenuOption = "select MO.menu_option_id, MO.option_name, MO.option_price \n" +
                "from Menu_Option MO\n" +
                "where MO.menu_id = ?";

        return this.jdbcTemplate.queryForObject(getMenu,
                (rs, rowNum) -> new GetMenuRes(
                        rs.getInt("menu_id"),
                        rs.getString("menu_name"),
                        rs.getString("menu_img_url"),
                        rs.getInt("menu_price"),
                        this.jdbcTemplate.query(getMenuOption,
                                (rs1, rowNum1) -> new MenuOption(
                                        rs1.getInt("menu_option_id"),
                                        rs1.getString("option_name"),
                                        rs1.getInt("option_price")
                                ), menuIdx)
                        ),menuIdx, storeIdx
                );
    }

    public int createReview(int user_id, PostReviewReq postReviewReq){
        String storeGet = "select store_id from Order_Info where order_info_id = ?";
        Integer store_id = this.jdbcTemplate.queryForObject(storeGet, int.class, postReviewReq.getOrder_info_id());

        Map<Integer, Integer> is_menu_good = postReviewReq.getIs_menu_good();

        String isGoodQuery = "select is_good from Menu where menu_id = ?;";
        String isGood_Menu_Query = "UPDATE Menu SET is_good=? WHERE menu_id = ?";
        if (postReviewReq.getIs_menu_good().size()!=0){
            Set<Map.Entry<Integer,Integer>> entrySet = postReviewReq.getIs_menu_good().entrySet();
            Iterator<Map.Entry<Integer, Integer>> entryIterator = entrySet.iterator();

            while(entryIterator.hasNext()){
                Map.Entry<Integer, Integer> entry = entryIterator.next();
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                Integer is_good_count = this.jdbcTemplate.queryForObject(isGoodQuery, int.class, key);

                this.jdbcTemplate.update(isGood_Menu_Query, is_good_count+value, key);
            }
        }
        String create = "insert into Review (order_info_id, user_id, review_star, review_image_url, review_content, store_id, is_delivery_good) VALUES (?,?,?,?,?,?,?)";
        Object[] createReviewParams = new Object[]{postReviewReq.getOrder_info_id(), user_id, postReviewReq.getReview_star(),
                postReviewReq.getReview_image_url(), postReviewReq.getReview_content(), store_id, postReviewReq.getIs_delivery_good()};

        return this.jdbcTemplate.update(create, createReviewParams);
    }

    public int modifyReview(int userIdx, int reviewIdx, PatchReviewReq patchReviewReq){
        String updateStar = "UPDATE Review SET review_star=? WHERE review_id=? and user_id=?";
        String updateContent = "UPDATE Review SET review_content=? WHERE review_id=? and user_id=?";
        Object[] patchStarParams = new Object[]{patchReviewReq.getReview_star(), reviewIdx, userIdx};
        Object[] patchContentParams = new Object[]{patchReviewReq.getReview_content(), reviewIdx, userIdx};
        this.jdbcTemplate.update(updateStar, patchStarParams);
        return this.jdbcTemplate.update(updateContent, patchContentParams);
    }

    public GetReviewOrderRes getOrderReview(int orderIdx){
        String ReviewQuery = "select S.store_name, R.review_star, R.review_image_url, R.created_at, R.review_content \n" +
                "from Review R\n" +
                "inner join Store S\n" +
                "on S.store_id = R.store_id\n" +
                "inner join Order_Info OI\n" +
                "on R.order_info_id = OI.order_info_id\n" +
                "where OI.order_info_id = ?";

        String MenuQuery = "select M.menu_name, MO.option_name\n" +
                "from Order_Detail OD\n" +
                "inner join Menu_Option MO\n" +
                "on MO.menu_option_id = OD.menu_option_id\n" +
                "inner join Menu M\n" +
                "on M.menu_id = OD.menu_id \n" +
                "where OD.order_info_id = ?";

        return this.jdbcTemplate.queryForObject(ReviewQuery,
                (rs, row) -> new GetReviewOrderRes(
                        rs.getString("store_name"),
                        rs.getInt("review_star"),
                        rs.getString("review_image_url"),
                        rs.getTimestamp("created_at"),
                        rs.getString("review_content"),
                        this.jdbcTemplate.query(MenuQuery,
                                (rs1, row1) -> new OrderMenu(
                                        rs1.getString("menu_name"),
                                        rs1.getString("option_name")
                                ), orderIdx)
                ), orderIdx);
    }

    public GetReviewStoreRes getReviewStoreRes(int storeIdx){
        String storeQuery = "select S.store_name, count(R.review_star) count_star, avg(R.review_star) avg_star\n" +
                "from Review R\n" +
                "inner join Store S\n" +
                "on S.store_id = R.store_id\n" +
                "where S.store_id = ?";

        String ReviewQuery = "select U.user_name, R.review_content, R.review_image_url, R.review_star, R.created_at, R.order_info_id\n" +
                "from Review R\n" +
                "inner join User U\n" +
                "on U.user_id = R.user_id\n" +
                "inner join Store S\n" +
                "on S.store_id = R.store_id\n" +
                "where S.store_id = ?\n" +
                "order by R.created_at desc";

        String MenuQuery = "select M.menu_name, MO.option_name\n" +
                "from Order_Detail OD\n" +
                "inner join Menu_Option MO\n" +
                "on MO.menu_option_id = OD.menu_option_id\n" +
                "inner join Menu M\n" +
                "on M.menu_id = OD.menu_id\n" +
                "inner join Store S\n" +
                "on OD.store_id = S.store_id\n" +
                "where S.store_id =? and OD.order_info_id = ?";


        List<StoreGetReview> reviewList = this.jdbcTemplate.query(ReviewQuery,
                (rs, rowNum) -> new StoreGetReview(
                        rs.getString("user_name"),
                        rs.getString("review_content"),
                        rs.getString("review_image_url"),
                        rs.getInt("review_star"),
                        rs.getTimestamp("created_at"),
                        this.jdbcTemplate.query(MenuQuery,
                                (rs1, rowNum1) -> new OrderMenu(
                                        rs1.getString("menu_name"),
                                        rs1.getString("option_name")
                                ), storeIdx, rs.getInt("order_info_id"))
                ), storeIdx);


        return this.jdbcTemplate.queryForObject(storeQuery,
                (rs, rowNum) -> new GetReviewStoreRes(
                        rs.getString("store_name"),
                        rs.getInt("count_star"),
                        rs.getFloat("avg_star"),
                        reviewList
                ), storeIdx);
    }
    public int deleteReview(int userIdx, int reviewIdx){
        String deleteReview = "UPDATE Review SET status = 'N' WHERE review_id=? and user_id=?";
        return this.jdbcTemplate.update(deleteReview, reviewIdx, userIdx);
    }

    public int createHelpSign(int userIdx, PostHelpReq postHelpReq){
        String createHelpSignQuery = "insert into Help_Sign (review_id, help_sign_value, user_id) VALUES (?,?,?)";
        return this.jdbcTemplate.update(createHelpSignQuery, postHelpReq.getReview_id(), postHelpReq.getHelp_sign_value(), userIdx);
    }

    public int existsReview(int reviewIdx){
        String createHelpSignQuery = "SELECT EXISTS(SELECT * FROM Review WHERE review_id=? and status= 'Y')";
        return this.jdbcTemplate.update(createHelpSignQuery, reviewIdx);
    }

    public int deleteHelpSign(int userIdx, PatchHelpReq patchHelpReq){
        String deleteHelpSignQuery = "UPDATE Help_Sign SET status = 'N' WHERE review_id=? and user_id=?";
        int update = this.jdbcTemplate.update(deleteHelpSignQuery, patchHelpReq.getReview_id(), userIdx);
        return this.jdbcTemplate.update(deleteHelpSignQuery, patchHelpReq.getReview_id(), userIdx);
    }
}
