package com.xqxy.dr.modular.adjustable.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.adjustable.DTO.DrDeviceAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.VO.*;
import com.xqxy.dr.modular.adjustable.entity.DrDeviceAdjustablePotential;
import com.xqxy.dr.modular.adjustable.service.DrDeviceAdjustablePotentialService;
import io.swagger.annotations.Api;
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

@Api(tags = "设备可调节潜力 API接口")
@RestController
@RequestMapping("deviceAdjustable")
public class DrDeviceAdjustablePotentialController {
    
    @Autowired
    private DrDeviceAdjustablePotentialService drDeviceAdjustablePotentialService;

    /**
     * 设备可调节潜力分页
     * @param drDeviceAdjustablePotentialDTO
     * @return
     */
    @ApiOperation(value = "设备可调节潜力 分页", notes = "设备可调节潜力 分页", produces = "application/json")
    @PostMapping("/pageDeviceAdjustable")
    public ResponseData pageDeviceAdjustable(@RequestBody DrDeviceAdjustablePotentialDTO drDeviceAdjustablePotentialDTO) {
        Page<DrDeviceAdjustablePotentialVO> page = new Page<>(drDeviceAdjustablePotentialDTO.getCurrent(),drDeviceAdjustablePotentialDTO.getSize());
        List<DrDeviceAdjustablePotentialVO> list = drDeviceAdjustablePotentialService.pageDeviceAdjustable(page,drDeviceAdjustablePotentialDTO);

        page.setRecords(list);
        return ResponseData.success(page);
    }


    /**
     * 设备可调节潜力 新增
     * @param DeviceAdjustablePotential
     * @return
     */
    @ApiOperation(value = "设备可调节潜力 新增", notes = "设备可调节潜力 新增", produces = "application/json")
    @PostMapping("/addDeviceAdjustable")
    public ResponseData addDeviceAdjustable(@RequestBody DrDeviceAdjustablePotential DeviceAdjustablePotential) {
        drDeviceAdjustablePotentialService.addDeviceAdjustable(DeviceAdjustablePotential);
        return ResponseData.success();
    }

    /**
     * 设备可调节潜力 修改
     * @param DeviceAdjustablePotential
     * @return
     */
    @ApiOperation(value = "设备可调节潜力 修改", notes = "设备可调节潜力 修改", produces = "application/json")
    @PostMapping("/editDeviceAdjustable")
    public ResponseData editDeviceAdjustable(@RequestBody DrDeviceAdjustablePotential DeviceAdjustablePotential) {
        drDeviceAdjustablePotentialService.editDeviceAdjustable(DeviceAdjustablePotential);
        return ResponseData.success();
    }


    /**
     * 设备可调节潜力 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "设备可调节潜力 删除", notes = "设备可调节潜力 删除", produces = "application/json")
    @GetMapping("/deleteDeviceAdjustable")
    public ResponseData deleteDeviceAdjustable(@RequestParam("id") Long id) {
        drDeviceAdjustablePotentialService.deleteDeviceAdjustable(id);
        return ResponseData.success();
    }


    /**
     * 设备可调节潜力 导出
     * @param DeviceAdjustablePotentialDTO
     * @return
     */
    @ApiOperation(value = "设备可调节潜力 导出", notes = "设备可调节潜力 导出", produces = "application/json")
    @PostMapping("/exportDeviceAdjustable")
    public ResponseData exportDeviceAdjustable(@RequestBody DrDeviceAdjustablePotentialDTO DeviceAdjustablePotentialDTO, HttpServletResponse response, HttpServletRequest request) {
        List<DrDeviceAdjustablePotentialVO> list = drDeviceAdjustablePotentialService.exportDeviceAdjustable(DeviceAdjustablePotentialDTO);

        ExportParams params = new ExportParams("设备可调节潜力导出", "设备可调节潜力导出", ExcelType.XSSF);
        HashMap<String,Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, DrDeviceAdjustablePotential.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "设备可调节潜力导出");

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);

        return ResponseData.success();
    }


    /**
     * 设备情况统计列表 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    @ApiOperation(value = "设备可调节潜力列表 分组条件为 市码，查询条件为年度", notes = "设备可调节潜力列表 分组条件为 市码，查询条件为年度", produces = "application/json")
    @PostMapping("/groupDeviceAdjustable")
    public ResponseData groupDeviceAdjustable(@RequestBody DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO) {

        Page<GroupCityDeviceAdjustVO> page = new Page<>(deviceAdjustablePotentialDTO.getCurrent(),deviceAdjustablePotentialDTO.getSize());
        List<GroupCityDeviceAdjustVO> list = drDeviceAdjustablePotentialService.groupDeviceAdjustable(page,deviceAdjustablePotentialDTO);

        page.setRecords(list);
        return ResponseData.success(page);
    }


    /**
     * 设备情况统计详情 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    @ApiOperation(value = "设备可调节潜力 分组条件为 市码，查询条件为年度", notes = "设备可调节潜力 分组条件为 市码，查询条件为年度", produces = "application/json")
    @PostMapping("/groupDeviceTypeAdjustable")
    public ResponseData groupDeviceTypeAdjustable(@RequestBody DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO) {
        Page<GroupDeviceTypeAdjustVO> page = new Page<>(deviceAdjustablePotentialDTO.getCurrent(),deviceAdjustablePotentialDTO.getSize());
        List<GroupDeviceTypeAdjustVO> list = drDeviceAdjustablePotentialService.groupDeviceTypeAdjustable(page,deviceAdjustablePotentialDTO);

        page.setRecords(list);
        return ResponseData.success(page);
    }

    /**
     * 分组条件为 用户类型，查询条件为年度 市码 导出
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    @ApiOperation(value = "用户可调节潜力 导出", notes = "用户可调节潜力 导出", produces = "application/json")
    @PostMapping("/exportDeviceTypeAdjustable")
    public void exportConsTypeAdjustable(@RequestBody DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO, HttpServletResponse response, HttpServletRequest request) {

        List<GroupDeviceTypeAdjustVO> list1 = drDeviceAdjustablePotentialService.exportGroupDeviceTypeAdjustable(deviceAdjustablePotentialDTO);
        List<GroupCityDeviceAdjustVO> list2 = drDeviceAdjustablePotentialService.exportGroupDeviceAdjustable(deviceAdjustablePotentialDTO);

        try {
            //读取模板输入流
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("static/2.xlsx");

            if(resourceAsStream == null){
                System.out.println("未读取到 模板");
            }

            //设置文件名称
            String fileName = URLEncoder.encode("" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), "UTF-8");
            //设置文件类型
            response.setContentType("application/vnd.ms-excel");
            //设置编码格式
            response.setCharacterEncoding("utf-8");
            // https://www.jb51.net/article/30565.htm Content-Disposition 使用说明
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //创建excel
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(resourceAsStream).build();
            //创建sheet
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

            // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 a，然后多个list必须用 FillWrapper包裹
            excelWriter.fill(new FillWrapper("a", list1), fillConfig, writeSheet);
            excelWriter.fill(new FillWrapper("b", list2), fillConfig, writeSheet);
//
            Map map = new HashMap<>();
            map.put("total","");
            excelWriter.fill(map, writeSheet);

            // 别忘记关闭流
            excelWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
