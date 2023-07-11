
package com.xqxy.dr.modular.data.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.pojo.page.PageResult;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.enums.ConsCurveExceptionEnum;
import com.xqxy.dr.modular.data.mapper.ConsCurveMapper;
import com.xqxy.dr.modular.data.param.ConsAndDate;
import com.xqxy.dr.modular.data.param.ConsCurveConsParam;
import com.xqxy.dr.modular.data.param.ConsCurveParam;
import com.xqxy.dr.modular.data.service.ConsCurveService;
import com.xqxy.dr.modular.event.entity.EventPowerSample;
import com.xqxy.dr.modular.strategy.CalculateStrategyContext;
import com.xqxy.dr.modular.strategy.DataAccessStrategy;
import com.xqxy.dr.modular.strategy.DataAccessStrategyContext;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户功率曲线 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@Service
public class ConsCurveServiceImpl extends ServiceImpl<ConsCurveMapper, ConsCurve> implements ConsCurveService {
    private static final Log log = Log.get();

    @Resource
    private CalculateStrategyContext calculateStrategyContext;

    @Resource
    private ConsService consService;

    @Resource
    private ConsCurveMapper consCurveMapper;

    @Resource
    private DataAccessStrategyContext dataAccessStrategyContext;

    @Value("${dataAccessStrategy}")
    private String dataAccessStrategy;


    @Override
    public PageResult<ConsCurve> page(ConsCurveParam consCurveParam) {
        return null;
    }

    @Override
    public Object list(ConsCurveParam consCurveParam) {
        LambdaQueryWrapper<ConsCurve> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consCurveParam)) {
            // 根据用户ID查询
            if (ObjectUtil.isNotEmpty(consCurveParam.getConsId())) {
                queryWrapper.eq(ConsCurve::getConsId, consCurveParam.getConsId());
            }

            // 根据开始时间查询
            if (ObjectUtil.isNotEmpty(consCurveParam.getSearchBeginTime())) {
                queryWrapper.ge(ConsCurve::getDataDate, consCurveParam.getSearchBeginTime());
            }

            // 根据结束时间查询
            if (ObjectUtil.isNotEmpty(consCurveParam.getSearchEndTime())) {
                queryWrapper.le(ConsCurve::getDataDate, consCurveParam.getSearchEndTime());
            }
        }

        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(ConsCurve::getDataDate);
        return this.list(queryWrapper);
    }

    @Override
    public void add(ConsCurveParam consCurveParam) {

    }

    @Override
    public void delete(ConsCurveParam consCurveParam) {

    }

    @Override
    public void edit(ConsCurveParam consCurveParam) {

    }

    @Override
    public ConsCurve detail(ConsCurveParam consCurveParam) {
        LambdaQueryWrapper<ConsCurve> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consCurveParam)) {
            // 根据用户ID查询
            if (ObjectUtil.isNotEmpty(consCurveParam.getConsId())) {
                queryWrapper.eq(ConsCurve::getConsId, consCurveParam.getConsId());
            } else {
//                String consId = LoginContextHolder.me().getSysLoginUser().getCons().getConsId();
//                queryWrapper.eq(ConsCurve::getConsId, consId);
            }

            // 根据预测日期查询
            if (ObjectUtil.isNotEmpty(consCurveParam.getDataDate())) {
                queryWrapper.eq(ConsCurve::getDataDate, consCurveParam.getDataDate());
            }
        }
        List<ConsCurve> consCurveList = this.list(queryWrapper);
        if (consCurveList.size() > 1) {
            throw new ServiceException(ConsCurveExceptionEnum.CONS_CURVE_NOT_EXIST);
        }
        return consCurveList.size() == 1 ? consCurveList.get(0) : null;
    }


    @Override
    public Page<ConsCurve> getHistoricalCurve(ConsCurveConsParam consCurveConsParam) {
        LambdaQueryWrapper<ConsCurve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ConsCurve::getConsId, consCurveConsParam.getConsNoList());
        queryWrapper.eq(ConsCurve::getDataDate, consCurveConsParam.getDataDate());
        return this.page(consCurveConsParam.getPage(), queryWrapper);
    }

    @Override
    public ConsCurve getCurveByConsIdAndDate(String consId, String dataDate) {
        LambdaQueryWrapper<ConsCurve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsCurve::getConsId, consId);
        queryWrapper.eq(ConsCurve::getDataDate, dataDate);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public ConsCurve getCurveByConsIdAndDate(ConsCurveParam consCurveParam) {
        Cons cons = consService.getById(consCurveParam.getConsId());
        if (ObjectUtil.isNull(cons)) {
            return null;
        }
        ConsCurve consCurve = new ConsCurve();
        String dateString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(consCurveParam.getDataDate());
        if (LocalDate.now().isEqual(consCurveParam.getDataDate())) {
            // 如果日期是今天，调用实时接口数据
            DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataAccessStrategy);
            consCurve = getDataStrategy.queryTodayLoadCurveByConsNo(cons.getId(), dateString);
        } else {
            // 日期不是今天，调用历史接口数据
            DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataAccessStrategy);
            consCurve = getDataStrategy.queryDayLoadCurveByConsNo(cons.getId(), dateString);
        }
        return consCurve;
    }


    /**
     * @description: 用户历史负荷
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2021/11/0 13:39
     */
    @Override
    public List<ConsCurve> queryConsCurveByConsAndDateList(ConsAndDate consAndDate) {

        String consNo = consAndDate.getConsNo();

        //调用对应策略的  历史负荷查询服务
        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataAccessStrategy);
        List<ConsCurve> consCurves = getDataStrategy.queryHistoryCurveList(consNo, consAndDate.getStartDate(), consAndDate.getEndDate());
