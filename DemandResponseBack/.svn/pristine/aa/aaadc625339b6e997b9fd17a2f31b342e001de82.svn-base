package com.xqxy.sys.modular.cust.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.annotion.BusinessLog;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.enums.LogAnnotionOpTypeEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.adjustable.VO.GroupCityAdjustVO;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.dr.modular.strategy.DataAccessStrategy;
import com.xqxy.dr.modular.strategy.DataAccessStrategyContext;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.enums.CustCheckStatusEnum;
import com.xqxy.sys.modular.cust.enums.CustStatusEnum;
import com.xqxy.sys.modular.cust.enums.IdentityTypeEnum;
import com.xqxy.sys.modular.cust.param.BusConfigParam;
import com.xqxy.sys.modular.cust.param.ConsParam;
import com.xqxy.sys.modular.cust.param.CustInfoParam;
import com.xqxy.sys.modular.cust.param.CustParam;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.cust.service.CustService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户控制器
 *
 * @author xuyuxiang
 * @date 2020/3/19 21:14
 */
@Api(tags = "账号认证接口")
@RestController
@RequestMapping("/cust/cust")
public class CustController {

    @Resource
    CustService custService;

    @Resource
    private ConsService consService;

    @Resource
    private DataAccessStrategyContext dataAccessStrategyContext;

    @Value("${consDataAccessStrategy}")
    private String consDataAccessStrategy;

    /**
     * 账号认证信息修改
     *
     * @param custInfoParam
     * @return
     * @author shi
     * @date 2021-10-9 10:10
     */
//    @BusinessLog(title = "客户信息更新", opType = LogAnnotionOpTypeEnum.EDIT)
    @ApiOperation(value = "客户信息更新", notes = "客户信息更新", produces = "application/json")
    @PostMapping("/update")
    public ResponseData update(@RequestBody CustInfoParam custInfoParam) {
        custService.update(custInfoParam);
        return ResponseData.success();
    }

    /**
     * 账号认证信息修改
     * @param cust
     * @return
     * @author shi
     * @date 2021-10-9 10:10
     */
//    @BusinessLog(title = "客户信息更新", opType = LogAnnotionOpTypeEnum.EDIT)
    @ApiOperation(value = "客户信息更新", notes = "客户信息更新", produces = "application/json")
    @PostMapping("/updateByid")
    public ResponseData updateByid(@RequestBody Cust cust) {
        custService.updateByid(cust);
        return ResponseData.success();
    }
    /**
     * 	用户认证提交审核接口
     * @return
     * @author shi
     * @date 2021-10-8 16:10
     */
//    @BusinessLog(title = "用户认证提交审核接口", opType = LogAnnotionOpTypeEnum.EDIT)
    @ApiOperation(value = "用户认证提交审核接口", notes = "用户认证提交审核接口", produces = "application/json")
    @GetMapping("/approve")
    public ResponseData update(@RequestParam(name = "applyOrgId",required = true) Long applyOrgId) {
        custService.approve(applyOrgId);
        return ResponseData.success();
    }

    /**
     * 	用户认证提交审核接口
     * @return
     * @author shi
     * @date 2021-10-8 16:10
     */
//    @BusinessLog(title = "是否修改信息", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "是否修改信息", notes = "是否修改信息", produces = "application/json")
    @PostMapping("/modifyInformation")
    public ResponseData modifyInformation(@RequestBody CustInfoParam custInfoParam) {
        return ResponseData.success(custService.modifyInformation(custInfoParam));
    }

    /**
     * 登录用户查询
     *
     * @author shi
     * @date 2021-10-12 20:59
     */
//    @BusinessLog(title = "登录用户查询", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "登录用户查询", notes = "登录用户查询", produces = "application/json")
    @PostMapping("/getCustById")
    public ResponseData detailById() {

        return ResponseData.success(custService.detailById());
    }
    /**
     * 登录用户查询
     *
     * @author shi
     * @date 2022-1-6 20:59
     */

    @ApiOperation(value = "用户档案详情", notes = "用户档案详情", produces = "application/json")
    @GetMapping("/detailByCustId")
    public ResponseData detailByCustId(@RequestParam(name = "id",required = true) Long id) {

        return ResponseData.success(custService.detailByCustId(id));
    }


