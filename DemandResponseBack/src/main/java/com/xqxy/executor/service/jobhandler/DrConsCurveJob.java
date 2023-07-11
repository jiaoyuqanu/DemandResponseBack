package com.xqxy.executor.service.jobhandler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.entity.ConsCurveToday;
import com.xqxy.dr.modular.data.service.ConsCurveService;
import com.xqxy.dr.modular.data.service.ConsCurveTodayService;
import com.xqxy.dr.modular.strategy.DataAccessStrategy;
import com.xqxy.dr.modular.strategy.DataAccessStrategyContext;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Description 事件任务
 * @ClassName EventJob
 * @Author lqr
 * @date 2021.05.11 14:35
 */
@Component
public class DrConsCurveJob {

    @Value("${dataAccessStrategyLqr}")
    private String dataAccessStrategyLqr;

    @Resource
    private DataAccessStrategyContext dataAccessStrategyContext;

    @Autowired
    private ConsService consService;

    @Autowired
    private ConsCurveService consCurveService;

    @Autowired
    private ConsCurveTodayService consCurveTodayService;

    /**
     * 用户实时负荷导入
     *
     * @param
     * @return
     * @throws Exception
     */
    @XxlJob("consCurveTodayInsert")
    public ReturnT<String> consCurveTodayInsert(String param){
        XxlJobLogger.log("同步用户实时负荷");
        DateTimeFormatter dateTimeFormatter =DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentTime =null;
        String today=null;
        if(null!=param && !"".equals(param)) {
            if(isDate(param)) {
                currentTime = param;
            } else {
                currentTime = dateTimeFormatter.format(LocalDate.now().plusDays(-1));
            }
        } else {
            currentTime = dateTimeFormatter.format(LocalDate.now().plusDays(-1));
        }

        //查询数据库所有户号  以及所有户号的实时负荷曲线
        List<Cons> consList = consService.list();
        LambdaQueryWrapper<ConsCurveToday> consCurveTodayQueryWrapper = new LambdaQueryWrapper<>();
        consCurveTodayQueryWrapper.eq(ConsCurveToday::getDataDate,LocalDate.parse(currentTime).plusDays(1));
        List<ConsCurveToday> sourceConsCurveTodayList = null;

        //获取历史负荷表 所有数据 时间为 前一天
        LambdaQueryWrapper<ConsCurve> consCurveQueryWrapper = new LambdaQueryWrapper<>();
        consCurveQueryWrapper.eq(ConsCurve::getDataDate,LocalDate.parse(currentTime));
        List<ConsCurve> sourceConsCurveList = null;

        if(!CollectionUtils.isEmpty(consList)){
            //List<String> idList = consList.stream().map(Cons::getId).collect(Collectors.toList());
            DataAccessStrategy dataAccessStrategy = dataAccessStrategyContext.strategySelect(this.dataAccessStrategyLqr);
            List<ConsCurve> consCurves = null;
            //获取实时负荷
            int temp = 20;
            List<String> ids = new ArrayList<>() ;
            if(null!=consList && consList.size()>0) {
                for(int i=0;i<consList.size();i++) {
                    ids.add(consList.get(i).getId());
                    if(i==consList.size()-1 || i % temp==0) {
                        consCurves = dataAccessStrategy.queryTodayCurveList(ids, currentTime);
                        ids = new ArrayList<>();
                        List<ConsCurveToday> consCurveTodayList = new ArrayList<>();
                        sourceConsCurveTodayList = consCurveTodayService.list(consCurveTodayQueryWrapper);
                        sourceConsCurveList = consCurveService.list(consCurveQueryWrapper);
                        for (ConsCurve consCurve : consCurves) {
                            ConsCurveToday consCurveToday = new ConsCurveToday();
                            BeanUtils.copyProperties(consCurve,consCurveToday);
                            if(null!=consCurve.getConsId()) {
                                consCurveToday.setConsId(consCurve.getConsId());
                            }
                            consCurveToday.setDataPointFlag("1");
                            consCurveToday.setDataDate(LocalDate.parse(currentTime).plusDays(1));
                            consCurve.setDataDate(LocalDate.parse(currentTime));
                            consCurve.setDataPointFlag("1");
                            //consCurveToday.setDataDate(consCurve.getDataDate());
                            consCurveTodayList.add(consCurveToday);
                        }

                        if(CollectionUtils.isEmpty(sourceConsCurveTodayList)){
                            //为空 直接新增所有
                            consCurveTodayService.saveBatch(consCurveTodayList);
                        }else {
                            //不为空  ， 进一步判断此用户 在当日是否有 实时负荷已入库
                            for (ConsCurveToday consCurveToday : consCurveTodayList) {

                                List<ConsCurveToday> ConsCurvesTodayList1 = sourceConsCurveTodayList.stream().filter(n-> n.getConsId().equals(consCurveToday.getConsId()) && n.getDataDate().equals(consCurveToday.getDataDate())).collect(Collectors.toList());
                                if(CollectionUtils.isEmpty(ConsCurvesTodayList1)){
                                    //为空新增
                                    consCurveTodayService.save(consCurveToday);
                                }else {
                                    //不为空，则修改
                                    LambdaQueryWrapper<ConsCurveToday> queryWrapper = new LambdaQueryWrapper<>();
                                    queryWrapper.eq(ConsCurveToday::getConsId, consCurveToday.getConsId());
                                    queryWrapper.eq(ConsCurveToday::getDataDate, consCurveToday.getDataDate());
                                    boolean update = consCurveTodayService.update(consCurveToday, queryWrapper);
                                }
                            }
                        }

                        //用户历史负荷 入库
                        if(CollectionUtils.isEmpty(sourceConsCurveList)){
                            //为空 直接新增所有
                            consCurveService.saveBatch(consCurves);
                        }else {
                            for (ConsCurve consCurve : consCurves) {

                                //不为空  ， 进一步判断此用户 在前一日是否有 实时负荷已入库
                                List<ConsCurve> consCurves1 = sourceConsCurveList.stream().filter(n -> n.getConsId().equals(consCurve.getConsId()) && n.getDataDate().equals(consCurve.getDataDate())).collect(Collectors.toList());
                                if(CollectionUtils.isEmpty(consCurves1)){
                                    //为空新增
                                    consCurveService.save(consCurve);
                                }else {
                                    //不为空，则修改
                                    LambdaQueryWrapper<ConsCurve> queryWrapper = new LambdaQueryWrapper<>();
                                    queryWrapper.eq(ConsCurve::getConsId, consCurve.getConsId());
                                    queryWrapper.eq(ConsCurve::getDataDate, consCurve.getDataDate());
                                    boolean update = consCurveService.update(consCurve, queryWrapper);
                                }
                            }
                        }
                    }
                }
            }



        }


        return ReturnT.SUCCESS;
    }

    //校验参数是否日期
    public boolean isDate(String param) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(param);
            return true;
        } catch (ParseException e) {
            e.getMessage();
            return false;
        }
    }

}
