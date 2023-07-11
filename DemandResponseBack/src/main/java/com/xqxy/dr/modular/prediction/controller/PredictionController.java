package com.xqxy.dr.modular.prediction.controller;


import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.prediction.result.AreaPrediction;
import com.xqxy.dr.modular.prediction.result.ConsPrediction;
import com.xqxy.dr.modular.prediction.service.ConsAbilityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 预测分析
 * </p>
 *
 * @author wang yunfei
 * @since 2021-11-04
 */

@Api(tags = "潜力分析、负荷预测")
@RestController
@RequestMapping("/prediction/prediction")
public class PredictionController {

    @Autowired
    ConsAbilityService consAbilityService;

    /**
     *  区域预测分析
     * @author wang yunfei
     * @date 2021-11-04
     */
    @ApiOperation(value = "区域预测分析", notes = "区域预测分析", produces = "application/json")
    @PostMapping("/queryAreaPrediction")
    public ResponseData queryAreaPrediction(@RequestBody AreaPrediction param) {

        return ResponseData.success(consAbilityService.queryAreaPrediction(param.getAreaId(), param.getStatDate(),param.getAreaType()));
    }

    /**
     * 用户预测分析
     * @author wang yunfei
     * @date 2021-11-05
     * @param param
     * @return
     */
    @ApiOperation(value = "用户预测分析", notes = "用户预测分析", produces = "application/json")
    @PostMapping(value = "/queryConsPrediction")
    public ResponseData queryConsPrediction(@RequestBody ConsPrediction param) {

        return ResponseData.success(consAbilityService.queryConsPrediction(param.getConsId(), param.getStatDate()));
    }


}
