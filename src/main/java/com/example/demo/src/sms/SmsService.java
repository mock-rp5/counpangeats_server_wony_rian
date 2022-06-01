package com.example.demo.src.sms;

import com.example.demo.config.BaseException;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.example.demo.config.BaseResponseStatus.FAIL_SEND_MESSAGE;

@Service
public class SmsService {
    public void certifiedPhoneNumber(String phoneNumber, String numStr) throws BaseException {
        String api_key = "NCSHISBKI2NF7JWH";
        String api_secret = "9OLIDGF4WD4LHQREY3WVPMUZLUAJ5YVY";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", "01052818009");       // 발신전화번호.
        params.put("type", "SMS");
        params.put("text", "[쿠팡이츠!!!] 회원가입 인증 메시지 : 인증번호는" + "["+numStr+"]" + "입니다.");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
            throw new BaseException(FAIL_SEND_MESSAGE);
        }
    }
}
