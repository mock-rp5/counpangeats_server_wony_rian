package com.example.demo.src.etc;

import com.example.demo.src.etc.Model.Res.GetEventRes;
import com.example.demo.src.etc.Model.Res.GetNoticeRes;
import com.example.demo.src.etc.Model.Res.GetQuestionRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EtcDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //진행 중 이벤트
    public List<GetEventRes> getEventRes(){
        String query = "select ad_image_url, link_url from Ad;";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetEventRes(
                        rs.getString("ad_image_url"),
                        rs.getString("link_url")
                ));
    }

    //자주 묻는 질문
    public List<GetQuestionRes> getQuestionRes(){
        String query = "select category_name, question_name, question_answer from Question;";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetQuestionRes(
                        rs.getString("category_name"),
                        rs.getString("question_name"),
                        rs.getString("question_answer")
                ));
    }


    //공지사항 조회
    public List<GetNoticeRes> getNoticeRes(){
        String query = "select date_format(created_at,'%Y-%m-%d') as created_at, notice_name, notice_content\n" +
                "from Notice;";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetNoticeRes(
                        rs.getString("created_at"),
                        rs.getString("notice_name"),
                        rs.getString("notice_content")
                ));
    }

    //공지사항 조회
    public List<List<GetNoticeRes>> getNoticeResList(){
        String maxQuery = "select max(notice_id) from Notice;";
        Integer maxRow = this.jdbcTemplate.queryForObject(maxQuery, int.class);

        List<List<GetNoticeRes>> arr = new ArrayList<>();
        for(int k=1; k<=maxRow; k+=10){
            String query = "select date_format(created_at,'%Y-%m-%d') as created_at, notice_name, notice_content\n" +
                    "from Notice order by notice_id desc limit ?, ?;";
            arr.add(this.jdbcTemplate.query(query,
                    (rs, rowNum) -> new GetNoticeRes(
                            rs.getString("created_at"),
                            rs.getString("notice_name"),
                            rs.getString("notice_content")
                    ), k, k + 9));
        }
        return arr;
    }
}
