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
import com.xqxy.dr.modular.adjustable.DTO.DrConsAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.DTO.DrConsUserAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.VO.DrConsAdjustablePotentialVO;
import com.xqxy.dr.modular.adjustable.VO.GroupCityAdjustVO;
import com.xqxy.dr.modular.adjustable.VO.GroupConsTypeAdjustVO;
import com.xqxy.dr.modular.adjustable.entity.DrConsAdjustablePotential;
import com.xqxy.dr.modular.adjustable.service.DrConsAdjustablePotentialService;
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

@Api(tags = "用户可调节潜力 API接口")
@RestController
@RequestMapping("consAdjustable")
public class DrConsAdjustablePotentialcontroller {

    @Autowired
    private DrConsAdjustablePotentialService drConsAdjustablePotentialService;

    /**
     * 用户可调节潜力分页
     * @param consAdjustablePotentialDTO
     * @return
     */
    @ApiOperation(value = "用户可调节潜力分页", notes = "用户可调节潜力分页", produces = "application/json")
    @PostMapping("/pageConsAdjustable")
    public ResponseData pageConsAdjustable(@RequestBody DrConsAdjustablePotentialDTO consAdjustablePotentialDTO) {
        Page<DrConsAdjustablePotentialVO> page = new Page<>(consAdjustablePotentialDTO.getCurrent(),consAdjustablePotentialDTO.getSize());
        List<DrConsAdjustablePotentialVO> list = drConsAdjustablePotentialService.pageConsAdjustable(page,consAdjustablePotentialDTO);

        page.setRecords(list);
        return ResponseData.success(page);
    }

    /**
     * 用户可调节潜力 新增
     * @param consAdjustablePotential
     * @return
     */
    @ApiOperation(value = "用户可调节潜力 新增", notes = "用户可调节潜力 新增", produces = "application/json")
    @PostMapping("/addConsAdjustable")
    public ResponseData addConsAdjustable(@RequestBody DrConsAdjustablePotential consAdjustablePotential) {
        drConsAdjustablePotentialService.addConsAdjustable(consAdjustablePotential);
        return ResponseData.success();
    }

    /**
     * 用户可调节潜力 修改
     * @param consAdjustablePotential
     * @return
     */
    @ApiOperation(value = "用户可调节潜力 修改", notes = "用户可调节潜力 修改", produces = "application/json")
    @PostMapping("/editConsAdjustable")
    public ResponseData editConsAdjustable(@RequestBody DrConsAdjustablePotential consAdjustablePotential) {
        drConsAdjustablePotentialService.editConsAdjustable(consAdjustablePotential);
        return ResponseData.success();
    }


    /**
     * 用户可调节潜力 删除
     * @param consId
     * @return
     */
    @ApiOperation(value = "用户可调节潜力 删除", notes = "用户可调节潜力 删除", produces = "application/json")
    @GetMapping("/deleteConsAdjustable")
    public ResponseData deleteConsAdjustable(@RequestParam("consId") String consId) {
        drConsAdjustablePotentialService.deleteConsAdjustable(consId);
        return ResponseData.success();
    }


