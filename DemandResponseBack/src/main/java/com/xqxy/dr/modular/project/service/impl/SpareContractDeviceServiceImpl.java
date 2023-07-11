package com.xqxy.dr.modular.project.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.adjustable.mapper.DrDeviceAdjustablePotentialMapper;
import com.xqxy.dr.modular.adjustable.service.DrDeviceAdjustablePotentialService;
import com.xqxy.dr.modular.device.entity.DeviceAdjustableBase;
import com.xqxy.dr.modular.device.service.DeviceAdjustableBaseService;
import com.xqxy.dr.modular.project.entity.ConsContractDetail;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.project.entity.ProjectDetail;
import com.xqxy.dr.modular.project.entity.SpareContractDevice;
import com.xqxy.dr.modular.project.mapper.SpareContractDeviceMapper;
import com.xqxy.dr.modular.project.params.SpareContractDeviceParam;
import com.xqxy.dr.modular.project.service.ConsContractDetailService;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.dr.modular.project.service.ProjectDetailService;
import com.xqxy.dr.modular.project.service.SpareContractDeviceService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 备用容量签约设备表 服务实现类
 * </p>
 *
 * @author Caoj
 * @since 2022-01-05
 */
@Service
public class SpareContractDeviceServiceImpl extends ServiceImpl<SpareContractDeviceMapper, SpareContractDevice> implements SpareContractDeviceService {

    @Resource
    private ProjectDetailService projectDetailService;

    @Resource
    private ConsContractDetailService consContractDetailService;

    @Resource
    private ConsContractInfoService consContractInfoService;

    @Resource
    private DeviceAdjustableBaseService deviceAdjustableBaseService;

    @Override
    public Map<String,List> listDeviceContract(SpareContractDeviceParam spareContractDeviceParam) {
        Map<String,List> map = new HashMap<>();

        //查询该用户 的 所有设备
        LambdaQueryWrapper<DeviceAdjustableBase> deviceWrapper = new LambdaQueryWrapper<>();
        if(spareContractDeviceParam != null){
            if(!StringUtils.isEmpty(spareContractDeviceParam.getConsId())){
                deviceWrapper.eq(DeviceAdjustableBase::getConsId,spareContractDeviceParam.getConsId());
            }
        }
        List<DeviceAdjustableBase> deviceAdjustableBases = deviceAdjustableBaseService.list(deviceWrapper);

        //查询该用户已经签约的项目
        List<SpareContractDevice> spareContractDevices = getBaseMapper().queryDeviceContract(spareContractDeviceParam.getConsId(), spareContractDeviceParam.getProjectDetailId());
        List<String> deviceIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(spareContractDevices)){
            deviceIds = spareContractDevices.stream().map(SpareContractDevice::getDeviceId).collect(Collectors.toList());

            List<DeviceAdjustableBase> allDevices = new ArrayList<>();
            //排除掉 该用户所有设备中 已经签约设备
            for (int i = 0; i < deviceAdjustableBases.size(); i++) {
                DeviceAdjustableBase deviceAdjustableBase = deviceAdjustableBases.get(i);
                if (!deviceIds.contains(deviceAdjustableBase.getDeviceId())){
                    allDevices.add(deviceAdjustableBase);
                }
            }
            map.put("allDevices",allDevices);
        }else {
            map.put("allDevices",deviceAdjustableBases);
        }
        map.put("spareContractDevices",spareContractDevices);
        return map;
    }

    /**
     * 项目明细的设备签约
     *
     * @param spareContractDeviceParam 备用容量参数
     * @date 1/7/2022 14:29
     * @author Caoj
     */
    @Override
    public void deviceContract(SpareContractDeviceParam spareContractDeviceParam) {
        // 删除设备签约信息
        LambdaQueryWrapper<SpareContractDevice> spareDeviceQueryWrapper = new LambdaQueryWrapper<>();
        if(ObjectUtil.isNotEmpty(spareContractDeviceParam.getConsId())){
            spareDeviceQueryWrapper.eq(SpareContractDevice::getConsId,spareContractDeviceParam.getConsId());
        }
        if(ObjectUtil.isNotEmpty(spareContractDeviceParam.getDetailId())){
            spareDeviceQueryWrapper.eq(SpareContractDevice::getProjectDetailId,spareContractDeviceParam.getDetailId());
        }
        this.remove(spareDeviceQueryWrapper);

        ProjectDetail projectDetail = projectDetailService.getById(spareContractDeviceParam.getDetailId());
        LambdaQueryWrapper<SpareContractDevice> spareWrapper = new LambdaQueryWrapper<>();
        spareWrapper.eq(SpareContractDevice::getContractId,spareContractDeviceParam.getContractId());
        this.remove(spareWrapper);

        for (SpareContractDevice spareContractDevice : spareContractDeviceParam.getDeviceList()) {
            spareContractDevice.setId(null);
            spareContractDevice.setProjectId(projectDetail.getProjectId());
            spareContractDevice.setContractId(spareContractDeviceParam.getContractDetailId());
            spareContractDevice.setResponseType(projectDetail.getResponseType());
            spareContractDevice.setTimeType(projectDetail.getTimeType());
            spareContractDevice.setContractCap(new BigDecimal(spareContractDevice.getRatedPower()));
//            spareContractDevice.setParticType(consContract.getParticType());
            spareContractDevice.setProjectDetailId(projectDetail.getDetailId());
        }
        saveBatch(spareContractDeviceParam.getDeviceList());
    }
}
