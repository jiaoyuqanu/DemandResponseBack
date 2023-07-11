package com.xqxy.dr.modular.device.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.client.SystemLoadbusiClient;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.exception.SystemErrorType;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.device.VO.DeviceAdjustableBaseVO;
import com.xqxy.dr.modular.device.VO.EquipmentRecordVO;
import com.xqxy.dr.modular.device.entity.DeviceAdjustableBase;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.device.mapper.DeviceAdjustableBaseMapper;
import com.xqxy.dr.modular.device.mapper.TDevBranchMapper;
import com.xqxy.dr.modular.device.model.DeviceBaseModel;
import com.xqxy.dr.modular.device.param.DeviceAdjustableBaseParam;
import com.xqxy.dr.modular.device.service.DeviceAdjustableBaseService;
import com.xqxy.dr.modular.strategy.DataAccessStrategy;
import com.xqxy.dr.modular.strategy.DataAccessStrategyContext;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import io.netty.util.internal.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备可调节基础信息 服务实现类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-11-08
 */
@Service
public class DeviceAdjustableBaseServiceImpl extends ServiceImpl<DeviceAdjustableBaseMapper, DeviceAdjustableBase> implements DeviceAdjustableBaseService {

    @Resource
    private ConsService consService;

    @Resource
    private DeviceAdjustableBaseMapper deviceAdjustableBaseMapper;

    @Autowired
    private TDevBranchMapper branchMapper;

    @Resource
    private DataAccessStrategyContext dataAccessStrategyContext;

    @Value("${dataAccessStrategy}")
    private String dataStrategy;

    @Resource
    private SystemClientService client;

    @Resource
    private SystemLoadbusiClient systemClient;

    @Override
    public ResponseData generateDevice(DeviceAdjustableBaseParam deviceAdjustableBaseParam) {

        if (ObjectUtil.isNotNull(deviceAdjustableBaseParam)) {

            LambdaQueryWrapper<DeviceAdjustableBase> deviceQueryWrapper = new LambdaQueryWrapper<>();
            deviceQueryWrapper.eq(DeviceAdjustableBase::getDeviceId,deviceAdjustableBaseParam.getDeviceId());
            deviceQueryWrapper.ne(DeviceAdjustableBase::getId,deviceAdjustableBaseParam.getId());
            List<DeviceAdjustableBase> deviceAdjustableBases = this.list(deviceQueryWrapper);
            if(!CollectionUtils.isEmpty(deviceAdjustableBases)){
                return  ResponseData.fail(SystemErrorType.DEVICEID_NO_REPEAT);
            }

            if(!StringUtils.isEmpty(deviceAdjustableBaseParam.getOrgNo())) {
                //
                DeviceAdjustableBase deviceAdjustableBase = getDeviceAdjustableBase(deviceAdjustableBaseParam.getOrgNo());
                deviceAdjustableBase.setConsId(deviceAdjustableBaseParam.getConsId());
                deviceAdjustableBase.setDeviceTypeName(deviceAdjustableBaseParam.getDeviceTypeName());
                deviceAdjustableBase.setDeviceTypeCode(deviceAdjustableBaseParam.getDeviceTypeCode());
                deviceAdjustableBase.setDeviceNum(deviceAdjustableBaseParam.getDeviceNum());
                deviceAdjustableBase.setRatedVoltage(deviceAdjustableBaseParam.getRatedVoltage());
                deviceAdjustableBase.setRatedPower(deviceAdjustableBaseParam.getRatedPower());
                deviceAdjustableBase.setRatedCurrent(deviceAdjustableBaseParam.getRatedCurrent());
                deviceAdjustableBase.setDesResponseTime(deviceAdjustableBaseParam.getDesResponseTime());
                deviceAdjustableBase.setDesResponseTimeCode(deviceAdjustableBaseParam.getDesResponseTimeCode());
                deviceAdjustableBase.setRisResponseTime(deviceAdjustableBaseParam.getRisResponseTime());
                deviceAdjustableBase.setRisResponseTimeCode(deviceAdjustableBaseParam.getRisResponseTimeCode());
                deviceAdjustableBase.setDeviceRiseTime(deviceAdjustableBaseParam.getDeviceRiseTime());
                deviceAdjustableBase.setDeviceRiseTimeCode(deviceAdjustableBaseParam.getDeviceRiseTimeCode());
                deviceAdjustableBase.setPowerLevel(deviceAdjustableBaseParam.getPowerLevel());
                deviceAdjustableBase.setPowerLevelCode(deviceAdjustableBaseParam.getPowerLevelCode());
                deviceAdjustableBase.setDeviceId(deviceAdjustableBaseParam.getDeviceId());
                deviceAdjustableBase.setDeviceName(deviceAdjustableBaseParam.getDeviceName());
                deviceAdjustableBase.setDeviceModel(deviceAdjustableBaseParam.getDeviceModel());
                deviceAdjustableBase.setAddress(deviceAdjustableBaseParam.getAddress());
                deviceAdjustableBase.setMontored(deviceAdjustableBaseParam.getMontored());
                deviceAdjustableBase.setAccessDate(deviceAdjustableBaseParam.getAccessDate());
                deviceAdjustableBase.setFileId(deviceAdjustableBaseParam.getFileId());
                deviceAdjustableBase.setFileType(deviceAdjustableBaseParam.getFileType());
                deviceAdjustableBase.setBranchId(deviceAdjustableBaseParam.getBranchId());
                deviceAdjustableBase.setBranchName(deviceAdjustableBaseParam.getBranchName());
                deviceAdjustableBase.setGaugeNo(deviceAdjustableBaseParam.getGaugeNo());
                deviceAdjustableBase.setGaugeName(deviceAdjustableBaseParam.getGaugeName());
                deviceAdjustableBase.setRunMinLoad(deviceAdjustableBaseParam.getRunMinLoad());
                deviceAdjustableBase.setRunMaxLoad(deviceAdjustableBaseParam.getRunMaxLoad());

                if (ObjectUtil.isNull(deviceAdjustableBaseParam.getId())) {
                    this.save(deviceAdjustableBase);
                } else {
                    deviceAdjustableBase.setId(deviceAdjustableBaseParam.getId());
                    this.updateById(deviceAdjustableBase);
                }
                return ResponseData.success();
            }
        }

        return ResponseData.fail();
    }

