package com.xqxy.dr.modular.newloadmanagement.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.event.param.ConsInvitationParam;
import com.xqxy.dr.modular.newloadmanagement.service.CitiesInvitedService;
import com.xqxy.dr.modular.newloadmanagement.util.ResponseResult;
import com.xqxy.dr.modular.newloadmanagement.vo.UserSignalDetailVo;
import com.xqxy.dr.modular.project.VO.DrConsContractDetailsVO;
import com.xqxy.dr.modular.project.params.ConsContractParam;
import com.xqxy.dr.modular.project.result.Region;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/citiesinvited")
public class CitiesInvitedController {

    @Autowired
    CitiesInvitedService citiesInvitedService;

    @RequestMapping(value = "/querycitiesinvited",method = RequestMethod.POST)
    public ResponseData queryCitiesInvited(@RequestBody ConsInvitationParam consInvitationParam){

        Page page = citiesInvitedService.queryCitiesInvited(consInvitationParam);

        return ResponseData.success(page);
    }


    @ApiOperation(value = "认约用户情况导出", notes = "认约用户情况导出", produces = "application/json")
    @PostMapping("/exportDrConsInvitation")
    public void exportDrConsInvitation(@RequestBody ConsInvitationParam consInvitationParam, HttpServletResponse response, HttpServletRequest request) {

        List<UserSignalDetailVo> list = citiesInvitedService.queryDrConsInvitation(consInvitationParam);


        try {
            // 读取模板输入流
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("static/4.xlsx");

            if (resourceAsStream == null) {
                System.out.println("未读取到 模板");
            }

            // 设置文件名称
            String fileName = URLEncoder.encode("Cons" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), "UTF-8");
            // 设置文件类型
            response.setContentType("application/vnd.ms-excel");
            // 设置编码格式
            response.setCharacterEncoding("utf-8");
            // https://www.jb51.net/article/30565.htm Content-Disposition 使用说明
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 创建excel
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(resourceAsStream).build();
            // 创建sheet
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

            // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 a，然后多个list必须用 FillWrapper包裹
            excelWriter.fill(new FillWrapper("a", list), fillConfig, writeSheet);
//            excelWriter.fill(new FillWrapper("b", list2), fillConfig, writeSheet);
//
            Map map = new HashMap<>();
            excelWriter.fill(map, writeSheet);

            // 别忘记关闭流
            excelWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
