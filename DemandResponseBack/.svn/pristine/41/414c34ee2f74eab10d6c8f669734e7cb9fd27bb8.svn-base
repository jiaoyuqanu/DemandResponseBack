package com.xqxy.dr.modular.device.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.device.entity.DeviceMonitor;
import com.xqxy.dr.modular.device.entity.RegionalResources;
import com.xqxy.dr.modular.device.param.DeviceMonitorParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface DeviceMonitorMapper {

    List<DeviceMonitor> page(Page<DeviceMonitor> page, @Param("item") DeviceMonitorParam monitorParam);

    Integer getUserNum();

    Integer getDeviceNum();

    BigDecimal getAccessCapacity();

    Integer getBigUserNum();

    List<RegionalResources> regionalResources();

}