    private DeviceAdjustableBase getDeviceAdjustableBase(String orgNo) {
        DeviceAdjustableBase deviceAdjustableBase = new DeviceAdjustableBase();

        List<SysOrgs> allOrgs = new ArrayList<>();
        JSONObject jsonObject2 = client.queryAllOrg();
        if("000000".equals(jsonObject2.getString("code"))){
            JSONArray data = jsonObject2.getJSONArray("data");
            for (Object ignored : data) {
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(ignored);
                SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);

                allOrgs.add(sysOrgs);
            }
        }
        List<SysOrgs> sysOrgs = allOrgs.stream().filter(n -> orgNo.equals(n.getId())).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(sysOrgs)) {
            SysOrgs sysOrg = sysOrgs.get(0);
            String orgTitle = sysOrg.getOrgTitle();
            if (OrgTitleEnum.PROVINCE.getCode().equals(orgTitle)) {
                deviceAdjustableBase.setProvinceEleCode(sysOrg.getId());
                deviceAdjustableBase.setProvinceEleName(sysOrg.getName());
            } else if (OrgTitleEnum.CITY.getCode().equals(orgTitle)) {

                //塞值市级
                deviceAdjustableBase.setCityEleCode(sysOrg.getId());
                deviceAdjustableBase.setCityEleName(sysOrg.getName());

                //获取父级   --->  省级
                List<SysOrgs> provinceFilter = allOrgs.stream().filter(n -> n.getId().equals(sysOrg.getParentId())).collect(Collectors.toList());
                //塞值省级
                if (!CollectionUtils.isEmpty(provinceFilter)) {
                    SysOrgs provinceSysOrgs = provinceFilter.get(0);
                    deviceAdjustableBase.setProvinceEleCode(provinceSysOrgs.getId());
                    deviceAdjustableBase.setProvinceEleName(provinceSysOrgs.getName());
                }
            } else if (OrgTitleEnum.COUNTY.getCode().equals(orgTitle)) {
                //现在是 区县级

                //获取父级   --->  市级
                List<SysOrgs> cityFilter = allOrgs.stream().filter(n -> n.getId().equals(sysOrg.getParentId())).collect(Collectors.toList());
                //塞值市级
                if (!CollectionUtils.isEmpty(cityFilter)) {
                    SysOrgs citySysOrgs = cityFilter.get(0);
                    deviceAdjustableBase.setCityEleCode(citySysOrgs.getId());
                    deviceAdjustableBase.setCityEleName(citySysOrgs.getName());

                    //获取父级   --->  省级
                    List<SysOrgs> provinceFilter = allOrgs.stream().filter(n -> n.getId().equals(citySysOrgs.getParentId())).collect(Collectors.toList());
                    //塞值省级
                    if (!CollectionUtils.isEmpty(provinceFilter)) {
                        SysOrgs provinceSysOrgs = provinceFilter.get(0);
                        deviceAdjustableBase.setProvinceEleCode(provinceSysOrgs.getId());
                        deviceAdjustableBase.setProvinceEleName(provinceSysOrgs.getName());
                    }
                }
            } else if (OrgTitleEnum.STREET.getCode().equals(orgTitle)) {
                //现在是 街道（乡镇）级

                //获取区县级
                List<SysOrgs> countyFilter = allOrgs.stream().filter(n -> n.getId().equals(sysOrg.getParentId())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(countyFilter)) {
                    SysOrgs countySysOrgs = countyFilter.get(0);
                    //获取市级
                    List<SysOrgs> cityFilter = allOrgs.stream().filter(n -> n.getId().equals(countySysOrgs.getParentId())).collect(Collectors.toList());
                    //塞值市级
                    if (!CollectionUtils.isEmpty(cityFilter)) {
                        SysOrgs citySysOrgs = cityFilter.get(0);
                        deviceAdjustableBase.setCityEleCode(citySysOrgs.getId());
                        deviceAdjustableBase.setCityEleName(citySysOrgs.getName());

                        //获取父级   --->  省级
                        List<SysOrgs> provinceFilter = allOrgs.stream().filter(n -> n.getId().equals(citySysOrgs.getParentId())).collect(Collectors.toList());
                        //塞值省级
                        if (!CollectionUtils.isEmpty(provinceFilter)) {
                            SysOrgs provinceSysOrgs = provinceFilter.get(0);
                            deviceAdjustableBase.setProvinceEleCode(provinceSysOrgs.getId());
                            deviceAdjustableBase.setProvinceEleName(provinceSysOrgs.getName());
                        }
                    }
                }

            }
        }

        return  deviceAdjustableBase;
    }

    /**
     * 电力专责用户 列表
     * @param deviceAdjustableBaseParam
     * @return
     */
    @Override
    public Page<DeviceAdjustableBaseVO> page(DeviceAdjustableBaseParam deviceAdjustableBaseParam) {
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        List<String> orgIds = null;
        String orgTitle = currentUserInfo.getOrgTitle();

        if(!OrgTitleEnum.PROVINCE.getCode().equals(orgTitle)){
             orgIds = OrganizationUtil.getAllOrgByOrgNo();
        }

        Page<DeviceAdjustableBaseVO> page = deviceAdjustableBaseMapper.page(orgIds,deviceAdjustableBaseParam.getPage(),deviceAdjustableBaseParam);

//        Page<DeviceAdjustableBase> page = this.page(deviceAdjustableBaseParam.getPage(), queryWrapper);
        return page;
    }


    /**
     * 设备下拉框查询
     * @param deviceAdjustableBase
     * @return
     * @author hu xingxing
     * @date 2021-11-08 10:20
     */
    @Override
    public List<DeviceAdjustableBase> listDeviceBase(DeviceAdjustableBase deviceAdjustableBase) {
        QueryWrapper<DeviceAdjustableBase> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(deviceAdjustableBase)) {
            if (ObjectUtil.isNotEmpty(deviceAdjustableBase.getDeviceName())) {
                queryWrapper.like("DEVICE_NAME", deviceAdjustableBase.getDeviceName());
            }
        }

        return this.list(queryWrapper);
    }

    @Override
    public List<DeviceAdjustableBase> getByConsId(String consId)
    {
        LambdaQueryWrapper<DeviceAdjustableBase> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(StringUtil.isNullOrEmpty(consId))
        {
            return null;
        }
        else
        {
            lambdaQueryWrapper.eq(DeviceAdjustableBase::getConsId,consId);
            return this.list(lambdaQueryWrapper);
        }
    }

    @Override
    public boolean isDeviceIdRepeat(String deviceId) {

        LambdaQueryWrapper<DeviceAdjustableBase> queryWrapper = new LambdaQueryWrapper<>();
        if (deviceId == null || deviceId.trim().equals("")) {
            return false;
        } else {
            queryWrapper.eq(DeviceAdjustableBase::getDeviceId, deviceId);
            DeviceAdjustableBase deviceAdjustableBase = this.getOne(queryWrapper);
            return ObjectUtil.isNotNull(deviceAdjustableBase);
        }
    }

    @Override
    public int delDevice(Long id) {

        if (id != null) {
            this.removeById(id);
            return 1;
        }

        return 0;
    }


    /**
     *设备基础档案管理
     * @param deviceAdjustableBaseParam
     * @return
     * @author lqr
     * @date 2021-11-08 10:20
     */
    @Override
    public ResponseData pageDeviceBase(DeviceAdjustableBaseParam deviceAdjustableBaseParam) {
        Long custId = Long.valueOf(Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId());

        //根据custId 查对应户号
        List<String> consIdList = consService.getConsIdListByCust(custId);
        if(CollectionUtils.isEmpty(consIdList)){
//            return ResponseData.fail(SystemErrorType.CONS_RELA_NULL);
            return ResponseData.success(deviceAdjustableBaseParam.getPage());
        }

        Page<DeviceAdjustableBaseVO> page = deviceAdjustableBaseMapper.pageDeviceBase(consIdList,deviceAdjustableBaseParam.getPage(),deviceAdjustableBaseParam);

        return ResponseData.success(page);
    }

    @Override
    public void update(DeviceAdjustableBase deviceAdjustableBase) {
         baseMapper.updateById(deviceAdjustableBase);
    }

    @Override
    public void delete(DeviceAdjustableBase deviceAdjustableBase) {
        baseMapper.deleteById(deviceAdjustableBase);
    }


    /**
     *设备基础档案新增(电力用户)
     * @param deviceAdjustableBase
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    @Override
    public ResponseData add(DeviceAdjustableBase deviceAdjustableBase) {
        LambdaQueryWrapper<DeviceAdjustableBase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(ObjectUtil.isNotEmpty(deviceAdjustableBase)){
            lambdaQueryWrapper.eq(DeviceAdjustableBase::getDeviceId,deviceAdjustableBase.getDeviceId());
        }
        List<DeviceAdjustableBase> list = this.list(lambdaQueryWrapper);

        if(CollectionUtils.isEmpty(list)){

            //通过用户 拿到用户对象   拿到用户的供电公司
            Cons cons = consService.getById(deviceAdjustableBase.getConsId());
            if(cons == null){
                return  ResponseData.fail(SystemErrorType.NO_CONTAINS_CONSID);
            }
            DeviceAdjustableBase base = getDeviceAdjustableBase(cons.getOrgNo());

            deviceAdjustableBase.setProvinceEleName(base.getProvinceEleName());
            deviceAdjustableBase.setProvinceEleCode(base.getProvinceEleCode());
            deviceAdjustableBase.setCityEleName(base.getCityEleName());
            deviceAdjustableBase.setCityEleCode(base.getCityEleCode());
            this.save(deviceAdjustableBase);
            return  ResponseData.success();
        }

        return  ResponseData.fail(SystemErrorType.DEVICEID_NO_REPEAT);
    }


    /**
     *设备基础档案详情(电力用户)
     * @param id
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    @Override
    public DeviceAdjustableBaseVO detailDeviceBase(Long id) {
        DeviceAdjustableBaseVO deviceAdjustableBaseVO = new DeviceAdjustableBaseVO();

        DeviceAdjustableBase deviceAdjustableBase = this.getById(id);
        BeanUtils.copyProperties(deviceAdjustableBase,deviceAdjustableBaseVO);

        //访问设备负荷 默认当日
        /*DataAccessStrategy dataAccessStrategy = dataAccessStrategyContext.strategySelect(dataStrategy);
        List<EquipmentRecordVO> list = dataAccessStrategy.queryDeviceRealTimeCurvePage(id.toString());

        List<String> stringList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            //由于只传一个 设备编号 只返回一条数据
            EquipmentRecordVO equipmentRecordVO = list.get(0);

            //转换 equipmentRecordVO 的 负荷 为数组
            Class<? extends EquipmentRecordVO> equipmentRecordClass = equipmentRecordVO.getClass();
            Field[] declaredFields = equipmentRecordClass.getDeclaredFields();

            for (Field field : declaredFields) {
                field.setAccessible(true);

                String name = field.getName();
                try {
                    if(name.startsWith("t") && name.length() == 5){
                        String str = (String) field.get(equipmentRecordVO);
                        stringList.add(str);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        deviceAdjustableBaseVO.setPointList(stringList);*/
        return deviceAdjustableBaseVO;
    }


    /**
     *批量提交 导入 (电力用户)
     * @param
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    @Override
    public ResponseData importDeviceModel(MultipartFile file) {
        ImportParams params1 = new ImportParams();
        params1.setHeadRows(1);
        params1.setTitleRows(0);//标题
        params1.setStartSheetIndex(0);

        List<DeviceBaseModel> importlist = new ArrayList<>();
        try {
            importlist = ExcelImportUtil.importExcel(file.getInputStream(), DeviceBaseModel.class, params1);

            if(CollectionUtils.isEmpty(importlist)){
                return ResponseData.fail(SystemErrorType.NO_DATA.getCode(),"读取到的文件内容为空，请校验",null);
            }
        } catch (Exception e) {
            throw new ServiceException(500,"导入文件有误");
        }

        try {
            Workbook workbook= new HSSFWorkbook(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);
            //获取 excel 第一行数据（表头）
            Row row = sheet.getRow(0);
            //存放表头信息
            //存放表头信息
            Set<String> set = new HashSet<>();
            //算下有多少列
//            int colCount = sheet.getRow(0).getLastCellNum();
//            System.out.println(colCount);
//            for (int j = 0; j < colCount; j++) {
//                Cell cell = row.getCell(j);
//                String cellValue = cell.getStringCellValue().trim();
//                set.add(cellValue);
//            }
            String str1 = row.getCell(0).getStringCellValue().trim();
            String str2 = row.getCell(1).getStringCellValue().trim();
            if(!"电力户号".equals(str1) || !"设备类型编码".equals(str2)){
                throw new ServiceException(500,"导入模板错误");
            }
        } catch (Exception e) {
            throw new ServiceException(500,"导入模板错误");
        }


        for (DeviceBaseModel deviceBaseModel : importlist) {
            if(StringUtils.isEmpty(deviceBaseModel.getConsId())){return ResponseData.fail("500","存在 电力户号 为空的数据，请核查",null); }
            if(StringUtils.isEmpty(deviceBaseModel.getDeviceTypeCode())){return ResponseData.fail("500","存在 设备类型编码 为空的数据，请核查",null); }
            if(StringUtils.isEmpty(deviceBaseModel.getRatedPower())){ return ResponseData.fail("500","存在 额定功率 为空的数据，请核查",null); }
            if(StringUtils.isEmpty(deviceBaseModel.getDeviceName())){ return ResponseData.fail("500","存在 设备名称 为空的数据，请核查",null); }
            if(StringUtils.isEmpty(deviceBaseModel.getDeviceId())){ return ResponseData.fail("500","存在设备编号为空的数据，请核查",null); }
            if(StringUtils.isEmpty(deviceBaseModel.getMontored())){ return ResponseData.fail("500","存在 监测 为空的数据，请核查",null); }
            if(StringUtils.isEmpty(deviceBaseModel.getPowerLevelCode())){ return ResponseData.fail("500","存在 负荷等级 为空的数据，请核查",null); }
            if(StringUtils.isEmpty(deviceBaseModel.getRunMaxLoad())){ return ResponseData.fail("500","存在 设备运行最大负荷(kW) 为空的数据，请核查",null); }
            if(StringUtils.isEmpty(deviceBaseModel.getRunMinLoad())){ return ResponseData.fail("500","存在 设备运行最小负荷(kW) 为空的数据，请核查",null); }
        }

        //获取导入数据  自身是否有重复deviceId
        Map<String, List<DeviceBaseModel>> importMap = importlist.stream().collect(Collectors.groupingBy(DeviceBaseModel::getDeviceId));
        Set<Map.Entry<String, List<DeviceBaseModel>>> entrySet = importMap.entrySet();
        for (Map.Entry<String, List<DeviceBaseModel>> entry : entrySet) {
            if(entry.getValue().size() > 1){
                    return ResponseData.fail(SystemErrorType.DEVICEID_NO_REPEAT.getCode(), " 存在设备编号重复的数据，请修改。 对应数据设备编号为 "+ entry.getKey(),null);
            }
        }

        // 查询所有设备
        List<DeviceAdjustableBase> allList = this.list();
        if(!CollectionUtils.isEmpty(importlist) && !CollectionUtils.isEmpty(allList)){
            // 户号是否是登录人所属户号

            //根据custId 查对应户号
            Long custId = Long.valueOf(Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId());
            List<String> consIdList = consService.getConsIdListByCust(custId);

            Map<String, List<DeviceBaseModel>> importConsIdMap = importlist.stream().collect(Collectors.groupingBy(DeviceBaseModel::getConsId));
            Set<String> importConsIdSet = importConsIdMap.keySet();
            for (String cosnId : importConsIdSet) {
                if(!consIdList.contains(cosnId)){
                    //不包含导入的户号
                    return ResponseData.fail(SystemErrorType.NO_CONTAINS_CONSID.getCode(), "电力户号 "+ cosnId + " 未被当前客户代理， 请修改",null);
                }
            }

            //入库前判断  deviceid是否重复
            List<String> allDeviceIDList = allList.stream().map(DeviceAdjustableBase::getDeviceId).collect(Collectors.toList());
            List<String> importDeviceIDList = importlist.stream().map(DeviceBaseModel::getDeviceId).collect(Collectors.toList());
            for (String deviceID : importDeviceIDList) {
                if(allDeviceIDList.contains(deviceID)){
                    //包含导入的deviceId
                    return ResponseData.fail(SystemErrorType.DEVICEID_NO_REPEAT.getCode(), " 存在设备编号和已录入数据重复的数据，请修改。 对应数据设备编号为 "+ deviceID,null);
                }
            }

            //满足条件则 入库
            List<DeviceAdjustableBase> list = devicModelToBase(importlist);

            for (DeviceAdjustableBase deviceAdjustableBase : list) {

                //通过用户 拿到用户对象   拿到用户的供电公司
                Cons cons = consService.getById(deviceAdjustableBase.getConsId());
                if(cons == null){
                    return  ResponseData.fail(SystemErrorType.NO_CONTAINS_CONSID);
                }
                DeviceAdjustableBase base = getDeviceAdjustableBase(cons.getOrgNo());

                deviceAdjustableBase.setProvinceEleName(base.getProvinceEleName());
                deviceAdjustableBase.setProvinceEleCode(base.getProvinceEleCode());
                deviceAdjustableBase.setCityEleName(base.getCityEleName());
                deviceAdjustableBase.setCityEleCode(base.getCityEleCode());
            }

            this.saveBatch(list);
        }
        return ResponseData.success();
    }


    /**
     *批量删除 (电力用户)
     * @param
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    @Override
    public void deleteBatchDevice(List<Long> ids) {
        this.removeByIds(ids);
    }

    /**
     * 导入model 转换成 实体类
     * @param importlist
     * @return
     */
    private List<DeviceAdjustableBase> devicModelToBase(List<DeviceBaseModel> importlist) {
        List<DeviceAdjustableBase> list = new ArrayList<>();
        for (DeviceBaseModel deviceBaseModel : importlist) {
            DeviceAdjustableBase deviceAdjustableBase = new DeviceAdjustableBase();
            BeanUtils.copyProperties(deviceBaseModel,deviceAdjustableBase);

            list.add(deviceAdjustableBase);
        }

        return list;
    }

    @Override
    public JSONArray selectBranchByConsId(String consId) {
        Map<String,Object> map = new HashMap<>();
        map.put("consNo",consId);
        JSONObject jsonObject = systemClient.getBranchByConNo(map);
        if(null!=jsonObject && null!=jsonObject.getString("code")){
            if("000000".equals(jsonObject.getString("code"))) {
                if(null!=jsonObject.get("data")) {
                    JSONArray datas = jsonObject.getJSONArray("data");
                    if(datas.size()>0) {
                        return datas;
                    } else {
                        log.error("返回data长度为0");
                        return new JSONArray();
                    }
                } else {
                    log.error("返回data为空");
                    return new JSONArray();
                }
            } else {
                log.error("返回数据状态码异常");
                return  new JSONArray();
            }
        } else {
            log.error("返回result为空");
            return  new JSONArray();
        }
    }
}
