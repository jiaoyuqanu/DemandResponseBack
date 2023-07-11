package com.xqxy.dr.modular.adjustable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.adjustable.DTO.DrDeviceAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.VO.DrDeviceAdjustablePotentialVO;
import com.xqxy.dr.modular.adjustable.VO.GroupCityAdjustVO;
import com.xqxy.dr.modular.adjustable.VO.GroupCityDeviceAdjustVO;
import com.xqxy.dr.modular.adjustable.VO.GroupDeviceTypeAdjustVO;
import com.xqxy.dr.modular.adjustable.entity.DrDeviceAdjustablePotential;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DrDeviceAdjustablePotentialMapper extends BaseMapper<DrDeviceAdjustablePotential> {


    /**
     * 设备情况统计列表 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    List<GroupCityDeviceAdjustVO> groupDeviceAdjustable(@Param("page")Page<GroupCityDeviceAdjustVO> page,@Param("drDeviceAdjustablePotentialDTO") DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO);

    /**
     * 设备情况统计详情 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    List<GroupDeviceTypeAdjustVO> groupDeviceTypeAdjustable(@Param("page")Page<GroupDeviceTypeAdjustVO> page,@Param("drDeviceAdjustablePotentialDTO") DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO);

    /**
     * <pre>设备可调节潜力分页
     * 接口修改：添加数据权限 2022/2/21</pre>
     * @param drDeviceAdjustablePotentialDTO 设备可调节潜力入参
     */
    List<DrDeviceAdjustablePotentialVO> pageDeviceAdjustable(@Param("page") Page<DrDeviceAdjustablePotentialVO> page,@Param("drDeviceAdjustablePotentialDTO") DrDeviceAdjustablePotentialDTO drDeviceAdjustablePotentialDTO);


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
    List<GroupCityDeviceAdjustVO> exportGroupDeviceAdjustable(DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO);


    /**
     * 查询所有 设备可调节潜力 分组条件为 市码，查询条件为年度
     * @param deviceAdjustablePotentialDTO
     * @return
     */
    List<GroupDeviceTypeAdjustVO> exportGroupDeviceTypeAdjustable(DrDeviceAdjustablePotentialDTO deviceAdjustablePotentialDTO);
}
