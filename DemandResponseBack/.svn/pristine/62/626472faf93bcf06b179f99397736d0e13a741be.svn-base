package com.xqxy.dr.modular.pcdata.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xqxy.core.context.requestno.RequestNoContext;
import com.xqxy.dr.modular.adjustable.entity.DrConsAdjustablePotential;
import com.xqxy.dr.modular.adjustable.entity.DrDeviceAdjustablePotential;
import com.xqxy.dr.modular.adjustable.service.DrConsAdjustablePotentialService;
import com.xqxy.dr.modular.adjustable.service.DrDeviceAdjustablePotentialService;
import com.xqxy.dr.modular.device.entity.DeviceAdjustableBase;
import com.xqxy.dr.modular.device.service.DeviceAdjustableBaseService;
import com.xqxy.dr.modular.pcdata.param.PcCons;
import com.xqxy.dr.modular.pcdata.param.PcConsAblity;
import com.xqxy.dr.modular.pcdata.param.PcDevice;
import com.xqxy.dr.modular.pcdata.param.PcDeviceAblity;
import com.xqxy.dr.modular.prediction.entity.ConsAbility;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: zgy
 * Date: 2021/11/26 15:55
 * Content: 资源库普查信息入库接口，第三方调用
 */
@RestController
@RequestMapping("/adjustable/v1")
public class PcDataController {

    @Autowired
    private ConsService consService;
    @Autowired
    private DrConsAdjustablePotentialService drConsAdjustablePotentialService;

    @Autowired
    private DeviceAdjustableBaseService deviceAdjustableBaseService;
    @Autowired
    private DrDeviceAdjustablePotentialService drDeviceAdjustablePotentialService;

    private static final Log log = Log.get();



