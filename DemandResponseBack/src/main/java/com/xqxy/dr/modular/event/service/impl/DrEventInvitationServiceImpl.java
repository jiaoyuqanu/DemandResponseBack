package com.xqxy.dr.modular.event.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.enums.RegionLevelEnum;
import com.xqxy.core.poi.POIExcelUtil;
import com.xqxy.core.util.HttpServletUtil;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.event.entity.*;
import com.xqxy.dr.modular.event.enums.RangeTypeEnum;
import com.xqxy.dr.modular.event.mapper.DrEventInvitationMapper;
import com.xqxy.dr.modular.event.mapper.EventMapper;
import com.xqxy.dr.modular.event.service.DrEventInvitationService;
import com.xqxy.dr.modular.event.utils.PoiExcelUtils;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.dr.modular.subsidy.result.RegionConsSubsidyInfo;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户邀约信息(DrEventInvitation)表服务实现类
 *
 * @author makejava
 * @since 2021-07-14 21:37:52
 */
@Service
public class DrEventInvitationServiceImpl extends ServiceImpl<DrEventInvitationMapper, DrEventInvitation> implements DrEventInvitationService {

    @Resource
    DrEventInvitationMapper drEventInvitationMapper;

    @Resource
    EventMapper eventMapper;

    @Autowired
    private SystemClientService systemClientService;

    @Autowired
    private SystemClient systemClient;

    /**
     * 通过ID查询单条数据
     *
     * @param invitationId 主键
     * @return 实例对象
     */
    @Override
    public DrEventInvitation queryById(Long invitationId) {
        return this.drEventInvitationMapper.queryById(invitationId);

    }

    /**
     * 统计数据
     *
     * @param
     * @return 实例对象
     */
    //todo 无调用
    @Override
    public List<DrConsCountEntity> costData(Long eventId, Long cityCode) {
        //如何 cityCode=430000为湖南 直接为空
        if (cityCode != null) {
            if ("430000".equals(String.valueOf(cityCode))) {
                cityCode = null;
            }
        }
        List<DrConsCountEntity> drConsCountEntities = drEventInvitationMapper.costData(eventId, cityCode);

        //如果cityCode为空 统计湖南数据
        if (drConsCountEntities != null && drConsCountEntities.size() > 0) {
            if (cityCode == null) {
                BigDecimal sumInv = new BigDecimal(0);
                BigDecimal sumEva = new BigDecimal(0);
                BigDecimal sumPer1 = new BigDecimal(0);
                BigDecimal sumReplyCap = new BigDecimal(0);
                BigDecimal sumActualCap = new BigDecimal(0);
                BigDecimal sumPer2 = new BigDecimal(0);

                for (DrConsCountEntity d : drConsCountEntities) {
                    sumInv = sumInv.add(d.getInv()); //累加应邀户数
                    sumEva = sumEva.add(d.getEva());//累加响应成功率
                    sumReplyCap = sumReplyCap.add(d.getReplyCap());//累加应邀响应负荷
                    sumActualCap = sumActualCap.add(d.getActualCap());//累加响应负荷
                }

                if (!sumInv.equals(BigDecimal.ZERO)) {
                    sumPer1 = sumEva.divide(sumInv, 4, BigDecimal.ROUND_HALF_UP);
                }
                if (!sumReplyCap.equals(BigDecimal.ZERO)) {
                    sumPer2 = sumActualCap.divide(sumReplyCap, 4, BigDecimal.ROUND_HALF_UP);
                }
                DrConsCountEntity entity = new DrConsCountEntity();
                entity.setEventId(eventId);//时间id
                entity.setCityCode(430000L);//湖南code
                entity.setCityName("湖南");//湖南名称
                entity.setInv(sumInv);
                entity.setEva(sumEva);
                entity.setReplyCap(sumReplyCap);
                entity.setActualCap(sumActualCap);
                entity.setPer1(sumPer1);
                entity.setPer2(sumPer2);
                List<DrConsCountEntity> list = new ArrayList<DrConsCountEntity>();
                list.add(entity);
                list.addAll(drConsCountEntities);
                return list;
            }
        }


        return drConsCountEntities;
    }


    /**
     * 统计数据
     *
     * @param
     * @return 实例对象
     */
    @Override
    public List<DrConsCountEntity> costDataImmediate(Long eventId, Long cityCode) {
        //如何 cityCode=430000为湖南 直接为空
        if (cityCode != null) {
            if ("430000".equals(String.valueOf(cityCode))) {
                cityCode = null;
            }
        }
        List<DrConsCountEntity> drConsCountEntities = drEventInvitationMapper.costDataImmediate(eventId, cityCode);

        //如果cityCode为空 统计湖南数据
        if (drConsCountEntities != null && drConsCountEntities.size() > 0) {
            if (cityCode == null) {
                BigDecimal sumInv = new BigDecimal(0);
                BigDecimal sumEva = new BigDecimal(0);
                BigDecimal sumPer1 = new BigDecimal(0);
                BigDecimal sumReplyCap = new BigDecimal(0);
                BigDecimal sumActualCap = new BigDecimal(0);
                BigDecimal sumPer2 = new BigDecimal(0);

                for (DrConsCountEntity d : drConsCountEntities) {
                    sumInv = sumInv.add(d.getInv()); //累加应邀户数
                    sumEva = sumEva.add(d.getEva());//累加响应成功率
                    sumReplyCap = sumReplyCap.add(d.getReplyCap() == null ? BigDecimal.ZERO : d.getReplyCap());//累加应邀响应负荷
                    sumActualCap = sumActualCap.add(d.getActualCap());//累加响应负荷
                }
                if (!sumInv.equals(BigDecimal.ZERO)) {
                    sumPer1 = sumEva.divide(sumInv, 4, BigDecimal.ROUND_HALF_UP);
                }
                if (!sumReplyCap.equals(BigDecimal.ZERO)) {
                    sumPer2 = sumActualCap.divide(sumReplyCap, 4, BigDecimal.ROUND_HALF_UP);
                }
                DrConsCountEntity entity = new DrConsCountEntity();
                entity.setEventId(eventId);//时间id
                entity.setCityCode(430000L);//湖南code
                entity.setCityName("湖南");//湖南名称
                entity.setInv(sumInv);
                entity.setEva(sumEva);
                entity.setReplyCap(sumReplyCap);
                entity.setActualCap(sumActualCap);
                entity.setPer1(sumPer1);
                entity.setPer2(sumPer2);
                List<DrConsCountEntity> list = new ArrayList<DrConsCountEntity>();
                list.add(entity);
                list.addAll(drConsCountEntities);
                return list;
            }
        }


        return drConsCountEntities;
    }

    /**
     * 统计数据 -导出数据
     *
     * @param
     * @return 实例对象
     */
    @Override
    public void exportCostDataImmediate(String excelName, Long eventId, Long cityCode) {

        if (cityCode != null) {
            if ("430000".equals(String.valueOf(cityCode))) {
                cityCode = null;
            }
        }
        List<DrConsCountEntity> drConsCountEntities = drEventInvitationMapper.costDataImmediate(eventId, cityCode);
        List<DrConsCountEntity> list = new ArrayList<DrConsCountEntity>();

        //如果cityCode为空 统计湖南数据
        if (drConsCountEntities != null && drConsCountEntities.size() > 0) {
            if (cityCode == null) {
                BigDecimal sumInv = new BigDecimal(0);
                BigDecimal sumEva = new BigDecimal(0);
                BigDecimal sumPer1 = new BigDecimal(0);
                BigDecimal sumReplyCap = new BigDecimal(0);
                BigDecimal sumActualCap = new BigDecimal(0);
                BigDecimal sumPer2 = new BigDecimal(0);

                for (DrConsCountEntity d : drConsCountEntities) {
                    sumInv = sumInv.add(d.getInv()); //累加应邀户数
                    sumEva = sumEva.add(d.getEva());//累加响应成功率
                    sumReplyCap = sumReplyCap.add(d.getReplyCap());//累加应邀响应负荷
                    sumActualCap = sumActualCap.add(d.getActualCap());//累加响应负荷
                }

                if (!sumInv.equals(BigDecimal.ZERO)) {
                    sumPer1 = sumEva.divide(sumInv, 4, BigDecimal.ROUND_HALF_UP);
                }
                if (!sumReplyCap.equals(BigDecimal.ZERO)) {
                    sumPer2 = sumActualCap.divide(sumReplyCap, 4, BigDecimal.ROUND_HALF_UP);
                }
                DrConsCountEntity entity = new DrConsCountEntity();
                entity.setEventId(eventId);//时间id
                entity.setCityCode(430000L);//湖南code
                entity.setCityName("湖南");//湖南名称
                entity.setInv(sumInv);
                entity.setEva(sumEva);
                entity.setReplyCap(sumReplyCap);
                entity.setActualCap(sumActualCap);
                entity.setPer1(sumPer1);
                entity.setPer2(sumPer2);
                list.add(entity);
            }
        }
        list.addAll(drConsCountEntities);

//        String titleName = excelName;
        /*if(eventId !=null){
            Event event = eventMapper.getLeadEventById(String.valueOf(eventId));
            if(event !=null){
                titleName =event.getEventName();
            }
        }*/

//        String sheetName = "sheet1";
//        String[] titleRow = {"序号", "地区", "应邀户数", "成功响应户数", "用户成功率", "应邀响应负荷", "实际响应负荷", "响应成功率"};
//        String[][] dataRows = new String[list.size()][titleRow.length];
//        for (int i = 0; i < list.size(); i++) {
//            dataRows[i][0] = String.valueOf((i + 1));
//            dataRows[i][1] = String.valueOf(list.get(i).getCityName());//地区
//            dataRows[i][2] = String.valueOf(list.get(i).getInv());//应邀户数
//            dataRows[i][3] = String.valueOf(list.get(i).getEva());//成功响应户数
//
//            dataRows[i][4] = String.valueOf(list.get(i).getPer1());//用户成功率
//            dataRows[i][5] = String.valueOf(list.get(i).getReplyCap());//应邀响应负荷
//            dataRows[i][6] = String.valueOf(list.get(i).getActualCap());//实际响应负荷
//            dataRows[i][7] = String.valueOf(list.get(i).getPer2());//响应成功率
//        }
//
//        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
        String sheetName = "效果评估表";
        Map<String, Map<String, Object>> headerRows = new HashMap<>();
        String[] headerVerticalRow = {"序号", "响应区域", "响应日期", "开始时间", "结束时间", "目标容量", "实际响应负荷", "实际响应电量", "目标完成率"};
        String[] headerLevelRow = {"实际参与用户数量", "有效响应用户数量"};
        String[] headerLevelRow2 = {"电动汽车", "电动汽车"};
        String[] headerNormalRow = {"工业用户", "商业用户", "农业用户", "居民用户", "公交场站", "居民侧", "储能", "工业用户", "商业用户", "农业用户", "居民用户", "公交场站", "居民侧", "储能"};
        String[][] dataRows = new String[20][headerVerticalRow.length + headerNormalRow.length];
        int len = headerVerticalRow.length + headerNormalRow.length;
        Map<String, Object> titleName = new HashMap<>();
        titleName.put("headerName", new String[]{excelName});
        titleName.put("startRow", new int[]{0});
        titleName.put("endRow", new int[]{0});
        titleName.put("startCol", new int[]{0});
        titleName.put("endCol", new int[]{len - 1});
        titleName.put("rowCount", 4);
        titleName.put("colCount", len);

        Map<String, Object> verticalMap = new HashMap<>();
        verticalMap.put("titleRow", headerVerticalRow);
        verticalMap.put("startRow", new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
        verticalMap.put("endRow", new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3});
        verticalMap.put("startCol", new int[]{0, 1, 2, 3, 4, 5, 13, 14, 15});
        verticalMap.put("endCol", new int[]{0, 1, 2, 3, 4, 5, 13, 14, 15});

        Map<String, Object> levelMap = new HashMap<>();
        levelMap.put("titleRow", headerLevelRow);
        levelMap.put("startRow", new int[]{1, 1});
        levelMap.put("endRow", new int[]{1, 1});
        levelMap.put("startCol", new int[]{6, 16});
        levelMap.put("endCol", new int[]{12, 22});

        Map<String, Object> levelMap2 = new HashMap<>();
        levelMap2.put("titleRow", headerLevelRow2);
        levelMap2.put("startRow", new int[]{2, 2});
        levelMap2.put("endRow", new int[]{2, 2});
        levelMap2.put("startCol", new int[]{10, 20});
        levelMap2.put("endCol", new int[]{11, 21});

        Map<String, Object> normalMap = new HashMap<>();
        normalMap.put("titleRow", headerNormalRow);
        normalMap.put("startRow", new int[]{2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 2, 3, 3, 2});
        normalMap.put("endRow", new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3});
        normalMap.put("startCol", new int[]{6, 7, 8, 9, 10, 11, 12, 16, 17, 18, 19, 20, 21, 22});
        normalMap.put("endCol", new int[]{6, 7, 8, 9, 10, 11, 12, 16, 17, 18, 19, 20, 21, 22});


        headerRows.put("level0", levelMap);
        headerRows.put("level1", levelMap2);
        headerRows.put("level2", normalMap);
        headerRows.put("level3", verticalMap);


