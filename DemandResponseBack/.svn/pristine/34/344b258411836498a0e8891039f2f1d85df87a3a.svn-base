package com.xqxy.dr.modular.adjustable.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.enums.DrSysDictDataEnum;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.adjustable.DTO.DrDeviceAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.VO.DrDeviceAdjustablePotentialVO;
import com.xqxy.dr.modular.adjustable.VO.GroupCityAdjustVO;
import com.xqxy.dr.modular.adjustable.VO.GroupCityDeviceAdjustVO;
import com.xqxy.dr.modular.adjustable.VO.GroupDeviceTypeAdjustVO;
import com.xqxy.dr.modular.adjustable.entity.DrDeviceAdjustablePotential;
import com.xqxy.dr.modular.adjustable.mapper.DrDeviceAdjustablePotentialMapper;
import com.xqxy.dr.modular.adjustable.service.DrConsAdjustablePotentialService;
import com.xqxy.dr.modular.adjustable.service.DrDeviceAdjustablePotentialService;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.device.VO.EquipmentRecordVO;
import com.xqxy.dr.modular.device.entity.DeviceAdjustableBase;
import com.xqxy.dr.modular.device.param.DeviceMonitorParam;
import com.xqxy.dr.modular.device.service.DeviceAdjustableBaseService;
import com.xqxy.dr.modular.strategy.DataAccessStrategy;
import com.xqxy.dr.modular.strategy.DataAccessStrategyContext;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.utils.SnowflakeIdWorker;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Service
public class DrDeviceAdjustablePotentialServiceImpl extends ServiceImpl<DrDeviceAdjustablePotentialMapper, DrDeviceAdjustablePotential> implements DrDeviceAdjustablePotentialService {


    @Resource
    private com.xqxy.sys.modular.dict.service.DictTypeService dictTypeService;

    @Resource
    private DrDeviceAdjustablePotentialMapper drDeviceAdjustablePotentialMapper;

    @Resource
    private DeviceAdjustableBaseService adjustableBaseService;


    @Resource
    private DataAccessStrategyContext dataAccessStrategyContext;

    @Value("${dataAccessStrategy}")
    private String dataStrategy;

    /**
     * <pre>设备可调节潜力分页
     * 接口修改：添加数据权限 2022/2/21</pre>
     * @param drDeviceAdjustablePotentialDTO 设备可调节潜力入参
     */
    @Override
    public List<DrDeviceAdjustablePotentialVO> pageDeviceAdjustable(Page<DrDeviceAdjustablePotentialVO> page,DrDeviceAdjustablePotentialDTO drDeviceAdjustablePotentialDTO) {
        // 添加数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if(!OrgTitleEnum.PROVINCE.getCode().equals(currenUserInfo.getOrgTitle())){
            List<String> orgIds = OrganizationUtil.getAllOrgByOrgNo();
            drDeviceAdjustablePotentialDTO.setOrgIds(orgIds);
        }

        //请求字典接口
        DictTypeParam dictTypeParam = new DictTypeParam();

        //需求响应类型的字典
        dictTypeParam.setCode(DrSysDictDataEnum.RESPONSE_TYPEE.getCode());
        List<Dict> responseTypees = dictTypeService.dropDown(dictTypeParam);

        //时间类型的字典
        dictTypeParam.setCode(DrSysDictDataEnum.TIME_TYPE.getCode());
        List<Dict> timeTypees = dictTypeService.dropDown(dictTypeParam);