    @PostMapping("/updateUserInfo")
    @Transactional
    public String updateUserInfo(@RequestBody List<PcCons> pcData) {


        JSONObject resjo = new JSONObject();
        resjo.put("code", 0);
        resjo.put("msg", "处理成功");

        if (pcData == null || pcData.size() == 0) {
            return resjo.toJSONString();
        }


        List<String> ids = pcData.stream().map(PcCons::getYHHH).collect(Collectors.toList());
        log.info(">>> 传入户号集合：{}", ids.toString());
        //判断推送过来的数据是否已经入库，找到已经入库的id
        List<Cons> consList = consService.listByIds(ids);
        List<String> existConsIdList = consList.stream().map(Cons::getId).collect(Collectors.toList());
        log.info(">>> 已在库中的档案户号集合：{}", existConsIdList.toString());
        //未入库的
        ids.removeAll(existConsIdList);

        log.info(">>> 未入库档案户号集合：{}", ids.toString());
//        List<Cons> leftCons = consList.stream().filter(cons->"0".equals(cons.getState())).collect(Collectors.toList());

        //查询到了说明已经入库
        //判断状态是否是0：普查资源，不是0忽略，只处理普查资源
        //List<Cons> leftCons = consList.stream().filter(cons->"0".equals(cons.getState())).collect(Collectors.toList());
        List<Cons> updateConsList = new ArrayList<>();
        List<DrConsAdjustablePotential> updrConsAdjustablePotentialArrayList = new ArrayList<>();
        List<DrConsAdjustablePotential> adddrConsAdjustablePotentialArrayList = new ArrayList<>();
        List<DeviceAdjustableBase> updeviceAdjustableBaseList = new ArrayList<>();
        List<DeviceAdjustableBase> adddeviceAdjustableBaseList = new ArrayList<>();
        List<DrDeviceAdjustablePotential> drDeviceAdjustablePotentialList = new ArrayList<>();
        for (Cons cons : consList) {
            if ("0".equals(cons.getState())) {
                //户号只处理
                for (PcCons pcCons : pcData) {
                    if (cons.getId().equals(pcCons.getYHHH())) {
                        updateConsList.add(this.transPcCons2Cons(pcCons));
                        //查询用户潜力信息
                        DrConsAdjustablePotential drConsAdjustablePotential = drConsAdjustablePotentialService.getbyConsId(cons.getId());
                        //判断用户潜力档案是否存在
                        if (ObjectUtil.isNotNull(drConsAdjustablePotential)) {
                            DrConsAdjustablePotential drConsAdjustablePotential1 = this.transPcConsAblity(pcCons.getPcConsAblity());
                            drConsAdjustablePotential1.setId(drConsAdjustablePotential.getId());
                            drConsAdjustablePotential1.setConsId(drConsAdjustablePotential.getConsId());
                            updrConsAdjustablePotentialArrayList.add(drConsAdjustablePotential1);
                        } else {
                            DrConsAdjustablePotential drConsAdjustablePotential1 = this.transPcConsAblity(pcCons.getPcConsAblity());
//                            drConsAdjustablePotential1.setId(drConsAdjustablePotential.getId());
                            drConsAdjustablePotential1.setConsId(cons.getId());
                            adddrConsAdjustablePotentialArrayList.add(drConsAdjustablePotential1);
                        }
                        //查询用户下的设备信息
                        List<PcDevice> deviceList = pcCons.getDevices();
                        //查询用户下的所有已存在设备信息
                        List<DeviceAdjustableBase> deviceAdjustableBaseList = deviceAdjustableBaseService.getByConsId(cons.getId());
//                        //查询普查资源设备Id
                        List<String> deviceIdList = deviceList.stream().map(PcDevice::getGUID).collect(Collectors.toList());
//
                        //已存在的用户设备进行更新
                        for (PcDevice pcDevice : deviceList) {
                            for (DeviceAdjustableBase deviceAdjustableBase : deviceAdjustableBaseList) {
                                if (pcDevice.getGUID().equals(deviceAdjustableBase.getDeviceId())) {
                                    DeviceAdjustableBase deviceAdjustableBase1 = this.transPcDevice(pcDevice);
                                    deviceAdjustableBase1.setId(deviceAdjustableBase.getId());
                                    updeviceAdjustableBaseList.add(deviceAdjustableBase1);
                                    deviceIdList.remove(pcDevice.getGUID());
                                }
                            }
                        }
                        //不存在的用户设备进行保存
                        if (deviceIdList.size() > 0) {
                            for (String id : deviceIdList) {
                                List<PcDevice> deviceAdjustableBaseList1 = deviceList.stream().filter(device -> device.getGUID().equals(id)).collect(Collectors.toList());
                                for (PcDevice pcDevice : deviceAdjustableBaseList1) {
                                    adddeviceAdjustableBaseList.add(this.transPcDevice(pcDevice));
                                }
                            }
                        }
                        List<PcDeviceAblity> pcDeviceAblityList = deviceList.stream().map(PcDevice::getPcDeviceAblity).collect(Collectors.toList());
                        //查询用户下的设备潜力信息
                        for (PcDeviceAblity pcDeviceAblity : pcDeviceAblityList) {
                            DrDeviceAdjustablePotential drDeviceAdjustablePotential = drDeviceAdjustablePotentialService.getByDeviceId(pcDeviceAblity.getDEVICEGUID());
                            DrDeviceAdjustablePotential drDeviceAdjustablePotential1 = this.transPcDevicePotential(pcDeviceAblity);
                            if (ObjectUtil.isNotNull(drDeviceAdjustablePotential) && ObjectUtil.isNotEmpty(drDeviceAdjustablePotential)) {
                                drDeviceAdjustablePotential1.setId(drDeviceAdjustablePotential.getId());
                            }
                            drDeviceAdjustablePotentialList.add(drDeviceAdjustablePotential1);

                        }

                    }
                }
            }
        }
        //用户档案更新
        consService.updateBatchById(updateConsList);
        //用户潜力档案更新
        drConsAdjustablePotentialService.updateBatchById(updrConsAdjustablePotentialArrayList);
        //用户潜力档案保存
        drConsAdjustablePotentialService.updateBatchById(adddrConsAdjustablePotentialArrayList);
        //用户设备更新
        deviceAdjustableBaseService.updateBatchById(updeviceAdjustableBaseList);
        //用户设备保存
        deviceAdjustableBaseService.saveBatch(adddeviceAdjustableBaseList);
        //设备潜力保存或更新
        drDeviceAdjustablePotentialService.saveOrUpdateBatch(drDeviceAdjustablePotentialList);


        //所有未入库普查数据
        List<PcCons> newPcDataList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(ids)&&ids.size() > 0){
            for (String id : ids) {
                System.out.println("id"+ ids);
                List<PcCons> newPcData = pcData.stream().filter(pcCons -> pcCons.getYHHH().contains(id)).collect(Collectors.toList());
                newPcDataList.addAll(newPcData);
            }
    }
        //定义批量入库列表
        List<Cons> dbCons = new ArrayList<>();
        List<DrConsAdjustablePotential> dbConsAbilities = new ArrayList<>();
        List<DeviceAdjustableBase> dbDevices = new ArrayList<>();
        List<DrDeviceAdjustablePotential> dbDeviceAp = new ArrayList<>();

