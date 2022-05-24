package com.example.demo.src.store;

import com.example.demo.src.store.model.GetStoreHomeRes;
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
}