//
        if (null != consCurves && consCurves.size() > 0) {
            consCurves = consCurves.stream().sorted((n1, n2) -> n1.getDataDate().compareTo(n2.getDataDate())).collect(Collectors.toList());
        }
        return consCurves;
    }

    /**
     * @description: 负荷预测 ，根据时间和户号集合 返回一条 sum(p1),sum(p2),sum(p3), 的 历史负荷数据
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2022/1/15 13:39
     */
    @Override
    public ConsCurve getCurveByConsIdListAndDate(List<String> condIdList, String date) {
        return consCurveMapper.getCurveByConsIdListAndDate(condIdList, date);
    }

    @Override
    public List<ConsCurve> getCurveByConsIdListAndDate2(List<String> condIdList, String date) {
        return consCurveMapper.getCurveByConsIdListAndDate2(condIdList, date);
    }

    @Override
    public List<ConsCurve> getCurveByConsIdListAndDate3(List<String> condIdList, String date) {
        return consCurveMapper.getCurveByConsIdListAndDate3(condIdList, date);
    }

    @Override
    public List<ConsCurve> getCurveAllByDate(List<String> simpList, Integer size) {
        return consCurveMapper.getCurveAllByDate(simpList, size);
    }

    @Override
    public List<EventPowerSample> getCurveAllAmendByDate(List<String> simpList, List<String> cons, Long baselinId) {
        return consCurveMapper.getCurveAllAmendByDate(simpList, cons, baselinId);
    }

    @Override
    public List<EventPowerSample> getCurveAmendByDate(List<String> simpList, List<String> cons, Long baselinId) {
        return consCurveMapper.getCurveAmendByDate(simpList, cons, baselinId);
    }