        List<DrDeviceAdjustablePotentialVO> list = drDeviceAdjustablePotentialMapper.pageDeviceAdjustable(page,drDeviceAdjustablePotentialDTO);
        for (DrDeviceAdjustablePotentialVO device : list) {
            responseTypees.forEach(n->{
                if(n.get("code").toString().equals(device.getResponseType())){
                    device.setResponseTypeDesc(n.get("value").toString());
                }
            });

            timeTypees.forEach(n->{
                if(n.get("code").toString().equals(device.getTimeType())){
                    device.setTimeTypeDesc(n.get("value").toString());
                }
            });
        }
        return list;
    }


    /**
     * 设备可调节潜力 导出
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    @Override
    public List<DrDeviceAdjustablePotentialVO> exportDeviceAdjustable(DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO) {
        //请求字典接口
        DictTypeParam dictTypeParam = new DictTypeParam();

        //需求响应类型的字典
        dictTypeParam.setCode(DrSysDictDataEnum.RESPONSE_TYPEE.getCode());
        List<Dict> responseTypees = dictTypeService.dropDown(dictTypeParam);

        //时间类型的字典
        dictTypeParam.setCode(DrSysDictDataEnum.TIME_TYPE.getCode());
        List<Dict> timeTypees = dictTypeService.dropDown(dictTypeParam);

        // 联查 基础表
        List<DrDeviceAdjustablePotentialVO> list = drDeviceAdjustablePotentialMapper.exportDeviceAdjustable(deviceAdjustablePotentialDTO);
        for (DrDeviceAdjustablePotentialVO device : list) {
            responseTypees.forEach(n->{
                if(n.get("code").toString().equals(device.getResponseType())){
                    device.setResponseTypeDesc(n.get("value").toString());
                }
            });

            timeTypees.forEach(n->{
                if(n.get("code").toString().equals(device.getTimeType())){
                    device.setTimeTypeDesc(n.get("value").toString());
                }
            });
        }
        return list;
    }

    /**
     * 查询所有 设备可调节潜力 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    @Override
    public List<GroupDeviceTypeAdjustVO> exportGroupDeviceTypeAdjustable(DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO) {
        List<GroupDeviceTypeAdjustVO> list = drDeviceAdjustablePotentialMapper.exportGroupDeviceTypeAdjustable(deviceAdjustablePotentialDTO);

        //请求字典接口
        DictTypeParam dictTypeParam = new DictTypeParam();
        dictTypeParam.setCode(DrSysDictDataEnum.RESPONSE_TYPEE.getCode());

        List<Dict> dicts = dictTypeService.dropDown(dictTypeParam);
        for (GroupDeviceTypeAdjustVO device : list) {
            dicts.forEach(n -> {
                if (n.get("code").toString().equals(device.getResponseType())) {
                    device.setResponseTypeDesc(n.get("value").toString());
                }
            });
        }
        return list;
    }

    /**
     * 查询所有 设备可调节潜力 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    @Override
    public List<GroupCityDeviceAdjustVO> exportGroupDeviceAdjustable(DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO) {
        List<GroupCityDeviceAdjustVO> list = drDeviceAdjustablePotentialMapper.exportGroupDeviceAdjustable(deviceAdjustablePotentialDTO);
        return list;
    }

    /**
     * 设备可调节潜力 新增
     * @param deviceAdjustablePotential
     * @return
     */
    @Override
    public void addDeviceAdjustable(DrDeviceAdjustablePotential deviceAdjustablePotential) {
        deviceAdjustablePotential.setId(SnowflakeIdWorker.generateId());
        this.save(deviceAdjustablePotential);
    }

    /**
     * 设备可调节潜力 修改
     * @param deviceAdjustablePotential
     * @return
     */
    @Override
    public void editDeviceAdjustable(DrDeviceAdjustablePotential deviceAdjustablePotential) {
        this.updateById(deviceAdjustablePotential);
        DeviceAdjustableBase adjustableBase = adjustableBaseService.getById(deviceAdjustablePotential.getDeviceId());
        /*if(null!=adjustableBase) {
            adjustableBase.setDeviceName(deviceAdjustablePotential.getDeviceName());
            adjustableBase.setDeviceTypeCode(deviceAdjustablePotential.getDeviceTypeCode());
            adjustableBaseService.updateById(adjustableBase);
        }*/

    }

    /**
     * 设备可调节潜力 删除
     * @param id
     * @return
     */
    @Override
    public void deleteDeviceAdjustable(Long id) {
        this.removeById(id);
    }



    /**
     * 设备情况统计列表 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    @Override
    public List<GroupCityDeviceAdjustVO> groupDeviceAdjustable(Page<GroupCityDeviceAdjustVO> page,DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO) {
        if(!(null != deviceAdjustablePotentialDTO.getYear() && deviceAdjustablePotentialDTO.getYear().trim().length()>0)){
            Calendar calendar = Calendar.getInstance();
            Integer year  = calendar.get(Calendar.YEAR);
            deviceAdjustablePotentialDTO.setYear(year.toString());
        }

        List<GroupCityDeviceAdjustVO> list = drDeviceAdjustablePotentialMapper.groupDeviceAdjustable(page,deviceAdjustablePotentialDTO);
        return list;
    }

    /**
     * 设备情况统计详情 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    @Override
    public List<GroupDeviceTypeAdjustVO> groupDeviceTypeAdjustable(Page<GroupDeviceTypeAdjustVO> page,DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO) {
        List<GroupDeviceTypeAdjustVO> list = drDeviceAdjustablePotentialMapper.groupDeviceTypeAdjustable(page,deviceAdjustablePotentialDTO);

        //请求字典接口
        DictTypeParam dictTypeParam = new DictTypeParam();
        dictTypeParam.setCode(DrSysDictDataEnum.RESPONSE_TYPEE.getCode());

        List<Dict> dicts = dictTypeService.dropDown(dictTypeParam);
        for (GroupDeviceTypeAdjustVO device : list) {
            dicts.forEach(n -> {
                if (n.get("code").toString().equals(device.getResponseType())) {
                    device.setResponseTypeDesc(n.get("value").toString());
                }
            });
        }
        return list;
    }

    @Override
    public  DrDeviceAdjustablePotential getByDeviceId(String deviceId)
    {
        LambdaQueryWrapper<DrDeviceAdjustablePotential> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(StringUtil.isNullOrEmpty(deviceId))
        {
            lambdaQueryWrapper.eq(DrDeviceAdjustablePotential::getDeviceId,deviceId);
        }
        List<DrDeviceAdjustablePotential> list=this.list(lambdaQueryWrapper);
        if(CollectionUtil.isNotEmpty(list))
        {
            return list.get(0);
        }
        return null;
    }

    /**
     * 设备监测详情展示
     * @param monitorParam
     * @return
     */
    @Override
    public Page<DrDeviceAdjustablePotential> detailBydeviceId(Page<DrDeviceAdjustablePotential> page, DeviceMonitorParam monitorParam) {
        QueryWrapper<DrDeviceAdjustablePotential> queryWrapper = new QueryWrapper<>();

        if(ObjectUtils.isNotEmpty(monitorParam)){
            if(!StringUtil.isNullOrEmpty(monitorParam.getDeviceId())){
                queryWrapper.eq("DEVICE_ID",monitorParam.getDeviceId());
            }
        }
        return this.page(page,queryWrapper);
    }


    /**
     * 设备监测 查询历史负荷
     * @param
     * @return
     */
    @Override
    public List<String> getCurveByDeviceAndDate(String deviceId, String date) {
        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataStrategy);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String format = simpleDateFormat.format(calendar.getTime());

        date = date.replace("-","");
        List<EquipmentRecordVO> list;
        if(format.equals(date)){
            list = getDataStrategy.queryDeviceRealTimeCurvePage(deviceId);
        }else {
            list = getDataStrategy.queryDeviceHistoryCurvePage(deviceId, date);
        }


        List<String> stringList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            //由于只传一个 设备编号 和 一个日期 只返回一条数据
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

        return stringList;
    }
}
