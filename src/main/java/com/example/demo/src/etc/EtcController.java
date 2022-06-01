package com.example.demo.src.etc;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.etc.Model.Res.GetEventRes;
import com.example.demo.src.etc.Model.Res.GetNoticeRes;
import com.example.demo.src.etc.Model.Res.GetQuestionRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/etc")
public class EtcController {
    @Autowired
    private final EtcService etcService;
    @Autowired
    private final JwtService jwtService;

    public EtcController(EtcService etcService, JwtService jwtService) {
        this.etcService = etcService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/events")
    public BaseResponse<List<GetEventRes>> getEvent() throws BaseException {
        return new BaseResponse<>(etcService.getEventRes());
    }

    @ResponseBody
    @GetMapping("/faq")
    public BaseResponse<List<GetQuestionRes>> getQuestion() throws BaseException {
        return new BaseResponse<>(etcService.getQuestionRes());
    }
    @ResponseBody
    @GetMapping("/notices")
    public BaseResponse<List<GetNoticeRes>> getNotice() throws BaseException {
        return new BaseResponse<>(etcService.getNoticeRes());
    }
}