//    @Override
//    public List<PointGotten> getPointPercent(ConsAndDate consAndDate) {
//        ArrayList<PointGotten> pointGottens = new ArrayList<>();
//        // 筛选用户等级和用户区域
//        String regionLevel = consAndDate.getRegionLevel();
//        String regionCode = consAndDate.getRegionCode();
//        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataAccessStrategy);
//        //获取所有直接参与用户和曲线
//        List<Cons> directCons = consService.getDirectCons(regionLevel,regionCode);
//        List<ConsCurve> directCurves = directCons.stream().map(cons -> {
//            return getDataStrategy.queryDayLoadCurveByConsNo(cons.getId(), consAndDate.getDataDate());
//        }).collect(Collectors.toList());
//        //获取所有代理用户和曲线
//        List<Cons> proxyCons = consService.getProxyCons(regionLevel,regionCode);
//        List<ConsCurve> proxyCurves = proxyCons.stream().map(cons -> {
//            return  getDataStrategy.queryDayLoadCurveByConsNo(cons.getId(), consAndDate.getDataDate());
//        }).collect(Collectors.toList());
//
//        //临时对象，里面的值为空。
//        ConsCurve tempCurve = new ConsCurve();
//        LocalTime localTime = LocalTime.now().withHour(0).withMinute(0);
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
//        int directTemp = 0;
//        int proxyTemp = 0;
//        for (int i = 1; i <= 96; i++) {
//            PointGotten pointGotten = new PointGotten();
//            //处理直接参与用户
//            pointGotten.setDerectConsNum(directCons.size());
//            Iterator<Cons> directIterator = directCons.iterator();
//            while (directIterator.hasNext()) {
//                Cons directCon = directIterator.next();
//                Optional<ConsCurve> first = directCurves.stream().filter(consCurve -> directCon.getId().equals(consCurve.getConsNo())).findFirst();
//                if(ObjectUtil.isNotNull(ReflectUtil.getFieldValue(first.orElse(tempCurve), "p" + i))){
//                    directTemp++;
//                }
//            }
//            pointGotten.setDirectPointGotten(directTemp);
//            //处理代理用户
//            pointGotten.setProxyConsNum(proxyCons.size());
//            Iterator<Cons> proxyIterator = proxyCons.iterator();
//            while (proxyIterator.hasNext()) {
//                Cons proxyCon = proxyIterator.next();
//                Optional<ConsCurve> first = proxyCurves.stream().filter(consCurve -> proxyCon.getElecConsNo().equals(consCurve.getConsNo())).findFirst();
//                if(ObjectUtil.isNotNull(ReflectUtil.getFieldValue(first.orElse(tempCurve), "p" + i))) {
//                    proxyTemp++;
//                }
//            }
//            pointGotten.setProxyPointGotten(proxyTemp);
//            //;
//            String format = dateTimeFormatter.format(localTime.plusMinutes(15 * i));
//            pointGotten.setPointName(format);
//            pointGottens.add(pointGotten);
//            directTemp = 0;
//            proxyTemp = 0;
//        }
//       return pointGottens;
//    }

//    @Override
//    public List<ConsCurve> getCurveByConsIdList(List<Cons> directCons, LocalDate statDate) {
//        List<Long> collect = directCons.stream().map(Cons::getId).collect(Collectors.toList());
//        LambdaQueryWrapper<ConsCurve> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.in(ConsCurve::getConsId,collect);
//        queryWrapper.eq(ConsCurve::getDataDate,statDate);
//        return this.list(queryWrapper);
//    }

//    @Override
//    public ConsCurve getTodayCurve(ConsCurveParam consCurveParam) {
//        String elecConsNo = LoginContextHolder.me().getSysLoginUser().getCons().getElecConsNo();
//        String formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
//        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataAccessStrategy);
//        ConsCurve consCurve = getDataStrategy.queryDayLoadCurveByConsNo(elecConsNo, formatDate);
//        return consCurve;
//    }

//    @Override
//    public ConsCurve getLastyearTodayCurve() {
//        String elecConsNo = LoginContextHolder.me().getSysLoginUser().getCons().getElecConsNo();
//        LocalDate localDate = LocalDate.now().minusYears(1);
//        String formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate);
//        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataAccessStrategy);
//        ConsCurve consCurve = getDataStrategy.queryDayLoadCurveByConsNo(elecConsNo, formatDate);
//        return consCurve;
//    }

