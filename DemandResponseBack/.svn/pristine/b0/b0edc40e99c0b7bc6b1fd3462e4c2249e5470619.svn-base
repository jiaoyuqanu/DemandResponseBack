package com.xqxy.dr.modular.newloadmanagement.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.event.param.ConsInvitationParam;
import com.xqxy.dr.modular.newloadmanagement.param.BaseLineParam;
import com.xqxy.dr.modular.newloadmanagement.service.CitiesInvitedService;
import com.xqxy.dr.modular.newloadmanagement.service.ReportDataService;
import com.xqxy.dr.modular.newloadmanagement.vo.UserSignalDetailVo;
import com.xqxy.executor.service.jobhandler.BaselineAndMeasurementByJdbcJob;
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
@RequestMapping("/reportData")
public class ReportDataController {

    @Autowired
    ReportDataService reportDataService;

    @Autowired
    BaselineAndMeasurementByJdbcJob baselineAndMeasurementByJdbcJob;

    @RequestMapping(value = "/reportBaseLine",method = RequestMethod.POST)
    public ResponseData responseData(@RequestBody BaseLineParam baseLineParam){
        baselineAndMeasurementByJdbcJob.saveBaselineToDatabase2(baseLineParam.getParam(),baseLineParam.getPoint());

        baselineAndMeasurementByJdbcJob.saveFrozenToDatabase2(baseLineParam.getParam());
        return ResponseData.success();
    }




}
