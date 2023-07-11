package com.xqxy.dr.modular.grsg.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.grsg.DTO.CustDTO;
import com.xqxy.dr.modular.grsg.DTO.DrApplyRecDTO;
import com.xqxy.dr.modular.grsg.VO.DrApplyRecVO;
import com.xqxy.dr.modular.grsg.entity.DrApplyRec;
import com.xqxy.dr.modular.grsg.result.GrsgResult;
import com.xqxy.dr.modular.grsg.service.DrApplyRecService;
import com.xqxy.sys.modular.cust.entity.Cons;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 绿色国网业务申请记录表 前端控制器
 * </p>
 *
 * @author liqirui
 * @since 2021-11-17
 */
@RestController
@RequestMapping("/rest/grsg")
public class DrApplyRecController {

    @Autowired
    private DrApplyRecService drApplyRecService;

    /**
     * @description: 申请业务工单接口
     * @param: DrApplyRec
     * @return:
     * @author: liqirui
     * @date: 2021/11/17 14:27
     */
    @ApiOperation(value = "申请业务工单接口", notes = "申请业务工单接口", produces = "application/json")
    @PostMapping("/sendAppOrder")
    public GrsgResult sendAppOrder(@RequestBody DrApplyRecDTO drApplyRecDTO) {
        Boolean flag  = drApplyRecService.sendAppOrder(drApplyRecDTO);
        if(flag){
            return GrsgResult.success();
        }
        return GrsgResult.error();
    }

    /**
     * @description: 用户是否开通业务接口
     * @param: DrApplyRec
     * @return:
     * @author: liqirui
     * @date: 2021/11/17 15:27
     */
    @ApiOperation(value = "用户是否开通业务接口", notes = "用户是否开通业务接口", produces = "application/json")
    @PostMapping("/checkUserOpenApp")
    public GrsgResult checkUserOpenApp(@RequestBody CustDTO custDTO) {
        String flag  = drApplyRecService.checkUserOpenApp(custDTO);
        if("1".equals(flag)){
            return GrsgResult.success("请求成功",flag);
        }
        return GrsgResult.error("请求失败",flag);
    }

    /**
     * @description: 分页列表
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    @ApiOperation(value = "需求响应 分页列表", notes = "需求响应 分页列表", produces = "application/json")
    @PostMapping("/pageDrApplyRec")
    public ResponseData pageDrApplyRec(@RequestBody DrApplyRecDTO drApplyRecDTO) {
        Page<DrApplyRecVO> page = new Page<>(drApplyRecDTO.getCurrent(),drApplyRecDTO.getSize());
        List<DrApplyRecVO> list = drApplyRecService.pageDrApplyRec(page,drApplyRecDTO);
        page.setRecords(list);
        return ResponseData.success("查询成功",page);
    }

    /**
     * @description: 需求响应 审批详情
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    @ApiOperation(value = "需求响应 审批详情", notes = "需求响应 审批详情", produces = "application/json")
    @GetMapping("/detailDrApplyRec")
    public ResponseData detailDrApplyRec(@RequestParam("id") Long id) {
        DrApplyRec drApplyRec = drApplyRecService.detailDrApplyRec(id);
        return ResponseData.success("查询成功",drApplyRec);
    }

    /**
     * @description: 需求响应 审批修改
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    @ApiOperation(value = "需求响应 审批修改", notes = "需求响应 审批修改", produces = "application/json")
    @PostMapping("/editDrApplyRec")
    public ResponseData editDrApplyRec(@RequestBody DrApplyRecDTO drApplyRecDTO) {
        //验证 list 是否为空
        if(ObjectUtils.isEmpty(drApplyRecDTO.getList())){
            return ResponseData.success("未查到对应用户数据");
        }
        drApplyRecService.editDrApplyRec(drApplyRecDTO);
        return ResponseData.success();
    }


    /**
     * @description: 需求响应 审批  查询按钮
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    @ApiOperation(value = "需求响应 审批  查询按钮", notes = "需求响应 审批  查询按钮", produces = "application/json")
    @GetMapping("/listCons")
    public ResponseData listCons(@RequestParam("consNoList") String consNoList) {
        if(ObjectUtils.isEmpty(consNoList)){
            return ResponseData.fail("必传参数 consNoList 为空");
        }
        List<Cons> list = drApplyRecService.listCons(consNoList);
        return ResponseData.success("查询成功",list);
    }

}

