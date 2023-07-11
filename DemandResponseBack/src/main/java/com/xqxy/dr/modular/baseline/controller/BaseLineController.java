package com.xqxy.dr.modular.baseline.controller;


import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.baseline.param.BaseLineDetailParam;
import com.xqxy.dr.modular.baseline.param.BaseLineParam;
import com.xqxy.dr.modular.baseline.service.BaseLineDetailService;
import com.xqxy.dr.modular.baseline.service.BaseLineService;
import com.xqxy.dr.modular.event.entity.Event;
import com.xqxy.dr.modular.event.service.EventService;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.dr.modular.strategy.CalculateStrategy;
import com.xqxy.dr.modular.strategy.CalculateStrategyContext;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 基线管理 前端控制器
 * </p>
 *
 * @author chenzhijun
 * @since 2021-10-18
 */
@RestController
@RequestMapping("/baseline/baseline")
public class BaseLineController {

    @Resource
    private BaseLineService baseLineService;

    @Resource
    private BaseLineDetailService baseLineDetailService;

    @Resource
    private ConsContractInfoService consContractInfoService;

    @Value("${calculateStrategy}")
    private String calculateStrategyValue;

    @Resource
    private CalculateStrategyContext calculateStrategyContext;

    @Resource
    private EventService eventService;

    /**
     * @description: 基线新增
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/17 21:12
     */
    //@BusinessLog(title = "基线新增", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "基线新增", notes = "基线新增", produces = "application/json")
    @PostMapping("/add")
    public ResponseData add(@RequestBody @Validated(BaseLineParam.add.class)BaseLineParam baseLineParam) {
        return ResponseData.success(baseLineService.add(baseLineParam));
    }

    /**
     * @description: 基线分页查询
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/17 21:16
     */
    //@BusinessLog(title = "基线分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "基线分页查询", notes = "基线分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody(required = false) BaseLineParam baseLineParam) {
        return ResponseData.success(baseLineService.page(baseLineParam));
    }

    /**
     * @description: 基线详情分页查询
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/17 21:16
     */
    //@BusinessLog(title = "基线详情", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "基线详情", notes = "基线详情", produces = "application/json")
    @PostMapping("/detail")
    public ResponseData detail(@RequestBody @Validated BaseLineDetailParam baseLineDetailParam) {
        return  ResponseData.success(baseLineDetailService.detail(baseLineDetailParam));
    }

    /**
     * @description: 聚合商基线详情分页查询
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/17 21:16
     */
    //@BusinessLog(title = "基线详情", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "聚合商基线详情", notes = "聚合商基线详情", produces = "application/json")
    @PostMapping("/detailCust")
    public ResponseData detailCust(@RequestBody @Validated BaseLineDetailParam baseLineDetailParam) {
        return  ResponseData.success(baseLineDetailService.detailCust(baseLineDetailParam));
    }

    /**
     * @description: 基线详情曲线
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/17 21:16
     */
    //@BusinessLog(title = "基线详情曲线", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "基线详情曲线", notes = "基线详情曲线", produces = "application/json")
    @PostMapping("/getDetailData")
    public ResponseData getDetailData(@RequestBody @Validated BaseLineDetailParam baseLineDetailParam) {
        return  ResponseData.success(baseLineDetailService.getDetailData(baseLineDetailParam));
    }

    /**
     * @description: 聚合商基线详情曲线
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/17 21:16
     */
    //@BusinessLog(title = "基线详情曲线", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "聚合商基线详情曲线", notes = "聚合商基线详情曲线", produces = "application/json")
    @PostMapping("/getDetailDataCust")
    public ResponseData getDetailDataCust(@RequestBody @Validated BaseLineDetailParam baseLineDetailParam) {
        return  ResponseData.success(baseLineDetailService.getDetailDataCust(baseLineDetailParam));
    }

    /**
     * @description: 测试
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/17 21:16
     */
    //@BusinessLog(title = "基线计算测试", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "测试", notes = "测试", produces = "application/json")
    @PostMapping("/getTest")
    public ResponseData getDetailData() {
        String param ="1503278448850800641";
        CalculateStrategy calculateStrategy = calculateStrategyContext.strategySelect(calculateStrategyValue);

        //calculateStrategy.createBaseline(param);
        //基线计算
        //calculateStrategy.createBaseline(param);
        /*String param ="s";
        CalculateStrategy calculateStrategy = calculateStrategyContext.strategySelect(calculateStrategyValue);
        //基线计算
        calculateStrategy.createBaseline();

        //当日效果评估
        calculateStrategy.executeEvaluationListImmediate("s");

        //次日效果评估
        calculateStrategy.executeEvaluationList(param);

        //客户当日
        calculateStrategy.executeCustEvaluationListImmediate(param);

        //客户次日
        calculateStrategy.executeCustEvaluationList(param);

        //同步邀约信息到方案

        //calculateStrategy.synchroInvitationToPlan(param);
        //calculateStrategy.synchroCustInvitationToPlan(param);


        Event event = new Event();
        String id = "1480717978143051778";
        event = eventService.getById(id);
        consContractInfoService.listConsTractInfoByOrg(event);


        CalculateStrategy calculateStrategy = calculateStrategyContext.strategySelect(calculateStrategyValue);
        //客户基线计算
        String param ="s";
        calculateStrategy.createCustBaseline(param);

         */
        return  ResponseData.success();
    }
}

