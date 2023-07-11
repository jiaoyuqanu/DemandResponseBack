package com.xqxy.sys.modular.cust.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.annotion.NeedSetValueField;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.enums.CurrentRegionEnum;
import com.xqxy.core.enums.DrSysDictDataEnum;
import com.xqxy.core.enums.RegionLevelEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.adjustable.VO.WorkCityYearAdjustVo;
import com.xqxy.dr.modular.adjustable.mapper.DrConsAdjustablePotentialMapper;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.data.enums.ConsCurveExceptionEnum;
import com.xqxy.dr.modular.data.param.ConsAndDate;
import com.xqxy.dr.modular.data.param.WorkUserParam;
import com.xqxy.dr.modular.data.result.WorkUserStatsResult;
import com.xqxy.dr.modular.device.entity.SysOrgs;
import com.xqxy.dr.modular.event.enums.CheckStatusEnum;
import com.xqxy.dr.modular.event.utils.OrgUtils;
import com.xqxy.dr.modular.project.entity.ConsContractInfo;
import com.xqxy.dr.modular.project.entity.Project;
import com.xqxy.dr.modular.project.enums.PlanExceptionEnum;
import com.xqxy.dr.modular.project.mapper.ProjectMapper;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.dr.modular.project.service.ConsContractInfoService;
import com.xqxy.dr.modular.strategy.DataAccessStrategy;
import com.xqxy.dr.modular.strategy.DataAccessStrategyContext;
import com.xqxy.eum.DataSourceEnum;
import com.xqxy.rentation.DsSwitcher;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.entity.ConsTopologyFile;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.entity.UserConsRela;
import com.xqxy.sys.modular.cust.enums.ConsExceptionEnum;
import com.xqxy.sys.modular.cust.enums.IsAggregatorEnum;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.cust.mapper.ConsMapper;
import com.xqxy.sys.modular.cust.mapper.CustMapper;
import com.xqxy.sys.modular.cust.param.ConsParam;
import com.xqxy.sys.modular.cust.param.CustParam;
import com.xqxy.sys.modular.cust.result.ConsMonitor;
import com.xqxy.sys.modular.cust.result.StatisticsByTypeResult;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.cust.service.UserConsRelaService;
import com.xqxy.sys.modular.dict.entity.DictData;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import com.xqxy.sys.modular.utils.CityAndCountyUtils;
import com.xqxy.sys.modular.utils.JSONObjectToEntityUtils;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsServiceImpl extends ServiceImpl<ConsMapper, Cons> implements ConsService {

    private static final Log log = Log.get();

    @Resource
    CustService custService;
    @Resource
    private ConsMapper consMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private UserConsRelaService userConsRelaService;

    @Resource
    private DataAccessStrategyContext dataAccessStrategyContext;

    @Value("${dataAccessStrategy}")
    private String dataStrategy;

    //独立用户档案策略配置
    @Value("${consDataAccessStrategy}")
    private String consDataAccessStrategy;

    @Resource
    private SystemClient systemClient;

    @Resource
    private SystemClientService systemClientService;

    @Resource
    private DrConsAdjustablePotentialMapper drConsAdjustablePotentialMapper;
    @Resource
    private CustMapper custMapper;

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private ConsTopologyFileServiceImpl consTopologyFileService;

    @Autowired
    private ConsContractInfoService consContractInfoService;

    @NeedSetValueField
    @Override
    @DsSwitcher(DataSourceEnum.PG)
    public Page<Cons> page(ConsParam consParam) {
        LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();

        // 添加数据权限
        List<String> orgIds = OrganizationUtil.getAllOrgByOrgNo();
        queryWrapper.in(Cons::getOrgNo, orgIds);

        if (ObjectUtil.isNotNull(queryWrapper)) {
            // 根据省
            if (ObjectUtil.isNotEmpty(consParam.getProvinceCode())) {
                queryWrapper.eq(Cons::getProvinceCode, consParam.getProvinceCode());
            }
            // 根据市
            if (ObjectUtil.isNotEmpty(consParam.getCityCode())) {
                queryWrapper.eq(Cons::getCityCode, consParam.getCityCode());
            }
            // 根据区
            if (ObjectUtil.isNotEmpty(consParam.getCountyCode())) {
                queryWrapper.eq(Cons::getCountyCode, consParam.getCountyCode());
            }
            // 根据名称
            if (ObjectUtil.isNotEmpty(consParam.getConsName())) {
                queryWrapper.like(Cons::getConsName, consParam.getConsName());
            }
//            if (ObjectUtil.isNotEmpty(consParam.getIntegrator()) && consParam.getIntegrator().toString().equals(IsAggregatorEnum.NOT_AGGREGATOR.getCode())) {
//                // 根据户号
            if (ObjectUtil.isNotEmpty(consParam.getConsNo())) {
                queryWrapper.like(Cons::getId, consParam.getConsNo());
//                }
            }
            if (ObjectUtil.isNotEmpty(consParam.getFirstContactInfo())) {
                queryWrapper.like(Cons::getFirstContactInfo, consParam.getFirstContactInfo());

            }
            //供电单位名称
            if (ObjectUtil.isNotEmpty(consParam.getOrgName())) {
                queryWrapper.like(Cons::getOrgName, consParam.getOrgName());
            }
            if (ObjectUtil.isNotEmpty(consParam.getOrgCode())) {
                queryWrapper.like(Cons::getOrgNo, consParam.getOrgCode());
            }
        }
        // 根据创建时间排序
//        queryWrapper.orderByDesc(Cust::getCreateTime);
        Page<Cons> objectPage = consParam.getPage();
        objectPage.setCurrent(consParam.getCurrent());
        objectPage.setSize(consParam.getSize());
        return this.page(objectPage, queryWrapper);

    }


    @Override
    public List<Cons> list(ConsParam consParam) {
        LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(queryWrapper)) {
            // 根据省
            if (ObjectUtil.isNotEmpty(consParam.getProvinceCode())) {
                queryWrapper.eq(Cons::getProvinceCode, consParam.getProvinceCode());
            }
            // 根据市
            if (ObjectUtil.isNotEmpty(consParam.getCityCode())) {
                queryWrapper.eq(Cons::getCityCode, consParam.getCityCode());
            }
            // 根据区
            if (ObjectUtil.isNotEmpty(consParam.getCountyCode())) {
                queryWrapper.eq(Cons::getCountyCode, consParam.getCountyCode());
            }
            // 根据名称
            if (ObjectUtil.isNotEmpty(consParam.getConsName())) {
                queryWrapper.like(Cons::getConsName, consParam.getConsName());
            }
            if (ObjectUtil.isNotEmpty(consParam.getConsNo())) {
                queryWrapper.like(Cons::getId, consParam.getConsNo());
            }
            //供电单位名称
            if (ObjectUtil.isNotEmpty(consParam.getOrgName())) {
                queryWrapper.like(Cons::getOrgName, consParam.getOrgName());
            }
            if (ObjectUtil.isNotEmpty(consParam.getOrgCode())) {
                queryWrapper.like(Cons::getOrgNo, consParam.getOrgCode());
            }
            if (ObjectUtil.isNotEmpty(consParam.getFirstContactInfo())) {
                queryWrapper.like(Cons::getFirstContactInfo, consParam.getFirstContactInfo());

            }
        }
        // 根据创建时间排序
        queryWrapper.orderByDesc(Cons::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public Cons detail(String consId) {
        Cons cons = this.getById(consId);
        if (ObjectUtil.isNotNull(cons)) {
            return cons;
        } else {
            throw new ServiceException(ConsExceptionEnum.CONS_NOT_EXIST);
        }
    }

    @Override
    public void edit(Cons cons) {
        if (ObjectUtil.isNotEmpty(cons.getId())) {
            this.updateById(cons);
        }
    }

    @Override
    public Page<Cons> pageConsList(ConsParam consParam) {
        String custId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId();
        Cust cust = custService.getById(custId);
        if (ObjectUtil.isEmpty(cust)) {
            throw new ServiceException(ConsExceptionEnum.CUST_NOT_EXIST);
        }
        Page<Cons> consPage;
        if (IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            // 客户为集成商
            consPage = consMapper.pageProxyConsList(consParam.getPage(), consParam);
        } else {
            // 客户为电力用户
            LambdaQueryWrapper<Cons> lambdaQueryWrapper = Wrappers.lambdaQuery();
            lambdaQueryWrapper.eq(Cons::getCustId, custId);
            if (!StringUtil.isNullOrEmpty(consParam.getId())) {
                lambdaQueryWrapper.like(Cons::getId, consParam.getId());
            }
            if (!StringUtil.isNullOrEmpty(consParam.getConsName())) {
                lambdaQueryWrapper.like(Cons::getConsName, consParam.getConsName());
            }
            lambdaQueryWrapper.orderByAsc(Cons::getCreateTime);
            consPage = page(new Page<>(consParam.getCurrent(), consParam.getSize()), lambdaQueryWrapper);
        }

        List<Cons> list = consPage.getRecords();
        //查询用户对应的 用户拓扑图
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> consIds = list.stream().map(Cons::getId).collect(Collectors.toList());
            LambdaQueryWrapper<ConsTopologyFile> lambdaQuryWrapper = new LambdaQueryWrapper<>();
            lambdaQuryWrapper.in(ConsTopologyFile::getConsId, consIds);

            List<ConsTopologyFile> consTopologyFiles = consTopologyFileService.list(lambdaQuryWrapper);
            if (CollectionUtils.isNotEmpty(consTopologyFiles)) {
                // 把拓扑图集合分别塞给对应用户
                Map<String, List<ConsTopologyFile>> map = consTopologyFiles.stream()
                        .collect(Collectors.groupingBy(ConsTopologyFile::getConsId));
                for (Cons cons1 : list) {
                    List<ConsTopologyFile> topologyFiles = map.get(cons1.getId());
                    cons1.setConsTopologyFileList(topologyFiles);
                }
            }
        }
        consPage.setRecords(list);

        return consPage;
    }

    @Override
    public Page<Project> listDeclareDetail(ConsParam consParam) {
        return projectMapper.listProjectByConsId(consParam.getPage(), consParam.getConsId());
    }

    @Override
    public void updateFirstContact(Cons cons) {
        if (ObjectUtil.isNotEmpty(cons.getId())) {
            this.updateById(cons);
        }
    }

    @Override
    public void add(Cons cons) {
        if (StringUtil.isNullOrEmpty(cons.getId())) {
            throw new ServiceException(-1, "请输入所要添加的电力户号");
        }
        String custId = ObjectUtil.isNull(cons.getCustId()) ? Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId() : cons.getCustId().toString();
        Cust cust = custService.getById(custId);
        if (ObjectUtil.isNull(cust) || ObjectUtil.isNull(cust.getIntegrator())) {
            throw new ServiceException(-1, "客户无法判断为集成商或电力用户");
        }

        //查询省 市 区/县 乡/镇/街道 ,塞入Cons对象
        if (IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            // 判断户号是否被集成商代理
            int count = userConsRelaService.count(Wrappers.<UserConsRela>lambdaQuery()
                    .eq(UserConsRela::getConsNo, cons.getId()));
            if (count != 0) {
                throw new ServiceException(ConsExceptionEnum.CONS_REPEAT);
            } else {
                Cons marketCons = null;
                try {
                    // 从营销档案获取用户数据,使用模拟接口
                    DataAccessStrategy dataAccessStrategy = dataAccessStrategyContext.strategySelect(consDataAccessStrategy);
                    marketCons = dataAccessStrategy.getConsFromMarketing(cons.getId(), null, null);
                } catch (Exception e) {
                    log.error("调取营销档案接口失败，无法查询该户号信息！", e);
                }
                if (ObjectUtil.isNull(marketCons)) {
                    throw new ServiceException(500, "用户户号不存在，请重新输入");
                }
                if (ObjectUtil.isNull(marketCons)) {
                    throw new ServiceException(500, "用户户号不存在，请重新输入");
                } else {
                    if(StringUtils.isEmpty(marketCons.getId())){
                        throw new ServiceException(500,"用户户号不存在，请重新输入");
                    }
                    Cons copyCons = new Cons();
                    BeanUtils.copyProperties(marketCons, copyCons);
                    copyCons.setProvinceOrgNo(cons.getProvinceOrgNo());
                    copyCons.setCityOrgNo(cons.getCityOrgNo());
                    copyCons.setAreaOrgNo(cons.getAreaOrgNo());
                    copyCons.setStreetOrgNo(cons.getStreetOrgNo());
                    copyCons.setConsName(cons.getConsName());
                    copyCons.setOrgName(cons.getOrgName());
                    copyCons.setOrgNo(cons.getOrgNo());
                    if (ObjectUtil.isNotNull(cons.getVoltCode())) {
                        // 前端暂时没提供
                        copyCons.setVoltCode(cons.getVoltCode());
                    }
                    if (ObjectUtil.isNotNull(cons.getProvinceCode())) {
                        copyCons.setProvinceCode(cons.getProvinceCode());
                    }
                    if (ObjectUtil.isNotNull(cons.getCityCode())) {
                        copyCons.setCityCode(cons.getCityCode());
                    }
                    if (ObjectUtil.isNotNull(cons.getCountyCode())) {
                        copyCons.setCountyCode(cons.getCountyCode());
                    }
                    if (ObjectUtil.isNotNull(cons.getSafetyLoad())) {
                        copyCons.setSafetyLoad(cons.getSafetyLoad());
                    }
                    if (ObjectUtil.isNotNull(cons.getBigTradeCode())) {
                        copyCons.setBigTradeCode(cons.getBigTradeCode());
                    }
                    if (ObjectUtil.isNotNull(cons.getSecondContactName())) {
                        copyCons.setSecondContactName(cons.getSecondContactName());
                    }
                    if (ObjectUtil.isNotNull(cons.getSecondContactInifo())) {
                        copyCons.setSecondContactInifo(cons.getSecondContactInifo());
                    }
                    copyCons.setElecAddr(cons.getElecAddr());
                    copyCons.setContractCap(cons.getContractCap());
                    copyCons.setRunCap(cons.getRunCap());
                    copyCons.setState(cons.getState());
                    copyCons.setFirstContactName(cons.getFirstContactName());
                    copyCons.setFirstContactInfo(cons.getFirstContactInfo());
                    copyCons.setId(cons.getId());
                    //代理用户的关系不在dr_cons表，如果有则强制取消
                    copyCons.setCustId(null);
                    save(copyCons);
                }
                UserConsRela userConsRela = new UserConsRela();
                userConsRela.setCustId(Long.parseLong(custId));
                // 关系表填电力户号
                userConsRela.setConsNo(cons.getId());
                // 关系都为代理关系
                userConsRela.setRelaType("2");
                userConsRelaService.save(userConsRela);
            }
        } else {
            Cons marketCons = null;
            try {
                // 从营销档案获取用户数据,使用模拟接口
                DataAccessStrategy dataAccessStrategy = dataAccessStrategyContext.strategySelect(consDataAccessStrategy);
                marketCons = dataAccessStrategy.getConsFromMarketing(cons.getId(), null, null);
            } catch (Exception e) {
                log.error("调取营销档案接口失败，无法查询该户号信息！", e);
            }
            if (ObjectUtil.isNull(marketCons)) {
                throw new ServiceException(500, "用户户号不存在，请重新输入");
            } else {
                Cons copyCons = new Cons();
                BeanUtils.copyProperties(marketCons, copyCons);
                copyCons.setProvinceOrgNo(cons.getProvinceOrgNo());
                copyCons.setCityOrgNo(cons.getCityOrgNo());
                copyCons.setAreaOrgNo(cons.getAreaOrgNo());
                copyCons.setStreetOrgNo(cons.getStreetOrgNo());
                copyCons.setConsName(cons.getConsName());
                copyCons.setOrgName(cons.getOrgName());
                copyCons.setOrgNo(cons.getOrgNo());
                if (ObjectUtil.isNotNull(cons.getVoltCode())) {
                    // 前端暂时没提供
                    copyCons.setVoltCode(cons.getVoltCode());
                }
                if (ObjectUtil.isNotNull(cons.getProvinceCode())) {
                    copyCons.setProvinceCode(cons.getProvinceCode());
                }
                if (ObjectUtil.isNotNull(cons.getCityCode())) {
                    copyCons.setCityCode(cons.getCityCode());
                }
                if (ObjectUtil.isNotNull(cons.getCountyCode())) {
                    copyCons.setCountyCode(cons.getCountyCode());
                }
                if (ObjectUtil.isNotNull(cons.getSafetyLoad())) {
                    copyCons.setSafetyLoad(cons.getSafetyLoad());
                }
                if (ObjectUtil.isNotNull(cons.getBigTradeCode())) {
                    copyCons.setBigTradeCode(cons.getBigTradeCode());
                }
                if (ObjectUtil.isNotNull(cons.getSecondContactName())) {
                    copyCons.setSecondContactName(cons.getSecondContactName());
                }
                if (ObjectUtil.isNotNull(cons.getSecondContactInifo())) {
                    copyCons.setSecondContactInifo(cons.getSecondContactInifo());
                }
                copyCons.setElecAddr(cons.getElecAddr());
                copyCons.setContractCap(cons.getContractCap());
                copyCons.setRunCap(cons.getRunCap());
                copyCons.setState(cons.getState());
                copyCons.setFirstContactName(cons.getFirstContactName());
                copyCons.setFirstContactInfo(cons.getFirstContactInfo());
                copyCons.setId(cons.getId());
                copyCons.setCustId(Long.parseLong(custId));
                save(copyCons);
            }
        }
    }

    @Override
    public void deleteCons(ConsParam consParam) {
        if (StringUtil.isNullOrEmpty(consParam.getId())) {
            throw new ServiceException(-1, "前端请传用户户号(id)");
        }

        //查询该用户的签约信息
        List<ConsContractInfo> consContractInfos = consContractInfoService.list(Wrappers.<ConsContractInfo>lambdaQuery().eq(ConsContractInfo::getConsId, consParam.getId()));
        if(CollectionUtils.isNotEmpty(consContractInfos)){
           throw new ServiceException(500,"该用户已签约，删除失败");
        }

        LambdaQueryWrapper<UserConsRela> lambdaQueryWrapper = Wrappers.<UserConsRela>lambdaQuery()
                .eq(UserConsRela::getConsNo, consParam.getId());
        userConsRelaService.remove(lambdaQueryWrapper);
        removeById(consParam.getId());
    }

    @Override
    public List<Map<String, Object>> consByCustList() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        Long custId = Long.parseLong(currentUserInfo.getId());
        Cust cust = custService.getById(currentUserInfo.getId());
        if (ObjectUtil.isNull(cust)) {
            throw new ServiceException(ConsExceptionEnum.CONS_NOT_EXIST);
        }
        // 非集成商
        if (cust.getIntegrator().equals(IsAggregatorEnum.NOT_AGGREGATOR.getCode())) {
            LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Cons::getCustId, custId);
            List<Cons> list = this.list(queryWrapper);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("consType", "2");
                map.put("custId", custId);
                map.put("consId", list.get(i).getId());
                map.put("consName", list.get(i).getConsName());
                mapList.add(map);
            }
            return mapList;
        }
        // 集成商
        if (cust.getIntegrator().equals(IsAggregatorEnum.AGGREGATOR.getCode())) {
            List<Map<String, Object>> UserConsRelalists = consMapper.getConsOrCust();
//            LambdaQueryWrapper<UserConsRela> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(UserConsRela::getCustId,custId);
//            List<UserConsRela> UserConsRelalists = userConsRelaService.list(queryWrapper);
            for (int i = 0; i < UserConsRelalists.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("consType", "1");
                map.put("custId", custId);
                map.put("consId", UserConsRelalists.get(i).get("consNo"));
                map.put("consName", UserConsRelalists.get(i).get("consName"));
                mapList.add(map);
            }
            return mapList;
        }
        return null;
    }

    /**
     * 根据账号获取户号
     *
     * @return
     */
    @Override
    public List<String> getConsIdByCust() {
        List<String> consIdlist = new ArrayList<>();
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        Long custId = Long.parseLong(currentUserInfo.getId());
        Cust cust = custService.getById(currentUserInfo.getId());
        if (ObjectUtil.isNull(cust)) {
            return null;
        }
        // 非集成商
        if (IsAggregatorEnum.NOT_AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Cons::getCustId, custId);
            List<Cons> list = this.list(queryWrapper);
            if (list.size() > 0) {
                consIdlist = list.stream().map(Cons::getId).collect(Collectors.toList());
                return consIdlist;
            } else {
//                throw new ServiceException(ConsExceptionEnum.CONS_NOT_EXIST);
            }

        }
        // 集成商
        if (IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            LambdaQueryWrapper<UserConsRela> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserConsRela::getCustId, custId);
            List<UserConsRela> UserConsRelalists = userConsRelaService.list(queryWrapper);
            if (UserConsRelalists.size() > 0) {
                consIdlist = UserConsRelalists.stream().map(UserConsRela::getConsNo).collect(Collectors.toList());
                return consIdlist;
            } else {
//                throw new ServiceException(ConsExceptionEnum.PROXY_USER_NOT_EXIST);
            }
        }
        return null;
    }

    /**
     * 根据账号获取户号和用户名
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getConsIdAndNameByCust() {
        List<Map<String, Object>> consIdlist = new ArrayList<>();
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        Long custId = Long.parseLong(currentUserInfo.getId());
        Cust cust = custService.getById(currentUserInfo.getId());
        if (ObjectUtil.isNull(cust)) {
            return null;
        }
        // 非集成商
        if (IsAggregatorEnum.NOT_AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Cons::getCustId, custId);
            List<Cons> list = this.list(queryWrapper);
            if (null != list && list.size() > 0) {
                for (Cons cons : list) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("consId", cons.getId());
                    map.put("consName", cons.getConsName());
                    consIdlist.add(map);
                }
                return consIdlist;
            } else {
//                throw new ServiceException(ConsExceptionEnum.CONS_NOT_EXIST);
            }

        }
        // 集成商
        if (IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            List<UserConsRela> UserConsRelalists = userConsRelaService.getUserConsByCustId(custId);
            if (null != UserConsRelalists && UserConsRelalists.size() > 0) {
                for (UserConsRela cons : UserConsRelalists) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("consId", cons.getConsNo());
                    map.put("consName", cons.getConsName());
                    consIdlist.add(map);
                }
                return consIdlist;
            } else {
//                throw new ServiceException(ConsExceptionEnum.PROXY_USER_NOT_EXIST);
            }
        }
        return null;
    }


    /**
     * 根据账号获取户号
     *
     * @return
     */
    @Override
    public List<String> getConsIdListByCust(Long custId) {
        List<String> consIdlist = new ArrayList<>();

        if (custId == null) {
            custId = Long.valueOf(Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId());
        }


        Cust cust = custService.getById(custId);
        if (ObjectUtil.isNull(cust)) {
            throw new ServiceException(ConsExceptionEnum.CUST_NOT_EXIST);
        }
        // 非集成商
        if (IsAggregatorEnum.NOT_AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Cons::getCustId, custId);
            List<Cons> list = this.list(queryWrapper);
            if (list.size() > 0) {
                consIdlist = list.stream().map(Cons::getId).collect(Collectors.toList());
                return consIdlist;
            } else {
                throw new ServiceException(ConsExceptionEnum.CONS_NOT_EXIST);
            }

        }
        // 集成商
        if (IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            LambdaQueryWrapper<UserConsRela> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserConsRela::getCustId, custId);
            List<UserConsRela> UserConsRelalists = userConsRelaService.list(queryWrapper);
            if (UserConsRelalists.size() > 0) {
                consIdlist = UserConsRelalists.stream().map(UserConsRela::getConsNo).collect(Collectors.toList());
                return consIdlist;
            } else {
                throw new ServiceException(ConsExceptionEnum.PROXY_USER_NOT_EXIST);
            }
        }
        return null;
    }

    @Override
    public Page<Cons> getHistoricalCurve(ConsAndDate consAndDate) {
        // 添加数据权限
        List<String> orgIds = OrganizationUtil.getAllOrgByOrgNo();

        LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Cons::getOrgNo, orgIds);
        if (ObjectUtil.isNotNull(consAndDate)) {
            // 根据区域筛选用户
            if (ObjectUtil.isNotEmpty(consAndDate.getRegionLevel()) && ObjectUtil.isNotEmpty(consAndDate.getRegionCode())) {
                if (String.valueOf(RegionLevelEnum.LEVEL_ONE.getCode()).equals(consAndDate.getRegionLevel())) {
                    queryWrapper.eq(Cons::getProvinceCode, consAndDate.getRegionCode());
                }
                if (String.valueOf(RegionLevelEnum.LEVEL_TWO.getCode()).equals(consAndDate.getRegionLevel())) {
                    queryWrapper.eq(Cons::getCityCode, consAndDate.getRegionCode());
                }
                if (String.valueOf(RegionLevelEnum.LEVEL_THREE.getCode()).equals(consAndDate.getRegionLevel())) {
                    queryWrapper.eq(Cons::getCountyCode, consAndDate.getRegionCode());
                }
            }
            // 根据用户编号模糊查询
            if (ObjectUtil.isNotEmpty(consAndDate.getConsNo())) {
                queryWrapper.like(Cons::getId, consAndDate.getConsNo());
            }
            // 根据用户名模糊查询
            if (ObjectUtil.isNotEmpty(consAndDate.getConsName())) {
                queryWrapper.like(Cons::getConsName, consAndDate.getConsName());
            }

        }
        Page<Cons> consPage = this.page(consAndDate.getPage(), queryWrapper);
        List<Cons> records = consPage.getRecords();
        if (records.isEmpty()) {
            return (consPage);
        }
        ConsCurve tempCurve = new ConsCurve();
        List<String> consIdList = records.stream().map(Cons::getId).collect(Collectors.toList());
        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataStrategy);
        List<ConsCurve> curveByConsIdAndDate = getDataStrategy.queryHistoryCurvePage(consIdList, consAndDate.getDataDate());
        if (CollectionUtil.isEmpty(curveByConsIdAndDate) || ObjectUtil.isNull(curveByConsIdAndDate)) {
            for (int i = 0; i < records.size(); i++) {
                Cons cons = records.get(i);
                tempCurve.setConsId(cons.getId());
                String ymd = consAndDate.getDataDate();
                LocalDate ofDate = LocalDate.of(Integer.parseInt(ymd.substring(0, 4)), Integer.parseInt(ymd.substring(5, 7)), Integer.parseInt(ymd.substring(8, 10)));
                tempCurve.setDataDate(ofDate);
                cons.setConsCurve(tempCurve);
            }
            return (consPage);
        }
        tempCurve.setConsId(null);
        tempCurve.setDataDate(null);
        // List<Cons> datas = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            Cons cons = records.get(i);
