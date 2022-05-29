package com.example.demo.src.category;


import com.example.demo.src.category.model.CategorySimple;
import com.example.demo.src.category.model.PopularSearch;
import com.example.demo.src.category.model.Res.GetSearchRes;
import com.example.demo.src.category.model.Search;
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

    public int createSearch(int userIdx, String category_name) {
        String createSearchQuery = "insert into Search(user_id, category_name) values (?,?);";
        Object[] createSearchParams = new Object[]{userIdx, category_name};

        this.jdbcTemplate.update(createSearchQuery, createSearchParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);

    }

    public GetSearchRes getSearchList(int userIdx) {
        String getUpdateTimeQuery = "SELECT date_format(UPDATE_TIME,'%m.%d') as update_time\n" +
                "FROM INFORMATION_SCHEMA.TABLES\n" +
                "WHERE table_name='Popular_Search';";
        String getPopularSearchListQuery = "select p_search_id,search_rank,category_name from Popular_Search\n" +
                "where status='Y'\n" +
                "order by search_rank;";
        String getSearchListQuery = "select search_id, category_name, date_format(created_at,'%m.%d') as created_at\n" +
                "from Search\n" +
                "where user_id=? and status='Y'\n" +
                "order by created_at DESC;";
        int user_id = userIdx;

        return this.jdbcTemplate.queryForObject(getUpdateTimeQuery,
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
                                        rs2.getString("created_at")),
                                user_id)));
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

    public void deleteAllSearch(int userIdx){
        String deleteAllSearchQuery="update Search set status='N'\n" +
                "where user_id=?;";
        this.jdbcTemplate.update(deleteAllSearchQuery,userIdx);

    }
}
