package com.xqxy.dr.modular.device.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.device.VO.DeviceAdjustableBaseVO;
import com.xqxy.dr.modular.device.entity.DeviceAdjustableBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.device.param.DeviceAdjustableBaseParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备可调节基础信息 Mapper 接口
 * </p>
 *
 * @author hu xingxing
 * @since 2021-11-08
 */
public interface DeviceAdjustableBaseMapper extends BaseMapper<DeviceAdjustableBase> {

    /**
     * 电力专责用户 设备列表
     * @param orgIds
     * @param page
     * @param deviceAdjustableBaseParam
     * @return
     */
    Page<DeviceAdjustableBaseVO> page(@Param("orgIds") List<String> orgIds, @Param("page") Page<DeviceAdjustableBaseVO> page, @Param("deviceAdjustableBaseParam") DeviceAdjustableBaseParam deviceAdjustableBaseParam);

    /**
     * 客户 设备列表
     * @param consIdList
     * @param page
     * @param deviceAdjustableBaseParam
     * @return
     */
    Page<DeviceAdjustableBaseVO> pageDeviceBase(@Param("consIdList") List<String> consIdList,@Param("orgIds") Page<DeviceAdjustableBaseVO> page, @Param("deviceAdjustableBaseParam") DeviceAdjustableBaseParam deviceAdjustableBaseParam);
}
