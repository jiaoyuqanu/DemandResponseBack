package com.xqxy.dr.modular.device.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.adjustable.entity.DrDeviceAdjustablePotential;
import com.xqxy.dr.modular.device.entity.DeviceMonitor;
import com.xqxy.dr.modular.device.entity.ProportionOfUsers;
import com.xqxy.dr.modular.device.entity.RegionalResources;
import com.xqxy.dr.modular.device.entity.ResourceOverview;
import com.xqxy.dr.modular.device.mapper.DeviceMonitorMapper;
import com.xqxy.dr.modular.device.param.DeviceMonitorParam;
import com.xqxy.dr.modular.device.service.DeviceMonitorService;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceMonitorServiceImpl implements DeviceMonitorService {
    @Autowired
    DeviceMonitorMapper deviceMonitorMapper;
    @Override
    public List<DeviceMonitor> page(Page<DeviceMonitor> page,DeviceMonitorParam monitorParam) {
        // 添加数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if(!OrgTitleEnum.PROVINCE.getCode().equals(currenUserInfo.getOrgTitle())){
            List<String> orgIds = OrganizationUtil.getAllOrgByOrgNo();
            monitorParam.setOrgIds(orgIds);
        }

        return deviceMonitorMapper.page(page,monitorParam);
    }

    @Override
    public ResourceOverview resourceOverview() {
        ResourceOverview resourceOverview = new ResourceOverview();
        //接入用户数量
        resourceOverview.setConsNum(deviceMonitorMapper.getUserNum());
        //接入设备数
        resourceOverview.setDeviceNum(deviceMonitorMapper.getDeviceNum());
        //接入用户容量
        resourceOverview.setAccessCapacity(deviceMonitorMapper.getAccessCapacity());
        return resourceOverview;
    }

    @Override
    public List<ProportionOfUsers> proportionOfUsers() {
        Integer user1=deviceMonitorMapper.getUserNum();
        Integer user2=deviceMonitorMapper.getBigUserNum();
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        ProportionOfUsers eleUser = new ProportionOfUsers();
       eleUser.setName("电力用户占比");
       eleUser.setValue(numberFormat.format((float)(user1)/(float)(user1+user2)*100));
        ProportionOfUsers bigUsers = new ProportionOfUsers();
        bigUsers.setName("聚合商用户占比");
        bigUsers.setValue(numberFormat.format((float)user2/(float)(user1+user2)*100));
        List<ProportionOfUsers>list=new ArrayList<>();
        list.add(bigUsers);
        list.add(eleUser);
        return list;

    }

    @Override
    public RegionalResources regionalResources() {
        List<RegionalResources> regionalResources = deviceMonitorMapper.regionalResources();
        List<String>areaList=new ArrayList<>();
        List<BigDecimal>resourcesList=new ArrayList<>();
        for (RegionalResources regionalResource : regionalResources) {
            areaList.add(regionalResource.getAreaName());
            resourcesList.add(regionalResource.getResources());
        }
        RegionalResources resources = new RegionalResources();
        resources.setAreaList(areaList);
        resources.setResourcesList(resourcesList);
        return resources;
    }

}
