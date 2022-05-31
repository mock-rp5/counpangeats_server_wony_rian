package com.example.demo.src.category;


import com.example.demo.src.category.model.*;
import com.example.demo.src.category.model.Res.GetCategoryDetailRes;
import com.example.demo.src.category.model.Res.GetSearchRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CategoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<CategorySimple> getCategoryList() {
        String getCategoryListQuery = "select category_id, category_image_url, category_name\n" +
                "from Category;";
        return this.jdbcTemplate.query(getCategoryListQuery,
                (rs, rowNum) -> new CategorySimple(
                        rs.getInt("category_id"),
                        rs.getString("category_image_url"),
                        rs.getString("category_name")
                ));
    }

    public GetCategoryDetailRes getCategoryDetail(int categoryIdx) {
        String getCategoryBannerListQuery="select category_id, category_image_url, category_name\n" +
                "from Category;";
        String getCategoryDetailListQuery="select S.store_id as store_id, store_image_url, store_name, is_cheetah_delivery,takeout_time, round(avg(review_star),1) as avg_review, count(review_star) as count_review, start_delivery_fee, is_takeout\n" +
                "from (select * from Store_Category\n" +
                "where category_id=?) SC\n" +
                "join Store_Image SI on SC.store_id = SI.store_id\n" +
                "join Store S on SC.store_id = S.store_id\n" +
                "join Store_Takeout ST on S.store_id = ST.store_id\n" +
                "join Review R on R.store_id = S.store_id\n" +
                "join Store_Delivery SD on S.store_id = SD.store_id\n" +
                "group by S.store_id;";

        return new GetCategoryDetailRes(
                        this.jdbcTemplate.query(getCategoryBannerListQuery,
                                (rs1,rowNum1)->new CategorySimple(
                                        rs1.getInt("category_id"),
                                        rs1.getString("category_image_url"),
                                        rs1.getString("category_name"))),
                        this.jdbcTemplate.query(getCategoryDetailListQuery,
                                (rs2,rowNum2)->new StoreInfo(
                                        rs2.getInt("store_id"),
                                        rs2.getString("store_image_url"),
                                        rs2.getString("store_name"),
                                        rs2.getString("is_cheetah_delivery"),
                                        rs2.getString("takeout_time"),
                                        rs2.getDouble("avg_review"),
                                        rs2.getInt("count_review"),
                                        rs2.getInt("start_delivery_fee"),
                                        rs2.getString("is_takeout")
                                ), categoryIdx));

    }

    public List<StoreInfo> getSearchStoreList(String categoryName){
        String getSearchStoreListQuery="select S.store_id as store_id, store_image_url, store_name, is_cheetah_delivery,takeout_time, round(avg(review_star),1) as avg_review, count(review_star) as count_review, start_delivery_fee, is_takeout\n" +
                "from (select * from Store_Category\n" +
                "where category_id=(select category_id\n" +
                "from Category\n" +
                "where category_name=?)) SC\n" +
                "join Store_Image SI on SC.store_id = SI.store_id\n" +
                "join Store S on SC.store_id = S.store_id\n" +
                "join Store_Takeout ST on S.store_id = ST.store_id\n" +
                "join Review R on R.store_id = S.store_id\n" +
                "join Store_Delivery SD on S.store_id = SD.store_id\n" +
                "group by S.store_id";

        return this.jdbcTemplate.query(getSearchStoreListQuery,
                (rs,rowNum)->new StoreInfo(
                        rs.getInt("store_id"),
                        rs.getString("store_image_url"),
                        rs.getString("store_name"),
                        rs.getString("is_cheetah_delivery"),
                        rs.getString("takeout_time"),
                        rs.getDouble("avg_review"),
                        rs.getInt("count_review"),
                        rs.getInt("start_delivery_fee"),
                        rs.getString("is_takeout")
                ), categoryName);
    }

    public int createSearch(int userIdx, String category_name) {
        String createSearchQuery = "insert into Search(user_id, category_name) values (?,?);";
        Object[] createSearchParams = new Object[]{userIdx, category_name};

        this.jdbcTemplate.update(createSearchQuery, createSearchParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }
    public String getSearchStatus(int userIdx, int searchIdx){
        String getSearchStatusQuery="select status from Search\n" +
                "where user_id=? and search_id=?;";
        Object[] getSearchStatusParams=new Object[]{userIdx,searchIdx};
        return this.jdbcTemplate.queryForObject(getSearchStatusQuery,String.class,getSearchStatusParams);
    }

    public int deleteOneSearch(int userIdx, int searchIdx){
        String deleteSearchOneQuery="update Search set status='N'\n" +
                "where user_id=? and search_id=?;";
        String deleteSearchIdQuery="select search_id from Search\n" +
                "where user_id=? and search_id=?";
        Object[] deleteSearchOneParams=new Object[]{userIdx, searchIdx};
        this.jdbcTemplate.update(deleteSearchOneQuery,deleteSearchOneParams);

        return jdbcTemplate.queryForObject(deleteSearchIdQuery,int.class,deleteSearchOneParams);
    }

    public void deleteAllSearch(int userIdx) {
        String deleteAllSearchQuery = "update Search set status='N'\n" +
                "where user_id=?;";
        this.jdbcTemplate.update(deleteAllSearchQuery, userIdx);
    }

    public GetSearchRes getSearchList(int userIdx) {
        String getUpdatedPopularQuery = "SELECT date_format(UPDATE_TIME,'%m.%d') as update_time\n" +
                "FROM INFORMATION_SCHEMA.TABLES\n" +
                "WHERE table_name='Popular_Search';";
        String getPopularSearchListQuery = "select p_search_id,search_rank,category_name from Popular_Search\n" +
                "where status='Y'\n" +
                "order by search_rank;";
        String getSearchListQuery = "select search_id, category_name, date_format(created_at,'%m.%d') as created_at\n" +
                "from Search\n" +
                "where user_id=? and status='Y'\n" +
                "order by created_at DESC;";
        int getSearchListParam = userIdx;

        return this.jdbcTemplate.queryForObject(getUpdatedPopularQuery,
                (rs, rowNum) -> new GetSearchRes(
                        rs.getString("update_time"),
                        this.jdbcTemplate.query(getPopularSearchListQuery,
                                (rs1, rowNum1) -> new PopularSearch(
                                        rs1.getInt("p_search_id"),
                                        rs1.getInt("search_rank"),
                                        rs1.getString("category_name"))),
                        this.jdbcTemplate.query(getSearchListQuery,
                                (rs2, rowNum2) -> new Search(
                                        rs2.getInt("search_id"),
                                        rs2.getString("category_name"),
                                        rs2.getString("created_at")), getSearchListParam))
        );
    }
}
