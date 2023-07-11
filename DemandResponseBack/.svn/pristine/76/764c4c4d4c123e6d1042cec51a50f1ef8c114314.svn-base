package com.xqxy.sys.modular.sms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.client.SmsSendCilent;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.sys.modular.sms.entity.SysSmsSend;
import com.xqxy.sys.modular.sms.param.SmsSendParam;
import com.xqxy.sys.modular.sms.service.SysSmsSendService;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 短信发送记录 前端控制器
 * </p>
 *
 * @author Caoj
 * @since 2021-10-21
 */
@RestController
@RequestMapping("/sms/sys-sms-send")
public class SysSmsSendController {
    @Resource
    private SysSmsSendService smsSendService;

    /**
     * 短信服务
     */
    @Resource
    private SmsSendCilent smsSendCilent;

    @ApiOperation(value = "短信集合测试", notes = "短信添加", produces = "application/json")
    @PostMapping("/test")
    public ResponseData<?> list(@RequestBody SysSmsSend sysSmsSend) {

        return ResponseData.success();
    }


    //    @BusinessLog(title = "短信添加", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "短信添加", notes = "短信添加", produces = "application/json")
    @PostMapping("/add")
    public ResponseData<?> add(@RequestBody SysSmsSend sysSmsSend) {
        List<SysSmsSend> smsSendList = new ArrayList<>();
        smsSendList.add(sysSmsSend);
        JSONObject result = smsSendCilent.saveBatch(smsSendList);
        return ResponseData.success();
    }

    @ApiOperation(value = "短信生成", notes = "短信生成", produces = "application/json")
    @PostMapping("/generateSms")
    public ResponseData<?> generateSms(@RequestBody @Validated(SmsSendParam.generate.class) SmsSendParam smsSendParam) {
        String sms = smsSendService.generateSms(smsSendParam.getBusinessRela(), smsSendParam.getTemplateType(), smsSendParam.getUserType(), smsSendParam.getContent());
        return ResponseData.success(sms);
    }

    @ApiOperation(value = "短信下发", notes = "短信下发", produces = "application/json")
    @PostMapping("/issueSms")
    public ResponseData<?> issueSms(@RequestBody @Validated(SmsSendParam.qeuryById.class) SmsSendParam smsSendParam) {
        smsSendService.issueSms(smsSendParam);
        return ResponseData.success();
    }

    @ApiOperation(value = "短信查询", notes = "短信查询", produces = "application/json")
    @PostMapping("/getSmsByBusiness")
    public ResponseData<?> getSmsByBusiness(@RequestBody SmsSendParam smsSendParam) {
        Page<SysSmsSend> smsByBusiness = smsSendService.getSmsByBusiness(smsSendParam);
        if (null == smsByBusiness) {
            smsByBusiness = new Page<SysSmsSend>();
        }
        return ResponseData.success(smsByBusiness);
    }

    @ApiOperation(value = "未发送短信修改", notes = "未发送短信修改", produces = "application/json")
    @PostMapping("/updateTemplate")
    public ResponseData<?> updateTemplate(@RequestBody SmsSendParam smsSendParam) {
        JSONObject jsonObject = smsSendCilent.updateTemplate(smsSendParam);
        if (jsonObject.get("code").equals("000000")) {
            return ResponseData.success(jsonObject);
        } else {
            return ResponseData.fail("-1", "仅未发送状态短信模板可修改！", jsonObject);
        }
    }
}

