package com.xqxy.dr.modular.device.controller;


import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.lang.Dict;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.enums.DrSysDictDataEnum;
import com.xqxy.core.exception.SystemErrorType;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.adjustable.VO.DrConsAdjustablePotentialVO;
import com.xqxy.dr.modular.device.VO.DeviceAdjustableBaseVO;
import com.xqxy.dr.modular.device.entity.DeviceAdjustableBase;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.device.entity.TDevBranch;
import com.xqxy.dr.modular.device.model.DeviceBaseModel;
import com.xqxy.dr.modular.device.model.DictModel;
import com.xqxy.dr.modular.device.param.DeviceAdjustableBaseParam;
import com.xqxy.dr.modular.device.service.DeviceAdjustableBaseService;
import com.xqxy.dr.modular.device.service.SysOrgsService;
import com.xqxy.dr.modular.evaluation.entity.EvaluTask;
import com.xqxy.dr.modular.project.entity.CustContractInfo;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.enums.CustStatusEnum;
import com.xqxy.sys.modular.cust.result.Result;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 设备可调节基础信息 前端控制器
 * </p>
 *
 * @author hu xingxing
 * @since 2021-11-08
 */
@RestController
@RequestMapping(value = "/device/device-adjustable-base",produces = "application/json")
public class DeviceAdjustableBaseController {

    @Resource
    private DeviceAdjustableBaseService deviceAdjustableBaseService;

    @Autowired
    private SysOrgsService sysOrgsService;

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 设备生成
     * @param deviceAdjustableBaseParam
     * @return
     * @author hu xingxing
     * @date 2021-11-08 10:00
     */
    //@BusinessLog(title = "设备生成", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "设备生成", notes = "设备生成", produces = "application/json")
    @PostMapping("/generateDevice")
    public ResponseData generateDevice(@RequestBody DeviceAdjustableBaseParam deviceAdjustableBaseParam) {

        ResponseData responseData = deviceAdjustableBaseService.generateDevice(deviceAdjustableBaseParam);
        return responseData;
    }

    /**
     * 电力专责用户 列表
     * @param deviceAdjustableBaseParam
     * @return
     * @author hu xingxing
     * @date 2021-11-08 10:20
     */
//    @BusinessLog(title = "设备分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "设备分页查询", notes = "设备分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody DeviceAdjustableBaseParam deviceAdjustableBaseParam) {

        return ResponseData.success(deviceAdjustableBaseService.page(deviceAdjustableBaseParam));
    }

    /**
     * 设备下拉框查询
     * @param deviceAdjustableBase
     * @return
     * @author lqr
     * @date 2021-12-24 10:20
     */