    /**
     * 账号认证信息修改
     * @param tel
     * @return
     * @author shi
     * @date 2021-10-9 10:10
     */
//    @BusinessLog(title = "客户信息插入", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "客户信息插入", notes = "客户信息插入", produces = "application/json")
    @GetMapping("/add")
    public ResponseData add(@RequestParam(name = "tel",required = true) String tel,@RequestParam(name = "id",required = true) Long id) {
        custService.add(tel,id);
        return ResponseData.success();
    }

    /**
     * 账号认证信息修改
     * @param busConfigParam
     * @return
     * @author shi
     * @date 2021-10-9 10:10
     */
//    @BusinessLog(title = "审核结果下发", opType = LogAnnotionOpTypeEnum.EDIT)
    @ApiOperation(value = "审核结果下发", notes = "审核结果下发", produces = "application/json")
    @PostMapping("/approveResult")
    public ResponseData approveResult(@RequestBody @Valid BusConfigParam busConfigParam) {
        custService.approveResult(busConfigParam);
        return ResponseData.success();
    }

    /**
     * 获取审核信息
     * @param custId
     * @return
     * @author shi
     * @date 2021-10-9 10:10
     */
//    @BusinessLog(title = "获取审核信息", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "获取审核信息", notes = "获取审核信息", produces = "application/json")
    @GetMapping("/getApproveInfo")
    public ResponseData getApproveInfo(@RequestParam(name = "custId",required = true) Long custId ) {

        return ResponseData.success(custService.getApproveInfo(custId));
    }

    /**
     * 用户档案负荷集成商分页查询
     * @param custParam
     * @return
     * @author shi
     * @date 2021-11-3 10:10
     */
    @ApiOperation(value = "用户档案负荷集成商分页查询", notes = "用户档案负荷集成商分页查询", produces = "application/json")
    @PostMapping("/page")
    public ResponseData page(@RequestBody CustParam custParam) {
        return ResponseData.success(custService.page(custParam));
    }

    /**
     * 户号管理 客户查询
     * @param custParam
     * @return
     * @author shi
     * @date 2022-1-10 10:10
     */
    @ApiOperation(value = "户号管理 客户查询", notes = "户号管理 客户查询", produces = "application/json")
    @PostMapping("/list")
    public ResponseData list(@RequestBody CustParam custParam) {
        return ResponseData.success(custService.list(custParam));
    }

    @Autowired
    private SystemClientService systemClientService;