//    @Override
//    public List<PointGotten> getTodayPointPercent(ConsAndDate consAndDate) {
//
//
//        ArrayList<PointGotten> pointGottens = new ArrayList<>();
//
//        String regionLevel = consAndDate.getRegionLevel();
//        String regionCode = consAndDate.getRegionCode();
//        //获取所有直接参与用户和曲线
//        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataAccessStrategy);
//        List<Cons> directCons = consService.getDirectCons(regionLevel,regionCode);
//        List<ConsCurve> directCurves = directCons.stream().map(cons -> {
//            return getDataStrategy.queryTodayLoadCurveByConsNo(cons.getElecConsNo(), consAndDate.getDataDate());
//        }).collect(Collectors.toList());
//        //获取所有代理用户和曲线
//        List<Cons> proxyCons = consService.getProxyCons(regionLevel,regionCode);
//        List<ConsCurve> proxyCurves = proxyCons.stream().map(cons -> {
//            return getDataStrategy.queryTodayLoadCurveByConsNo(cons.getElecConsNo(), consAndDate.getDataDate());
//        }).collect(Collectors.toList());
//
//        //临时对象，里面的值为空。
//        ConsCurve tempCurve = new ConsCurve();
//
//        LocalTime localTime = LocalTime.now().withHour(0).withMinute(0);
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
//        int directTemp = 0;
//        int proxyTemp = 0;
//        for (int i = 1; i <= 96; i++) {
//            PointGotten pointGotten = new PointGotten();
//            //处理直接参与用户
//            pointGotten.setDerectConsNum(directCons.size());
//            Iterator<Cons> directIterator = directCons.iterator();
//            while (directIterator.hasNext()) {
//                Cons directCon = directIterator.next();
//                Optional<ConsCurve> first = directCurves.stream().filter(consCurve -> directCon.getElecConsNo().equals(consCurve.getConsNo())).findFirst();
//                if(ObjectUtil.isNotNull(ReflectUtil.getFieldValue(first.orElse(tempCurve), "p" + i))){
//                    directTemp++;
//                }
//            }
//            pointGotten.setDirectPointGotten(directTemp);
//            //处理代理用户
//            pointGotten.setProxyConsNum(proxyCons.size());
//            Iterator<Cons> proxyIterator = proxyCons.iterator();
//            while (proxyIterator.hasNext()) {
//                Cons proxyCon = proxyIterator.next();
//                Optional<ConsCurve> first = proxyCurves.stream().filter(consCurve -> proxyCon.getElecConsNo().equals(consCurve.getConsNo())).findFirst();
//                if(ObjectUtil.isNotNull(ReflectUtil.getFieldValue(first.orElse(tempCurve), "p" + i))) {
//                    proxyTemp++;
//                }
//            }
//            pointGotten.setProxyPointGotten(proxyTemp);
//            ;
//            //;
//            String format = dateTimeFormatter.format(localTime.plusMinutes(15 * i));
//            pointGotten.setPointName(format);
//            pointGottens.add(pointGotten);
//            directTemp = 0;
//            proxyTemp = 0;
//        }
//        return pointGottens;
//    }

//    @Override
//    public List<ConsCurve> getCurveByIdAndDateList(ConsParam consParam) {
//        List<ConsCurve> curveList = new ArrayList<>();
//        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataAccessStrategy);
//        String elecConsNo = null;
//        if(ObjectUtil.isNotNull(consParam.getElecConsNo())) {
//            elecConsNo = consParam.getElecConsNo();
//        } else  {
//            elecConsNo = LoginContextHolder.me().getSysLoginUser().getCons().getElecConsNo();
//        }
//        if(ObjectUtil.isNotNull(consParam)) {
//            if (ObjectUtil.isNotNull(consParam.getConsNo())) {
//                elecConsNo = consParam.getElecConsNo();
//            }
//            if(ObjectUtil.isNotNull(consParam.getLocalDateList())){
//                for(int i = 0;i<consParam.getLocalDateList().size();i++) {
//                    LocalDate localDate = consParam.getLocalDateList().get(i);
//                    ConsCurve consCurve1 = getDataStrategy.queryDayLoadCurveByConsNo(elecConsNo, DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate));
//                    curveList.add(consCurve1);
//                }
//            }
//        }
//
//        return curveList;
//
//    }

//    @Override
//    public List<ConsCurve> getHistoryCurveList(String consId, String startDate, String endDate) {
//        Cons cons = new Cons();
//        if (ObjectUtil.isNotEmpty(consId)) {
//            // 获取用户信息
//            cons = consService.getById(consId);
//        } else {
//            // 获取当前用户信息
//            cons = LoginContextHolder.me().getSysLoginUser().getCons();
//        }
//        if (ObjectUtil.isNull(cons)) {
//            return null;
//        }
//        List<ConsCurve> consCurveList = new ArrayList<>();
//        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataAccessStrategy);
//        consCurveList = getDataStrategy.queryHistoryCurveList(cons.getElecConsNo(), startDate, endDate);
//
//        return consCurveList;
//
//    }

}

