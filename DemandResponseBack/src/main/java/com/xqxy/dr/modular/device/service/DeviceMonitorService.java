package com.xqxy.dr.modular.device.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.adjustable.entity.DrDeviceAdjustablePotential;
import com.xqxy.dr.modular.device.entity.DeviceMonitor;
import com.xqxy.dr.modular.device.entity.ProportionOfUsers;
import com.xqxy.dr.modular.device.entity.RegionalResources;
import com.xqxy.dr.modular.device.entity.ResourceOverview;
import com.xqxy.dr.modular.device.param.DeviceMonitorParam;


import java.util.List;
import java.util.Map;

public interface DeviceMonitorService {
    List<DeviceMonitor> page(Page<DeviceMonitor> page, DeviceMonitorParam monitorParam);

    ResourceOverview resourceOverview();

    List<ProportionOfUsers> proportionOfUsers();

    RegionalResources regionalResources();
}