//    @BusinessLog(title = "设备分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "设备下拉框查询", notes = "设备下拉框查询", produces = "application/json")
    @PostMapping("/listDeviceBase")
    public ResponseData listDeviceBase(@RequestBody DeviceAdjustableBase deviceAdjustableBase) {
        List<DeviceAdjustableBase> list = deviceAdjustableBaseService.listDeviceBase(deviceAdjustableBase);
        return ResponseData.success(list);
    }

    /**
     * 设备ID是否重复
     * @param deviceId
     * @return
     * @author hu xingxing
     * @date 2021-12-24 10:00
     */
    @ApiOperation(value = "设备ID是否重复", notes = "设备ID是否重复", produces = "application/json")
    @GetMapping("/isDeviceIdRepeat")
    public ResponseData isDeviceIdRepeat(@RequestParam(name="deviceId",required = true) String deviceId) {

        return ResponseData.success(deviceAdjustableBaseService.isDeviceIdRepeat(deviceId));
    }

    /**
     * 删除设备基础档案
     * @param id
     * @return
     * @author hu xingxing
     * @date 2021-12-24 10:00
     */
    @ApiOperation(value = "删除设备基础档案", notes = "删除设备基础档案", produces = "application/json")
    @GetMapping("/delDevice")
    public ResponseData delDevice(@RequestParam(name="id",required = true) Long id) {

        return ResponseData.success(deviceAdjustableBaseService.delDevice(id));
    }

    /**
     *设备基础档案管理(电力用户)
     * @param deviceAdjustableBaseParam
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    @ApiOperation(value = "设备基础档案管理", notes = "设备基础档案管理", produces = "application/json")
    @PostMapping("/pageDeviceBase")
    public ResponseData pageDeviceBase(@RequestBody DeviceAdjustableBaseParam deviceAdjustableBaseParam) {
        ResponseData responseData = deviceAdjustableBaseService.pageDeviceBase(deviceAdjustableBaseParam);
        return responseData;
    }

    /**
     *设备基础档案新增(电力用户)
     * @param deviceAdjustableBase
     * @return
     * @author lqr
     * @date 2022-1-6 15:20
     */
    @ApiOperation(value = "设备基础档案新增", notes = "设备基础档案新增", produces = "application/json")
    @PostMapping("/addDeviceBase")
    public ResponseData addDeviceBase(@RequestBody DeviceAdjustableBase deviceAdjustableBase) {
        String custId = SecurityUtils.getCurrentUserInfoUTF8().getId();
        Cust cust = custService.getById(custId);
        if(cust == null){
            return ResponseData.fail("500","登录客户不存在",null);
        }
        if (!CustStatusEnum.APPROVING.getCode().equals(cust.getState())) {
            return ResponseData.fail("500", "请认证后操作",null);
        }

        if(StringUtils.isEmpty(deviceAdjustableBase.getConsId())){
            return ResponseData.fail(SystemErrorType.PARAMETER_NULL);
        }
        ResponseData responseData = deviceAdjustableBaseService.add(deviceAdjustableBase);
        return responseData;
    }

    /**
     *设备基础档案修改(电力用户)
     * @param deviceAdjustableBase
     * @return
     * @author czj
     * @date 2022-1-6 15:20
     */
    @ApiOperation(value = "设备基础档案修改", notes = "设备基础档案修改", produces = "application/json")
    @PostMapping("/updateDeviceBase")
    public ResponseData updateDeviceBase(@RequestBody DeviceAdjustableBase deviceAdjustableBase) {
        String custId = SecurityUtils.getCurrentUserInfoUTF8().getId();
        Cust cust = custService.getById(custId);
        if(cust == null){
            return ResponseData.fail("500","登录客户不存在",null);
        }
        if (!CustStatusEnum.APPROVING.getCode().equals(cust.getState())) {
            return ResponseData.fail("500", "请认证后操作",null);
        }
        deviceAdjustableBaseService.update(deviceAdjustableBase);
        return ResponseData.success();
    }

    /**
     *设备基础档案删除(电力用户)
     * @param deviceAdjustableBase
     * @return
     * @author czj
     * @date 2022-1-6 15:20
     */
    @ApiOperation(value = "设备基础档案删除", notes = "设备基础档案删除", produces = "application/json")
    @PostMapping("/deleteDeviceBase")
    public ResponseData deleteDeviceBase(@RequestBody DeviceAdjustableBase deviceAdjustableBase) {
        String custId = SecurityUtils.getCurrentUserInfoUTF8().getId();
        Cust cust = custService.getById(custId);
        if(cust == null){
            return ResponseData.fail("500","登录客户不存在",null);
        }
        if (!CustStatusEnum.APPROVING.getCode().equals(cust.getState())) {
            return ResponseData.fail("500", "请认证后操作",null);
        }
        if(StringUtils.isEmpty(deviceAdjustableBase.getId())){
            return ResponseData.fail(SystemErrorType.PARAMETER_NULL);
        }
        deviceAdjustableBaseService.delete(deviceAdjustableBase);
        return ResponseData.success();
    }

    /**
     *设备基础档案详情(电力用户)
     * @param id
     * @return
     * @author lqr
     * @date 2022-1-6 15:20
     */
    @ApiOperation(value = "设备基础档案详情", notes = "设备基础档案详情", produces = "application/json")
    @GetMapping("/detailDeviceBase")
    public ResponseData detailDeviceBase(@RequestParam("id") Long id) {
        if(id == null){
            return ResponseData.fail(SystemErrorType.PARAMETER_NULL);
        }
        DeviceAdjustableBaseVO deviceAdjustableBaseVO = deviceAdjustableBaseService.detailDeviceBase(id);
        return ResponseData.success(deviceAdjustableBaseVO);
    }



    /**
     *下载模板(电力用户)
     * @param
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    @ApiOperation(value = "下载模板(电力用户)", notes = "下载模板(电力用户)", produces = "application/json")
    @PostMapping("/dowmDeviceModel")
    public void  dowmDeviceModel(HttpServletResponse response, HttpServletRequest request) {
        Workbook workBook = null;
        String fileName = null;
        try {
//            fileName = URLEncoder.encode("设备导入模板.xlsx", "GBK");
//            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            //设置文件名称
            fileName = URLEncoder.encode("设备导入模板", "UTF-8");
            //设置文件类型
            response.setContentType("application/vnd.ms-excel");
            //设置编码格式
            response.setCharacterEncoding("utf-8");
            // https://www.jb51.net/article/30565.htm Content-Disposition 使用说明
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> mapList = new ArrayList<>();

        // 创建参数对象（用来设定excel得sheet得内容等信息）
        ExportParams deptExportParams = new ExportParams();
        // 设置sheet得名称
        deptExportParams.setSheetName("设备导入模板");
        // 创建sheet1使用得map
        Map<String, Object> deviceMap = new HashMap<>();
        // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
        deviceMap.put("title", deptExportParams);
        // 模版导出对应得实体类型
        deviceMap.put("entity", DeviceBaseModel.class);
        // sheet中要填充得数据
        deviceMap.put("data", new ArrayList<>());
        mapList.add(deviceMap);

        ExportParams orgParams = new ExportParams();
        orgParams.setSheetName("设备类型字典");

        //查询 设备类型
        DictTypeParam dictTypeParam = new DictTypeParam();
        dictTypeParam.setCode(DrSysDictDataEnum.EQUIP_TYPE.getCode());
        List<Dict> dicts = dictTypeService.dropDown(dictTypeParam);
        List<DictModel> dictModels = new ArrayList<>();

        for (Dict dict : dicts) {
            DictModel dictModel = new DictModel();
            dictModel.setCode((String) dict.get("code"));
            dictModel.setValue((String) dict.get("value"));
            dictModels.add(dictModel);
        }

        HashMap<String, Object> orgMap = new HashMap<>();
        orgMap.put(NormalExcelConstants.DATA_LIST, dictModels);
        orgMap.put(NormalExcelConstants.CLASS, DictModel.class);
        orgMap.put("title", orgParams);
        mapList.add(orgMap);

        //查询 设备爬坡时间
        ExportParams deviceRiseTimeParams = new ExportParams();
        deviceRiseTimeParams.setSheetName("设备爬坡时间字典");

        dictTypeParam.setCode(DrSysDictDataEnum.DEVICE_RISE_TIME.getCode());
        List<Dict> deviceRiseTimeDicts = dictTypeService.dropDown(dictTypeParam);
        List<DictModel> deviceRiseTimeDictModels = new ArrayList<>();

        for (Dict dict : deviceRiseTimeDicts) {
            DictModel dictModel = new DictModel();
            dictModel.setCode((String) dict.get("code"));
            dictModel.setValue((String) dict.get("value"));
            deviceRiseTimeDictModels.add(dictModel);
        }

        HashMap<String, Object> deviceRiseTimeMap = new HashMap<>();
        deviceRiseTimeMap.put(NormalExcelConstants.DATA_LIST, deviceRiseTimeDictModels);
        deviceRiseTimeMap.put(NormalExcelConstants.CLASS, DictModel.class);
        deviceRiseTimeMap.put("title", deviceRiseTimeParams);
        mapList.add(deviceRiseTimeMap);

        //查询 削峰响应可持续时间
        ExportParams desResponseTimeParams = new ExportParams();
        desResponseTimeParams.setSheetName("削峰响应可持续时间字典");

        dictTypeParam.setCode(DrSysDictDataEnum.DES_RESPONSE_TIME.getCode());
        List<Dict> desResponseTimeDicts = dictTypeService.dropDown(dictTypeParam);
        List<DictModel> desResponseTimeDictModels = new ArrayList<>();

        for (Dict dict : desResponseTimeDicts) {
            DictModel dictModel = new DictModel();
            dictModel.setCode((String) dict.get("code"));
            dictModel.setValue((String) dict.get("value"));
            desResponseTimeDictModels.add(dictModel);
        }

        HashMap<String, Object> desResponseTimeMap = new HashMap<>();
        desResponseTimeMap.put(NormalExcelConstants.DATA_LIST, desResponseTimeDictModels);
        desResponseTimeMap.put(NormalExcelConstants.CLASS, DictModel.class);
        desResponseTimeMap.put("title", desResponseTimeParams);
        mapList.add(desResponseTimeMap);

        //查询 填谷响应可持续时间
        ExportParams risResponseTimeParams = new ExportParams();
        risResponseTimeParams.setSheetName("填谷响应可持续时间");

        dictTypeParam.setCode(DrSysDictDataEnum.RIS_RESPONSE_TIME.getCode());
        List<Dict> risResponseTimeDicts = dictTypeService.dropDown(dictTypeParam);
        List<DictModel> risResponseTimeDictModels = new ArrayList<>();

        for (Dict dict : risResponseTimeDicts) {
            DictModel dictModel = new DictModel();
            dictModel.setCode((String) dict.get("code"));
            dictModel.setValue((String) dict.get("value"));
            risResponseTimeDictModels.add(dictModel);
        }

        HashMap<String, Object> risResponseTimeMap = new HashMap<>();
        risResponseTimeMap.put(NormalExcelConstants.DATA_LIST, risResponseTimeDictModels);
        risResponseTimeMap.put(NormalExcelConstants.CLASS, DictModel.class);
        risResponseTimeMap.put("title", risResponseTimeParams);
        mapList.add(risResponseTimeMap);

        //查询 负荷等级
        ExportParams powerLevelParams = new ExportParams();
        powerLevelParams.setSheetName("负荷等级");

        dictTypeParam.setCode(DrSysDictDataEnum.POWER_LEVEL.getCode());
        List<Dict> powerLevelDicts = dictTypeService.dropDown(dictTypeParam);
        List<DictModel> powerLevelDictModels = new ArrayList<>();

        for (Dict dict : powerLevelDicts) {
            DictModel dictModel = new DictModel();
            dictModel.setCode((String) dict.get("code"));
            dictModel.setValue((String) dict.get("value"));
            powerLevelDictModels.add(dictModel);
        }

        HashMap<String, Object> powerLevelMap = new HashMap<>();
        powerLevelMap.put(NormalExcelConstants.DATA_LIST, powerLevelDictModels);
        powerLevelMap.put(NormalExcelConstants.CLASS, DictModel.class);
        powerLevelMap.put("title", powerLevelParams);
        mapList.add(powerLevelMap);

        try {
            // 执行方法
            workBook = ExcelExportUtil.exportExcel(mapList, ExcelType.HSSF);
            workBook.write(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(workBook != null) {
                try {
                    workBook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Autowired
    private CustService custService;

    /**
     *批量提交 导入 (电力用户)
     * @param
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    @ApiOperation(value = "批量提交 导入 (电力用户)", notes = "批量提交 导入 (电力用户)", produces = "application/json")
    @PostMapping("/importDeviceModel")
    public ResponseData importDeviceModel(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        String custId = SecurityUtils.getCurrentUserInfoUTF8().getId();
        Cust cust = custService.getById(custId);

        if(cust == null){
            return ResponseData.fail("500","登录客户不存在",null);
        }
        if (!CustStatusEnum.APPROVING.getCode().equals(cust.getState())) {
            return ResponseData.fail("500", "请认证后操作",null);
        }

        String originalFilename = file.getOriginalFilename();
        if(originalFilename.contains(".")){

            //获取最后一个.的位置
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件的后缀名 .jpg
            String suffix = originalFilename.substring(lastIndexOf);

            if(".xlsx".equals(suffix) || ".xls".equals(suffix)){
                ResponseData responseData = deviceAdjustableBaseService.importDeviceModel(file);
                return responseData;
            }else {
                return ResponseData.fail(SystemErrorType.ERROR_FILE_TYPE);
            }
        }else {
            return ResponseData.fail(SystemErrorType.ERROR_FILE_TYPE);
        }
    }

    /**
     *批量删除 (电力用户)
     * @param
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    @ApiOperation(value = "批量删除 (电力用户)", notes = "批量删除 (电力用户)", produces = "application/json")
    @PostMapping("/deleteBatchDevice")
    public ResponseData deleteBatch(@RequestBody List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return ResponseData.fail(SystemErrorType.PARAMETER_NULL);
        }
        deviceAdjustableBaseService.deleteBatchDevice(ids);
        return ResponseData.success();
    }

    /**
     * 修改时查询出支路信息列表
     */
    @ApiOperation(value = "/selectBranch/{consId}", notes = "查询支路信息", produces = "application/json")
    @GetMapping("/selectBranch/{consId}")
    public ResponseData selectByConsId(@PathVariable(value = "consId") String consId){

        if (StringUtils.isEmpty(consId)){
            return ResponseData.fail(SystemErrorType.PARAMETER_NULL);
        }
        return ResponseData.success(deviceAdjustableBaseService.selectBranchByConsId(consId));

    }

}

