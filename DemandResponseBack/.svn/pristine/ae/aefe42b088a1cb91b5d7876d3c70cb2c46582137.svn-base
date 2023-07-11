package com.xqxy.executor.service.jobhandler;

import cn.hutool.core.lang.Dict;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.dispatch.entity.OrgDemand;
import com.xqxy.dr.modular.dispatch.service.OrgDemandService;
import com.xqxy.dr.modular.event.utils.OrgUtils;
import com.xqxy.dr.modular.statistics.result.TotalStatisticsTableResult;
import com.xqxy.dr.modular.statistics.service.TotalStatisticsService;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description 数据总览统计开始
 * @ClassName EventJob
 * @Author lqr
 * @date 2021.05.11 14:35
 */
@Component
public class TotalStaticJob {
    private static final Log log = Log.get();


    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private SystemClientService systemClientService;

    @Resource
    private TotalStatisticsService totalStatisticsService;

    @Resource
    private OrgDemandService orgDemandService;


    //数据总览统计开始
    @XxlJob("totalStatic")
    public ReturnT<String> totalStatic(String param) throws Exception{
        XxlJobLogger.log("数据总览统计开始");
        this.saveTotalStatic(param);
        return ReturnT.SUCCESS;
    }

    public void saveTotalStatic(String param) {
        JSONObject data = new JSONObject();
        List<Map<String, Object>> records = new ArrayList<>();
        //获取市级组织机构
        List<SysOrgs> orgsList = null;
        List<SysOrgs> provinceList = null;
        JSONArray datas = null;
        List<SysOrgs> orgsListDate = new ArrayList<>();
        String provinceCode = null;
        try {
            DictTypeParam dictTypeParam3 = new DictTypeParam();
            dictTypeParam3.setCode("province_code");
            List<Dict> list3 = dictTypeService.dropDown(dictTypeParam3);
            Object value2 = null;
            if (null != list3 && list3.size() > 0) {
                for (Dict dict : list3) {
                    if ("anhui_org_code".equals(dict.get("code"))) {
                        value2 = dict.get("value");
                    }
                }
                if (null != value2) {
                    provinceCode = (String) value2;
                }
            } else {
                provinceCode = "34101";
                log.error("未配置省级供电单位编码!");
            }
            //获取所有组织机构
            JSONObject result = systemClientService.queryAllOrg();
            if ("000000".equals(result.getString("code"))) {
                datas = result.getJSONArray("data");
                if (null != datas && datas.size() > 0) {
                    for (Object object : datas) {
                        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);
                        SysOrgs sysOrgs = JSONObjectToEntityUtils.JSONObjectToSysOrg(jsonObject);
                        orgsListDate.add(sysOrgs);
                    }
                } else {
                    log.warn("组织机构为空");
                    return;
                }
                String province = provinceCode;
                orgsList = orgsListDate.stream().filter(n -> "2".equals(n.getOrgTitle())).collect(Collectors.toList());
                provinceList = orgsListDate.stream().filter(n -> province.equals(n.getId())).collect(Collectors.toList());
            } else {
                log.warn("组织机构不存在");
                return;
            }
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            String yearStr = String.valueOf(year);
            LambdaQueryWrapper<TotalStatisticsTableResult> tableResultLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tableResultLambdaQueryWrapper.eq(TotalStatisticsTableResult::getYear,year);
            List<TotalStatisticsTableResult> totalStatisticsTableResults = totalStatisticsService.list(tableResultLambdaQueryWrapper);
            List<TotalStatisticsTableResult> updateList = new ArrayList<>();
            List<TotalStatisticsTableResult> insertList = new ArrayList<>();
            //查询省级数据
            if (null != provinceList && provinceList.size() > 0) {
                String name = provinceList.get(0).getName();
                List<TotalStatisticsTableResult> tableResults = totalStatisticsService.getExecuteData(null);
                if(null!=tableResults && tableResults.size()>0) {
                    for(TotalStatisticsTableResult res : tableResults) {
                        res.setOrgId(provinceList.get(0).getId());
                        res.setOrgName(name);
                        List<TotalStatisticsTableResult> tableResults1 = totalStatisticsTableResults.
                                stream().filter(n -> yearStr.equals(n.getYear())
                                && res.getProjectId().equals(n.getProjectId())
                                && res.getOrgId().equals(n.getOrgId())
                        ).collect(Collectors.toList());
                        if(null!=tableResults1 && tableResults1.size()>0) {
                            res.setId(tableResults1.get(0).getId());
                            updateList.add(res);
                        } else {
                            insertList.add(res);
                        }
                    }
                }
            }
            //查询市级数据
            if (null != orgsList && orgsList.size() > 0) {
                OrgUtils orgUtils = new OrgUtils();
                for (SysOrgs org : orgsList) {
                    String name = "";
                    List<String> orgs = orgUtils.getData(datas, org.getId(), new ArrayList<>());
                    List<SysOrgs> single = orgsListDate.stream().filter(n -> org.getId().equals(n.getId())).collect(Collectors.toList());
                    if (null != single && single.size() > 0) {
                        name = single.get(0).getName();
                    }
                    List<TotalStatisticsTableResult> tableResults = totalStatisticsService.getExecuteDataCity(orgs);
                    if(null!=tableResults && tableResults.size()>0) {
                        for(TotalStatisticsTableResult res : tableResults) {
                            res.setOrgId(org.getId());
                            res.setOrgName(name);
                            List<TotalStatisticsTableResult> tableResults1 = totalStatisticsTableResults.
                                    stream().filter(n -> yearStr.equals(n.getYear())
                                    && res.getProjectId().equals(n.getProjectId())
                                    && res.getOrgId().equals(n.getOrgId())
                            ).collect(Collectors.toList());
                            if(null!=tableResults1 && tableResults1.size()>0) {
                                res.setId(tableResults1.get(0).getId());
                                updateList.add(res);
                            } else {
                                insertList.add(res);
                            }
                        }
                    }

                }
            }
            //保存数据
            if(null!=updateList && updateList.size()>0) {
                totalStatisticsService.updateBatchById(updateList);
            }
            log.info("更新统计数据成功，共："+updateList.size() + "条");

            if(null!=insertList && insertList.size()>0) {
                totalStatisticsService.saveBatch(insertList);
            }
            log.info("新增统计数据成功，共："+insertList.size() + "条");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