//            tempCurve.setConsId(cons.getId());
//            tempCurve.setDataDate(LocalDate.consAndDate.getDataDate());
            curveByConsIdAndDate.stream().forEach(consCurve -> {
                if (cons.getId().equals(consCurve.getConsId())) {
                    BeanUtils.copyProperties(consCurve, cons);
//                    cons.setConsCurve(consCurve);
                    int temp = 0;
                    for (int j = 1; j < 97; j++) {
                        if (ObjectUtil.isNotNull(ReflectUtil.getFieldValue(consCurve, "p" + j))) {
                            temp++;
                        }
                    }
                    cons.setGetPointNum(temp);
                    //datas.add(cons);
                }
            });
        }
        consPage.setRecords(records);
        return consPage;
    }

    @Override
    public List<Cons> getTrueTimeOrHistoryCurveList(ConsAndDate consAndDate) {
        List<Cons> consList = new ArrayList<>();
        LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
        //添加数据权限
        queryWrapper.in(Cons::getOrgNo, OrganizationUtil.getAllOrgByOrgNo());
        //根据区域筛选用户
        screenConsByRegion(consAndDate, queryWrapper);
        //根据custNo和CustName筛选用户
        screenConsByIdAndName(consAndDate, queryWrapper);
        //筛选出所有符合条件的用户(不分页)
        consList = this.list(queryWrapper);
        //根据id和date查询历史用户曲线列表
        List<ConsCurve> consCurveList = queryDataStrategyCons(consList, consAndDate);
        //条件查询历史用户曲线列表为空时，consCurve赋值当前用户id和当日日期，返回曲线为空的用户列表
        if (CollectionUtil.isEmpty(consCurveList) || ObjectUtil.isNull(consCurveList)) {
            String ymd = consAndDate.getDataDate();
            LocalDate ofDate = LocalDate.of(Integer.parseInt(ymd.substring(0, 4)), Integer.parseInt(ymd.substring(5, 7)), Integer.parseInt(ymd.substring(8, 10)));
            List<Cons> blankCurveConsList = consList.stream().filter(con -> {
                ConsCurve tempCurve = new ConsCurve();
                tempCurve.setDataDate(ofDate);
                tempCurve.setConsId(con.getId());
                con.setConsCurve(tempCurve);
                return true;
            }).collect(Collectors.toList());
            return blankCurveConsList;
        }

        //创建consId和consCurve(用户id和其曲线对象映射map，防止循环嵌套)
        Map<String, ConsCurve> consCurveMap = consCurveList.stream().collect(Collectors.toMap(consCurve -> consCurve.getConsId(), consCurve -> consCurve));

        //省市区机构树
        JSONObject orgsJson = systemClientService.queryAllOrg();
        CityAndCountyUtils cityAndCountyOrgUtils = new CityAndCountyUtils();
        //遍历cons用户列表，将consCurve赋值con,增加区县机构，并统计非空曲线节点个数
        consList.forEach(con -> {
            try {
                Map<String, Object> cityAndCountyOrgMap = cityAndCountyOrgUtils.cityAndCounty(con.getOrgNo(), orgsJson);
                ConsCurve consCurve = consCurveMap.get(con.getId());
                BeanUtils.copyProperties(consCurve, con);
                con.setGetPointNum(consCurve.countPointNum());
                con.setProvinceOrgNo((String)cityAndCountyOrgMap.get("province"));
                con.setCityOrgNo((String)cityAndCountyOrgMap.get("city"));
                con.setAreaOrgNo((String)cityAndCountyOrgMap.get("county"));
            } catch (Exception e) {
                log.error(e);
            }
        });
        return consList;
    }

    /**
     * 参数列表queryWrapper传的实际上是一个指向实参指向内存的指针拷贝(与实参对象本身不是同一个地址)，可借助形参修改实参内存内容，但其本质是值传递
     * @param consAndDate
     * @param queryWrapper
     */
    private void screenConsByRegion(ConsAndDate consAndDate,LambdaQueryWrapper<Cons> queryWrapper){
        if (ObjectUtil.isNotEmpty(consAndDate.getRegionLevel()) && ObjectUtil.isNotEmpty(consAndDate.getRegionCode())) {
            if (String.valueOf(RegionLevelEnum.LEVEL_ONE.getCode()).equals(consAndDate.getRegionLevel())) {
                queryWrapper.eq(Cons::getProvinceCode, consAndDate.getRegionCode());
            }
            if (String.valueOf(RegionLevelEnum.LEVEL_TWO.getCode()).equals(consAndDate.getRegionLevel())) {
                queryWrapper.eq(Cons::getCityCode, consAndDate.getRegionCode());
            }
            if (String.valueOf(RegionLevelEnum.LEVEL_THREE.getCode()).equals(consAndDate.getRegionLevel())) {
                queryWrapper.eq(Cons::getCountyCode, consAndDate.getRegionCode());
            }
        }
    }

    private void screenConsByIdAndName(ConsAndDate consAndDate,LambdaQueryWrapper<Cons> queryWrapper){
        // 根据用户编号模糊查询
        if (ObjectUtil.isNotEmpty(consAndDate.getConsNo())) {
            queryWrapper.like(Cons::getId, consAndDate.getConsNo());
        }
        // 根据用户名模糊查询
        if (ObjectUtil.isNotEmpty(consAndDate.getConsName())) {
            queryWrapper.like(Cons::getConsName, consAndDate.getConsName());
        }
    }

    private List<ConsCurve> queryDataStrategyCons(List<Cons> consList,ConsAndDate consAndDate){
        List<String> consIdList = consList.stream().map(Cons::getId).collect(Collectors.toList());
        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataStrategy);
        //如果传入查询数据日期是今天，则查询实时曲线，否则查询历史曲线
        if (consAndDate.isTodayData()){
            return getDataStrategy.queryTodayCurveList(consIdList, consAndDate.getDataDate());
        }
        return getDataStrategy.queryHistoryCurvePage(consIdList, consAndDate.getDataDate());
    }


    @Override
    public Page<Cons> getTodayConsCurve(ConsAndDate consAndDate) {
        // 添加数据权限
        List<String> orgIds = OrganizationUtil.getAllOrgByOrgNo();
        //昨天数据作为今天历史数据
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        consAndDate.setDataDate(format);
        LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Cons::getOrgNo, orgIds);
        if (ObjectUtil.isNotNull(consAndDate)) {
            // 根据用户编号模糊查询
            if (ObjectUtil.isNotEmpty(consAndDate.getConsNo())) {
                queryWrapper.like(Cons::getId, consAndDate.getConsNo());
            }
//            // 根据用户类型
//            if (ObjectUtil.isNotEmpty(consAndDate.getConsType()) && !ConsTypeEnum.AGGREGATOR_USER.equals(consAndDate.getConsType())) {
//                queryWrapper.eq(Cons::getConsType, consAndDate.getConsType());
//            }
            // 根据用户名称
            if (ObjectUtil.isNotEmpty(consAndDate.getConsName())) {
                queryWrapper.like(Cons::getConsName, consAndDate.getConsName());
            }
            // 根据区域筛选用户
            if (ObjectUtil.isNotEmpty(consAndDate.getRegionLevel()) && ObjectUtil.isNotEmpty(consAndDate.getRegionCode())) {
                if (String.valueOf(RegionLevelEnum.LEVEL_ONE.getCode()).equals(consAndDate.getRegionLevel())) {
                    queryWrapper.eq(Cons::getProvinceCode, consAndDate.getRegionCode());
                }
                if (String.valueOf(RegionLevelEnum.LEVEL_TWO.getCode()).equals(consAndDate.getRegionLevel())) {
                    queryWrapper.eq(Cons::getCityCode, consAndDate.getRegionCode());
                }
                if (String.valueOf(RegionLevelEnum.LEVEL_THREE.getCode()).equals(consAndDate.getRegionLevel())) {
                    queryWrapper.eq(Cons::getCountyCode, consAndDate.getRegionCode());
                }
            }
        }

        Page<Cons> consPage = this.page(consAndDate.getPage(), queryWrapper);
        List<Cons> records = consPage.getRecords();
        if (records.isEmpty()) {
            return (consPage);
        }
        String formatDate = consAndDate.getDataDate();
        // 营销户号集合
        List<String> consIdList = records.stream().map(Cons::getId).collect(Collectors.toList());
        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataStrategy);
        List<ConsCurve> consCurveList = getDataStrategy.queryTodayCurveList(consIdList, formatDate);
        if(!CollectionUtils.isEmpty(consCurveList)){
            for (int i = 0; i < records.size(); i++) {
                Cons cons = records.get(i);
//            tempCurve.setConsId(cons.getId());
//            tempCurve.setDataDate(LocalDate.now());
                consCurveList.stream().forEach(consCurve -> {
                    if (cons.getId().equals(consCurve.getConsId())) {
                        //System.out.println(cons);
                        BeanUtils.copyProperties(consCurve, cons);
                        //log.info("截取后  循环赋值给返回 结果" + cons);
                       // System.out.println(cons);
//                    cons.setConsCurve(consCurve);
                        int temp = 0;
                        for (int j = 1; j < 97; j++) {
                            if (ObjectUtil.isNotNull(ReflectUtil.getFieldValue(consCurve, "p" + j))) {
                                temp++;
                            }
                        }
                        cons.setGetPointNum(temp);
                    }
                });
            }
            consPage.setRecords(records);
        }
        return (consPage);
    }

    @Override
    public Page<Cons> energyMonitorList(ConsAndDate consAndDate) {
        LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consAndDate)) {
            // 根据用户编号模糊查询
            if (ObjectUtil.isNotEmpty(consAndDate.getConsNo())) {
                queryWrapper.like(Cons::getId, consAndDate.getConsNo());
            }
            // 根据用户名称模糊查询
            if (ObjectUtil.isNotEmpty(consAndDate.getConsName())) {
                queryWrapper.like(Cons::getConsName, consAndDate.getConsName());
            }
//            // 根据用户类型
//            if (ObjectUtil.isNotEmpty(consAndDate.getConsType()) && !ConsTypeEnum.AGGREGATOR_USER.equals(consAndDate.getConsType())) {
//                queryWrapper.eq(Cons::getConsType, consAndDate.getConsType());
//            }
//            queryWrapper.ne(Cons::getConsType, ConsTypeEnum.AGGREGATOR_USER.getCode());
            // 根据区域筛选用户
            if (ObjectUtil.isNotEmpty(consAndDate.getRegionLevel()) && ObjectUtil.isNotEmpty(consAndDate.getRegionCode())) {
                if (String.valueOf(RegionLevelEnum.LEVEL_ONE.getCode()).equals(consAndDate.getRegionLevel())) {
                    queryWrapper.eq(Cons::getProvinceCode, consAndDate.getRegionCode());
                }
                if (String.valueOf(RegionLevelEnum.LEVEL_TWO.getCode()).equals(consAndDate.getRegionLevel())) {
                    queryWrapper.eq(Cons::getCityCode, consAndDate.getRegionCode());
                }
                if (String.valueOf(RegionLevelEnum.LEVEL_THREE.getCode()).equals(consAndDate.getRegionLevel())) {
                    queryWrapper.eq(Cons::getCountyCode, consAndDate.getRegionCode());
                }
            }
        }
        Page<Cons> consPage = this.page(consAndDate.getPage(), queryWrapper);
        List<Cons> records = consPage.getRecords();
        if (records.isEmpty()) {
            return (consPage);
        }
        String formatDate = consAndDate.getDataDate();
        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataStrategy);
        List<String> consIdList = records.stream().map(Cons::getId).collect(Collectors.toList());
        List<ConsEnergyCurve> energyCurveList = getDataStrategy.queryDayLoadEnergyByConsNo(consIdList, formatDate);
        //List<Cons> datas = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            Cons cons = records.get(i);
            if (null != energyCurveList && energyCurveList.size() > 0) {
                energyCurveList.stream().forEach(consEnergyCurve -> {
                    if (cons.getId().equals(consEnergyCurve.getConsNo())) {
                        int temp = 0;
                        BeanUtils.copyProperties(consEnergyCurve, cons);
                        for (int j = 1; j < 97; j++) {
                            if (ObjectUtil.isNotNull(ReflectUtil.getFieldValue(consEnergyCurve, "e" + j))) {
                                temp++;
                            }
                        }
                        cons.setGetPointNum(temp);
                        //datas.add(cons);
                    }
                });
            }
        }
        consPage.setRecords(records);
        return consPage;
    }

    @Override
    public Cons proxyUserDetail(ConsParam consParam) {
        Cons cons = getById(consParam.getConsId());
        if (ObjectUtil.isNotNull(cons) && ObjectUtil.isNull(cons.getCustId())) {
            throw new ServiceException(-1, "该用户已被代理");
        } else if (ObjectUtil.isNotNull(cons)) {
            throw new ServiceException(-1, "直接用户无法被代理");
        }
        DataAccessStrategy dataAccessStrategy = dataAccessStrategyContext.strategySelect(consDataAccessStrategy);
        return dataAccessStrategy.getConsFromMarketing(consParam.getConsId(), null, null);
    }

    @Override
    public ConsCurve getConsCurveByConsAndDate(ConsAndDate consAndDate) {
        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataStrategy);
        List<String> consId = Arrays.asList(consAndDate.getConsNo());
        List<ConsCurve> curveByConsIdAndDate = getDataStrategy.queryHistoryCurvePage(consId, consAndDate.getDataDate());
        if (CollectionUtil.isEmpty(curveByConsIdAndDate)) {
            throw new ServiceException(ConsCurveExceptionEnum.CONS_CURVE_NOT_EXIST);
        }
        return curveByConsIdAndDate.get(0);
    }

    @Override
    public Page<ConsMonitor> consMonitor(ConsParam consParam) {
        // 添加数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        List<String> orgIds = OrganizationUtil.getAllOrgByOrgNo();
        consParam.setOrgIds(orgIds);

        Page<ConsMonitor> consMonitorPage = this.getBaseMapper().consMonitor(consParam.getPage(), consParam);
        List<ConsMonitor> consMonitorList = consMonitorPage.getRecords();
        for (ConsMonitor consMonitor : consMonitorList) {
            if (ObjectUtil.isNotNull(consMonitor.getContractCap()) && ObjectUtil.isNotEmpty(consMonitor.getContractCap())) {
                BigDecimal contractCap = consMonitor.getContractCap().setScale(2, BigDecimal.ROUND_HALF_EVEN);
                consMonitor.setContractCap(contractCap);
            }
            if (ObjectUtil.isNotNull(consMonitor.getSignontractCap()) && ObjectUtil.isNotEmpty(consMonitor.getSignontractCap())) {
                BigDecimal signontractCap = consMonitor.getSignontractCap().setScale(2, BigDecimal.ROUND_HALF_EVEN);
                consMonitor.setSignontractCap(signontractCap);
            }
            consMonitorPage.setRecords(consMonitorList);
        }
        return consMonitorPage;
    }

    @Override
    public List<StatisticsByTypeResult> statisticsByType() {
        List<DictData> dictDataList = dictTypeService.getDictDataByTypeCode(DrSysDictDataEnum.CURRENT_REGION.getCode());
        DictData regionInfo = dictDataList.stream()
                .filter(dictData -> CurrentRegionEnum.CURRENT_REGION_CODE.getCode().equals(dictData.getCode()))
                .findFirst()
                .orElse(null);
        StatisticsByTypeResult statisticsByTypeResult = new StatisticsByTypeResult();
        statisticsByTypeResult.setRegionCode(regionInfo == null ? "" : regionInfo.getValue());
        List<StatisticsByTypeResult> statisticsByTypeResultList = getBaseMapper().statisticsByType();
        statisticsByTypeResult.setIndustrialCount(statisticsByTypeResultList.stream()
                .mapToInt(StatisticsByTypeResult::getIndustrialCount)
                .sum());
        statisticsByTypeResult.setResidentCount(statisticsByTypeResultList.stream()
                .mapToInt(StatisticsByTypeResult::getResidentCount)
                .sum());
        statisticsByTypeResult.setBuildingCount(statisticsByTypeResultList.stream()
                .mapToInt(StatisticsByTypeResult::getBuildingCount)
                .sum());
        statisticsByTypeResult.setEmergingLoadUser(statisticsByTypeResultList.stream()
                .mapToInt(StatisticsByTypeResult::getEmergingLoadUser)
                .sum());
        statisticsByTypeResult.setAgriculCount(statisticsByTypeResultList.stream()
                .mapToInt(StatisticsByTypeResult::getAgriculCount)
                .sum());
        statisticsByTypeResult.setOtherCount(statisticsByTypeResultList.stream()
                .mapToInt(StatisticsByTypeResult::getOtherCount)
                .sum());

        statisticsByTypeResult.setTotalCount(statisticsByTypeResultList.stream()
                .mapToInt(StatisticsByTypeResult::getTotalCount)
                .sum());

        statisticsByTypeResultList.add(statisticsByTypeResult);

        //按照 区域码 排序
        if (CollectionUtils.isNotEmpty(statisticsByTypeResultList)) {
            statisticsByTypeResultList = statisticsByTypeResultList.stream().sorted((n1, n2) -> n1.getRegionCode().compareTo(n2.getRegionCode())).collect(Collectors.toList());
        }
        return statisticsByTypeResultList;
    }

    @Override
    public Page<Cons> statisticsByTypeDetail(ConsParam consParam) {
//        System.out.println(this.getBaseMapper().statisticsByTypeDetail(regionCode,regionLevel).);
        return this.getBaseMapper()
                .statisticsByTypeDetail(consParam.getPage(), consParam.getRegionCode(), consParam.getRegionLevel());

    }

    @Override
    public ResponseData delete(String id, Integer integrator) {
        if (IsAggregatorEnum.AGGREGATOR.getCode().toString().equals(integrator.toString())) {
            LambdaQueryWrapper<UserConsRela> lambdaQueryWrapper = Wrappers.<UserConsRela>lambdaQuery()
                    .eq(UserConsRela::getCustId, id);
            List<UserConsRela> consRelas = userConsRelaService.list(lambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(consRelas)) {
                return ResponseData.fail("500", "存在关联户号，不允许删除", null);
            }
        } else if (IsAggregatorEnum.NOT_AGGREGATOR.getCode().toString().equals(integrator.toString())) {
            // 删除电力用户绑定的用户
            List<Cons> cons = this.list(Wrappers.<Cons>lambdaQuery().eq(Cons::getCustId, id));
            if (!CollectionUtils.isEmpty(cons)) {
                return ResponseData.fail("500", "存在关联户号，不允许删除", null);
            }
        }
        // 删除客户
        custService.removeById(id);
        return ResponseData.success();
    }

    @Override
    public void addCons(Cons cons) {
        if (ObjectUtil.isNotNull(cons) && ObjectUtil.isNotEmpty(cons)) {
            Cons copyCons = new Cons();
            BeanUtils.copyProperties(cons, copyCons);
            this.save(copyCons);
        }
    }


    /**
     * 根据custId 获取对应的cons集合
     *
     * @return
     * @author lqr
     */
    @Override
    public List<Cons> getConsListByCust(Cons cons) {
        List<Cons> list = new ArrayList<>();
        Long custId = Long.valueOf(Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getId());


        Cust cust = custService.getById(custId);
        if (ObjectUtil.isNull(cust)) {
            throw new ServiceException(ConsExceptionEnum.CUST_NOT_EXIST);
        }
        // 非集成商
        if (IsAggregatorEnum.NOT_AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();

            if (!ObjectUtils.isEmpty(cons)) {
                if (!StringUtil.isNullOrEmpty(cons.getId())) {
                    queryWrapper.like(Cons::getId, cons.getId());
                }
                if (!StringUtil.isNullOrEmpty(cons.getOrgName())) {
                    queryWrapper.like(Cons::getOrgName, cons.getOrgName());
                }
                if (!StringUtil.isNullOrEmpty(cons.getConsName())) {
                    queryWrapper.like(Cons::getConsName, cons.getConsName());
                }
            }

            queryWrapper.eq(Cons::getCustId, custId);
            list = this.list(queryWrapper);

        }
        // 集成商
        if (IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            LambdaQueryWrapper<UserConsRela> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserConsRela::getCustId, custId);
            List<UserConsRela> UserConsRelalists = userConsRelaService.list(queryWrapper);

            if (UserConsRelalists.size() > 0) {
                List<String> consIdList = UserConsRelalists.stream()
                        .map(UserConsRela::getConsNo)
                        .collect(Collectors.toList());
                LambdaQueryWrapper<Cons> consLambdaQueryWrapper = new LambdaQueryWrapper<>();

                if (!ObjectUtils.isEmpty(cons)) {
                    if (!StringUtil.isNullOrEmpty(cons.getId())) {
                        consLambdaQueryWrapper.like(Cons::getId, cons.getId());
                    }
                    if (!StringUtil.isNullOrEmpty(cons.getOrgName())) {
                        consLambdaQueryWrapper.like(Cons::getOrgName, cons.getOrgName());
                    }
                    if (!StringUtil.isNullOrEmpty(cons.getConsName())) {
                        consLambdaQueryWrapper.like(Cons::getConsName, cons.getConsName());
                    }
                }

                consLambdaQueryWrapper.in(Cons::getId, consIdList);
                list = this.list(consLambdaQueryWrapper);
            }
        }

        return list;
    }

    @Override
    public List<String> listOrgByChildId(String orgNo) {
        JSONObject result = systemClientService.queryAllOrg();
        com.alibaba.fastjson.JSONArray data = null;
        if (null != result) {
            data = result.getJSONArray("data");
        } else {
            throw new ServiceException(PlanExceptionEnum.NO_ORG_INFO);
        }
        List<String> listPart = new ArrayList<>();
        if (null != data && data.size() > 0) {
            OrgUtils orgUtils = new OrgUtils();
            listPart = orgUtils.getData2(data, orgNo, new ArrayList<>());
            if (null != listPart && listPart.size() > 0) {
                Collections.reverse(listPart);
            }
        }
        return listPart;
    }

    @Override
    public Page<Cons> getConsPageByCustId(CustParam custParam) {
        Page<Cons> page = new Page<>();
        String custId = custParam.getCustId();
        Cust cust = custService.getById(custId);
        if (ObjectUtil.isNull(cust)) {
            throw new ServiceException(ConsExceptionEnum.CUST_NOT_EXIST);
        }
        // 非集成商
        if (IsAggregatorEnum.NOT_AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Cons::getCustId, custId);
            if (!ObjectUtils.isEmpty(custParam)) {
                if (!StringUtil.isNullOrEmpty(custParam.getId())) {
                    queryWrapper.like(Cons::getId, custParam.getId());
                }
                if (!StringUtil.isNullOrEmpty(custParam.getConsName())) {
                    queryWrapper.like(Cons::getConsName, custParam.getConsName());
                }
                if (!StringUtil.isNullOrEmpty(custParam.getProvinceCode())) {
                    queryWrapper.eq(Cons::getProvinceCode, custParam.getProvinceCode());
                }
                if (!StringUtil.isNullOrEmpty(custParam.getCityCode())) {
                    queryWrapper.eq(Cons::getCityCode, custParam.getCityCode());
                }
                if (!StringUtil.isNullOrEmpty(custParam.getCountyCode())) {
                    queryWrapper.eq(Cons::getCountyCode, custParam.getCountyCode());
                }
            }
            page = this.page(custParam.getPage(), queryWrapper);

        }
        // 集成商
        if (IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            LambdaQueryWrapper<UserConsRela> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserConsRela::getCustId, custId);
            List<UserConsRela> UserConsRelalists = userConsRelaService.list(queryWrapper);

            if (UserConsRelalists.size() > 0) {
                List<String> consIdList = UserConsRelalists.stream()
                        .map(UserConsRela::getConsNo)
                        .collect(Collectors.toList());
                LambdaQueryWrapper<Cons> consLambdaQueryWrapper = new LambdaQueryWrapper<>();
                consLambdaQueryWrapper.in(Cons::getId, consIdList);
                if (!ObjectUtils.isEmpty(custParam)) {
                    if (!StringUtil.isNullOrEmpty(custParam.getId())) {
                        consLambdaQueryWrapper.like(Cons::getId, custParam.getId());
                    }
                    if (!StringUtil.isNullOrEmpty(custParam.getConsName())) {
                        consLambdaQueryWrapper.like(Cons::getConsName, custParam.getConsName());
                    }
                    if (!StringUtil.isNullOrEmpty(custParam.getCityCode())) {
                        consLambdaQueryWrapper.eq(Cons::getCityCode, custParam.getCityCode());
                    }
                    if (!StringUtil.isNullOrEmpty(custParam.getCountyCode())) {
                        consLambdaQueryWrapper.eq(Cons::getCountyCode, custParam.getCountyCode());
                    }
                    if (!StringUtil.isNullOrEmpty(custParam.getProvinceCode())) {
                        consLambdaQueryWrapper.eq(Cons::getProvinceCode, custParam.getProvinceCode());
                    }
                    page = this.page(custParam.getPage(), consLambdaQueryWrapper);
                }
            }

        }

        List<Cons> list = page.getRecords();
        //查询用户对应的 用户拓扑图
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> consIds = list.stream().map(Cons::getId).collect(Collectors.toList());
            LambdaQueryWrapper<ConsTopologyFile> lambdaQuryWrapper = new LambdaQueryWrapper<>();
            lambdaQuryWrapper.in(ConsTopologyFile::getConsId, consIds);

            List<ConsTopologyFile> consTopologyFiles = consTopologyFileService.list(lambdaQuryWrapper);
            if (CollectionUtils.isNotEmpty(consTopologyFiles)) {
                // 把拓扑图集合分别塞给对应用户
                Map<String, List<ConsTopologyFile>> map = consTopologyFiles.stream()
                        .collect(Collectors.groupingBy(ConsTopologyFile::getConsId));
                for (Cons cons1 : list) {
                    List<ConsTopologyFile> topologyFiles = map.get(cons1.getId());
                    cons1.setConsTopologyFileList(topologyFiles);
                }
            }
        }
        page.setRecords(list);
        return page;

    }

    @Override
    public Page<Cons> getConsPageAll(CustParam custParam) {
        LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
        if(custParam != null){
            if(StringUtils.isNotEmpty(custParam.getId())){
                queryWrapper.like(Cons::getId,custParam.getId());
            }
            if(StringUtils.isNotEmpty(custParam.getConsName())){
                queryWrapper.like(Cons::getConsName,custParam.getConsName());
            }
        }
        Page<Cons> page = this.page(custParam.getPage(), queryWrapper);

        List<Cons> list = page.getRecords();
        //查询用户对应的 用户拓扑图
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> consIds = list.stream().map(Cons::getId).collect(Collectors.toList());
            LambdaQueryWrapper<ConsTopologyFile> lambdaQuryWrapper = new LambdaQueryWrapper<>();
            lambdaQuryWrapper.in(ConsTopologyFile::getConsId, consIds);

            List<ConsTopologyFile> consTopologyFiles = consTopologyFileService.list(lambdaQuryWrapper);
            if (CollectionUtils.isNotEmpty(consTopologyFiles)) {
                // 把拓扑图集合分别塞给对应用户
                Map<String, List<ConsTopologyFile>> map = consTopologyFiles.stream()
                        .collect(Collectors.groupingBy(ConsTopologyFile::getConsId));
                for (Cons cons1 : list) {
                    List<ConsTopologyFile> topologyFiles = map.get(cons1.getId());
                    cons1.setConsTopologyFileList(topologyFiles);
                }
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public List<Cons> getAllCons(CustParam custParam) {
        LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
        if(custParam != null){
            if(StringUtils.isNotEmpty(custParam.getId())){
                queryWrapper.like(Cons::getId,custParam.getId());
            }
            if(StringUtils.isNotEmpty(custParam.getConsName())){
                queryWrapper.like(Cons::getConsName,custParam.getConsName());
            }
        }

        //查询出所有的cons记录
        List<Cons> queryConsList = this.list(queryWrapper);
        //cons记录列表里的code->name替换(省、市、区、电源类型)
        List<Cons> consList = replaceConsListCode2Name(queryConsList);
        return consList;
    }

    @Override
    public List<Cons> getConsListByCustId(CustParam custParam) {
        List<Cons> queryConsList = new ArrayList<>();//记录列表
        String custId = custParam.getCustId();
        Cust cust = custService.getById(custId);
        if (ObjectUtil.isNull(cust)) {
            throw new ServiceException(ConsExceptionEnum.CUST_NOT_EXIST);
        }
        // 非集成商
        if (IsAggregatorEnum.NOT_AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            LambdaQueryWrapper<Cons> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Cons::getCustId, custId);
            if (!ObjectUtils.isEmpty(custParam)) {
                if (!StringUtil.isNullOrEmpty(custParam.getId())) {
                    queryWrapper.like(Cons::getId, custParam.getId());
                }
                if (!StringUtil.isNullOrEmpty(custParam.getConsName())) {
                    queryWrapper.like(Cons::getConsName, custParam.getConsName());
                }
                if (!StringUtil.isNullOrEmpty(custParam.getProvinceCode())) {
                    queryWrapper.eq(Cons::getProvinceCode, custParam.getProvinceCode());
                }
                if (!StringUtil.isNullOrEmpty(custParam.getCityCode())) {
                    queryWrapper.eq(Cons::getCityCode, custParam.getCityCode());
                }
                if (!StringUtil.isNullOrEmpty(custParam.getCountyCode())) {
                    queryWrapper.eq(Cons::getCountyCode, custParam.getCountyCode());
                }
            }
            queryConsList = this.list(queryWrapper);

        }
        // 集成商
        if (IsAggregatorEnum.AGGREGATOR.getCode().equals(cust.getIntegrator())) {
            LambdaQueryWrapper<UserConsRela> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserConsRela::getCustId, custId);
            List<UserConsRela> UserConsRelalists = userConsRelaService.list(queryWrapper);

            if (UserConsRelalists.size() > 0) {
                List<String> consIdList = UserConsRelalists.stream()
                        .map(UserConsRela::getConsNo)
                        .collect(Collectors.toList());
                LambdaQueryWrapper<Cons> consLambdaQueryWrapper = new LambdaQueryWrapper<>();
                consLambdaQueryWrapper.in(Cons::getId, consIdList);
                if (!ObjectUtils.isEmpty(custParam)) {
                    if (!StringUtil.isNullOrEmpty(custParam.getId())) {
                        consLambdaQueryWrapper.like(Cons::getId, custParam.getId());
                    }
                    if (!StringUtil.isNullOrEmpty(custParam.getConsName())) {
                        consLambdaQueryWrapper.like(Cons::getConsName, custParam.getConsName());
                    }
                    if (!StringUtil.isNullOrEmpty(custParam.getCityCode())) {
                        consLambdaQueryWrapper.eq(Cons::getCityCode, custParam.getCityCode());
                    }
                    if (!StringUtil.isNullOrEmpty(custParam.getCountyCode())) {
                        consLambdaQueryWrapper.eq(Cons::getCountyCode, custParam.getCountyCode());
                    }
                    if (!StringUtil.isNullOrEmpty(custParam.getProvinceCode())) {
                        consLambdaQueryWrapper.eq(Cons::getProvinceCode, custParam.getProvinceCode());
                    }
                    queryConsList = this.list(consLambdaQueryWrapper);
                }
            }
        }
        //cons记录列表里的code->name替换(省、市、区、电源类型)
        List<Cons> consList = replaceConsListCode2Name(queryConsList);
        return consList;
    }

    /**
     * cons列表 code-name转换方法
     * @param cons
     * @return
     */
    private List<Cons> replaceConsListCode2Name(List<Cons> cons){
        //电压等级唯一编码
        String VOLTCODE = "VOLTCODE";
        //电压等级字典key-value对应关系map
        Map<String, String> voltCodeAndNameDicMap = getCodeAndNameDicMap(VOLTCODE);
        //行业类别唯一编码
        String HYFL_TRANS = "BIG_CLASS_NAME";
        //行业类别字典key-value对应关系map
        Map<String, String> tradeCodeAndNameDicMap = getCodeAndNameDicMap(HYFL_TRANS);
        //电源类型唯一编码
        String type_code = "type_code";
        //电源类型字典key-vaule对应关系map
        Map<String, String> typeCodeAndNameDicMap = getCodeAndNameDicMap(type_code);
        //省市区code-name对应关系map
        Map<String, String> orgNoAndOrgNameMap = getOrgNoAndOrgNameMap();
        //省市区机构树
        JSONObject orgsJson = systemClientService.queryAllOrg();
        CityAndCountyUtils cityAndCountyOrgUtils = new CityAndCountyUtils();
        //consList code值替换
        List<Cons> consList = cons.stream().filter(con -> {
            try {
                Map<String, Object> cityAndCountyOrgMap = cityAndCountyOrgUtils.cityAndCounty(con.getOrgNo(), orgsJson);
                con.setProvinceOrgNo((String)cityAndCountyOrgMap.get("province"));
                con.setCityOrgNo((String)cityAndCountyOrgMap.get("city"));
                con.setAreaOrgNo((String)cityAndCountyOrgMap.get("county"));
            } catch (Exception e) {
                log.error("查询市省区供电单位map失败",e);
            }
            con.setTypeCode(typeCodeAndNameDicMap.get(con.getTypeCode()));
            con.setProvinceCode(orgNoAndOrgNameMap.get(con.getProvinceCode()));
            con.setCityCode(orgNoAndOrgNameMap.get(con.getCityCode()));
            con.setCountyCode(orgNoAndOrgNameMap.get(con.getCountyCode()));
            con.setVoltCode(voltCodeAndNameDicMap.get(con.getVoltCode()));
            con.setTradeName(tradeCodeAndNameDicMap.get(con.getBigTradeCode())==null?con.getBigTradeCode():tradeCodeAndNameDicMap.get(con.getBigTradeCode()));
            return true;
        }).collect(Collectors.toList());
        return consList;
    }

    /**
     * 获取省市区机构树节点对应code-name map
     * @return
     */
    private Map<String,String> getOrgNoAndOrgNameMap(){
        List<Region> regions = systemClientService.queryAll();
        Map<String,String> orgNoAndNameMap = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
        return orgNoAndNameMap;
    }

    /**
     * 获取电压等级code-name 字典map
     * @return
     */
    private Map<String,String> getCodeAndNameDicMap(String uniqueCode){
        Map<String,String> codeAndNameDicMap = new HashMap<>();
        DictTypeParam dictTypeParam = new DictTypeParam();
        dictTypeParam.setCode(uniqueCode);
        List<Dict> dictList = dictTypeService.dropDown(dictTypeParam);
        for (Dict dict : dictList) {
            codeAndNameDicMap.put((String)dict.get("code"),(String)dict.get("value"));
        }
        return codeAndNameDicMap;
    }


    @Override
    public List<String> getConsIdlistByCustId(CustParam custParam) {

        String custId = custParam.getCustId();
        Cust cust = custService.getById(custId);
        if (ObjectUtil.isNull(cust)) {
            throw new ServiceException(ConsExceptionEnum.CUST_NOT_EXIST);
        }
        LambdaQueryWrapper<UserConsRela> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserConsRela::getCustId, custId);
        List<UserConsRela> UserConsRelalists = userConsRelaService.list(queryWrapper);

        List<String> consIdList = UserConsRelalists.stream().map(UserConsRela::getConsNo).collect(Collectors.toList());

        return consIdList;

    }

    @Override
    public List<Cons> getConInfo() {
        return baseMapper.getConInfo();
    }

    @Override
    public WorkUserStatsResult getWorkUserDetail(WorkUserParam workUserParam) {
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        String orgNo = StringUtils.isNotEmpty(workUserParam.getOrgNo()) ? workUserParam.getOrgNo() : currenUserInfo.getOrgId();
        List<String> orgNos = systemClientService.getAllNextOrgId(orgNo).getData();
        LocalDate nowYear = LocalDate.of(workUserParam.getYear(), 1, 1);
        LocalDate beforeYear = LocalDate.of(workUserParam.getYear() - 1, 1, 1);
        LocalDate nextYear = LocalDate.of(workUserParam.getYear() + 1, 1, 1);
        //获取今年聚合商数
        LambdaQueryWrapper<Cust> custLambdaQueryWrapper = new LambdaQueryWrapper<>();
        custLambdaQueryWrapper.between(BaseEntity::getCreateTime, nowYear, nextYear);
        custLambdaQueryWrapper.eq(Cust::getCheckStatus, 3);
        custLambdaQueryWrapper.eq(Cust::getIntegrator, 1);
        Integer thisYearIntegratorSize = custMapper.selectCount(custLambdaQueryWrapper);
        //获取去年聚合商数
        custLambdaQueryWrapper = new LambdaQueryWrapper<>();
        custLambdaQueryWrapper.between(BaseEntity::getCreateTime, beforeYear, nowYear);
        custLambdaQueryWrapper.eq(Cust::getIntegrator, 1);
        custLambdaQueryWrapper.eq(Cust::getCheckStatus, 3);
        Integer lastYearIntegratorSize = custMapper.selectCount(custLambdaQueryWrapper);
        //获取今年电力用户数量
        LambdaQueryWrapper<Cons> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Cons::getOrgNo, orgNos);
        lambdaQueryWrapper.between(BaseEntity::getCreateTime, nowYear, nextYear);
        Integer consYearSize = consMapper.selectCount(lambdaQueryWrapper);
        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Cons::getOrgNo, orgNos);
        lambdaQueryWrapper.between(BaseEntity::getCreateTime, beforeYear, nowYear);
        Integer lastConsYearSize = consMapper.selectCount(lambdaQueryWrapper);
        WorkCityYearAdjustVo thisYearWorkCityYearAdjust = drConsAdjustablePotentialMapper.getWorkCityYearAdjust(orgNo, workUserParam.getYear());
        WorkCityYearAdjustVo lastYearWorkCityYearAdjust = drConsAdjustablePotentialMapper.getWorkCityYearAdjust(orgNo, workUserParam.getYear() - 1);

        WorkUserStatsResult workUserStatsResult = new WorkUserStatsResult();
        workUserStatsResult.setCountIntegrator(thisYearIntegratorSize);
        workUserStatsResult.setCountIntegratorAP(davidPercent(thisYearIntegratorSize, lastYearIntegratorSize));
        workUserStatsResult.setCountCons(consYearSize);
        workUserStatsResult.setCountConsAp(davidPercent(consYearSize, lastConsYearSize));
        workUserStatsResult.setCountDayMaxPower(thisYearWorkCityYearAdjust.getDayDesMaxPower());
        workUserStatsResult.setCountDayMaxPowerAP(davidPercent(thisYearWorkCityYearAdjust.getDayDesMaxPower(), lastYearWorkCityYearAdjust.getDayDesMaxPower()));
        workUserStatsResult.setCountHourPower(thisYearWorkCityYearAdjust.getHourDesMaxPower());
        workUserStatsResult.setCountHourPowerAP(davidPercent(thisYearWorkCityYearAdjust.getHourDesMaxPower(), lastYearWorkCityYearAdjust.getHourDesMaxPower()));
        workUserStatsResult.setCountMinusPower(thisYearWorkCityYearAdjust.getMinusDesMaxPower());
        workUserStatsResult.setCountMinusPowerAP(davidPercent(thisYearWorkCityYearAdjust.getMinusDesMaxPower(), lastYearWorkCityYearAdjust.getMinusDesMaxPower()));
        workUserStatsResult.setCountSecondPower(thisYearWorkCityYearAdjust.getSecondDesMaxPower());
        workUserStatsResult.setCountSecondPowerAP(davidPercent(thisYearWorkCityYearAdjust.getSecondDesMaxPower(), lastYearWorkCityYearAdjust.getSecondDesMaxPower()));
        workUserStatsResult.setCountReservePower(thisYearWorkCityYearAdjust.getRisMaxPower());
        workUserStatsResult.setCountReservePowerAP(davidPercent(thisYearWorkCityYearAdjust.getRisMaxPower(), lastYearWorkCityYearAdjust.getRisMaxPower()));

        return workUserStatsResult;
    }

    /**
     * 各地市用户类型统计 --- 签约数据 -- 明细
     * @param consParam
     * @return
     */
    @Override
    public Page<Cons> consStatisticsDetail(ConsParam consParam) {

        consParam.setCheckStatus(CheckStatusEnum.PASS_THE_AUDIT.getCode());
        Page<Cons> page = consMapper.consStatisticsDetail(consParam.getPage(),consParam);
        return page;
    }

    public static Integer davidPercent(Integer now, Integer last) {
        if (last == 0 && now > 0) {
            return 100;
        } else if (last == 0 || now < last) {
            return 0;
        }
        return BigDecimal.valueOf(now)
                .subtract(BigDecimal.valueOf(last))
                .divide(BigDecimal.valueOf(last), 2, BigDecimal.ROUND_HALF_UP)
                .intValue();
    }

}