        PoiExcelUtils.customFormatExcel(excelName, sheetName, titleName, headerRows, dataRows, HttpServletUtil.getResponse());

    }

    /**
     * 负荷集成商 统计
     *
     * @param eventId 主键
     * @return 实例对象
     */
    @Override
    public IPage<Map<String, Object>> fhjcCostData(long current, long size, Long eventId) {
        Page<RegionConsSubsidyInfo> page = new Page<>(current, size);
        IPage<Map<String, Object>> maps = this.drEventInvitationMapper.fhjcsCostDataPage(page, eventId);
        List<Map<String, Object>> param = new ArrayList<Map<String, Object>>();
        for (Map m : maps.getRecords()) {
            String consName = m.get("cons_name") != null ? m.get("cons_name").toString() : null;
            String consId = m.get("cons_id") != null ? m.get("cons_id").toString() : null;
            Long event_Id = m.get("event_id") != null ? Long.parseLong(m.get("event_id").toString()) : null;
            String isEffective = m.get("is_effective") != null ? m.get("is_effective").toString() : "N";
            Map<String, Object> p = new HashMap<String, Object>();
            //consId =1405092198123429890L;
            //eventId =1412598902293778434L;
            //应邀用户
            BigDecimal yyhs = drEventInvitationMapper.yyhsFHJCS(consId, eventId);
            yyhs = yyhs != null ? yyhs : new BigDecimal(0);
            p.put("invitedUsers", yyhs);

            //应邀负荷
            BigDecimal yyfu = drEventInvitationMapper.yyfuFHJCS(consId, eventId);
            yyfu = yyfu != null ? yyfu : new BigDecimal(0);
            p.put("invitedLoad", yyfu);

            //有效响应户数
            BigDecimal yxxyhs = drEventInvitationMapper.yxxyhsFHJCS(consId, eventId);
            yxxyhs = yxxyhs != null ? yxxyhs : new BigDecimal(0);
            p.put("effectiveUser", yxxyhs);

            //有效响应负荷
            BigDecimal yxxyfh = drEventInvitationMapper.yxxyfhFHJCS(consId, eventId);
            yxxyfh = yxxyfh != null ? yxxyfh : new BigDecimal(0);
            p.put("requestedResLoad", yxxyfh);

            p.put("consId", consId);//聚合商id
            p.put("eventId", event_Id);//事件Id
            p.put("isValidity", isEffective);//聚合商是否有效
            p.put("effectName", consName);//聚合商名称
            param.add(p);

        }
        maps.setRecords(param);
        return maps;

    }


    /**
     * 当日负荷集成商 统计
     *
     * @param eventId 主键
     * @return 实例对象
     */
    @Override
    public IPage<Map<String, Object>> fhjcCostImmediateData(long current, long size, Long eventId) {
        Page<RegionConsSubsidyInfo> page = new Page<>(current, size);
        IPage<Map<String, Object>> maps = this.drEventInvitationMapper.fhjcsCostImmediateDataPage(page, eventId);
        List<Map<String, Object>> param = new ArrayList<Map<String, Object>>();
        for (Map m : maps.getRecords()) {
            String consName = m.get("cons_name") != null ? m.get("cons_name").toString() : null;
            String consId = m.get("cons_id") != null ? m.get("cons_id").toString() : null;
            Long event_Id = m.get("event_id") != null ? Long.parseLong(m.get("event_id").toString()) : null;
            String isEffective = m.get("is_effective") != null ? m.get("is_effective").toString() : "N";
            Map<String, Object> p = new HashMap<String, Object>();
            //consId =1405092198123429890L;
            //eventId =1412598902293778434L;
            //应邀用户
            BigDecimal yyhs = drEventInvitationMapper.yyhsFHJCS(consId, eventId);
            yyhs = yyhs != null ? yyhs : new BigDecimal(0);
            p.put("invitedUsers", yyhs);

            //应邀负荷
            BigDecimal yyfu = drEventInvitationMapper.yyfuFHJCS(consId, eventId);
            yyfu = yyfu != null ? yyfu : new BigDecimal(0);
            p.put("invitedLoad", yyfu);

            //有效响应户数
            BigDecimal yxxyhs = drEventInvitationMapper.yxxyhsFHJCSImmediate(consId, eventId);
            yxxyhs = yxxyhs != null ? yxxyhs : new BigDecimal(0);
            p.put("effectiveUser", yxxyhs);

            //有效响应负荷
            BigDecimal yxxyfh = drEventInvitationMapper.yxxyfhFHJCSImmediate(consId, eventId);
            yxxyfh = yxxyfh != null ? yxxyfh : new BigDecimal(0);
            p.put("requestedResLoad", yxxyfh);

            p.put("consId", consId);//聚合商id
            p.put("eventId", event_Id);//事件Id
            p.put("isValidity", isEffective);//聚合商是否有效
            p.put("effectName", consName);//聚合商名称
            param.add(p);

        }
        maps.setRecords(param);
        return maps;

    }

    /**
     * 负荷集成商 统计 导出
     *
     * @param eventId 主键
     * @return 实例对象
     */
    @Override
    public void exportfhjcCostData(Long eventId, String excelName) {

        List<Map<String, Object>> maps = this.drEventInvitationMapper.exprotfhjcsCostData(eventId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (Map m : maps) {
            String consName = m.get("cons_name") != null ? m.get("cons_name").toString() : null;
            String consId = m.get("cons_id") != null ? m.get("cons_id").toString() : null;
            Long event_Id = m.get("event_id") != null ? Long.parseLong(m.get("event_id").toString()) : null;
            String isEffective = m.get("is_effective") != null ? m.get("is_effective").toString() : "N";
            Map<String, Object> p = new HashMap<String, Object>();
            //consId =1405092198123429890L;
            //eventId =1412598902293778434L;
            //应邀用户
            BigDecimal yyhs = drEventInvitationMapper.yyhsFHJCS(consId, eventId);
            yyhs = yyhs != null ? yyhs : new BigDecimal(0);
            p.put("invitedUsers", yyhs);

            //应邀负荷
            BigDecimal yyfu = drEventInvitationMapper.yyfuFHJCS(consId, eventId);
            yyfu = yyfu != null ? yyfu : new BigDecimal(0);
            p.put("invitedLoad", yyfu);

            //有效响应户数
            BigDecimal yxxyhs = drEventInvitationMapper.yxxyhsFHJCS(consId, eventId);
            yxxyhs = yxxyhs != null ? yxxyhs : new BigDecimal(0);
            p.put("effectiveUser", yxxyhs);

            //有效响应负荷
            BigDecimal yxxyfh = drEventInvitationMapper.yxxyfhFHJCS(consId, eventId);
            yxxyfh = yxxyfh != null ? yxxyfh : new BigDecimal(0);
            p.put("requestedResLoad", yxxyfh);

            p.put("consId", consId);//聚合商id
            p.put("eventId", event_Id);//事件Id
            p.put("isValidity", isEffective);//聚合商是否有效
            p.put("effectName", consName);//聚合商名称
            list.add(p);

        }


        String titleName = excelName;
        /*if(eventId !=null){
            Event event = eventMapper.getLeadEventById(String.valueOf(eventId));
            if(event !=null){
                titleName =event.getEventName();
            }
        }*/

        String sheetName = "sheet1";
        String[] titleRow = {"序号", "集成商名称", "应邀户数", "应邀负荷", "有效响应户数", "有效响应负荷", "聚合商是否有效"};
        String[][] dataRows = new String[list.size()][titleRow.length];
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            //序号
            dataRows[i][0] = String.valueOf((i + 1));

            //集成商名称
            String effectName = map.get("effectName") != null ? map.get("effectName").toString() : null;
            dataRows[i][1] = String.valueOf(effectName);

            //应邀户数
            String invitedUsers = map.get("invitedUsers") != null ? map.get("invitedUsers").toString() : null;
            dataRows[i][2] = String.valueOf(invitedUsers);

            //应邀负荷
            String invitedLoad = map.get("invitedLoad") != null ? map.get("invitedLoad").toString() : null;
            dataRows[i][3] = String.valueOf(invitedLoad);

            //有效响应户数
            String effectiveUser = map.get("effectiveUser") != null ? map.get("effectiveUser").toString() : null;
            dataRows[i][4] = String.valueOf(effectiveUser);

            //有效响应负荷
            String requestedResLoad = map.get("requestedResLoad") != null ? map.get("requestedResLoad").toString() : null;
            dataRows[i][5] = String.valueOf(requestedResLoad);

            //聚合商是否有效
            String isValidity = map.get("isValidity") != null ? map.get("isValidity").toString() : null;
            dataRows[i][6] = "Y".equals(isValidity) ? "是" : "否";

        }

        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
    }

    /**
     * 当日负荷集成商 统计 导出
     *
     * @param eventId 主键
     * @return 实例对象
     */
    @Override
    public void exportfhjcCostImmediateData(Long eventId, String excelName) {

        List<Map<String, Object>> maps = this.drEventInvitationMapper.exprotfhjcsCostImmediateData(eventId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (Map m : maps) {
            String consName = m.get("cons_name") != null ? m.get("cons_name").toString() : null;
            String consId = m.get("cons_id") != null ? m.get("cons_id").toString() : null;
            Long event_Id = m.get("event_id") != null ? Long.parseLong(m.get("event_id").toString()) : null;
            String isEffective = m.get("is_effective") != null ? m.get("is_effective").toString() : "N";
            Map<String, Object> p = new HashMap<String, Object>();
            //consId =1405092198123429890L;
            //eventId =1412598902293778434L;
            //应邀用户
            BigDecimal yyhs = drEventInvitationMapper.yyhsFHJCS(consId, eventId);
            yyhs = yyhs != null ? yyhs : new BigDecimal(0);
            p.put("invitedUsers", yyhs);

            //应邀负荷
            BigDecimal yyfu = drEventInvitationMapper.yyfuFHJCS(consId, eventId);
            yyfu = yyfu != null ? yyfu : new BigDecimal(0);
            p.put("invitedLoad", yyfu);

            //有效响应户数
            BigDecimal yxxyhs = drEventInvitationMapper.yxxyhsFHJCSImmediate(consId, eventId);
            yxxyhs = yxxyhs != null ? yxxyhs : new BigDecimal(0);
            p.put("effectiveUser", yxxyhs);

            //有效响应负荷
            BigDecimal yxxyfh = drEventInvitationMapper.yxxyfhFHJCSImmediate(consId, eventId);
            yxxyfh = yxxyfh != null ? yxxyfh : new BigDecimal(0);
            p.put("requestedResLoad", yxxyfh);

            p.put("consId", consId);//聚合商id
            p.put("eventId", event_Id);//事件Id
            p.put("isValidity", isEffective);//聚合商是否有效
            p.put("effectName", consName);//聚合商名称
            list.add(p);

        }


        String titleName = excelName;
        /*if(eventId !=null){
            Event event = eventMapper.getLeadEventById(String.valueOf(eventId));
            if(event !=null){
                titleName =event.getEventName();
            }
        }*/

        String sheetName = "sheet1";
        String[] titleRow = {"序号", "集成商名称", "应邀户数", "应邀负荷", "有效响应户数", "有效响应负荷", "聚合商是否有效"};
        String[][] dataRows = new String[list.size()][titleRow.length];
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            //序号
            dataRows[i][0] = String.valueOf((i + 1));

            //集成商名称
            String effectName = map.get("effectName") != null ? map.get("effectName").toString() : null;
            dataRows[i][1] = String.valueOf(effectName);

            //应邀户数
            String invitedUsers = map.get("invitedUsers") != null ? map.get("invitedUsers").toString() : null;
            dataRows[i][2] = String.valueOf(invitedUsers);

            //应邀负荷
            String invitedLoad = map.get("invitedLoad") != null ? map.get("invitedLoad").toString() : null;
            dataRows[i][3] = String.valueOf(invitedLoad);

            //有效响应户数
            String effectiveUser = map.get("effectiveUser") != null ? map.get("effectiveUser").toString() : null;
            dataRows[i][4] = String.valueOf(effectiveUser);

            //有效响应负荷
            String requestedResLoad = map.get("requestedResLoad") != null ? map.get("requestedResLoad").toString() : null;
            dataRows[i][5] = String.valueOf(requestedResLoad);

            //聚合商是否有效
            String isValidity = map.get("isValidity") != null ? map.get("isValidity").toString() : null;
            dataRows[i][6] = "Y".equals(isValidity) ? "是" : "否";

        }

        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
    }

    /**
     * 效果评估明细
     */
    @Override
    public IPage<DrEventInvitationEntity> getDrEventInvitationEntity(long current, long size, Long cityCode, Long eventId, String consType) {

        if (cityCode != null) {
            if ("430000".equals(String.valueOf(cityCode))) {
                cityCode = null;
            }
        }
        List<Region> regions = systemClientService.queryAll();
        Map<String, String> map = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));

        Page<RegionConsSubsidyInfo> page = new Page<>(current, size);

        IPage<DrEventInvitationEntity> drEventInvitationEntity = drEventInvitationMapper.getDrEventInvitationEntityPage(page, cityCode, eventId, consType);
        List<DrEventInvitationEntity> records = drEventInvitationEntity.getRecords();

        if (!CollectionUtils.isEmpty(records)) {
            for (DrEventInvitationEntity invitationEntity : records) {
                invitationEntity.setCityName(map.get(invitationEntity.getCityCode()));
                invitationEntity.setCountyName(map.get(invitationEntity.getCountyCode()));
            }
        }
        drEventInvitationEntity.setRecords(records);
        return drEventInvitationEntity;
    }

    /**
     * 效果评估统计首页 -導出报表
     */
    //todo 无调用
    @Override
    public void exprotDrEventInvitationEntity(Long cityCode, Long eventId, String excelName) {

        if (cityCode != null) {
            if ("430000".equals(String.valueOf(cityCode))) {
                cityCode = null;
            }
        }
        List<DrConsCountEntity> drConsCountEntities = drEventInvitationMapper.costData(eventId, cityCode);
        List<DrConsCountEntity> list = new ArrayList<DrConsCountEntity>();

        //如果cityCode为空 统计湖南数据
        if (drConsCountEntities != null && drConsCountEntities.size() > 0) {
            if (cityCode == null) {
                BigDecimal sumInv = new BigDecimal(0);
                BigDecimal sumEva = new BigDecimal(0);
                BigDecimal sumPer1 = new BigDecimal(0);
                BigDecimal sumReplyCap = new BigDecimal(0);
                BigDecimal sumActualCap = new BigDecimal(0);
                BigDecimal sumPer2 = new BigDecimal(0);

                for (DrConsCountEntity d : drConsCountEntities) {
                    sumInv = sumInv.add(d.getInv()); //累加应邀户数
                    sumEva = sumEva.add(d.getEva());//累加响应成功率
                    sumReplyCap = sumReplyCap.add(d.getReplyCap());//累加应邀响应负荷
                    sumActualCap = sumActualCap.add(d.getActualCap());//累加响应负荷
                }

                if (!sumInv.equals(BigDecimal.ZERO)) {
                    sumPer1 = sumEva.divide(sumInv, 4, BigDecimal.ROUND_HALF_UP);
                }
                if (!sumReplyCap.equals(BigDecimal.ZERO)) {
                    sumPer2 = sumActualCap.divide(sumReplyCap, 4, BigDecimal.ROUND_HALF_UP);
                }
                DrConsCountEntity entity = new DrConsCountEntity();
                entity.setEventId(eventId);//时间id
                entity.setCityCode(430000L);//湖南code
                entity.setCityName("湖南");//湖南名称
                entity.setInv(sumInv);
                entity.setEva(sumEva);
                entity.setReplyCap(sumReplyCap);
                entity.setActualCap(sumActualCap);
                entity.setPer1(sumPer1);
                entity.setPer2(sumPer2);
                list.add(entity);
            }
        }
        list.addAll(drConsCountEntities);

        String titleName = excelName;
       /* if(eventId !=null){
            Event event = eventMapper.getLeadEventById(String.valueOf(eventId));
            if(event !=null){
                titleName =event.getEventName();
            }
        }*/
        String sheetName = "sheet1";
        String[] titleRow = {"序号", "地区", "应邀户数", "成功响应户数", "用户成功率", "应邀响应负荷", "实际响应负荷", "响应成功率"};
        String[][] dataRows = new String[list.size()][titleRow.length];
        for (int i = 0; i < list.size(); i++) {
            dataRows[i][0] = String.valueOf((i + 1));
            dataRows[i][1] = String.valueOf(list.get(i).getCityName());//地区
            dataRows[i][2] = String.valueOf(list.get(i).getInv());//应邀户数
            dataRows[i][3] = String.valueOf(list.get(i).getEva());//成功响应户数

            dataRows[i][4] = String.valueOf(list.get(i).getPer1());//用户成功率
            dataRows[i][5] = String.valueOf(list.get(i).getReplyCap());//应邀响应负荷
            dataRows[i][6] = String.valueOf(list.get(i).getActualCap());//实际响应负荷
            dataRows[i][7] = String.valueOf(list.get(i).getPer2());//响应成功率
        }

        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
    }

    /**
     * 效果评估明细
     */
    @Override
    public IPage<DrEventInvitationEntity> getDrEventInvitationImmediateEntity(long current, long size, Long cityCode, Long eventId, String consType) {
        if (cityCode != null) {
            if ("430000".equals(String.valueOf(cityCode))) {
                cityCode = null;
            }
        }

        List<Region> regions = systemClientService.queryAll();
        Map<String, String> map = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
        Page<RegionConsSubsidyInfo> page = new Page<>(current, size);

        IPage<DrEventInvitationEntity> drEventInvitationEntity = drEventInvitationMapper.getDrEventInvitationImmediateEntityPage(page, cityCode, eventId, consType);
        List<DrEventInvitationEntity> records = drEventInvitationEntity.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (DrEventInvitationEntity invitationEntity : records) {
                invitationEntity.setCityName(map.get(invitationEntity.getCityCode()));
                invitationEntity.setCountyName(map.get(invitationEntity.getCountyCode()));
            }
        }

        drEventInvitationEntity.setRecords(records);
        return drEventInvitationEntity;
    }

    /**
     * 效果评估明细导出
     */
    @Override
    public void exportDrEventInvitationEntity(Long cityCode, Long eventId, String consType, String excelName) {
        if (cityCode != null) {
            if ("430000".equals(String.valueOf(cityCode))) {
                cityCode = null;
            }
        }
        List<DrEventInvitationEntity> list = drEventInvitationMapper.getDrEventInvitationEntity(cityCode, eventId, consType);
        String sheetName = "数据明细";
        String titleName = excelName;
        String[] titleRow = {"序号", "地市", "区县", "参与方式", "用户编号", "用户名称", "响应负荷确认值", "平均基线负荷", "最大基线负荷", "实际平均值", "实际最大负荷", "最大负荷与最大基线负荷差值", "实际响应负荷", "是否有效"};
        String[][] dataRows = new String[list.size()][titleRow.length];
        for (int i = 0; i < list.size(); i++) {
            dataRows[i][0] = String.valueOf((i + 1));
            dataRows[i][1] = String.valueOf(list.get(i).getCityName());//地市
            dataRows[i][2] = String.valueOf(list.get(i).getCountyName());//区县
            dataRows[i][3] = String.valueOf(list.get(i).getConsType());//参与方式

            dataRows[i][4] = String.valueOf(list.get(i).getConsNo());//用户编号
            dataRows[i][5] = String.valueOf(list.get(i).getConsName());//用户名称
            dataRows[i][6] = String.valueOf(list.get(i).getReplyCap());//响应负荷确认值
            dataRows[i][7] = String.valueOf(list.get(i).getAvgLoadBaseline());//最大基线负荷
            dataRows[i][8] = String.valueOf(list.get(i).getMaxLoadBaseline());//最大基线负荷
            dataRows[i][9] = String.valueOf(list.get(i).getAvgLoadActual());//实际平均值
            dataRows[i][10] = String.valueOf(list.get(i).getMaxLoadActual());//实际最大负荷

            dataRows[i][11] = String.valueOf(list.get(i).getMin());//最大负荷与最大基线负荷差值
            dataRows[i][12] = String.valueOf(list.get(i).getActualCap());//实际响应负荷
            String isEffective = list.get(i).getIsEffective();//是否有效
            if ("Y".equals(isEffective)) {
                dataRows[i][13] = "是";////是否有效
            } else {
                dataRows[i][13] = "否";////是否有效
            }

        }

        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
    }

    /**
     * 当日效果评估明细导出
     */
    @Override
    public void exportDrEventImmediateEntity(Long cityCode, Long eventId, String consType, String excelName) {
        if (cityCode != null) {
            if ("430000".equals(String.valueOf(cityCode))) {
                cityCode = null;
            }
        }
        List<DrEventInvitationEntity> list = drEventInvitationMapper.exprotDrEventInvitationImmediateEntity(cityCode, eventId, consType);
        String sheetName = "数据明细";
        String titleName = excelName;
        String[] titleRow = {"序号", "地市", "区县", "参与方式", "用户编号", "用户名称", "响应负荷确认值", "平均基线负荷", "最大基线负荷", "实际平均值", "实际最大负荷", "最大负荷与最大基线负荷差值", "实际响应负荷", "是否有效"};
        String[][] dataRows = new String[list.size()][titleRow.length];
        for (int i = 0; i < list.size(); i++) {
            dataRows[i][0] = String.valueOf((i + 1));
            dataRows[i][1] = String.valueOf(list.get(i).getCityName());//地市
            dataRows[i][2] = String.valueOf(list.get(i).getCountyName());//区县
            dataRows[i][3] = String.valueOf(list.get(i).getConsType());//参与方式

            dataRows[i][4] = String.valueOf(list.get(i).getConsNo());//用户编号
            dataRows[i][5] = String.valueOf(list.get(i).getConsName());//用户名称
            dataRows[i][6] = String.valueOf(list.get(i).getReplyCap());//响应负荷确认值
            dataRows[i][7] = String.valueOf(list.get(i).getAvgLoadBaseline());//最大基线负荷
            dataRows[i][8] = String.valueOf(list.get(i).getMaxLoadBaseline());//最大基线负荷
            dataRows[i][9] = String.valueOf(list.get(i).getAvgLoadActual());//实际平均值
            dataRows[i][10] = String.valueOf(list.get(i).getMaxLoadActual());//实际最大负荷

            dataRows[i][11] = String.valueOf(list.get(i).getMin());//最大负荷与最大基线负荷差值
            dataRows[i][12] = String.valueOf(list.get(i).getActualCap());//实际响应负荷
            String isEffective = list.get(i).getIsEffective();//是否有效
            if ("Y".equals(isEffective)) {
                dataRows[i][13] = "是";////是否有效
            } else {
                dataRows[i][13] = "否";////是否有效
            }

        }

        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());
    }

    /**
     * 负荷集成商效果评估明细
     */
    @Override
    public IPage<DrEventInvEntity> getDrEventInvEntity(long current, long size, Long eventId, String consId) {
        Page<DrEventInvEntity> page = new Page<>(current, size);

        //查询所有区域
        List<Region> regions = systemClientService.queryAll();
        Map<String, String> map = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));

        IPage<DrEventInvEntity> drEventInvEntityPage = drEventInvitationMapper.getDrEventInvEntityPage(page, eventId, consId);
        List<DrEventInvEntity> records = drEventInvEntityPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (DrEventInvEntity drEventInvEntity : records) {
                drEventInvEntity.setCityName(map.get(drEventInvEntity.getCityCode()));
                drEventInvEntity.setCountyName(map.get(drEventInvEntity.getCountyCode()));
            }
        }
        drEventInvEntityPage.setRecords(records);
        return drEventInvEntityPage;
    }

    /**
     * 当日负荷集成商效果评估明细
     */
    @Override
    public IPage<DrEventInvEntity> getDrEventInvEntityImmediate(long current, long size, Long eventId, String consId) {

        //查询 所有区域
        List<Region> regions = systemClientService.queryAll();
        Map<String, String> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(regions)) {
            map = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
        }

        Page<DrEventInvEntity> page = new Page<>(current, size);

        IPage<DrEventInvEntity> drEventInvEntityPage = drEventInvitationMapper.getDrEventInvEntityImmediatePage(page, eventId, consId);
        List<DrEventInvEntity> records = drEventInvEntityPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (DrEventInvEntity record : records) {
                record.setCountyName(map.get(record.getCountyCode()));
                record.setCityName(map.get(record.getCityCode()));
            }
        }
        drEventInvEntityPage.setRecords(records);
        return drEventInvEntityPage;
    }

    /**
     * 负荷集成商效果评估明细导出
     */
    @Override
    public void exportDrEventInvEntity(Long eventId, String consId, String excelName) {
        List<DrEventInvEntity> list = drEventInvitationMapper.getDrEventInvEntity(eventId, consId);

        String sheetName = "数据明细";
        String titleName = excelName;
        String[] titleRow = {"序号", "地市", "区县", "用户编号", "用户名称", "响应负荷确认值", "平均基线负荷", "最大基线负荷", "实际平均值", "实际最大负荷", "最大负荷与最大基线负荷差值", "实际响应负荷", "是否有效"};
        String[][] dataRows = new String[list.size()][titleRow.length];
        for (int i = 0; i < list.size(); i++) {
            dataRows[i][0] = String.valueOf((i + 1));
            dataRows[i][1] = String.valueOf(list.get(i).getCityName());//用户编号
            dataRows[i][2] = String.valueOf(list.get(i).getCountyName());//用户名称

            dataRows[i][3] = String.valueOf(list.get(i).getConsNo());//用户编号
            dataRows[i][4] = String.valueOf(list.get(i).getConsName());//用户名称
            dataRows[i][5] = String.valueOf(list.get(i).getReplyCap());//响应负荷确认值
            dataRows[i][6] = String.valueOf(list.get(i).getAvgLoadBaseline());//最大基线负荷
            dataRows[i][7] = String.valueOf(list.get(i).getMaxLoadBaseline());//最大基线负荷
            dataRows[i][8] = String.valueOf(list.get(i).getAvgLoadActual());//实际平均值
            dataRows[i][9] = String.valueOf(list.get(i).getMaxLoadActual());//实际最大负荷

            dataRows[i][10] = String.valueOf(list.get(i).getMin());//最大负荷与最大基线负荷差值
            dataRows[i][11] = String.valueOf(list.get(i).getActualCap());//实际响应负荷

            String isEffective = list.get(i).getIsEffective();//是否有效
            if ("Y".equals(isEffective)) {
                dataRows[i][12] = "是";////是否有效
            } else {
                dataRows[i][12] = "否";////是否有效
            }


        }
        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());

    }

    /**
     * 当日负荷集成商效果评估明细导出
     */
    //todo 涉及到的表不存在
    @Override
    public void exportDrEventInvImmediateEntity(Long eventId, String consId, String excelName) {
        List<DrEventInvEntity> list = drEventInvitationMapper.getDrEventInvImmediateEntity(eventId, consId);
        if (!CollectionUtils.isEmpty(list)) {
            List<Region> regions = systemClientService.queryAll();
            Map<String, String> map = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
            for (DrEventInvEntity entity : list) {
                entity.setCityName(map.get(entity.getCityCode()));
                entity.setCountyName(map.get(entity.getCountyCode()));
            }
        }

        String sheetName = "数据明细";
        String titleName = excelName;
        String[] titleRow = {"序号", "地市", "区县", "用户编号", "用户名称", "响应负荷确认值", "平均基线负荷", "最大基线负荷", "实际平均值", "实际最大负荷", "最大负荷与最大基线负荷差值", "实际响应负荷", "是否有效"};
        String[][] dataRows = new String[list.size()][titleRow.length];
        for (int i = 0; i < list.size(); i++) {
            dataRows[i][0] = String.valueOf((i + 1));
            dataRows[i][1] = String.valueOf(list.get(i).getCityName());//用户编号
            dataRows[i][2] = String.valueOf(list.get(i).getCountyName());//用户名称

            dataRows[i][3] = String.valueOf(list.get(i).getConsNo());//用户编号
            dataRows[i][4] = String.valueOf(list.get(i).getConsName());//用户名称
            dataRows[i][5] = String.valueOf(list.get(i).getReplyCap());//响应负荷确认值
            dataRows[i][6] = String.valueOf(list.get(i).getAvgLoadBaseline());//最大基线负荷
            dataRows[i][7] = String.valueOf(list.get(i).getMaxLoadBaseline());//最大基线负荷
            dataRows[i][8] = String.valueOf(list.get(i).getAvgLoadActual());//实际平均值
            dataRows[i][9] = String.valueOf(list.get(i).getMaxLoadActual());//实际最大负荷

            dataRows[i][10] = String.valueOf(list.get(i).getMin());//最大负荷与最大基线负荷差值
            dataRows[i][11] = String.valueOf(list.get(i).getActualCap());//实际响应负荷

            String isEffective = list.get(i).getIsEffective();//是否有效
            if ("Y".equals(isEffective)) {
                dataRows[i][12] = "是";////是否有效
            } else {
                dataRows[i][12] = "否";////是否有效
            }


        }
        POIExcelUtil.generatorExcel(excelName, titleName, sheetName, titleRow, dataRows, HttpServletUtil.getResponse());

    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<DrEventInvitation> queryAllByLimit(int offset, int limit) {
        //return this.drEventInvitationMapper.queryAllByLimit(offset, limit);
        return null;
    }


    /**
     * 新增数据
     *
     * @param drEventInvitation 实例对象
     * @return 实例对象
     */
    @Override
    public DrEventInvitation insert(DrEventInvitation drEventInvitation) {
        // this.drEventInvitationMapper.insert(drEventInvitation);
        // return drEventInvitation;
        return null;
    }

    /**
     * 修改数据
     *
     * @param drEventInvitation 实例对象
     * @return 实例对象
     */
    @Override
    public DrEventInvitation update(DrEventInvitation drEventInvitation) {
//        this.drEventInvitationMapper.update(drEventInvitation);
//        return this.queryById(drEventInvitation.getInvitationId());
        return null;
    }

    /**
     * 通过主键删除数据
     *
     * @param invitationId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long invitationId) {

//        return this.drEventInvitationMapper.deleteById(invitationId) > 0;
        return false;
    }


    @Override
    public List<String> queryCountByEventID(Long eventId) {
        return drEventInvitationMapper.queryCountByEventID(eventId);
    }

    @Override
    public List<Event> workBencworkBenchOverviewEventNamehOverview(String year) {
        return drEventInvitationMapper.getEventByYear(year);
    }

    /**
     * 用户参与情况统计
     */
    @Override
    public IPage<DrEventInvitationUser> getEventUser(long current, long size, String startDate, String endDate, Long eventId) {
        Page<DrEventInvitationUser> page = new Page<>(current, size);
        page.setOptimizeCountSql(false);
        IPage<DrEventInvitationUser> drEventUserPage = drEventInvitationMapper.getEventUser(page, startDate, endDate, eventId);
        List<DrEventInvitationUser> dataRows = drEventUserPage.getRecords();
        for (int i = 0; i < dataRows.size(); i++) {
            dataRows.get(i).setResponseTimes(dataRows.get(i).getResponseTimes() != null ? dataRows.get(i).getResponseTimes() : 0);
            dataRows.get(i).setPeakClippingTimes(dataRows.get(i).getPeakClippingTimes() != null ? dataRows.get(i).getPeakClippingTimes() : 0);
            dataRows.get(i).setValleyFillingTimes(dataRows.get(i).getValleyFillingTimes() != null ? dataRows.get(i).getValleyFillingTimes() : 0);
            dataRows.get(i).setTotalResponseLoad(dataRows.get(i).getTotalResponseLoad() != null ? dataRows.get(i).getTotalResponseLoad() : BigDecimal.ZERO);
            dataRows.get(i).setTotalResponsePower(dataRows.get(i).getTotalResponsePower() != null ? dataRows.get(i).getTotalResponsePower() : BigDecimal.ZERO);
            dataRows.get(i).setResponseAccuracy(dataRows.get(i).getResponseAccuracy() != null ? dataRows.get(i).getResponseAccuracy() : BigDecimal.ZERO);
            dataRows.get(i).setIncentiveAmount(dataRows.get(i).getIncentiveAmount() != null ? dataRows.get(i).getIncentiveAmount() : BigDecimal.ZERO);
        }

        return drEventUserPage;
    }

    /**
     * 用户参与情况统计导出
     */
    @Override
    public void exprotEventUser(String startDate, String endDate, Long eventId) {
        List<DrEventInvitationUser> list = drEventInvitationMapper.exprotEventUser(startDate, endDate, eventId);

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setResponseTimes(list.get(i).getResponseTimes() != null ? list.get(i).getResponseTimes() : 0);
            list.get(i).setPeakClippingTimes(list.get(i).getPeakClippingTimes() != null ? list.get(i).getPeakClippingTimes() : 0);
            list.get(i).setValleyFillingTimes(list.get(i).getValleyFillingTimes() != null ? list.get(i).getValleyFillingTimes() : 0);
            list.get(i).setTotalResponseLoad(list.get(i).getTotalResponseLoad() != null ? list.get(i).getTotalResponseLoad() : BigDecimal.ZERO);
            list.get(i).setTotalResponsePower(list.get(i).getTotalResponsePower() != null ? list.get(i).getTotalResponsePower() : BigDecimal.ZERO);
            list.get(i).setResponseAccuracy(list.get(i).getResponseAccuracy() != null ? list.get(i).getResponseAccuracy() : BigDecimal.ZERO);
            list.get(i).setIncentiveAmount(list.get(i).getIncentiveAmount() != null ? list.get(i).getIncentiveAmount() : BigDecimal.ZERO);
        }


        String sheetName = "用户参与情况统计";
        String excelName = "用户需求响应参与情况统计表";
        Map<String, Map<String, Object>> headerRows = new HashMap<>();
        String[] headerVerticalRow = {"序号", "户名", "用户类型", "参与响应次数", "削峰响应次数", "填谷响应次数", "总响应负荷(kW)", "总响应电量(kWh)", "响应准确度", "激励金额(元)"};
        String[] headerLevelRow = {"统计开始日期", startDate, "统计结束日期", endDate, ""};
        String[][] dataRows = new String[list.size()][headerVerticalRow.length];
        int len = headerVerticalRow.length;
        Map<String, Object> titleName = new HashMap<>();
        titleName.put("headerName", new String[]{excelName});
        titleName.put("startRow", new int[]{0});
        titleName.put("endRow", new int[]{0});
        titleName.put("startCol", new int[]{0});
        titleName.put("endCol", new int[]{len - 1});
        titleName.put("rowCount", 3);
        titleName.put("colCount", len);

        Map<String, Object> verticalMap = new HashMap<>();
        verticalMap.put("titleRow", headerVerticalRow);
        verticalMap.put("startRow", new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        verticalMap.put("endRow", new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        verticalMap.put("startCol", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        verticalMap.put("endCol", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});

        Map<String, Object> levelMap = new HashMap<>();
        levelMap.put("titleRow", headerLevelRow);
        levelMap.put("startRow", new int[]{1, 1, 1, 1, 1});
        levelMap.put("endRow", new int[]{1, 1, 1, 1, 1});
        levelMap.put("startCol", new int[]{1, 2, 5, 6, 9});
        levelMap.put("endCol", new int[]{1, 3, 5, 8, 9});


        headerRows.put("level0", levelMap);
        headerRows.put("level1", verticalMap);


        for (int i = 0; i < list.size(); i++) {
            dataRows[i][0] = String.valueOf((i + 1));
            dataRows[i][1] = String.valueOf(list.get(i).getConsName());//户名

//            dataRows[i][2] = String.valueOf(list.get(i).getConsType());//用户类型
            if (list.get(i).getConsType() != null) {
                dataRows[i][2] = list.get(i).getConsType() == 1 ? "直接需求用户" : "代理参与用户";
            } else {
                dataRows[i][2] = "";
            }

            dataRows[i][3] = String.valueOf(list.get(i).getResponseTimes());//参与响应次数
            dataRows[i][4] = String.valueOf(list.get(i).getPeakClippingTimes());//削峰响应次数
            dataRows[i][5] = String.valueOf(list.get(i).getValleyFillingTimes());//填谷响应次数
            dataRows[i][6] = String.valueOf(list.get(i).getTotalResponseLoad());//总响应负荷
            dataRows[i][7] = String.valueOf(list.get(i).getTotalResponsePower());//总响应电量

//            dataRows[i][8] = String.valueOf();//响应准确度
            dataRows[i][8] = String.format("%.2f", (list.get(i).getResponseAccuracy().multiply(new BigDecimal("100")))) + "%";//响应准确度

            dataRows[i][9] = String.valueOf(list.get(i).getIncentiveAmount());//激励金额

        }
        PoiExcelUtils.customFormatExcel(excelName, sheetName, titleName, headerRows, dataRows, HttpServletUtil.getResponse());

    }

    /**
     * 业务运行统计
     */
    @Override
    public IPage<DrEventInvitationBusiness> getEventBusiness(long current, long size, String startDate, String endDate, String eventType, Long provinceCode, Long cityCode) {
        Page<DrEventInvitationBusiness> page = new Page<>(current, size);
        page.setOptimizeCountSql(false);
        // 根据省级code查询所有城市
        IPage<DrEventInvitationBusiness> drEventBusinessPage = drEventInvitationMapper.getEventBusiness(page, provinceCode, cityCode);
        List<DrEventInvitationBusiness> dataRows = drEventBusinessPage.getRecords();
        for (int i = 0; i < dataRows.size(); i++) {
            dataRows.get(i).setBusinessNum(0);
            dataRows.get(i).setTotalResponseLoad(BigDecimal.ZERO);
            dataRows.get(i).setTotalResponsePower(BigDecimal.ZERO);
            dataRows.get(i).setResponseUsers(BigDecimal.ZERO);
        }
        // 查询时间范围和类型内的事件
        List<Map> getEventBusinessEvent = drEventInvitationMapper.getEventBusinessEvent(startDate, endDate, eventType);

        for (int i = 0; i < getEventBusinessEvent.size(); i++) {
            if (!StringUtils.isEmpty(getEventBusinessEvent.get(i).get("REGULATE_RANGE"))) {
                String[] rangeList = getEventBusinessEvent.get(i).get("REGULATE_RANGE").toString().split("],\\[");
//                System.out.println(rangeList.length);
                for (int k = 0; k < rangeList.length; k++) {
//                    System.out.println(rangeList[k]);
                    String[] rangeSplitList = rangeList[k].split("\",\"");
                    if (rangeSplitList.length == 3) {
//                        System.out.println(rangeList[k]);
                        String range = rangeSplitList[1];

                        for (int j = 0; j < dataRows.size(); j++) {

                            if (range.equals(dataRows.get(j).getCityCode().toString())) {
                                dataRows.get(j).setBusinessNum(dataRows.get(j).getBusinessNum() + 1);
                            }
                        } //[["430000","430100","430102"],["430000","430100","430103"],["430000","430100","430104"],["430000","430100","430105"],["430000","430100","430111"],["430000","430100","430121"],["430000","430100","430122"],["430000","430100","430124"],["430000","430100","430181"]]

                    } else if (rangeSplitList.length == 2) {
                        String range = rangeSplitList[1].substring(0, 6);
                        for (int j = 0; j < dataRows.size(); j++) {

                            if (range.equals(dataRows.get(j).getCityCode().toString())) {
                                dataRows.get(j).setBusinessNum(dataRows.get(j).getBusinessNum() + 1);
                            }
                        }
                    } else if (rangeSplitList.length == 1) {
                        String REGEX = "[^(0-9)]";
                        String range = Pattern.compile(REGEX).matcher(rangeSplitList[0]).replaceAll("").trim();

                        for (int j = 0; j < dataRows.size(); j++) {
                            dataRows.get(j).setBusinessNum(dataRows.get(j).getBusinessNum() + 1);
                        }
                    }
                }
            }
        }

        // 统计响应负荷、电量、户次
        Long eventId = null;
        List<Map> getCapEnergy = drEventInvitationMapper.getCapEnergy(startDate, endDate, eventId);
        for (int i = 0; i < getCapEnergy.size(); i++) {
            Map map = getCapEnergy.get(i);
            for (int j = 0; j < dataRows.size(); j++) {
                Map map1 = getCapEnergy.get(i);
                if(!CollectionUtils.isEmpty(map1)){
                    if(map1.get("cityCode") != null){
                        if (map.get("cityCode").toString().equals(dataRows.get(j).getCityCode().toString())) {
                            dataRows.get(j).setTotalResponseLoad(new BigDecimal(map.get("actual_cap") == null ? BigDecimal.ZERO.toString() : map.get("actual_cap").toString()));
                            dataRows.get(j).setTotalResponsePower(new BigDecimal(map.get("actual_energy") == null ? BigDecimal.ZERO.toString() : map.get("actual_energy").toString()));
                            dataRows.get(j).setResponseUsers(new BigDecimal(map.get("evaluation_id") == null ? BigDecimal.ZERO.toString() : map.get("evaluation_id").toString()));
                        }
                    }
                }
            }
        }

        return drEventBusinessPage;
    }


    /**
     * 业务运行统计导出
     */
    public void exprotBusinessOperation(String startDate, String endDate, String eventType, Long provinceCode, Long cityCode) {

        // 根据省级code查询所有城市
        List<Region> regions = systemClient.queryAll();
        if (cityCode != null) {
            regions = regions.stream().filter(n -> cityCode.equals(n.getId())).collect(Collectors.toList());
        } else {
            regions = regions.stream().filter(n -> RegionLevelEnum.LEVEL_TWO.getCode().equals(n.getLevel())).collect(Collectors.toList());
        }
        List<DrEventInvitationBusiness> dataRows = new ArrayList<>();
        for (Region region : regions) {
            DrEventInvitationBusiness drEventInvitationBusiness = new DrEventInvitationBusiness();

            drEventInvitationBusiness.setCityCode(Long.valueOf(region.getId()));
            drEventInvitationBusiness.setCityName(region.getName());
            drEventInvitationBusiness.setBusinessNum(0);
            drEventInvitationBusiness.setTotalResponseLoad(BigDecimal.ZERO);
            drEventInvitationBusiness.setTotalResponsePower(BigDecimal.ZERO);
            drEventInvitationBusiness.setResponseUsers(BigDecimal.ZERO);
            dataRows.add(drEventInvitationBusiness);
        }
        // 查询时间范围和类型内的事件
        List<Map> getEventBusinessEvent = drEventInvitationMapper.getEventBusinessEvent(startDate, endDate, eventType);

        for (int i = 0; i < getEventBusinessEvent.size(); i++) {
            if (!StringUtils.isEmpty(getEventBusinessEvent.get(i).get("REGULATE_RANGE"))) {
                String[] rangeList = getEventBusinessEvent.get(i).get("REGULATE_RANGE").toString().split("],\\[");
//                System.out.println(rangeList.length);
                for (int k = 0; k < rangeList.length; k++) {
//                    System.out.println(rangeList[k]);
                    String[] rangeSplitList = rangeList[k].split("\",\"");
                    if (rangeSplitList.length == 3) {
//                        System.out.println(rangeList[k]);
                        String range = rangeSplitList[1];

                        for (int j = 0; j < dataRows.size(); j++) {

                            if (range.equals(dataRows.get(j).getCityCode().toString())) {
                                dataRows.get(j).setBusinessNum(dataRows.get(j).getBusinessNum() + 1);
                            }
                        } //[["430000","430100","430102"],["430000","430100","430103"],["430000","430100","430104"],["430000","430100","430105"],["430000","430100","430111"],["430000","430100","430121"],["430000","430100","430122"],["430000","430100","430124"],["430000","430100","430181"]]

                    } else if (rangeSplitList.length == 2) {
                        String range = rangeSplitList[1].substring(0, 6);
                        for (int j = 0; j < dataRows.size(); j++) {

                            if (range.equals(dataRows.get(j).getCityCode().toString())) {
                                dataRows.get(j).setBusinessNum(dataRows.get(j).getBusinessNum() + 1);
                            }
                        }
                    } else if (rangeSplitList.length == 1) {
                        String REGEX = "[^(0-9)]";
                        String range = Pattern.compile(REGEX).matcher(rangeSplitList[0]).replaceAll("").trim();

                        for (int j = 0; j < dataRows.size(); j++) {
                            dataRows.get(j).setBusinessNum(dataRows.get(j).getBusinessNum() + 1);
                        }
                    }
                }
            }
        }


        // 统计响应负荷、电量、户次
        Long eventId = null;
        List<Map> getCapEnergy = drEventInvitationMapper.getCapEnergy(startDate, endDate, eventId);
        for (int i = 0; i < getCapEnergy.size(); i++) {
            Map map = getCapEnergy.get(i);
            for (int j = 0; j < dataRows.size(); j++) {
                if (null != getCapEnergy.get(i).get("cityCode").toString()) {
                    if (map.get("cityCode").toString().equals(dataRows.get(j).getCityCode().toString())) {
                        dataRows.get(j).setTotalResponseLoad(new BigDecimal(map.get("actual_cap") == null ? BigDecimal.ZERO.toString() : map.get("actual_cap").toString()));
                        dataRows.get(j).setTotalResponsePower(new BigDecimal(map.get("actual_energy") == null ? BigDecimal.ZERO.toString() : map.get("actual_energy").toString()));
                        dataRows.get(j).setResponseUsers(new BigDecimal(map.get("evaluation_id") == null ? BigDecimal.ZERO.toString() : map.get("evaluation_id").toString()));
                    }
                }
            }
        }

        String excelName = "需求响应业务运行统计表(" + startDate + "---" + endDate + ")";
        String sheetName = "需求响应业务运行统计表";
        Map<String, Map<String, Object>> titleRows = new HashMap<>();
        String[] viceHeaderRow = {"(削峰类、填谷、消纳新能源、调峰服务、调频服务分类统计)"};
        String[] headerVerticalRow = {"序号", "地市", "执行次数", "累计响应负荷(kW)", "累计响应电量(kWh)", "参与户次"};

        // 合计

        BigDecimal businessNum = new BigDecimal("0");
        BigDecimal totalResponseLoad = new BigDecimal("0");
        BigDecimal totalResponsePower = new BigDecimal("0");
        BigDecimal responseUsers = new BigDecimal("0");

        for (DrEventInvitationBusiness d : dataRows) {
            businessNum = businessNum.add(new BigDecimal(d.getBusinessNum())); //累加事件执行次数
            totalResponseLoad = totalResponseLoad.add(d.getTotalResponseLoad());//累加响应负荷
            totalResponsePower = totalResponsePower.add(d.getTotalResponsePower());//累加响应电量
            responseUsers = responseUsers.add(d.getResponseUsers());//累加响应户次
        }
        String[] footRow = {"合计", businessNum.toString(), totalResponseLoad.toString(), totalResponsePower.toString(), responseUsers.toString()};
        String[][] dataContent = new String[dataRows.size()][headerVerticalRow.length];
        int len = headerVerticalRow.length;
        Map<String, Object> headerName = new HashMap<>();
        headerName.put("headerName", new String[]{excelName});
        headerName.put("startRow", new int[]{0});
        headerName.put("endRow", new int[]{0});
        headerName.put("startCol", new int[]{0});
        headerName.put("endCol", new int[]{len - 1});
        headerName.put("rowCount", 3);  // 表头所占行数
        headerName.put("colCount", len);

        Map<String, Object> viceHeaderName = new HashMap<>();
        viceHeaderName.put("headerName", viceHeaderRow);
        viceHeaderName.put("startRow", new int[]{1});
        viceHeaderName.put("endRow", new int[]{1});
        viceHeaderName.put("startCol", new int[]{0});
        viceHeaderName.put("endCol", new int[]{len - 1});

        Map<String, Object> verticalMap = new HashMap<>();
        verticalMap.put("titleRow", headerVerticalRow);
        verticalMap.put("startRow", new int[]{2, 2, 2, 2, 2, 2});
        verticalMap.put("endRow", new int[]{2, 2, 2, 2, 2, 2});
        verticalMap.put("startCol", new int[]{0, 1, 2, 3, 4, 5});
        verticalMap.put("endCol", new int[]{0, 1, 2, 3, 4, 5});

        int footRowIndex = dataRows.size() + 3;
        Map<String, Object> normalMap = new HashMap<>();
        normalMap.put("titleRow", footRow);
        normalMap.put("startRow", new int[]{footRowIndex, footRowIndex, footRowIndex, footRowIndex, footRowIndex});
        normalMap.put("endRow", new int[]{footRowIndex, footRowIndex, footRowIndex, footRowIndex, footRowIndex});
        normalMap.put("startCol", new int[]{0, 2, 3, 4, 5});
        normalMap.put("endCol", new int[]{1, 2, 3, 4, 5});


        titleRows.put("level0", verticalMap);
        titleRows.put("level1", normalMap);


        for (int i = 0; i < dataRows.size(); i++) {
            dataContent[i][0] = String.valueOf((i + 1));
            dataContent[i][1] = String.valueOf(dataRows.get(i).getCityName());//地市
            dataContent[i][2] = String.valueOf(dataRows.get(i).getBusinessNum());//事件执行次数

            dataContent[i][3] = String.valueOf(dataRows.get(i).getTotalResponseLoad());//累计响应负荷
            dataContent[i][4] = String.valueOf(dataRows.get(i).getTotalResponsePower());//总响应电量
            dataContent[i][5] = String.valueOf(dataRows.get(i).getResponseUsers());//累计响应户次

        }

        PoiExcelUtils.customFormatExcelWithVice(excelName, sheetName, headerName, viceHeaderName, titleRows, dataContent, HttpServletUtil.getResponse());


    }

    /**
     * 事件效果评估统计--当日导出
     */
    @Override
    public void exprotEffectEvalImmediate(String startDate, String endDate, Long eventId, Long provinceCode, Long cityCode) {
        // 根据日期和事件id查询事件
        List<DrEventInvitationEffectEval> dataRows = drEventInvitationMapper.exprotEffectEvalImmediate(startDate, endDate, eventId, cityCode);

        //获取 所有地区
        List<Region> regions = systemClientService.queryAll();

        List<SysOrgs> allOrgs = new ArrayList<>();
        //获取 所有供电公司
        JSONObject jsonObject = systemClientService.queryAllOrg();
        if (jsonObject != null) {
            if ("000000".equals(jsonObject.get("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (Object o : jsonArray) {
                    JSONObject object = (JSONObject) JSONObject.toJSON(o);
                    SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(object);

                    allOrgs.add(sysOrgs);
                }
            }
        }
        // 筛选城市
        if (!CollectionUtils.isEmpty(regions) && !CollectionUtils.isEmpty(allOrgs)) {
            Map<String, String> regionMap = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
            Map<String, String> sysOrgMap = allOrgs.stream().collect(Collectors.toMap(SysOrgs::getId, SysOrgs::getName));

            for (int i = 0; i < dataRows.size(); i++) {
                DrEventInvitationEffectEval effectEval = dataRows.get(i);

                //判断 调控范围类别：地区/分区/变电站/线路/台区
                String rangeType = effectEval.getRangeType();
                if (!StringUtils.isEmpty(rangeType)) {
                    if (RangeTypeEnum.ADMINISTRATIVE_REGION.getCode().equals(rangeType)) {

                        if (!StringUtils.isEmpty(effectEval.getCityName())) {
                            String[] rangeList = effectEval.getCityName().split("],\\[");
                            String cityName = "";
                            if (rangeList.length > 0) {
                                for (String str : rangeList) {
                                    String[] rangeSplitList = str.replace("[", "").replace("]", "").replace("\"", "").split(",");
                                    if (rangeSplitList.length == 3) {
                                        String range = rangeSplitList[2];
                                        cityName = cityName + "/" + (regionMap.get(range) == null ? range : regionMap.get(range));
                                    } else if (rangeSplitList.length == 2) {
                                        String range = rangeSplitList[1].substring(0, 6);
                                        cityName = cityName + "/" + (regionMap.get(range) == null ? range : regionMap.get(range));
                                    } else if (rangeSplitList.length == 1) {
                                        String REGEX = "[^(0-9)]";
                                        String range = Pattern.compile(REGEX).matcher(rangeSplitList[0]).replaceAll("").trim();
                                        if (range != null && !range.equals("")) {
                                            effectEval.setCityName(regionMap.get(range) == null ? range : regionMap.get(range));
                                        } else {
                                            effectEval.setCityName("");
                                        }
                                    }
                                }

                                if(!StringUtils.isEmpty(cityName)){
                                    effectEval.setCityName(cityName.replaceFirst("/",""));
                                }
                            }
                        }

                    }
                }

                if (RangeTypeEnum.ELECTRICIC_REGION.getCode().equals(rangeType)) {

                    if (!StringUtils.isEmpty(effectEval.getCityName())) {
                        String[] rangeList = effectEval.getCityName().split("],\\[");
                        String cityName = "";
                        for (String str : rangeList) {
                            if (rangeList.length > 0) {
                                String[] rangeSplitList = str.replace("[", "").replace("]", "").replace("\"", "").split(",");
                                if (rangeSplitList.length == 3) {
                                    String range = rangeSplitList[2];
                                    cityName = cityName + "/" + (sysOrgMap.get(range) == null ? range : sysOrgMap.get(range));
                                } else if (rangeSplitList.length == 2) {
                                    String range = rangeSplitList[1];
                                    cityName = cityName + "/" + (sysOrgMap.get(range) == null ? range : sysOrgMap.get(range));
                                } else if (rangeSplitList.length == 1) {
                                    String REGEX = "[^(0-9)]";
                                    String range = Pattern.compile(REGEX).matcher(rangeSplitList[0]).replaceAll("").trim();
                                    if (range != null && !range.equals("")) {
                                        cityName = sysOrgMap.get(range) == null ? range : sysOrgMap.get(range);
                                    } else {
                                        effectEval.setCityName("");
                                    }
                                }
                            }
                        }
                        if(!StringUtils.isEmpty(cityName)){
                            effectEval.setCityName(cityName.replaceFirst("/",""));
                        }
                    }
                }
            }
        }

        for (int i = 0; i < dataRows.size(); i++) {
            dataRows.get(i).setActualIndustrialUsers(0);
            dataRows.get(i).setActualBusinessUsers(0);
            dataRows.get(i).setActualAgriculturalUsers(0);
            dataRows.get(i).setActualResidentUser(0);
            dataRows.get(i).setActualBusStation(0);
            dataRows.get(i).setActualResidentialside(0);
            dataRows.get(i).setActualEnergyStorage(0);
            dataRows.get(i).setActualResponseLoad(dataRows.get(i).getActualResponseLoad() != null ? dataRows.get(i).getActualResponseLoad() : BigDecimal.ZERO);
            dataRows.get(i).setActualResponsePower(dataRows.get(i).getActualResponsePower() != null ? dataRows.get(i).getActualResponsePower() : BigDecimal.ZERO);
            dataRows.get(i).setTargetCompletionRate(dataRows.get(i).getTargetCompletionRate() != 0 ? dataRows.get(i).getTargetCompletionRate() : 0.00);
            dataRows.get(i).setEffectiveIndustrialUsers(0);
            dataRows.get(i).setEffectBusinessUsers(0);
            dataRows.get(i).setEffectAgriculturalUsers(0);
            dataRows.get(i).setEffectResidentUser(0);
            dataRows.get(i).setEffectBusStation(0);
            dataRows.get(i).setEffectResidentialside(0);
            dataRows.get(i).setEffectEnergyStorage(0);
        }

//        实际用户
        for (DrEventInvitationEffectEval dataRow : dataRows) {
            List<Map> map = drEventInvitationMapper.getactualUsers(dataRow.getEventId());
            if (!CollectionUtils.isEmpty(map)) {
                Map map1 = map.get(0);
                if (map1 != null) {
                    dataRow.setActualIndustrialUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualIndustrialUsers")) ? "0" : map1.get("actualIndustrialUsers").toString()));
                    dataRow.setActualBusinessUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualBusinessUsers")) ? "0" : map1.get("actualBusinessUsers").toString()));
                    dataRow.setActualAgriculturalUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualAgriculturalUsers")) ? "0" : map1.get("actualAgriculturalUsers").toString()));
                    dataRow.setActualResidentUser(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualResidentUser")) ? "0" : map1.get("actualResidentUser").toString()));
                    dataRow.setActualBusStation(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualBusStation")) ? "0" : map1.get("actualBusStation").toString()));
                    dataRow.setActualResidentialside(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualResidentialside")) ? "0" : map1.get("actualResidentialside").toString()));
                    dataRow.setActualEnergyStorage(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualEnergyStorage")) ? "0" : map1.get("actualEnergyStorage").toString()));
                }
            }
        }

//        有效用户
        for (DrEventInvitationEffectEval dataRow : dataRows) {
            List<Map> map = drEventInvitationMapper.getEffectUsersImmediate(dataRow.getEventId());
            if (!CollectionUtils.isEmpty(map)) {
                Map map1 = map.get(0);
                if (map1 != null) {
                    dataRow.setEffectiveIndustrialUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectiveIndustrialUsers")) ? "0" : map1.get("effectiveIndustrialUsers").toString()));
                    dataRow.setEffectBusinessUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectBusinessUsers")) ? "0" : map1.get("effectBusinessUsers").toString()));
                    dataRow.setEffectAgriculturalUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectAgriculturalUsers")) ? "0" : map1.get("effectAgriculturalUsers").toString()));
                    dataRow.setEffectResidentUser(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectResidentUser")) ? "0" : map1.get("effectResidentUser").toString()));
                    dataRow.setEffectBusStation(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectBusStation")) ? "0" : map1.get("effectBusStation").toString()));
                    dataRow.setEffectResidentialside(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectResidentialside")) ? "0" : map1.get("effectResidentialside").toString()));
                    dataRow.setEffectEnergyStorage(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectEnergyStorage")) ? "0" : map1.get("effectEnergyStorage").toString()));
                }
            }
        }


        String sheetName = "效果评估表";
        String excelName = "需求响应事件效果评估表";
        Map<String, Map<String, Object>> headerRows = new HashMap<>();
        String[] headerVerticalRow = {"序号", "响应区域", "响应日期", "开始时间", "结束时间", "目标容量(kW)", "实际响应负荷(kW)", "实际响应电量(kWh)", "目标完成率"};
        String[] headerLevelRow = {"实际参与用户数量", "有效响应用户数量"};
        String[] headerLevelRow2 = {"电动汽车", "电动汽车"};
        String[] headerNormalRow = {"工业用户", "商业用户", "农业用户", "居民用户", "公交场站", "居民侧", "储能", "工业用户", "商业用户", "农业用户", "居民用户", "公交场站", "居民侧", "储能"};
        String[][] dataContent = new String[dataRows.size()][headerVerticalRow.length + headerNormalRow.length];
        int len = headerVerticalRow.length + headerNormalRow.length;
        Map<String, Object> titleName = new HashMap<>();
        titleName.put("headerName", new String[]{excelName});
        titleName.put("startRow", new int[]{0});
        titleName.put("endRow", new int[]{0});
        titleName.put("startCol", new int[]{0});
        titleName.put("endCol", new int[]{len - 1});
        titleName.put("rowCount", 4);
        titleName.put("colCount", len);

        Map<String, Object> verticalMap = new HashMap<>();
        verticalMap.put("titleRow", headerVerticalRow);
        verticalMap.put("startRow", new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
        verticalMap.put("endRow", new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3});
        verticalMap.put("startCol", new int[]{0, 1, 2, 3, 4, 5, 13, 14, 15});
        verticalMap.put("endCol", new int[]{0, 1, 2, 3, 4, 5, 13, 14, 15});

        Map<String, Object> levelMap = new HashMap<>();
        levelMap.put("titleRow", headerLevelRow);
        levelMap.put("startRow", new int[]{1, 1});
        levelMap.put("endRow", new int[]{1, 1});
        levelMap.put("startCol", new int[]{6, 16});
        levelMap.put("endCol", new int[]{12, 22});

        Map<String, Object> levelMap2 = new HashMap<>();
        levelMap2.put("titleRow", headerLevelRow2);
        levelMap2.put("startRow", new int[]{2, 2});
        levelMap2.put("endRow", new int[]{2, 2});
        levelMap2.put("startCol", new int[]{10, 20});
        levelMap2.put("endCol", new int[]{11, 21});

        Map<String, Object> normalMap = new HashMap<>();
        normalMap.put("titleRow", headerNormalRow);
        normalMap.put("startRow", new int[]{2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 2, 3, 3, 2});
        normalMap.put("endRow", new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3});
        normalMap.put("startCol", new int[]{6, 7, 8, 9, 10, 11, 12, 16, 17, 18, 19, 20, 21, 22});
        normalMap.put("endCol", new int[]{6, 7, 8, 9, 10, 11, 12, 16, 17, 18, 19, 20, 21, 22});


        headerRows.put("level0", levelMap);
        headerRows.put("level1", levelMap2);
        headerRows.put("level2", normalMap);
        headerRows.put("level3", verticalMap);

        for (int i = 0; i < dataRows.size(); i++) {
            dataContent[i][0] = String.valueOf((i + 1));
            dataContent[i][1] = String.valueOf(dataRows.get(i).getCityName());//响应区域
            dataContent[i][2] = String.valueOf(dataRows.get(i).getResponseDate());//响应日期

            dataContent[i][3] = String.valueOf(dataRows.get(i).getStartTime());//开始时间
            dataContent[i][4] = String.valueOf(dataRows.get(i).getEndTime());//结束时间
            dataContent[i][5] = String.valueOf(dataRows.get(i).getRegulateCap());//目标容量
            dataContent[i][6] = String.valueOf(dataRows.get(i).getActualIndustrialUsers());//实际工业用户数量
            dataContent[i][7] = String.valueOf(dataRows.get(i).getActualBusinessUsers());//实际商业用户数量
            dataContent[i][8] = String.valueOf(dataRows.get(i).getActualAgriculturalUsers());//实际农业用户数量
            dataContent[i][9] = String.valueOf(dataRows.get(i).getActualResidentUser());//实际居民用户数量
            dataContent[i][10] = String.valueOf(dataRows.get(i).getActualBusStation());//实际公交场站
            dataContent[i][11] = String.valueOf(dataRows.get(i).getActualResidentialside());//实际居民侧
            dataContent[i][12] = String.valueOf(dataRows.get(i).getActualEnergyStorage());//实际储能
            dataContent[i][13] = String.valueOf(dataRows.get(i).getActualResponseLoad());//实际响应负荷
            dataContent[i][14] = String.valueOf(dataRows.get(i).getActualResponsePower());//实际响应电量
//            dataContent[i][15] = String.valueOf(dataRows.get(i).getTargetCompletionRate());//目标完成率
            dataContent[i][15] = String.format("%.2f", (dataRows.get(i).getTargetCompletionRate() * 100)) + "%";//目标完成率
            dataContent[i][16] = String.valueOf(dataRows.get(i).getEffectiveIndustrialUsers());//有效工业用户数量
            dataContent[i][17] = String.valueOf(dataRows.get(i).getEffectBusinessUsers());//有效商业用户数量
            dataContent[i][18] = String.valueOf(dataRows.get(i).getEffectAgriculturalUsers());//有效农业用户数量
            dataContent[i][19] = String.valueOf(dataRows.get(i).getEffectResidentUser());//有效居民用户数量
            dataContent[i][20] = String.valueOf(dataRows.get(i).getEffectBusStation());//有效公交场站
            dataContent[i][21] = String.valueOf(dataRows.get(i).getEffectResidentialside());//有效居民侧
            dataContent[i][22] = String.valueOf(dataRows.get(i).getEffectEnergyStorage());//有效储能

        }


        PoiExcelUtils.customFormatExcel2(excelName, sheetName, titleName, headerRows, dataContent, HttpServletUtil.getResponse());


    }

    /**
     * 事件效果评估统计--次日导出
     */
    @Override
    public void exprotEffectEval(String startDate, String endDate, Long eventId, Long provinceCode, Long cityCode) {
        // 根据日期和事件id查询事件
        List<DrEventInvitationEffectEval> dataRows = drEventInvitationMapper.exprotffectEval(startDate, endDate, eventId, cityCode);


        //获取 所有地区
        List<Region> regions = systemClientService.queryAll();

        List<SysOrgs> allOrgs = new ArrayList<>();
        //获取 所有供电公司
        JSONObject jsonObject = systemClientService.queryAllOrg();
        if (jsonObject != null) {
            if ("000000".equals(jsonObject.get("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (Object o : jsonArray) {
                    JSONObject object = (JSONObject) JSONObject.toJSON(o);
                    SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(object);

                    allOrgs.add(sysOrgs);
                }
            }
        }
        // 筛选城市
        if (!CollectionUtils.isEmpty(regions) && !CollectionUtils.isEmpty(allOrgs)) {
            Map<String, String> regionMap = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
            Map<String, String> sysOrgMap = allOrgs.stream().collect(Collectors.toMap(SysOrgs::getId, SysOrgs::getName));

            for (int i = 0; i < dataRows.size(); i++) {
                DrEventInvitationEffectEval effectEval = dataRows.get(i);

                //判断 调控范围类别：地区/分区/变电站/线路/台区
                String rangeType = effectEval.getRangeType();
                if (!StringUtils.isEmpty(rangeType)) {
                    if (RangeTypeEnum.ADMINISTRATIVE_REGION.getCode().equals(rangeType)) {

                        if (!StringUtils.isEmpty(effectEval.getCityName())) {
                            String[] rangeList = effectEval.getCityName().split("],\\[");
                            String cityName = "";
                            if (rangeList.length > 0) {
                                for (String str : rangeList) {
                                    String[] rangeSplitList = str.replace("[", "").replace("]", "").replace("\"", "").split(",");
                                    if (rangeSplitList.length == 3) {
                                        String range = rangeSplitList[2];
                                        cityName = cityName + "/" + (regionMap.get(range) == null ? range : regionMap.get(range));
                                    } else if (rangeSplitList.length == 2) {
                                        String range = rangeSplitList[1].substring(0, 6);
                                        cityName = cityName + "/" + (regionMap.get(range) == null ? range : regionMap.get(range));
                                    } else if (rangeSplitList.length == 1) {
                                        String REGEX = "[^(0-9)]";
                                        String range = Pattern.compile(REGEX).matcher(rangeSplitList[0]).replaceAll("").trim();
                                        if (range != null && !range.equals("")) {
                                            effectEval.setCityName(regionMap.get(range) == null ? range : regionMap.get(range));
                                        } else {
                                            effectEval.setCityName("");
                                        }
                                    }
                                }

                                if(!StringUtils.isEmpty(cityName)){
                                    effectEval.setCityName(cityName.replaceFirst("/",""));
                                }
                            }
                        }

                    }
                }

                if (RangeTypeEnum.ELECTRICIC_REGION.getCode().equals(rangeType)) {

                    if (!StringUtils.isEmpty(effectEval.getCityName())) {
                        String[] rangeList = effectEval.getCityName().split("],\\[");
                        String cityName = "";
                        for (String str : rangeList) {
                            if (rangeList.length > 0) {
                                String[] rangeSplitList = str.replace("[", "").replace("]", "").replace("\"", "").split(",");
                                if (rangeSplitList.length == 3) {
                                    String range = rangeSplitList[2];
                                    cityName = cityName + "/" + (sysOrgMap.get(range) == null ? range : sysOrgMap.get(range));
                                } else if (rangeSplitList.length == 2) {
                                    String range = rangeSplitList[1];
                                    cityName = cityName + "/" + (sysOrgMap.get(range) == null ? range : sysOrgMap.get(range));
                                } else if (rangeSplitList.length == 1) {
                                    String REGEX = "[^(0-9)]";
                                    String range = Pattern.compile(REGEX).matcher(rangeSplitList[0]).replaceAll("").trim();
                                    if (range != null && !range.equals("")) {
                                        cityName = sysOrgMap.get(range) == null ? range : sysOrgMap.get(range);
                                    } else {
                                        effectEval.setCityName("");
                                    }
                                }
                            }
                        }
                        if(!StringUtils.isEmpty(cityName)){
                            effectEval.setCityName(cityName.replaceFirst("/",""));
                        }
                    }
                }
            }
        }

        for (int i = 0; i < dataRows.size(); i++) {
            dataRows.get(i).setActualIndustrialUsers(0);
            dataRows.get(i).setActualBusinessUsers(0);
            dataRows.get(i).setActualAgriculturalUsers(0);
            dataRows.get(i).setActualResidentUser(0);
            dataRows.get(i).setActualBusStation(0);
            dataRows.get(i).setActualResidentialside(0);
            dataRows.get(i).setActualEnergyStorage(0);
            dataRows.get(i).setActualResponseLoad(dataRows.get(i).getActualResponseLoad() != null ? dataRows.get(i).getActualResponseLoad() : BigDecimal.ZERO);
            dataRows.get(i).setActualResponsePower(dataRows.get(i).getActualResponsePower() != null ? dataRows.get(i).getActualResponsePower() : BigDecimal.ZERO);
            dataRows.get(i).setTargetCompletionRate(dataRows.get(i).getTargetCompletionRate() != 0 ? dataRows.get(i).getTargetCompletionRate() : 0.00);
            dataRows.get(i).setEffectiveIndustrialUsers(0);
            dataRows.get(i).setEffectBusinessUsers(0);
            dataRows.get(i).setEffectAgriculturalUsers(0);
            dataRows.get(i).setEffectResidentUser(0);
            dataRows.get(i).setEffectBusStation(0);
            dataRows.get(i).setEffectResidentialside(0);
            dataRows.get(i).setEffectEnergyStorage(0);
        }

//        实际用户
        for (DrEventInvitationEffectEval dataRow : dataRows) {
            List<Map> map = drEventInvitationMapper.getactualUsers(dataRow.getEventId());
            if (!CollectionUtils.isEmpty(map)) {
                Map map1 = map.get(0);
                if (map1 != null) {
                    dataRow.setActualIndustrialUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualIndustrialUsers")) ? "0" : map1.get("actualIndustrialUsers").toString()));
                    dataRow.setActualBusinessUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualBusinessUsers")) ? "0" : map1.get("actualBusinessUsers").toString()));
                    dataRow.setActualAgriculturalUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualAgriculturalUsers")) ? "0" : map1.get("actualAgriculturalUsers").toString()));
                    dataRow.setActualResidentUser(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualResidentUser")) ? "0" : map1.get("actualResidentUser").toString()));
                    dataRow.setActualBusStation(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualBusStation")) ? "0" : map1.get("actualBusStation").toString()));
                    dataRow.setActualResidentialside(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualResidentialside")) ? "0" : map1.get("actualResidentialside").toString()));
                    dataRow.setActualEnergyStorage(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualEnergyStorage")) ? "0" : map1.get("actualEnergyStorage").toString()));
                }
            }
        }

//        有效用户
        for (DrEventInvitationEffectEval dataRow : dataRows) {
            List<Map> map = drEventInvitationMapper.getEffectUsersImmediate(dataRow.getEventId());
            if (!CollectionUtils.isEmpty(map)) {
                Map map1 = map.get(0);
                if (map1 != null) {
                    dataRow.setEffectiveIndustrialUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectiveIndustrialUsers")) ? "0" : map1.get("effectiveIndustrialUsers").toString()));
                    dataRow.setEffectBusinessUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectBusinessUsers")) ? "0" : map1.get("effectBusinessUsers").toString()));
                    dataRow.setEffectAgriculturalUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectAgriculturalUsers")) ? "0" : map1.get("effectAgriculturalUsers").toString()));
                    dataRow.setEffectResidentUser(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectResidentUser")) ? "0" : map1.get("effectResidentUser").toString()));
                    dataRow.setEffectBusStation(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectBusStation")) ? "0" : map1.get("effectBusStation").toString()));
                    dataRow.setEffectResidentialside(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectResidentialside")) ? "0" : map1.get("effectResidentialside").toString()));
                    dataRow.setEffectEnergyStorage(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectEnergyStorage")) ? "0" : map1.get("effectEnergyStorage").toString()));
                }
            }
        }


        String sheetName = "效果评估表";
        String excelName = "需求响应事件效果评估表";
        Map<String, Map<String, Object>> headerRows = new HashMap<>();
        String[] headerVerticalRow = {"序号", "响应区域", "响应日期", "开始时间", "结束时间", "目标容量(kW)", "实际响应负荷(kW)", "实际响应电量(kWh)", "目标完成率"};
        String[] headerLevelRow = {"实际参与用户数量", "有效响应用户数量"};
        String[] headerLevelRow2 = {"电动汽车", "电动汽车"};
        String[] headerNormalRow = {"工业用户", "商业用户", "农业用户", "居民用户", "公交场站", "居民侧", "储能", "工业用户", "商业用户", "农业用户", "居民用户", "公交场站", "居民侧", "储能"};
        String[][] dataContent = new String[dataRows.size()][headerVerticalRow.length + headerNormalRow.length];
        int len = headerVerticalRow.length + headerNormalRow.length;
        Map<String, Object> titleName = new HashMap<>();
        titleName.put("headerName", new String[]{excelName});
        titleName.put("startRow", new int[]{0});
        titleName.put("endRow", new int[]{0});
        titleName.put("startCol", new int[]{0});
        titleName.put("endCol", new int[]{len - 1});
        titleName.put("rowCount", 4);
        titleName.put("colCount", len);

        Map<String, Object> verticalMap = new HashMap<>();
        verticalMap.put("titleRow", headerVerticalRow);
        verticalMap.put("startRow", new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
        verticalMap.put("endRow", new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3});
        verticalMap.put("startCol", new int[]{0, 1, 2, 3, 4, 5, 13, 14, 15});
        verticalMap.put("endCol", new int[]{0, 1, 2, 3, 4, 5, 13, 14, 15});

        Map<String, Object> levelMap = new HashMap<>();
        levelMap.put("titleRow", headerLevelRow);
        levelMap.put("startRow", new int[]{1, 1});
        levelMap.put("endRow", new int[]{1, 1});
        levelMap.put("startCol", new int[]{6, 16});
        levelMap.put("endCol", new int[]{12, 22});

        Map<String, Object> levelMap2 = new HashMap<>();
        levelMap2.put("titleRow", headerLevelRow2);
        levelMap2.put("startRow", new int[]{2, 2});
        levelMap2.put("endRow", new int[]{2, 2});
        levelMap2.put("startCol", new int[]{10, 20});
        levelMap2.put("endCol", new int[]{11, 21});

        Map<String, Object> normalMap = new HashMap<>();
        normalMap.put("titleRow", headerNormalRow);
        normalMap.put("startRow", new int[]{2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 2, 3, 3, 2});
        normalMap.put("endRow", new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3});
        normalMap.put("startCol", new int[]{6, 7, 8, 9, 10, 11, 12, 16, 17, 18, 19, 20, 21, 22});
        normalMap.put("endCol", new int[]{6, 7, 8, 9, 10, 11, 12, 16, 17, 18, 19, 20, 21, 22});


        headerRows.put("level0", levelMap);
        headerRows.put("level1", levelMap2);
        headerRows.put("level2", normalMap);
        headerRows.put("level3", verticalMap);

        for (int i = 0; i < dataRows.size(); i++) {
            dataContent[i][0] = String.valueOf((i + 1));
            dataContent[i][1] = String.valueOf(dataRows.get(i).getCityName());//响应区域
            dataContent[i][2] = String.valueOf(dataRows.get(i).getResponseDate());//响应日期

            dataContent[i][3] = String.valueOf(dataRows.get(i).getStartTime());//开始时间
            dataContent[i][4] = String.valueOf(dataRows.get(i).getEndTime());//结束时间
            dataContent[i][5] = String.valueOf(dataRows.get(i).getRegulateCap());//目标容量
            dataContent[i][6] = String.valueOf(dataRows.get(i).getActualIndustrialUsers());//实际工业用户数量
            dataContent[i][7] = String.valueOf(dataRows.get(i).getActualBusinessUsers());//实际商业用户数量
            dataContent[i][8] = String.valueOf(dataRows.get(i).getActualAgriculturalUsers());//实际农业用户数量
            dataContent[i][9] = String.valueOf(dataRows.get(i).getActualResidentUser());//实际居民用户数量
            dataContent[i][10] = String.valueOf(dataRows.get(i).getActualBusStation());//实际公交场站
            dataContent[i][11] = String.valueOf(dataRows.get(i).getActualResidentialside());//实际居民侧
            dataContent[i][12] = String.valueOf(dataRows.get(i).getActualEnergyStorage());//实际储能
            dataContent[i][13] = String.valueOf(dataRows.get(i).getActualResponseLoad());//实际响应负荷
            dataContent[i][14] = String.valueOf(dataRows.get(i).getActualResponsePower());//实际响应电量
            dataContent[i][15] = String.format("%.2f", (dataRows.get(i).getTargetCompletionRate() * 100)) + "%";//目标完成率
            dataContent[i][16] = String.valueOf(dataRows.get(i).getEffectiveIndustrialUsers());//有效工业用户数量
            dataContent[i][17] = String.valueOf(dataRows.get(i).getEffectBusinessUsers());//有效商业用户数量
            dataContent[i][18] = String.valueOf(dataRows.get(i).getEffectAgriculturalUsers());//有效农业用户数量
            dataContent[i][19] = String.valueOf(dataRows.get(i).getEffectResidentUser());//有效居民用户数量
            dataContent[i][20] = String.valueOf(dataRows.get(i).getEffectBusStation());//有效公交场站
            dataContent[i][21] = String.valueOf(dataRows.get(i).getEffectResidentialside());//有效居民侧
            dataContent[i][22] = String.valueOf(dataRows.get(i).getEffectEnergyStorage());//有效储能

        }


        PoiExcelUtils.customFormatExcel2(excelName, sheetName, titleName, headerRows, dataContent, HttpServletUtil.getResponse());


    }

    /**
     * 事件效果评估统计--次日
     */
    @Override
    public IPage<DrEventInvitationEffectEval> getEffectEval(long current, long size, String startDate, String endDate, Long eventId, Long provinceCode, Long cityCode) {
        Page<DrEventInvitationEffectEval> page = new Page<>(current, size);
        page.setOptimizeCountSql(false);
        // 根据日期和事件id查询事件
        List<DrEventInvitationEffectEval> dataRows = drEventInvitationMapper.getEffectEval(page, startDate, endDate, eventId, cityCode);
        //获取 所有地区
        List<Region> regions = systemClientService.queryAll();

        List<SysOrgs> allOrgs = new ArrayList<>();
        //获取 所有供电公司
        JSONObject jsonObject = systemClientService.queryAllOrg();
        if (jsonObject != null) {
            if ("000000".equals(jsonObject.get("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (Object o : jsonArray) {
                    JSONObject object = (JSONObject) JSONObject.toJSON(o);
                    SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(object);

                    allOrgs.add(sysOrgs);
                }
            }
        }
        if (!CollectionUtils.isEmpty(regions)) {
            Map<String, String> regionMap = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
            Map<String, String> sysOrgMap = allOrgs.stream().collect(Collectors.toMap(SysOrgs::getId, SysOrgs::getName));

            for (int i = 0; i < dataRows.size(); i++) {
                DrEventInvitationEffectEval effectEval = dataRows.get(i);

                //判断 调控范围类别：地区/分区/变电站/线路/台区
                String rangeType = effectEval.getRangeType();
                if (!StringUtils.isEmpty(rangeType)) {
                    if (RangeTypeEnum.ADMINISTRATIVE_REGION.getCode().equals(rangeType)) {

                        if (!StringUtils.isEmpty(effectEval.getCityName())) {
                            String[] rangeList = effectEval.getCityName().split("],\\[");
                            String cityName = "";
                            if (rangeList.length > 0) {
                                for (String str : rangeList) {
                                    String[] rangeSplitList = str.replace("[", "").replace("]", "").replace("\"", "").split(",");
                                    if (rangeSplitList.length == 3) {
                                        String range = rangeSplitList[2];
                                        cityName = cityName + "/" + (regionMap.get(range) == null ? range : regionMap.get(range));
                                    } else if (rangeSplitList.length == 2) {
                                        String range = rangeSplitList[1].substring(0, 6);
                                        cityName = cityName + "/" + (regionMap.get(range) == null ? range : regionMap.get(range));
                                    } else if (rangeSplitList.length == 1) {
                                        String REGEX = "[^(0-9)]";
                                        String range = Pattern.compile(REGEX).matcher(rangeSplitList[0]).replaceAll("").trim();
                                        if (range != null && !range.equals("")) {
                                            effectEval.setCityName(regionMap.get(range) == null ? range : regionMap.get(range));
                                        } else {
                                            effectEval.setCityName("");
                                        }
                                    }
                                }

                                if(!StringUtils.isEmpty(cityName)){
                                    effectEval.setCityName(cityName.replaceFirst("/",""));
                                }
                            }
                        }

                    }
                }

                if (RangeTypeEnum.ELECTRICIC_REGION.getCode().equals(rangeType)) {

                    if (!StringUtils.isEmpty(effectEval.getCityName())) {
                        String[] rangeList = effectEval.getCityName().split("],\\[");
                        String cityName = "";
                        for (String str : rangeList) {
                            if (rangeList.length > 0) {
                                String[] rangeSplitList = str.replace("[", "").replace("]", "").replace("\"", "").split(",");
                                if (rangeSplitList.length == 3) {
                                    String range = rangeSplitList[2];
                                    cityName = cityName + "/" + (sysOrgMap.get(range) == null ? range : sysOrgMap.get(range));
                                } else if (rangeSplitList.length == 2) {
                                    String range = rangeSplitList[1];
                                    cityName = cityName + "/" + (sysOrgMap.get(range) == null ? range : sysOrgMap.get(range));
                                } else if (rangeSplitList.length == 1) {
                                    String REGEX = "[^(0-9)]";
                                    String range = Pattern.compile(REGEX).matcher(rangeSplitList[0]).replaceAll("").trim();
                                    if (range != null && !range.equals("")) {
                                        cityName = sysOrgMap.get(range) == null ? range : sysOrgMap.get(range);
                                    } else {
                                        effectEval.setCityName("");
                                    }
                                }
                            }
                        }
                        if(!StringUtils.isEmpty(cityName)){
                            effectEval.setCityName(cityName.replaceFirst("/",""));
                        }
                    }
                }
            }
        }

        for (DrEventInvitationEffectEval dataRow : dataRows) {
            dataRow.setActualIndustrialUsers(0);
            dataRow.setActualBusinessUsers(0);
            dataRow.setActualAgriculturalUsers(0);
            dataRow.setActualResidentUser(0);
            dataRow.setActualBusStation(0);
            dataRow.setActualResidentialside(0);
            dataRow.setActualEnergyStorage(0);
            dataRow.setActualResponseLoad(dataRow.getActualResponseLoad() != null ? dataRow.getActualResponseLoad() : BigDecimal.ZERO);
            dataRow.setActualResponsePower(dataRow.getActualResponsePower() != null ? dataRow.getActualResponsePower() : BigDecimal.ZERO);
            dataRow.setTargetCompletionRate(dataRow.getTargetCompletionRate() != 0 ? dataRow.getTargetCompletionRate() : 0.00);
            dataRow.setEffectiveIndustrialUsers(0);
            dataRow.setEffectBusinessUsers(0);
            dataRow.setEffectAgriculturalUsers(0);
            dataRow.setEffectResidentUser(0);
            dataRow.setEffectBusStation(0);
            dataRow.setEffectResidentialside(0);
            dataRow.setEffectEnergyStorage(0);
        }

//        实际用户
        for (DrEventInvitationEffectEval dataRow : dataRows) {
            List<Map> map = drEventInvitationMapper.getactualUsers(dataRow.getEventId());
            if (!CollectionUtils.isEmpty(map)) {
                Map map1 = map.get(0);
                if (map1 != null) {
                    dataRow.setActualIndustrialUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualIndustrialUsers")) ? "0" : map1.get("actualIndustrialUsers").toString()));
                    dataRow.setActualBusinessUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualBusinessUsers")) ? "0" : map1.get("actualBusinessUsers").toString()));
                    dataRow.setActualAgriculturalUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualAgriculturalUsers")) ? "0" : map1.get("actualAgriculturalUsers").toString()));
                    dataRow.setActualResidentUser(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualResidentUser")) ? "0" : map1.get("actualResidentUser").toString()));
                    dataRow.setActualBusStation(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualBusStation")) ? "0" : map1.get("actualBusStation").toString()));
                    dataRow.setActualResidentialside(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualResidentialside")) ? "0" : map1.get("actualResidentialside").toString()));
                    dataRow.setActualEnergyStorage(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualEnergyStorage")) ? "0" : map1.get("actualEnergyStorage").toString()));
                }
            }
        }

//        有效用户
        for (DrEventInvitationEffectEval dataRow : dataRows) {
            List<Map> map = drEventInvitationMapper.getEffectUsersImmediate(dataRow.getEventId());
            if (!CollectionUtils.isEmpty(map)) {
                Map map1 = map.get(0);
                if (map1 != null) {
                    dataRow.setEffectiveIndustrialUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectiveIndustrialUsers")) ? "0" : map1.get("effectiveIndustrialUsers").toString()));
                    dataRow.setEffectBusinessUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectBusinessUsers")) ? "0" : map1.get("effectBusinessUsers").toString()));
                    dataRow.setEffectAgriculturalUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectAgriculturalUsers")) ? "0" : map1.get("effectAgriculturalUsers").toString()));
                    dataRow.setEffectResidentUser(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectResidentUser")) ? "0" : map1.get("effectResidentUser").toString()));
                    dataRow.setEffectBusStation(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectBusStation")) ? "0" : map1.get("effectBusStation").toString()));
                    dataRow.setEffectResidentialside(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectResidentialside")) ? "0" : map1.get("effectResidentialside").toString()));
                    dataRow.setEffectEnergyStorage(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectEnergyStorage")) ? "0" : map1.get("effectEnergyStorage").toString()));
                }
            }
        }


        page.setRecords(dataRows);
        return page;
    }

    /**
     * 事件效果评估统计当日
     */
    @Override
    public IPage<DrEventInvitationEffectEval> getEffectEvalImmediate(long current, long size, String startDate, String endDate, Long eventId, Long provinceCode, Long cityCode) {
        Page<DrEventInvitationEffectEval> page = new Page<>(current, size);
        page.setOptimizeCountSql(false);
        // 根据日期和事件id查询事件
        List<DrEventInvitationEffectEval> dataRows = drEventInvitationMapper.getEffectEvalImmediate(page, startDate, endDate, eventId, cityCode);

        //获取 所有地区
        List<Region> regions = systemClientService.queryAll();

        List<SysOrgs> allOrgs = new ArrayList<>();
        //获取 所有供电公司
        JSONObject jsonObject = systemClientService.queryAllOrg();
        if (jsonObject != null) {
            if ("000000".equals(jsonObject.get("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (Object o : jsonArray) {
                    JSONObject object = (JSONObject) JSONObject.toJSON(o);
                    SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(object);

                    allOrgs.add(sysOrgs);
                }
            }
        }

        if (!CollectionUtils.isEmpty(regions) && !CollectionUtils.isEmpty(allOrgs)) {
            Map<String, String> regionMap = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
            Map<String, String> sysOrgMap = allOrgs.stream().collect(Collectors.toMap(SysOrgs::getId, SysOrgs::getName));

            for (int i = 0; i < dataRows.size(); i++) {
                DrEventInvitationEffectEval effectEval = dataRows.get(i);

                //判断 调控范围类别：地区/分区/变电站/线路/台区
                String rangeType = effectEval.getRangeType();
                if (!StringUtils.isEmpty(rangeType)) {
                    if (RangeTypeEnum.ADMINISTRATIVE_REGION.getCode().equals(rangeType)) {

                        if (!StringUtils.isEmpty(effectEval.getCityName())) {
                            String[] rangeList = effectEval.getCityName().split("],\\[");
                            String cityName = "";
                            if (rangeList.length > 0) {
                                for (String str : rangeList) {
                                    String[] rangeSplitList = str.replace("[", "").replace("]", "").replace("\"", "").split(",");
                                    if (rangeSplitList.length == 3) {
                                        String range = rangeSplitList[2];
                                        cityName = cityName + "/" + (regionMap.get(range) == null ? range : regionMap.get(range));
                                    } else if (rangeSplitList.length == 2) {
                                        String range = rangeSplitList[1].substring(0, 6);
                                        cityName = cityName + "/" + (regionMap.get(range) == null ? range : regionMap.get(range));
                                    } else if (rangeSplitList.length == 1) {
                                        String REGEX = "[^(0-9)]";
                                        String range = Pattern.compile(REGEX).matcher(rangeSplitList[0]).replaceAll("").trim();
                                        if (range != null && !range.equals("")) {
                                            effectEval.setCityName(regionMap.get(range) == null ? range : regionMap.get(range));
                                        } else {
                                            effectEval.setCityName("");
                                        }
                                    }
                                }

                                if(!StringUtils.isEmpty(cityName)){
                                    effectEval.setCityName(cityName.replaceFirst("/",""));
                                }
                            }
                        }

                    }
                }

                if (RangeTypeEnum.ELECTRICIC_REGION.getCode().equals(rangeType)) {

                    if (!StringUtils.isEmpty(effectEval.getCityName())) {
                        String[] rangeList = effectEval.getCityName().split("],\\[");
                        String cityName = "";
                        for (String str : rangeList) {
                            if (rangeList.length > 0) {
                                String[] rangeSplitList = str.replace("[", "").replace("]", "").replace("\"", "").split(",");
                                if (rangeSplitList.length == 3) {
                                    String range = rangeSplitList[2];
                                    cityName = cityName + "/" + (sysOrgMap.get(range) == null ? range : sysOrgMap.get(range));
                                } else if (rangeSplitList.length == 2) {
                                    String range = rangeSplitList[1];
                                    cityName = cityName + "/" + (sysOrgMap.get(range) == null ? range : sysOrgMap.get(range));
                                } else if (rangeSplitList.length == 1) {
                                    String REGEX = "[^(0-9)]";
                                    String range = Pattern.compile(REGEX).matcher(rangeSplitList[0]).replaceAll("").trim();
                                    if (range != null && !range.equals("")) {
                                        cityName = sysOrgMap.get(range) == null ? range : sysOrgMap.get(range);
                                    } else {
                                        effectEval.setCityName("");
                                    }
                                }
                            }
                        }
                        if(!StringUtils.isEmpty(cityName)){
                            effectEval.setCityName(cityName.replaceFirst("/",""));
                        }
                    }
                }
            }
        }

        // 筛选城市


        for (DrEventInvitationEffectEval dataRow : dataRows) {
            dataRow.setActualIndustrialUsers(0);
            dataRow.setActualBusinessUsers(0);
            dataRow.setActualAgriculturalUsers(0);
            dataRow.setActualResidentUser(0);
            dataRow.setActualBusStation(0);
            dataRow.setActualResidentialside(0);
            dataRow.setActualEnergyStorage(0);
            dataRow.setActualResponseLoad(dataRow.getActualResponseLoad() != null ? dataRow.getActualResponseLoad() : BigDecimal.ZERO);
            dataRow.setActualResponsePower(dataRow.getActualResponsePower() != null ? dataRow.getActualResponsePower() : BigDecimal.ZERO);
            dataRow.setTargetCompletionRate(dataRow.getTargetCompletionRate() != 0 ? dataRow.getTargetCompletionRate() : 0.00);
            dataRow.setEffectiveIndustrialUsers(0);
            dataRow.setEffectBusinessUsers(0);
            dataRow.setEffectAgriculturalUsers(0);
            dataRow.setEffectResidentUser(0);
            dataRow.setEffectBusStation(0);
            dataRow.setEffectResidentialside(0);
            dataRow.setEffectEnergyStorage(0);
        }

//        实际用户
        for (DrEventInvitationEffectEval dataRow : dataRows) {
            List<Map> map = drEventInvitationMapper.getactualUsers(dataRow.getEventId());
            if (!CollectionUtils.isEmpty(map)) {
                Map map1 = map.get(0);
                if (map1 != null) {
                    dataRow.setActualIndustrialUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualIndustrialUsers")) ? "0" : map1.get("actualIndustrialUsers").toString()));
                    dataRow.setActualBusinessUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualBusinessUsers")) ? "0" : map1.get("actualBusinessUsers").toString()));
                    dataRow.setActualAgriculturalUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualAgriculturalUsers")) ? "0" : map1.get("actualAgriculturalUsers").toString()));
                    dataRow.setActualResidentUser(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualResidentUser")) ? "0" : map1.get("actualResidentUser").toString()));
                    dataRow.setActualBusStation(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualBusStation")) ? "0" : map1.get("actualBusStation").toString()));
                    dataRow.setActualResidentialside(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualResidentialside")) ? "0" : map1.get("actualResidentialside").toString()));
                    dataRow.setActualEnergyStorage(Integer.valueOf(ObjectUtils.isEmpty(map1.get("actualEnergyStorage")) ? "0" : map1.get("actualEnergyStorage").toString()));
                }
            }
        }

//        有效用户
        for (DrEventInvitationEffectEval dataRow : dataRows) {
            List<Map> map = drEventInvitationMapper.getEffectUsersImmediate(dataRow.getEventId());
            if (!CollectionUtils.isEmpty(map)) {
                Map map1 = map.get(0);
                if (map1 != null) {
                    dataRow.setEffectiveIndustrialUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectiveIndustrialUsers")) ? "0" : map1.get("effectiveIndustrialUsers").toString()));
                    dataRow.setEffectBusinessUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectBusinessUsers")) ? "0" : map1.get("effectBusinessUsers").toString()));
                    dataRow.setEffectAgriculturalUsers(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectAgriculturalUsers")) ? "0" : map1.get("effectAgriculturalUsers").toString()));
                    dataRow.setEffectResidentUser(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectResidentUser")) ? "0" : map1.get("effectResidentUser").toString()));
                    dataRow.setEffectBusStation(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectBusStation")) ? "0" : map1.get("effectBusStation").toString()));
                    dataRow.setEffectResidentialside(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectResidentialside")) ? "0" : map1.get("effectResidentialside").toString()));
                    dataRow.setEffectEnergyStorage(Integer.valueOf(ObjectUtils.isEmpty(map1.get("effectEnergyStorage")) ? "0" : map1.get("effectEnergyStorage").toString()));
                }
            }
        }


        page.setRecords(dataRows);
        return page;
    }


    // 返回城市名
    public String getCityName(Long cityCode) {
        if (cityCode == null) {
            return "";
        }
        String cityName = null;
        // 根据省级code查询所有城市
        List<Region> cityRows = systemClient.queryAll();
        cityRows = cityRows.stream().filter(n -> n.getId().equals(cityCode.toString())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cityRows)) {
            return null;
        } else {
            return cityRows.get(0).getName();
        }
    }


    /**
     * 事件效果评估 次日用户明细
     */
    @Override
    public IPage<DrEventInvitationEffectEvalDetail> getEffectUsersDetail(long current, long size, Long eventId) {
        Page<DrEventInvitationEffectEvalDetail> page = new Page<>(current, size);
        page.setOptimizeCountSql(false);
        IPage<DrEventInvitationEffectEvalDetail> drEventUserPage = drEventInvitationMapper.getEffectUsersDetail(page, eventId);
        return drEventUserPage;
    }

    /**
     * 事件效果评估 当日用户明细
     */
    @Override
    public IPage<DrEventInvitationEffectEvalDetail> getEffectUsersDetailImmediate(long current, long size, Long eventId) {
        Page<DrEventInvitationEffectEvalDetail> page = new Page<>(current, size);
        page.setOptimizeCountSql(false);
        IPage<DrEventInvitationEffectEvalDetail> drEventUserPage = drEventInvitationMapper.getEffectUsersDetailImmediate(page, eventId);
        return drEventUserPage;
    }

}
