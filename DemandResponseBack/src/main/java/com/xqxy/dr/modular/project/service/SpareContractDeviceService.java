package com.xqxy.dr.modular.project.service;

import com.xqxy.dr.modular.project.entity.SpareContractDevice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.project.params.SpareContractDeviceParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 备用容量签约设备表 服务类
 * </p>
 *
 * @author Caoj
 * @since 2022-01-05
 */
public interface SpareContractDeviceService extends IService<SpareContractDevice> {

    /**
     * 展示某个项目明细下的设备签约情况
     *
     * @param spareContractDeviceParam 备用容量签约设备参数：consId获取用户下的设备，contractId和projectDetailId关联签约明细
     * @return java.util.List<com.xqxy.dr.modular.project.entity.SpareContractDevice>
     * @date 1/6/2022 10:15
     * @author Caoj
     */
    Map<String,List> listDeviceContract(SpareContractDeviceParam spareContractDeviceParam);

    /**
     * 项目明细的设备签约
     *
     * @param spareContractDeviceParam 备用容量参数
     * @date 1/7/2022 14:29
     * @author Caoj
     */
    void deviceContract(SpareContractDeviceParam spareContractDeviceParam);
}
