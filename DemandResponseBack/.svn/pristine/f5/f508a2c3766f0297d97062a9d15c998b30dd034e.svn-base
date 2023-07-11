package com.xqxy.dr.modular.newloadmanagement.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.newloadmanagement.param.MobileNumberParam;
import com.xqxy.dr.modular.newloadmanagement.service.MobileNumberChange;
import com.xqxy.dr.modular.newloadmanagement.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mobile")
public class MobileNumber {


    @Autowired
    private MobileNumberChange mobileNumberChange;

    @PostMapping("/Change")
    public ResponseData mobileChange(@RequestBody MobileNumberParam mobileNumberParam) {
        int i = mobileNumberChange.changeNumber(mobileNumberParam);
        if (i == -1) {
            return  ResponseData.fail("-1","验证码不匹配,修改失败","");
//            return new ResponseResult(-1, "验证码不匹配,修改失败", "", false);
        } else if (i == -2) {
            return  ResponseData.fail("-1","电话号码已经存在，修改失败","");
//            return new ResponseResult(-1, "电话号码已经存在，修改失败", "", false);
        } else {

            return  ResponseData.success("修改成功");
//            return new ResponseResult(000000, "修改成功", "", true);
        }
    }
}
