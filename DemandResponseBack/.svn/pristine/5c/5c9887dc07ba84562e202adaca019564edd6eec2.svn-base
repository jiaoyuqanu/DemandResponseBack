package com.xqxy.dr.modular.adjustable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.adjustable.DTO.DrDeviceAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.VO.DrDeviceAdjustablePotentialVO;
import com.xqxy.dr.modular.adjustable.VO.GroupCityAdjustVO;
import com.xqxy.dr.modular.adjustable.VO.GroupCityDeviceAdjustVO;
import com.xqxy.dr.modular.adjustable.VO.GroupDeviceTypeAdjustVO;
import com.xqxy.dr.modular.adjustable.entity.DrDeviceAdjustablePotential;
import com.xqxy.dr.modular.device.param.DeviceMonitorParam;

import java.util.List;

public interface DrDeviceAdjustablePotentialService extends IService<DrDeviceAdjustablePotential> {

    /**
     * 用户可调节潜力分页
     * @param drDeviceAdjustablePotentialDTO
     * @return
     */
    List<DrDeviceAdjustablePotentialVO> pageDeviceAdjustable(Page<DrDeviceAdjustablePotentialVO> page,DrDeviceAdjustablePotentialDTO drDeviceAdjustablePotentialDTO);

    /**
     * 设备可调节潜力 新增
     * @param deviceAdjustablePotential
     * @return
     */
    void addDeviceAdjustable(DrDeviceAdjustablePotential deviceAdjustablePotential);


    /**
     * 设备可调节潜力 修改
     * @param deviceAdjustablePotential
     * @return
     */
    void editDeviceAdjustable(DrDeviceAdjustablePotential deviceAdjustablePotential);

    /**
     * 设备可调节潜力 删除
     * @param id
     * @return
     */
    void deleteDeviceAdjustable(Long id);



    /**
     * 设备情况统计列表 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    List<GroupCityDeviceAdjustVO> groupDeviceAdjustable(Page<GroupCityDeviceAdjustVO> page,DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO);

    /**
     * 设备情况统计详情 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    List<GroupDeviceTypeAdjustVO> groupDeviceTypeAdjustable(Page<GroupDeviceTypeAdjustVO> page,DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO);


    /**
     * 设备可调节潜力 导出
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    List<DrDeviceAdjustablePotentialVO> exportDeviceAdjustable(DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO);

    /**
     * 查询所有 设备可调节潜力 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    List<GroupDeviceTypeAdjustVO> exportGroupDeviceTypeAdjustable(DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO);

    /**
     * 查询所有 设备可调节潜力 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    List<GroupCityDeviceAdjustVO> exportGroupDeviceAdjustable(DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO);

    DrDeviceAdjustablePotential getByDeviceId(String deviceId);

    Page<DrDeviceAdjustablePotential> detailBydeviceId(Page<DrDeviceAdjustablePotential> page, DeviceMonitorParam monitorParam);


    /**
     * 设备监测 查询历史负荷
     * @param
     * @return
     */
    List<String> getCurveByDeviceAndDate(String deviceId, String date);
}
