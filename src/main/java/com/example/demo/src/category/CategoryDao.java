package com.example.demo.src.category;


import com.example.demo.src.category.model.CategorySimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CategoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate=new JdbcTemplate(dataSource);
    }

    public List<CategorySimple> getCategoryList(){
        String getCategoryListQuery="select category_id, category_image_url, category_name\n" +
                "from Category;";
        return this.jdbcTemplate.query(getCategoryListQuery,
                (rs,rowNum)->new CategorySimple(
                        rs.getInt("category_id"),
                        rs.getString("category_image_url"),
                        rs.getString("category_name")
                ));

    }
}
