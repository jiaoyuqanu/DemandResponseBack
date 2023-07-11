package com.xqxy.dr.modular.data.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.dr.modular.data.entity.ConsEnergyCurve;
import com.xqxy.dr.modular.data.enums.ConsEnergyExceptionEnum;
import com.xqxy.dr.modular.data.mapper.ConsEnergyCurveMapper;
import com.xqxy.dr.modular.data.param.ConsAndDate;
import com.xqxy.dr.modular.data.service.ConsEnergyCurveService;
import com.xqxy.dr.modular.strategy.DataAccessStrategy;
import com.xqxy.dr.modular.strategy.DataAccessStrategyContext;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;


/**
 * <p>
 * 用户总电能量曲线 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-13
 */
@Service
public class ConsEnergyCurveServiceImpl extends ServiceImpl<ConsEnergyCurveMapper, ConsEnergyCurve> implements ConsEnergyCurveService {

    @Value("${dataAccessStrategy}")
    private String dataStrategy;

    @Resource
    private DataAccessStrategyContext dataAccessStrategyContext;

    @Resource
    private ConsService consService;

    @Override
    public ConsEnergyCurve getCurveByConsIdAndDate(String consId, String dataDate) {
        LambdaQueryWrapper<ConsEnergyCurve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsEnergyCurve::getConsId, consId);
        queryWrapper.eq(ConsEnergyCurve::getDataDate, dataDate);
        return this.getOne(queryWrapper);
    }

    @Override
    public ConsEnergyCurve getRealtimeCurve(ConsAndDate consAndDate) {
        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataStrategy);
        List<String>consIdlist= Arrays.asList(consAndDate.getElecConsNo());
        List<ConsEnergyCurve> consEnergyCurve = getDataStrategy.queryDayLoadEnergyByConsNo(consIdlist, consAndDate.getDataDate());
        return consEnergyCurve.get(0);
    }

    @Override
    public ConsEnergyCurve getHistoryCurve(ConsAndDate consAndDate) {
        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataStrategy);
        ConsEnergyCurve consEnergyCurve = getDataStrategy.queryTodayLoadEnergyByConsNo(consAndDate.getElecConsNo(), consAndDate.getDataDate());
        return consEnergyCurve;
    }

    @Override
    public Page<Cons> energyMonitorList(ConsAndDate consAndDate) {
        if(ObjectUtil.isNotNull(consAndDate.getDataDate())) {
        String dataDateStr = consAndDate.getDataDate();
        TemporalAccessor parse = DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(dataDateStr);
        LocalDate dataDate = LocalDate.from(parse);
        if(dataDate.isAfter(LocalDate.now())) {
            throw new ServiceException(ConsEnergyExceptionEnum.DATE_TOOLATE);
        }
        }
        return consService.energyMonitorList(consAndDate);
    }
}