    /**
     * 用户可调节潜力 导出
     * @param consAdjustablePotentialDTO
     * @return
     */
    @ApiOperation(value = "用户可调节潜力 导出", notes = "用户可调节潜力 导出", produces = "application/json")
    @PostMapping("/exportConsAdjustable")
    public ResponseData exportConsAdjustable(@RequestBody DrConsAdjustablePotentialDTO consAdjustablePotentialDTO, HttpServletResponse response, HttpServletRequest request) {
        List<DrConsAdjustablePotentialVO> list = drConsAdjustablePotentialService.exportConsAdjustable(consAdjustablePotentialDTO);

        ExportParams params = new ExportParams("用户可调节潜力导出", "用户可调节潜力导出", ExcelType.XSSF);
        HashMap<String,Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, DrConsAdjustablePotentialVO.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "用户可调节潜力导出");

        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);

        return ResponseData.success();
    }

    /**
     * 用户可调节潜力 分组条件为 市码，查询条件为年度
     * @param consAdjustablePotentialDTO
     * @return
     */
    @ApiOperation(value = "用户可调节潜力 分组条件为 市码，查询条件为年度", notes = "用户可调节潜力 分组条件为 市码，查询条件为年度", produces = "application/json")
    @PostMapping("/groupCityAdjustable")
    public ResponseData groupCityAdjustable(@RequestBody DrConsAdjustablePotentialDTO consAdjustablePotentialDTO) {

        Page<GroupCityAdjustVO> page = new Page<>(consAdjustablePotentialDTO.getCurrent(),consAdjustablePotentialDTO.getSize());
        List<GroupCityAdjustVO> list = drConsAdjustablePotentialService.groupCityAdjustable(page,consAdjustablePotentialDTO);

        page.setRecords(list);
        return ResponseData.success(page);
    }

    /**
     * 用户可调节潜力 分组条件为 用户类型，查询条件为年度 市码
     * @param consAdjustablePotentialDTO
     * @return
     */
    @ApiOperation(value = "用户可调节潜力 分组条件为 用户类型，查询条件为年度 市码", notes = "用户可调节潜力 分组条件为 用户类型，查询条件为年度 市码", produces = "application/json")
    @PostMapping("/groupConsTypeAdjustable")
    public ResponseData groupConsTypeAdjustable(@RequestBody DrConsAdjustablePotentialDTO consAdjustablePotentialDTO) {

        Page<GroupConsTypeAdjustVO> page = new Page<>(consAdjustablePotentialDTO.getCurrent(),consAdjustablePotentialDTO.getSize());
        List<GroupConsTypeAdjustVO> list = drConsAdjustablePotentialService.groupConsTypeAdjustable(page,consAdjustablePotentialDTO);

        page.setRecords(list);
        return ResponseData.success(page);
    }


    /**
     * 分组条件为 用户类型，查询条件为年度 市码 导出
     * @param consAdjustablePotentialDTO
     * @return
     */
    @ApiOperation(value = "用户可调节潜力分组 查询条件为年度 市码 导出", notes = "用户可调节潜力 查询条件为年度 市码 导出", produces = "application/json")
    @PostMapping("/exportConsTypeAdjustable")
    public void exportConsTypeAdjustable(@RequestBody DrConsAdjustablePotentialDTO consAdjustablePotentialDTO, HttpServletResponse response, HttpServletRequest request) {
        List<GroupConsTypeAdjustVO> list1 = drConsAdjustablePotentialService.exportConsTypeAdjustable(consAdjustablePotentialDTO);
        List<GroupCityAdjustVO> list2 = drConsAdjustablePotentialService.exportCityAdjustable(consAdjustablePotentialDTO);

        try {
            //读取模板输入流
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("static/1.xlsx");

            if(resourceAsStream == null){
                System.out.println("未读取到 模板");
            }

            //设置文件名称
            String fileName = URLEncoder.encode("Cons" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), "UTF-8");
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

    /**
     * 电力用户/用户可调节潜力分页
     * @param consAdjustablePotentialDTO
     * @return
     */
    @ApiOperation(value = "电力用户/用户可调节潜力分页", notes = "电力用户/用户可调节潜力分页", produces = "application/json")
    @PostMapping("/pageUserConsAdjustable")
    public ResponseData<Page<DrConsAdjustablePotentialVO>> pageUserConsAdjustable(@RequestBody DrConsUserAdjustablePotentialDTO consAdjustablePotentialDTO) {
        Page<DrConsAdjustablePotentialVO> list = drConsAdjustablePotentialService.pageUserConsAdjustable(consAdjustablePotentialDTO);
        return ResponseData.success(list);
    }
}
