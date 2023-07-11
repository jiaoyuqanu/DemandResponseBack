package com.xqxy.sys.modular.cust.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.sys.modular.cust.entity.ConsTopologyFile;
import com.xqxy.sys.modular.cust.service.ConsTopologyFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户拓扑图表 前端控制器
 * </p>
 *
 * @author liqirui
 * @since 2022-05-05
 */
@RestController
@RequestMapping("/consTopologyFile")
public class ConsTopologyFileController {

    @Autowired
    private ConsTopologyFileService consTopologyFileService;



    /**
     * 批量新增 用户拓扑图
     * @author lqr
     * @return
     */
    @ApiOperation(value = "新增 用户拓扑图 ", notes = "新增 用户拓扑图", produces = "application/json")
    @PostMapping("/insertBatch")
    public ResponseData insertBatch(@RequestBody List<ConsTopologyFile> consTopoologyFiles) {
        if(CollectionUtils.isEmpty(consTopoologyFiles)){
            return ResponseData.fail("500","参数为空","");
        }
        for (ConsTopologyFile consTopoologyFile : consTopoologyFiles) {
            if(consTopoologyFile != null){
                if(StringUtils.isEmpty(consTopoologyFile.getConsId())){
                    return ResponseData.fail("500","用户编号为空","");
                }
            }
        }
        ResponseData responseData = consTopologyFileService.insertBatch(consTopoologyFiles);
        return responseData;
    }


    /**
     * 根据主键删除 用户拓扑图
     * @author lqr
     * @return
     */
    @ApiOperation(value = "根据主键删除 用户拓扑图 ", notes = "根据主键删除 用户拓扑图", produces = "application/json")
    @GetMapping("/delete")
    public ResponseData delete(@RequestParam(value = "id") Long id) {
        if(id == null){
            return ResponseData.fail("500","参数为空","");
        }
        consTopologyFileService.deleteByFileId(id);
        return ResponseData.success();
    }

}

