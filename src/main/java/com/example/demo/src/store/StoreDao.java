package com.example.demo.src.store;

import com.example.demo.src.store.model.GetStoreHomeRes;
import com.example.demo.src.store.model.GetStoreRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StoreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetStoreHomeRes> getHome() {
        String getHomeQuery = "select Store.store_name, Store.is_cheetah_delivery, Store.store_main_image_url from Store";
        List<GetStoreHomeRes> list = new ArrayList<>();
        this.jdbcTemplate.query(getHomeQuery,
                (rs, rowNum) -> list.add(new GetStoreHomeRes(
                        rs.getString("store_name"),
                        rs.getString("is_cheetah_delivery"),
                        rs.getString("store_main_image_url"))));
        return list;
    }
}