    /**
     * 客户档案 导出
     * @param custParam
     * @return
     */
    @ApiOperation(value = "负荷集成商档案导出", notes = "负荷集成商档案导出", produces = "application/json")
    @PostMapping("/exportCust")
    public void exportCons(@RequestBody CustParam custParam, HttpServletResponse response, HttpServletRequest request) {
        List<Cust> list = custService.list(custParam);

        List<Region> regions = systemClientService.queryAll();
        Map<String, String> collect = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));

        for (Cust cust : list) {
            // 省市区 翻译
            cust.setProvinceCode(collect.get(cust.getProvinceCode()));
            cust.setCityCode(collect.get(cust.getCityCode()));
            cust.setCountyCode(collect.get(cust.getCountyCode()));
            // 审核状态 翻译
            cust.setCheckStatus(CustCheckStatusEnum.getValue(cust.getCheckStatus()));
            // 翻译 代表人的 证件类型
            cust.setLegalCardType(IdentityTypeEnum.getValue(cust.getLegalCardType()));
            cust.setApplyCardType(IdentityTypeEnum.getValue(cust.getApplyCardType()));
        }

        ExportParams params;
        HashMap<String,Object> map = new HashMap<>();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, Cust.class);

        if(1 == custParam.getIntegrator()){
            params = new ExportParams("负荷聚合商档案导出", "负荷聚合商档案导出", ExcelType.XSSF);
            map.put(NormalExcelConstants.FILE_NAME, "负荷聚合商档案导出");
        }else {
            params = new ExportParams("电力客户档案导出", "电力客户档案导出", ExcelType.XSSF);
            map.put(NormalExcelConstants.FILE_NAME, "电力客户档案导出");
        }

        map.put(NormalExcelConstants.PARAMS, params);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    /**
     * 根据户号查询营销档案信息
     *
     * @return com.xqxy.core.pojo.response.ResponseData
     * @date 11/5/2021 9:39
     * @author Caoj
     */
    @ApiOperation(value = "用户档案负荷集成商查询", notes = "用户档案负荷集成商查询", produces = "application/json")
    @PostMapping("/getArchives")
    public ResponseData<?> getArchives(@RequestBody ConsParam consParam) {
        Cons byId = consService.getById(consParam.getId());
        if (ObjectUtil.isNotNull(byId)) {
            throw new ServiceException(-1, "用户户号已被绑定，请重新确认后再添加");
        }
        // 从营销档案获取用户数据,使用模拟接口
        DataAccessStrategy dataAccessStrategy = dataAccessStrategyContext.strategySelect(consDataAccessStrategy);
        Cons marketCons = dataAccessStrategy.getConsFromMarketing(consParam.getId(), null, null);
        if(marketCons != null){
            if(StringUtils.isEmpty(marketCons.getId())){
                return ResponseData.fail("-1","无此电力户号","");
            }
            return ResponseData.success(marketCons);
        }else {
            return ResponseData.fail("-1","无此电力户号","");
        }
    }

    @BusinessLog(title = "通过统一社会信用代码获取用户", opType = LogAnnotionOpTypeEnum.QUERY)
    @ApiOperation(value = "通过统一社会信用代码获取用户", notes = "通过统一社会信用代码获取用户", produces = "application/json")
    @GetMapping("/getUserByCreditCode")
    public ResponseData getUserByCreditCode(@RequestParam(name = "creditCode", required = true) String creditCode) {
        return ResponseData.success(custService.getUserByCreditCode(creditCode));
    }

    /**
     * 集成商下拉框
     * @date 11/8/2021 14:47
     * @author Caoj
     */
    @ApiOperation(value = "集成商下拉框", notes = "集成商下拉框", produces = "application/json")
    @PostMapping("selectAggregator")
    public ResponseData<?> selectAggregator() {
        return ResponseData.success(custService.selectAggregator());
    }

    /**
     * 全部客户下拉框
     * @params
     * @return com.xqxy.core.pojo.response.ResponseData
     * @since 11/12/2021 13:47
     * @author liqirui
     */
    @ApiOperation(value = "全部客户下拉框", notes = "全部客户下拉框", produces = "application/json")
    @PostMapping("selectCust")
    public ResponseData selectCust(@RequestBody CustParam custParam) {
        LambdaQueryWrapper<Cust> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(custParam.getCustName())){
            lambdaQueryWrapper.like(Cust::getCustName,custParam.getCustName());
        }

        List<Cust> custList = custService.list(lambdaQueryWrapper);
        List<Map<String, String>> list = new ArrayList<>();

        return ResponseData.success(custList);
    }

    @ApiOperation(value = "判断客户是否认证", notes = "判断客户是否认证", produces = "application/json")
    @PostMapping("isVerify")
    public ResponseData<?> isVerify() {
        String custId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId();
        Cust cust = custService.getById(custId);
        if(cust != null){
            if (CustStatusEnum.APPROVING.getCode().equals(cust.getState())) {
                return ResponseData.success();
            }
        }
        throw new ServiceException(-1, "账户未认证，无法操作");
    }
    /**
     * 插入集成商信息
     * @param cust
     * @return
     * @author shi
     * @date 2021-12-24 10:10
     */
//    @BusinessLog(title = "客户信息插入", opType = LogAnnotionOpTypeEnum.ADD)
    @ApiOperation(value = "客户信息插入", notes = "客户信息插入", produces = "application/json")
    @PostMapping("/addCust")
    public ResponseData addCust(@RequestBody Cust cust) {
        custService.addCust(cust);
        return ResponseData.success();
    }

    @ApiOperation(value = "判断客户是否认证", notes = "判断客户是否认证", produces = "application/json")
    @PostMapping("isVerifyCust")
    public ResponseData<?> isVerifyCust(@RequestBody CustParam custParam) {

        if (CustStatusEnum.APPROVING.getCode().equals(custParam.getState())) {
            return ResponseData.success();
        } else {
            throw new ServiceException(-1, "账户未认证，不能添加户号");
        }
    }

    /**
     * 用户档案 新增修改接口
     *
     * @param custInfoParam
     * @return
     * @author shi
     * @date 2021-10-9 10:10
     */
//    @BusinessLog(title = "客户信息更新", opType = LogAnnotionOpTypeEnum.EDIT)
    @ApiOperation(value = "客户信息更新", notes = "客户信息更新", produces = "application/json")
    @PostMapping("/updateCust")
    public ResponseData updateCust(@RequestBody CustInfoParam custInfoParam) {
        custService.updateCust(custInfoParam);
        return ResponseData.success();
    }

}