        //获取所有的用户
        for(int i=0; i < newPcDataList.size(); i ++){
            PcCons pcCons = newPcDataList.get(i);
            dbCons.add(transPcCons2Cons(pcCons));

            PcConsAblity pcConsAblity = pcCons.getPcConsAblity();
            DrConsAdjustablePotential drConsAdjustablePotential= transPcConsAblity(pcConsAblity);
            drConsAdjustablePotential.setConsId(transPcCons2Cons(pcCons).getId());
            dbConsAbilities.add(transPcConsAblity(pcConsAblity));

            List<PcDevice> pcDeviceList=pcCons.getDevices();
            for(PcDevice device:pcDeviceList)
            {
                dbDevices.add(this.transPcDevice(device));
                PcDeviceAblity pcDeviceAblity=device.getPcDeviceAblity();
                dbDeviceAp.add(this.transPcDevicePotential(pcDeviceAblity));
            }

        }
        consService.saveOrUpdateBatch(dbCons);
        drConsAdjustablePotentialService.saveOrUpdateBatch(dbConsAbilities);
        deviceAdjustableBaseService.saveOrUpdateBatch(dbDevices);
        drDeviceAdjustablePotentialService.saveOrUpdateBatch(dbDeviceAp);

        return resjo.toJSONString();
    }

    /**
     * 可调节资源删除
     * @param data 用户户号
     * @return
     */
    @PostMapping("/delUserInfo")
    @Transactional
    public String delUserInfo(@RequestBody List<String> data) {
        JSONObject resjo = new JSONObject();

        //查询所有户号
        QueryWrapper<Cons> queryWrapper = new QueryWrapper();
        queryWrapper.eq("state",0);//只能删除普查数据，0是普查数据
        queryWrapper.in("id",data);

        List<Cons> consList = consService.list(queryWrapper);

        for(int i=0; i < consList.size(); i ++){
            Cons con = consList.get(i);
        }

        List<String> existConsIdList = consList.stream().map(Cons::getId).collect(Collectors.toList());
        log.info(">>> 已在库中并为普查资源的档案户号集合：{}", existConsIdList.toString());
        data.removeAll(existConsIdList);
        log.info(">>> 无法删除的档案户号集合：{}，原因为库中无该档案或非普查数据的档案", data.toString());

        if(CollectionUtil.isNotEmpty(existConsIdList)&&existConsIdList.size()>0) {
            //查询用户潜力信息
            List<DrConsAdjustablePotential> drConsAdjustablePotentialList=new ArrayList<>();
            LambdaQueryWrapper<DrConsAdjustablePotential> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(DrConsAdjustablePotential::getConsId,existConsIdList);
            drConsAdjustablePotentialList=drConsAdjustablePotentialService.list(lambdaQueryWrapper);
            List<String> existConsIdPotentialList = consList.stream().map(Cons::getId).collect(Collectors.toList());
            log.info(">>> 准备删除的用户潜力档案集合：{}", drConsAdjustablePotentialList.toString());

            //查询用户下的设备信息
            List<DeviceAdjustableBase> deviceAdjustableBaseList=new ArrayList<>();
            LambdaQueryWrapper<DeviceAdjustableBase> devicelambdaQueryWrapper = new LambdaQueryWrapper<>();
            devicelambdaQueryWrapper.in(DeviceAdjustableBase::getConsId,existConsIdList);
            deviceAdjustableBaseList=deviceAdjustableBaseService.list(devicelambdaQueryWrapper);
            log.info(">>> 准备删除的设备档案集合：{}", deviceAdjustableBaseList.toString());

            List<Long> existdeviceIdList = deviceAdjustableBaseList.stream().map(DeviceAdjustableBase::getId).collect(Collectors.toList());
            //查询用户下的设备潜力信息
            List<DrDeviceAdjustablePotential> drDeviceAdjustablePotentialList=new ArrayList<>();
            LambdaQueryWrapper<DrDeviceAdjustablePotential> drDeviceAdjustablePotentialLambdaQueryWrapper = new LambdaQueryWrapper<>();
            if(CollectionUtil.isNotEmpty(existdeviceIdList)&&existdeviceIdList.size()>0) {
                drDeviceAdjustablePotentialLambdaQueryWrapper.in(DrDeviceAdjustablePotential::getDeviceId, existdeviceIdList);
                drDeviceAdjustablePotentialList = drDeviceAdjustablePotentialService.list(drDeviceAdjustablePotentialLambdaQueryWrapper);
                //批量删除设备潜力信息
                drDeviceAdjustablePotentialService.remove(drDeviceAdjustablePotentialLambdaQueryWrapper);
            }
            log.info(">>> 准备删除的设备潜力档案集合：{}", drDeviceAdjustablePotentialList.toString());

            //批量删除用户信息
            consService.remove(queryWrapper);
            //批量删除用户潜力信息
            drConsAdjustablePotentialService.remove(lambdaQueryWrapper);

            //批量删除设备信息
            deviceAdjustableBaseService.remove(devicelambdaQueryWrapper);


        }




        resjo.put("code",0);
        resjo.put("msg","处理成功");
        return resjo.toJSONString();
    }
    private Cons transPcCons2Cons(PcCons pcCons){
        Cons cons = new Cons();
        if(pcCons == null){
            return null;
        }
        cons.setId(pcCons.getYHHH());
        cons.setConsName(pcCons.getYHMC());
        cons.setBigTradeName(pcCons.getHYDLMC());
        cons.setBigTradeCode(pcCons.getHYDLBM());
        cons.setTradeName(pcCons.getHYXFMC());
        cons.setTradeCode(pcCons.getHYXFBM());
        cons.setElecAddr(pcCons.getYDDZ());
        try {
            cons.setContractCap(new BigDecimal(pcCons.getZDRL()));
        }
        catch (Exception e)
        {
            cons.setContractCap(null);
        }
        try {
            cons.setRunCap(new BigDecimal(pcCons.getYXRL()));
        }
        catch (Exception e)
        {
            cons.setRunCap(null);
        }
        cons.setOrgName(pcCons.getGDDWMC());
        cons.setOrgNo(pcCons.getGDDWBM());
        cons.setSubsName(pcCons.getBDZ());
        cons.setSubsNo(pcCons.getBDZBH());
        cons.setLineName(pcCons.getXL());
        cons.setLineNo(pcCons.getXLBH());
        cons.setTgName(pcCons.getTQ());
        cons.setTgNo(pcCons.getTQBH());
        cons.setTypeCode(pcCons.getDYLXBM());
        return cons;
    }

    private DrConsAdjustablePotential transPcConsAblity(PcConsAblity pcConsAblity){
        DrConsAdjustablePotential drConsAdjustablePotential = new DrConsAdjustablePotential();
        if(pcConsAblity == null){
            return null;
        }
        drConsAdjustablePotential.setBigClassName(pcConsAblity.getYHDLMC());
        drConsAdjustablePotential.setBigClassCode(pcConsAblity.getYHDLBM());
        drConsAdjustablePotential.setClassName(pcConsAblity.getYHXLMC());
        drConsAdjustablePotential.setClassCode(pcConsAblity.getYHXLBM());
        drConsAdjustablePotential.setDesPrepareTime(Integer.valueOf(pcConsAblity.getXFXYZBSJ()));
        drConsAdjustablePotential.setRisPrepareTime(Integer.valueOf(pcConsAblity.getTGXYZBSJ()));
        drConsAdjustablePotential.setProdStartTime(pcConsAblity.getYXKSSJ());
        drConsAdjustablePotential.setProdEndTime(pcConsAblity.getYXJSSJ());
        drConsAdjustablePotential.setSafetyLoad(new BigDecimal(pcConsAblity.getBARL()));
        drConsAdjustablePotential.setLastYearRespPower(new BigDecimal(pcConsAblity.getSNSJXYZDFH()));
        drConsAdjustablePotential.setLastPartDesNum(Integer.valueOf(pcConsAblity.getQNCYXFXYCS()));
        drConsAdjustablePotential.setLastDesSumPower(new BigDecimal(pcConsAblity.getQNLJXJFH()));
        drConsAdjustablePotential.setLastPartRisNum(Integer.valueOf(pcConsAblity.getQNCYTGXYCS()));
        drConsAdjustablePotential.setLastRisSumEnergy(new BigDecimal(pcConsAblity.getQNLJXNDL()));
        drConsAdjustablePotential.setExpectDesPrice(new BigDecimal(pcConsAblity.getQWXFDJ()));
        drConsAdjustablePotential.setYear(new BigDecimal(pcConsAblity.getPCRQ()));
        drConsAdjustablePotential.setExpectRisPrice(new BigDecimal(pcConsAblity.getQWTGDJ()));
        return drConsAdjustablePotential;
    }

    private DeviceAdjustableBase transPcDevice(PcDevice pcDevice){
        DeviceAdjustableBase deviceAdjustableBase = new DeviceAdjustableBase();
        if(pcDevice == null){
            return null;
        }
        deviceAdjustableBase.setDeviceId(pcDevice.getGUID());
        deviceAdjustableBase.setConsId(pcDevice.getUSERID());
        deviceAdjustableBase.setDeviceName(pcDevice.getSBMC());
        deviceAdjustableBase.setDeviceTypeName(pcDevice.getSBLXMC());
        deviceAdjustableBase.setDeviceTypeCode(pcDevice.getSBLX());
        deviceAdjustableBase.setDeviceNum(Integer.valueOf(pcDevice.getSBSL()));
        deviceAdjustableBase.setRatedVoltage(Integer.valueOf(pcDevice.getEDDY()));
        deviceAdjustableBase.setRatedPower(Integer.valueOf(pcDevice.getEDGL()));
        deviceAdjustableBase.setDesResponseTime(pcDevice.getXFXYKCXSJ());
        deviceAdjustableBase.setRisResponseTime(pcDevice.getTGXYKCXSJ());
        deviceAdjustableBase.setDeviceRiseTime(pcDevice.getSBPPSJ());
        return deviceAdjustableBase;
    }

    private DrDeviceAdjustablePotential transPcDevicePotential(PcDeviceAblity pcDeviceAblity){
        DrDeviceAdjustablePotential drDeviceAdjustablePotential = new DrDeviceAdjustablePotential();
        if(pcDeviceAblity == null){
            return null;
        }
        drDeviceAdjustablePotential.setDeviceId(pcDeviceAblity.getDEVICEGUID());
        drDeviceAdjustablePotential.setResponsePower(new BigDecimal(pcDeviceAblity.getXYFH()));
        drDeviceAdjustablePotential.setResponseType(pcDeviceAblity.getXYLX());
        drDeviceAdjustablePotential.setTimeType(pcDeviceAblity.getSJLX());
        drDeviceAdjustablePotential.setStartTime(pcDeviceAblity.getSTARTTIME());
        drDeviceAdjustablePotential.setEndTime(pcDeviceAblity.getENDTIME());

        return drDeviceAdjustablePotential;
    }
}
