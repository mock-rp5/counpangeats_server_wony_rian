package com.example.demo.src.etc;

import com.example.demo.config.BaseException;
import com.example.demo.src.etc.Model.Res.GetEventRes;
import com.example.demo.src.etc.Model.Res.GetNoticeRes;
import com.example.demo.src.etc.Model.Res.GetQuestionRes;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class EtcService {
    private final EtcDao etcDao;

    public EtcService(EtcDao etcDao) {
        this.etcDao = etcDao;
    }

    public List<GetEventRes> getEventRes() throws BaseException{
        try {
            return etcDao.getEventRes();
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetQuestionRes> getQuestionRes() throws BaseException{
        try {
            return etcDao.getQuestionRes();
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 공지 사항 조회
    public List<GetNoticeRes> getNoticeRes() throws BaseException{
        try {
            return etcDao.getNoticeRes();
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 공지 사항 목록 조회
    public List<List<GetNoticeRes>> getNoticeResList() throws BaseException{
        try {
            return etcDao.getNoticeResList();
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
