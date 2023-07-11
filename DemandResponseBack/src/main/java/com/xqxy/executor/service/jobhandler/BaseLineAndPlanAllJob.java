package com.xqxy.executor.service.jobhandler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xqxy.core.enums.YesOrNotEnum;
import com.xqxy.core.util.SpringUtil;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetail;
import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetailAll;
import com.xqxy.dr.modular.baseline.entity.PlanBaseLine;
import com.xqxy.dr.modular.baseline.mapper.BaseLineMapper;
import com.xqxy.dr.modular.baseline.service.BaseLineService;
import com.xqxy.dr.modular.baseline.service.CustBaseLineDetailAllService;
import com.xqxy.dr.modular.baseline.service.PlanBaseLineService;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.enums.ConsBaseLineExceptionEnum;
import com.xqxy.dr.modular.data.service.ConsCurveService;
import com.xqxy.dr.modular.event.entity.ConsBaseline;
import com.xqxy.dr.modular.event.entity.EventPowerSample;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @Description 基线计算方案同步四个定时任务
 * @ClassName EventJob
 * @Author chen zhi jun
 * @date 2021.05.11 14:35
 */
@Component
public class BaseLineAndPlanAllJob {
    private static final Log log = Log.get();

    @Resource
    private BaseLineService baseLineService;

    @Resource
    private PlanBaseLineService planBaseLineService;

    @Resource
    private ConsService consService;

    @Resource
    private ConsCurveService consCurveService;

    @Resource
    private BaseLineMapper baseLineMapper;

    @Resource
    private CustBaseLineDetailAllService custBaseLineDetailAllService;

    @Value("${resultTimeTopic}")
    private String resultTimeTopic;

    @Value("${executor.corePoolSize}")
    private int corePoolSize;

    @Value("${executor.maxPoolSize}")
    private int maxPoolSize;

    @Value("${executor.workQueue}")
    private int workQueue;

    @Value("${spring.datasource.mysql.username}")
    private String userName;

    @Value("${spring.datasource.mysql.jdbc-url}")
    private String dataurl;

    @Value("${spring.datasource.mysql.password}")
    private String datapassword;

    @Value("${spring.datasource.mysql.driver-class-name}")
    private String driver;

    private final DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 计算基线详情
     *
     * @param
     * @return
     * @throws Exception
     */
    @XxlJob("calBaselineAll")
    public ReturnT<String> calBaseline(String param) throws Exception {
        XxlJobLogger.log("计算用户基线全部点详情");
        this.createBaseline(param);
        return ReturnT.SUCCESS;
    }

    /**
     * 计算客户基线详情
     *
     * @param
     * @return
     * @throws Exception
     */
    @XxlJob("calCustlineAll")
    public ReturnT<String> calCustline(String param) throws Exception {
        XxlJobLogger.log("计算客户基线详情");
        this.createCustBaseline(param);
        return ReturnT.SUCCESS;
    }

    public void createCustBaseline(String param) {
        //获取客户基线
        List<CustBaseLineDetail> list = baseLineService.getCustBaseLineAll();
        if(null==list || list.size()==0) {
            log.info("无基线计算任务");
            return;
        }
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(workQueue))
                .build();
        executor.execute(this.createCustBaseline(list));
        executor.shutdown();
    }

    public Runnable createCustBaseline(List<CustBaseLineDetail> list) {
        log.info("客户基线计算开始!");
        Map<Integer, Method> consMethodMap = new HashMap<>();
        Map<Integer, Method> consMethodMap2 = new HashMap<>();
        Map<Integer, Method> consMethodMap3 = new HashMap<>();
        try{
            for (int j=1; j<=96; j++){
                consMethodMap.put(j, ConsBaseline.class.getMethod("setP"+j, BigDecimal.class));
                consMethodMap2.put(j, CustBaseLineDetail.class.getMethod("getP"+j));
                consMethodMap3.put(j, CustBaseLineDetail.class.getMethod("setP"+j, BigDecimal.class));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PreparedStatement preparedStatement = null;
                PreparedStatement preparedStatement2 = null;
                PreparedStatement preparedStatement3 = null;
                Connection conn1 = null;
                Connection conn2 = null;
                Connection conn3 = null;
                //获取客户历史曲线
                long baselinId = list.get(0).getBaselineLibId();
                BaseLine baseLine = baseLineService.getById(baselinId);
                String sampleDates = null;
                String startTime = null;
                String endTime = null;
                if(null!=baseLine) {
                    sampleDates = baseLine.getSimplesDate();
                    startTime = baseLine.getStartPeriod();
                    endTime = baseLine.getEndPeriod();
                } else {
                    log.info("基线库为空");
                    return;
                }
                //开始下标
                int startIndex = 1;
                int endIndex = 96;
                //查询所有的历史曲线集合
                List<String> sampleDateList = Arrays.asList(sampleDates.split(","));
                List<String> conIds = list.stream().map(CustBaseLineDetail::getConsNo).collect(Collectors.toList());
                List<CustBaseLineDetail> cureList = baseLineService.getCustCurveAll(sampleDateList,conIds);
                //cureList = getPoint2(cureList,consMethodMap2,consMethodMap3,startIndex,endIndex);
                List<Long> custs = new ArrayList<>();
                if(null!=list && list.size()>0) {
                    try {
                    String url = dataurl;
                    String user = userName;
                    String password = datapassword;
                    List<CustBaseLineDetail> cons = null;
                    //根据基线库获取用户基线
                    List<ConsBaseline> consBaselines = baseLineMapper.getConsBaseLineByBaseLineId(baselinId);
                    //查询用户对应的客户
                    List<CustBaseLineDetail> custAndCons = baseLineService.getCustAndConsInfo();
                    //根据基线库查找客户基线
                    LambdaQueryWrapper<CustBaseLineDetailAll> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(CustBaseLineDetailAll::getBaselineLibId,baselinId);
                    List<CustBaseLineDetailAll> custBaseLineDetailList = custBaseLineDetailAllService.list(lambdaQueryWrapper);
                    List<CustBaseLineDetail>  insertList = new ArrayList<>();
                    List<CustBaseLineDetail>  updateList = new ArrayList<>();
                    List<ConsBaseline> baseLineDetailList = new ArrayList<>();
                    for (CustBaseLineDetail custBaseLineDetail : list) {
                        if(custs.contains(custBaseLineDetail.getCustId())) {
                            continue;
                        }
                        List<CustBaseLineDetail> custBaseLineDetails = null;
                        List<String> consStr = new ArrayList<>();
                        List<BigDecimal> validSamplePowerList = new ArrayList<>();
                        if (null != cureList && cureList.size() > 0) {
                            if (null != sampleDateList && sampleDateList.size() > 0) {
                                custBaseLineDetails = cureList.stream().filter(baseLineDetail -> baseLineDetail.getCustId().equals(custBaseLineDetail.getCustId()) &&
                                        sampleDateList.contains(baseLineDetail.getSimplesDate())
                                ).collect(Collectors.toList());
                            }

                        }
                        if(null==custBaseLineDetails || custBaseLineDetails.size()==0) {
                            continue;
                        }
                        //根据规则判断基线取值方式
                        if (null != custBaseLineDetail.getCalRule() && !"".equals(custBaseLineDetail.getCalRule())) {
                            if ("1".equals(custBaseLineDetail.getCalRule())) {
                                //获取时间段内有效点
                                for (int i = startIndex; i <= endIndex; i++) {
                                    List<BigDecimal> samplePowerList = new ArrayList<>();
                                    for (CustBaseLineDetail consCurve : custBaseLineDetails) {
                                        BigDecimal power = (BigDecimal) consMethodMap2.get(i).invoke(consCurve);
                                        //去除0值和null值数据
                                        if (null != power) {
                                            samplePowerList.add(power);
                                        }
                                    }
                                    if (samplePowerList.size() > 0) {
                                        BigDecimal sumPower = samplePowerList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                                        BigDecimal avgPower = NumberUtil.div(sumPower, samplePowerList.size(), 2);
                                        for (BigDecimal samplePower : samplePowerList) {
                                            //平均负荷25%-300%之间为有效负荷
                                            if (samplePower.compareTo(NumberUtil.mul(avgPower, new BigDecimal("0.25"))) >= 0 && samplePower.compareTo(NumberUtil.mul(avgPower, new BigDecimal("3")).setScale(2, BigDecimal.ROUND_HALF_UP)) <= 0) {
                                                validSamplePowerList.add(samplePower);
                                            }
                                        }
                                    }
                                }
                            } else if ("2".equals(custBaseLineDetail.getCalRule())) {
                                for (int i = startIndex; i <= endIndex; i++) {
                                    BigDecimal total = BigDecimal.ZERO;
                                    int j = 1;
                                    for (CustBaseLineDetail consCurve : custBaseLineDetails) {
                                        //去除0值和null值数据
                                        BigDecimal consCurveHis = (BigDecimal) consMethodMap2.get(i).invoke(consCurve);
                                        if (null != consCurveHis) {
                                            total = NumberUtil.add(total,consCurveHis);
                                            if(j==custBaseLineDetails.size()) {
                                                total = NumberUtil.div(total,(custBaseLineDetails.size()));
                                                consMethodMap3.get(i).invoke(custBaseLineDetail,total);
                                                validSamplePowerList.add(total);
                                            }
                                            //validSamplePowerList.add((BigDecimal) ReflectUtil.getFieldValue(consCurve, "p" + i));
                                        }
                                        j++;
                                    }
                                }
                            } else {
                                log.warn("客户基线计算规则不存在");
                                return;
                            }
                        } else {
                            log.warn("客户基线计算规则不存在");
                            return;
                        }
                        custBaseLineDetail.setNormal("Y");
                        //刷选该客户基线
                        List<CustBaseLineDetailAll> custBaseLineDetails1 = custBaseLineDetailList.stream().filter(baseLineDetail -> baseLineDetail.getCustId().
                                equals(custBaseLineDetail.getCustId())).collect(Collectors.toList());
                        if (null != validSamplePowerList && validSamplePowerList.size() > 0) {
                            //计算客户基线最大、最小、平均值
                            BigDecimal sumSamplePower = validSamplePowerList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                            BigDecimal avgSamplePower = NumberUtil.div(sumSamplePower, validSamplePowerList.size(), 2);
                            BigDecimal maxSamplePower = CollectionUtil.max(validSamplePowerList);
                            BigDecimal minSamplePower = CollectionUtil.min(validSamplePowerList);
                            custBaseLineDetail.setAvgLoadBaseline(avgSamplePower);
                            custBaseLineDetail.setMaxLoadBaseline(maxSamplePower);
                            custBaseLineDetail.setMinLoadBaseline(minSamplePower);
                            /*for (int i = startIndex; i <= endIndex; i++) {
                                consMethodMap3.get(i).invoke(custBaseLineDetail,avgSamplePower);
                            }*/
                            if(null!=custBaseLineDetails1 && custBaseLineDetails1.size()>0) {
                                custBaseLineDetail.setBaselineId(custBaseLineDetails1.get(0).getBaselineId());
                                updateList.add(custBaseLineDetail);
                            } else {
                                insertList.add(custBaseLineDetail);
                            }
                            //筛选该客户包含的用户
                            if(null!=custAndCons && custAndCons.size()>0) {
                                cons = custAndCons.stream().filter(baseLineDetail ->
                                        baseLineDetail.getCustId().equals(custBaseLineDetail.getCustId())
                                ).collect(Collectors.toList());
                            }
                            List<ConsBaseline> baseLineDetails = null;
                            //筛选该客户的用户基线
                            if(null!=cons && cons.size()>0) {
                                for(CustBaseLineDetail con : cons ) {
                                    consStr.add(con.getConsNo());
                                }

                                baseLineDetails = consBaselines.stream().filter(baseLineDetail ->
                                        baseLineDetail.getBaselineLibId().equals(custBaseLineDetail.getBaselineLibId())
                                                && consStr.contains(baseLineDetail.getConsId())
                                ).collect(Collectors.toList());
                            }
                            //将用户基线添加到更新集合
                            if (null != baseLineDetails && baseLineDetails.size() > 0) {
                                for (ConsBaseline baseLineDetail : baseLineDetails) {
                                    baseLineDetailList.add(baseLineDetail);
                                }
                            }
                        }
                        custs.add(custBaseLineDetail.getCustId());
                    }
                    //保存客户基线
                    String sql = "INSERT INTO dr_cust_baseline_all (\n" +
                            "\tbaseline_lib_id , cust_id , cust_name , baseline_date , simples_date , p1 , p2 , p3 , p4 , p5 , p6 , p7 , p8 , p9 , p10 , p11 , p12 , p13 , p14 , p15 , p16 , p17 , p18 , p19 , p20 , p21 , p22 , p23 , p24 , p25 , p26 , p27 , p28 , p29 , p30 , p31 , p32 , p33 , p34 , p35 , p36 , p37 , p38 , p39 , p40 , p41 , p42 , p43 , p44 , p45 , p46 , p47 , p48 , p49 , p50 , p51 , p52 , p53 , p54 , p55 , p56 , p57 , p58 , p59 , p60 , p61 , p62 , p63 , p64 , p65 , p66 , p67 , p68 , p69 , p70 , p71 , p72 , p73 , p74 , p75 , p76 , p77 , p78 , p79 , p80 , p81 , p82 , p83 , p84 , p85 , p86 , p87 , p88 , p89 , p90 , p91 , p92 , p93 , p94 , p95 , p96 , max_load_baseline , min_load_baseline , avg_load_baseline , normal , exception_remark , cal_rule \n" +
                            ")\n" +
                            "VALUES\n" +
                            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        //保存用户基线表
                        if (null != insertList && insertList.size() > 0) {
                            int k = 0;
                            //2）、加载驱动，不需要显示注册驱动
                            Class.forName(driver);
                            //获取数据库连接
                            conn1 = DriverManager.getConnection(url, user, password);
                            conn1.setAutoCommit(false);
                            preparedStatement = conn1.prepareStatement(sql);
                            for (CustBaseLineDetail consBaseline : insertList) {
                                preparedStatement.setLong(1, consBaseline.getBaselineLibId());
                                if(null!=consBaseline.getCustId()) {
                                    preparedStatement.setLong(2, consBaseline.getCustId());
                                } else {
                                    preparedStatement.setNull(2, Types.BIGINT);
                                }
                                preparedStatement.setString(3, consBaseline.getName());
                                if(null!=consBaseline.getBaselineDate()) {
                                    preparedStatement.setString(4, simpleDateFormat.format(consBaseline.getBaselineDate()));
                                } else {
                                    preparedStatement.setNull(4,Types.VARCHAR);
                                }
                                preparedStatement.setString(5, consBaseline.getSimplesDate());
                                preparedStatement.setBigDecimal(6, consBaseline.getP1());
                                preparedStatement.setBigDecimal(7, consBaseline.getP2());
                                preparedStatement.setBigDecimal(8, consBaseline.getP3());
                                preparedStatement.setBigDecimal(9, consBaseline.getP4());
                                preparedStatement.setBigDecimal(10, consBaseline.getP5());
                                preparedStatement.setBigDecimal(11, consBaseline.getP6());
                                preparedStatement.setBigDecimal(12, consBaseline.getP7());
                                preparedStatement.setBigDecimal(13, consBaseline.getP8());
                                preparedStatement.setBigDecimal(14, consBaseline.getP9());
                                preparedStatement.setBigDecimal(15, consBaseline.getP10());
                                preparedStatement.setBigDecimal(16, consBaseline.getP11());
                                preparedStatement.setBigDecimal(17, consBaseline.getP12());
                                preparedStatement.setBigDecimal(18, consBaseline.getP13());
                                preparedStatement.setBigDecimal(19, consBaseline.getP14());
                                preparedStatement.setBigDecimal(20, consBaseline.getP15());
                                preparedStatement.setBigDecimal(21, consBaseline.getP16());
                                preparedStatement.setBigDecimal(22, consBaseline.getP17());
                                preparedStatement.setBigDecimal(23, consBaseline.getP18());
                                preparedStatement.setBigDecimal(24, consBaseline.getP19());
                                preparedStatement.setBigDecimal(25, consBaseline.getP20());
                                preparedStatement.setBigDecimal(26, consBaseline.getP21());
                                preparedStatement.setBigDecimal(27, consBaseline.getP22());
                                preparedStatement.setBigDecimal(28, consBaseline.getP23());
                                preparedStatement.setBigDecimal(29, consBaseline.getP24());
                                preparedStatement.setBigDecimal(30, consBaseline.getP25());
                                preparedStatement.setBigDecimal(31, consBaseline.getP26());
                                preparedStatement.setBigDecimal(32, consBaseline.getP27());
                                preparedStatement.setBigDecimal(33, consBaseline.getP28());
                                preparedStatement.setBigDecimal(34, consBaseline.getP29());
                                preparedStatement.setBigDecimal(35, consBaseline.getP30());
                                preparedStatement.setBigDecimal(36, consBaseline.getP31());
                                preparedStatement.setBigDecimal(37, consBaseline.getP32());
                                preparedStatement.setBigDecimal(38, consBaseline.getP33());
                                preparedStatement.setBigDecimal(39, consBaseline.getP34());
                                preparedStatement.setBigDecimal(40, consBaseline.getP35());
                                preparedStatement.setBigDecimal(41, consBaseline.getP36());
                                preparedStatement.setBigDecimal(42, consBaseline.getP37());
                                preparedStatement.setBigDecimal(43, consBaseline.getP38());
                                preparedStatement.setBigDecimal(44, consBaseline.getP39());
                                preparedStatement.setBigDecimal(45, consBaseline.getP40());
                                preparedStatement.setBigDecimal(46, consBaseline.getP41());
                                preparedStatement.setBigDecimal(47, consBaseline.getP42());
                                preparedStatement.setBigDecimal(48, consBaseline.getP43());
                                preparedStatement.setBigDecimal(49, consBaseline.getP44());
                                preparedStatement.setBigDecimal(50, consBaseline.getP45());
                                preparedStatement.setBigDecimal(51, consBaseline.getP46());
                                preparedStatement.setBigDecimal(52, consBaseline.getP47());
                                preparedStatement.setBigDecimal(53, consBaseline.getP48());
                                preparedStatement.setBigDecimal(54, consBaseline.getP49());
                                preparedStatement.setBigDecimal(55, consBaseline.getP50());
                                preparedStatement.setBigDecimal(56, consBaseline.getP51());
                                preparedStatement.setBigDecimal(57, consBaseline.getP52());
                                preparedStatement.setBigDecimal(58, consBaseline.getP53());
                                preparedStatement.setBigDecimal(59, consBaseline.getP54());
                                preparedStatement.setBigDecimal(60, consBaseline.getP55());
                                preparedStatement.setBigDecimal(61, consBaseline.getP56());
                                preparedStatement.setBigDecimal(62, consBaseline.getP57());
                                preparedStatement.setBigDecimal(63, consBaseline.getP58());
                                preparedStatement.setBigDecimal(64, consBaseline.getP59());
                                preparedStatement.setBigDecimal(65, consBaseline.getP60());
                                preparedStatement.setBigDecimal(66, consBaseline.getP61());
                                preparedStatement.setBigDecimal(67, consBaseline.getP62());
                                preparedStatement.setBigDecimal(68, consBaseline.getP63());
                                preparedStatement.setBigDecimal(69, consBaseline.getP64());
                                preparedStatement.setBigDecimal(70, consBaseline.getP65());
                                preparedStatement.setBigDecimal(71, consBaseline.getP66());
                                preparedStatement.setBigDecimal(72, consBaseline.getP67());
                                preparedStatement.setBigDecimal(73, consBaseline.getP68());
                                preparedStatement.setBigDecimal(74, consBaseline.getP69());
                                preparedStatement.setBigDecimal(75, consBaseline.getP70());
                                preparedStatement.setBigDecimal(76, consBaseline.getP71());
                                preparedStatement.setBigDecimal(77, consBaseline.getP72());
                                preparedStatement.setBigDecimal(78, consBaseline.getP73());
                                preparedStatement.setBigDecimal(79, consBaseline.getP74());
                                preparedStatement.setBigDecimal(80, consBaseline.getP75());
                                preparedStatement.setBigDecimal(81, consBaseline.getP76());
                                preparedStatement.setBigDecimal(82, consBaseline.getP77());
                                preparedStatement.setBigDecimal(83, consBaseline.getP78());
                                preparedStatement.setBigDecimal(84, consBaseline.getP79());
                                preparedStatement.setBigDecimal(85, consBaseline.getP80());
                                preparedStatement.setBigDecimal(86, consBaseline.getP81());
                                preparedStatement.setBigDecimal(87, consBaseline.getP82());
                                preparedStatement.setBigDecimal(88, consBaseline.getP83());
                                preparedStatement.setBigDecimal(89, consBaseline.getP84());
                                preparedStatement.setBigDecimal(90, consBaseline.getP85());
                                preparedStatement.setBigDecimal(91, consBaseline.getP86());
                                preparedStatement.setBigDecimal(92, consBaseline.getP87());
                                preparedStatement.setBigDecimal(93, consBaseline.getP88());
                                preparedStatement.setBigDecimal(94, consBaseline.getP89());
                                preparedStatement.setBigDecimal(95, consBaseline.getP90());
                                preparedStatement.setBigDecimal(96, consBaseline.getP91());
                                preparedStatement.setBigDecimal(97, consBaseline.getP92());
                                preparedStatement.setBigDecimal(98, consBaseline.getP93());
                                preparedStatement.setBigDecimal(99, consBaseline.getP94());
                                preparedStatement.setBigDecimal(100, consBaseline.getP95());
                                preparedStatement.setBigDecimal(101, consBaseline.getP96());
                                preparedStatement.setBigDecimal(102, consBaseline.getMaxLoadBaseline());
                                preparedStatement.setBigDecimal(103, consBaseline.getMinLoadBaseline());
                                preparedStatement.setBigDecimal(104, consBaseline.getAvgLoadBaseline());
                                preparedStatement.setString(105, consBaseline.getNormal());
                                preparedStatement.setString(106, consBaseline.getExceptionRemark());
                                preparedStatement.setString(107, consBaseline.getCalRule());
                                preparedStatement.addBatch();
                                if ((k + 1) % 500 == 0 || k == insertList.size() - 1) {
                                    //每1000条提交一次
                                    preparedStatement.executeBatch();
                                    //清空记录
                                    preparedStatement.clearBatch();
                                }
                                k++;
                            }
                        }
                        log.info("客户基线保存成功,共" + insertList.size() + "条");
                        String sql3="UPDATE dr_cust_baseline_all  \n" +
                                "SET baseline_lib_id =?,cust_id =?,cust_name =?,baseline_date =?,simples_date=?,p1 =?,p2 =?,p3 =?,p4 =?,p5 =?,p6 =?,p7 =?,p8 =?,p9 =?,p10 =?,p11 =?,p12 =?,p13 =?,p14 =?,p15 =?,p16 =?,p17 =?,p18 =?,p19 =?,p20 =?,p21 =?,p22 =?,p23 =?,p24 =?,p25 =?,p26 =?,p27 =?,p28 =?,p29 =?,p30 =?,p31 =?,p32 =?,p33 =?,p34 =?,p35 =?,p36 =?,p37 =?,p38 =?,p39 =?,p40 =?,p41 =?,p42 =?,p43 =?,p44 =?,p45 =?,p46 =?,p47 =?,p48 =?,p49 =?,p50 =?,p51 =?,p52 =?,p53 =?,p54 =?,p55 =?,p56 =?,p57 =?,p58 =?,p59 =?,p60 =?,p61 =?,p62 =?,p63 =?,p64 =?,p65 =?,p66 =?,p67 =?,p68 =?,p69 =?,p70 =?,p71 =?,p72 =?,p73 =?,p74 =?,p75 =?,p76 =?,p77 =?,p78 =?,p79 =?,p80 =?,p81 =?,p82 =?,p83 =?,p84 =?,p85 =?,p86 =?,p87 =?,p88 =?,p89 =?,p90 =?,p91 =?,p92 =?,p93 =?,p94 =?,p95 =?,p96 =?,max_load_baseline=?,min_load_baseline=?,avg_load_baseline=?,normal=?,exception_remark=?,cal_rule=? \n" +
                                "  WHERE baseline_id =?";
                        if (null != updateList && updateList.size() > 0) {
                            int k = 0;
                            //2）、加载驱动，不需要显示注册驱动
                            Class.forName(driver);
                            //获取数据库连接
                            conn3 = DriverManager.getConnection(url, user, password);
                            conn3.setAutoCommit(false);
                            preparedStatement3 = conn3.prepareStatement(sql3);
                            for (CustBaseLineDetail consBaseline : updateList) {
                                preparedStatement3.setLong(1, consBaseline.getBaselineLibId());
                                if(null!=consBaseline.getCustId()) {
                                    preparedStatement3.setLong(2, consBaseline.getCustId());
                                } else {
                                    preparedStatement3.setNull(2, Types.BIGINT);
                                }
                                preparedStatement3.setString(3, consBaseline.getName());
                                if(null!=consBaseline.getBaselineDate()) {
                                    preparedStatement3.setString(4, simpleDateFormat.format(consBaseline.getBaselineDate()));
                                } else {
                                    preparedStatement3.setNull(4,Types.VARCHAR);
                                }
                                preparedStatement3.setString(5, consBaseline.getSimplesDate());
                                preparedStatement3.setBigDecimal(6, consBaseline.getP1());
                                preparedStatement3.setBigDecimal(7, consBaseline.getP2());
                                preparedStatement3.setBigDecimal(8, consBaseline.getP3());
                                preparedStatement3.setBigDecimal(9, consBaseline.getP4());
                                preparedStatement3.setBigDecimal(10, consBaseline.getP5());
                                preparedStatement3.setBigDecimal(11, consBaseline.getP6());
                                preparedStatement3.setBigDecimal(12, consBaseline.getP7());
                                preparedStatement3.setBigDecimal(13, consBaseline.getP8());
                                preparedStatement3.setBigDecimal(14, consBaseline.getP9());
                                preparedStatement3.setBigDecimal(15, consBaseline.getP10());
                                preparedStatement3.setBigDecimal(16, consBaseline.getP11());
                                preparedStatement3.setBigDecimal(17, consBaseline.getP12());
                                preparedStatement3.setBigDecimal(18, consBaseline.getP13());
                                preparedStatement3.setBigDecimal(19, consBaseline.getP14());
                                preparedStatement3.setBigDecimal(20, consBaseline.getP15());
                                preparedStatement3.setBigDecimal(21, consBaseline.getP16());
                                preparedStatement3.setBigDecimal(22, consBaseline.getP17());
                                preparedStatement3.setBigDecimal(23, consBaseline.getP18());
                                preparedStatement3.setBigDecimal(24, consBaseline.getP19());
                                preparedStatement3.setBigDecimal(25, consBaseline.getP20());
                                preparedStatement3.setBigDecimal(26, consBaseline.getP21());
                                preparedStatement3.setBigDecimal(27, consBaseline.getP22());
                                preparedStatement3.setBigDecimal(28, consBaseline.getP23());
                                preparedStatement3.setBigDecimal(29, consBaseline.getP24());
                                preparedStatement3.setBigDecimal(30, consBaseline.getP25());
                                preparedStatement3.setBigDecimal(31, consBaseline.getP26());
                                preparedStatement3.setBigDecimal(32, consBaseline.getP27());
                                preparedStatement3.setBigDecimal(33, consBaseline.getP28());
                                preparedStatement3.setBigDecimal(34, consBaseline.getP29());
                                preparedStatement3.setBigDecimal(35, consBaseline.getP30());
                                preparedStatement3.setBigDecimal(36, consBaseline.getP31());
                                preparedStatement3.setBigDecimal(37, consBaseline.getP32());
                                preparedStatement3.setBigDecimal(38, consBaseline.getP33());
                                preparedStatement3.setBigDecimal(39, consBaseline.getP34());
                                preparedStatement3.setBigDecimal(40, consBaseline.getP35());
                                preparedStatement3.setBigDecimal(41, consBaseline.getP36());
                                preparedStatement3.setBigDecimal(42, consBaseline.getP37());
                                preparedStatement3.setBigDecimal(43, consBaseline.getP38());
                                preparedStatement3.setBigDecimal(44, consBaseline.getP39());
                                preparedStatement3.setBigDecimal(45, consBaseline.getP40());
                                preparedStatement3.setBigDecimal(46, consBaseline.getP41());
                                preparedStatement3.setBigDecimal(47, consBaseline.getP42());
                                preparedStatement3.setBigDecimal(48, consBaseline.getP43());
                                preparedStatement3.setBigDecimal(49, consBaseline.getP44());
                                preparedStatement3.setBigDecimal(50, consBaseline.getP45());
                                preparedStatement3.setBigDecimal(51, consBaseline.getP46());
                                preparedStatement3.setBigDecimal(52, consBaseline.getP47());
                                preparedStatement3.setBigDecimal(53, consBaseline.getP48());
                                preparedStatement3.setBigDecimal(54, consBaseline.getP49());
                                preparedStatement3.setBigDecimal(55, consBaseline.getP50());
                                preparedStatement3.setBigDecimal(56, consBaseline.getP51());
                                preparedStatement3.setBigDecimal(57, consBaseline.getP52());
                                preparedStatement3.setBigDecimal(58, consBaseline.getP53());
                                preparedStatement3.setBigDecimal(59, consBaseline.getP54());
                                preparedStatement3.setBigDecimal(60, consBaseline.getP55());
                                preparedStatement3.setBigDecimal(61, consBaseline.getP56());
                                preparedStatement3.setBigDecimal(62, consBaseline.getP57());
                                preparedStatement3.setBigDecimal(63, consBaseline.getP58());
                                preparedStatement3.setBigDecimal(64, consBaseline.getP59());
                                preparedStatement3.setBigDecimal(65, consBaseline.getP60());
                                preparedStatement3.setBigDecimal(66, consBaseline.getP61());
                                preparedStatement3.setBigDecimal(67, consBaseline.getP62());
                                preparedStatement3.setBigDecimal(68, consBaseline.getP63());
                                preparedStatement3.setBigDecimal(69, consBaseline.getP64());
                                preparedStatement3.setBigDecimal(70, consBaseline.getP65());
                                preparedStatement3.setBigDecimal(71, consBaseline.getP66());
                                preparedStatement3.setBigDecimal(72, consBaseline.getP67());
                                preparedStatement3.setBigDecimal(73, consBaseline.getP68());
                                preparedStatement3.setBigDecimal(74, consBaseline.getP69());
                                preparedStatement3.setBigDecimal(75, consBaseline.getP70());
                                preparedStatement3.setBigDecimal(76, consBaseline.getP71());
                                preparedStatement3.setBigDecimal(77, consBaseline.getP72());
                                preparedStatement3.setBigDecimal(78, consBaseline.getP73());
                                preparedStatement3.setBigDecimal(79, consBaseline.getP74());
                                preparedStatement3.setBigDecimal(80, consBaseline.getP75());
                                preparedStatement3.setBigDecimal(81, consBaseline.getP76());
                                preparedStatement3.setBigDecimal(82, consBaseline.getP77());
                                preparedStatement3.setBigDecimal(83, consBaseline.getP78());
                                preparedStatement3.setBigDecimal(84, consBaseline.getP79());
                                preparedStatement3.setBigDecimal(85, consBaseline.getP80());
                                preparedStatement3.setBigDecimal(86, consBaseline.getP81());
                                preparedStatement3.setBigDecimal(87, consBaseline.getP82());
                                preparedStatement3.setBigDecimal(88, consBaseline.getP83());
                                preparedStatement3.setBigDecimal(89, consBaseline.getP84());
                                preparedStatement3.setBigDecimal(90, consBaseline.getP85());
                                preparedStatement3.setBigDecimal(91, consBaseline.getP86());
                                preparedStatement3.setBigDecimal(92, consBaseline.getP87());
                                preparedStatement3.setBigDecimal(93, consBaseline.getP88());
                                preparedStatement3.setBigDecimal(94, consBaseline.getP89());
                                preparedStatement3.setBigDecimal(95, consBaseline.getP90());
                                preparedStatement3.setBigDecimal(96, consBaseline.getP91());
                                preparedStatement3.setBigDecimal(97, consBaseline.getP92());
                                preparedStatement3.setBigDecimal(98, consBaseline.getP93());
                                preparedStatement3.setBigDecimal(99, consBaseline.getP94());
                                preparedStatement3.setBigDecimal(100, consBaseline.getP95());
                                preparedStatement3.setBigDecimal(101, consBaseline.getP96());
                                preparedStatement3.setBigDecimal(102, consBaseline.getMaxLoadBaseline());
                                preparedStatement3.setBigDecimal(103, consBaseline.getMinLoadBaseline());
                                preparedStatement3.setBigDecimal(104, consBaseline.getAvgLoadBaseline());
                                preparedStatement3.setString(105, consBaseline.getNormal());
                                preparedStatement3.setString(106, consBaseline.getExceptionRemark());
                                preparedStatement3.setString(107, consBaseline.getCalRule());
                                preparedStatement3.setLong(108,consBaseline.getBaselineId());
                                preparedStatement3.addBatch();
                                if ((k + 1) % 500 == 0 || k == updateList.size() - 1) {
                                    //每1000条提交一次
                                    preparedStatement3.executeBatch();
                                    //清空记录
                                    preparedStatement3.clearBatch();
                                }
                                k++;
                            }
                        }
                        log.info("客户基线更新成功,共" + updateList.size() + "条");
                        //更新用户基线同步状态
                        if(null!=baseLineDetailList && baseLineDetailList.size()>0) {
                            int j = 0;
                            String sql2 ="UPDATE dr_cons_baseline_all set state = ? WHERE baseline_id = ?";
                            conn2= DriverManager.getConnection(url,user,password);
                            conn2.setAutoCommit(false);
                            preparedStatement2 = conn2.prepareStatement(sql2);
                            for(ConsBaseline consBaseline : baseLineDetailList) {
                                preparedStatement2.setString(1,"Y");
                                preparedStatement2.setLong(2,consBaseline.getBaselineId());
                                preparedStatement2.addBatch();
                                if ((j + 1) % 500 == 0 || j == baseLineDetailList.size() - 1) {
                                    //每1000条提交一次
                                    preparedStatement2.executeBatch();
                                    //清空记录
                                    preparedStatement2.clearBatch();
                                }
                                j++;
                            }
                        }
                        log.info("更新用户基线同步状态成功,共" + baseLineDetailList.size() + "条");
                        //保存用户基线表
                        if(null!=conn1) {
                            conn1.commit();
                        }
                        if(null!=conn2) {
                            conn2.commit();
                        }
                        if(null!=conn3) {
                            conn3.commit();
                        }
                        log.info("客户基线计算完成！");

                    } catch (Exception e) {
                        try {
                            if(null!=conn1) {
                                conn1.rollback();
                            }
                            if(null!=conn2) {
                                conn2.rollback();
                            }
                            if(null!=conn3) {
                                conn3.rollback();
                            }
                        } catch (SQLException sqlException) {
                            sqlException.printStackTrace();
                        }
                        e.printStackTrace();
                    } finally {
                        if (null != conn1) {
                            try {
                                conn1.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (null != conn2) {
                            try {
                                conn2.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (null != preparedStatement2) {
                            try {
                                preparedStatement2.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }

                        if (null != preparedStatement) {
                            try {
                                preparedStatement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                        if (null != conn3) {
                            try {
                                conn3.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (null != preparedStatement3) {
                            try {
                                preparedStatement3.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if(null!=baseLine.getCustAllNum()) {
                            baseLine.setCustAllNum(baseLine.getCustAllNum()+1);
                            baseLineService.updateById(baseLine);
                        } else {
                            baseLine.setCustAllNum(1);
                            baseLineService.updateById(baseLine);
                        }
                    }
                }
            }

        };
        return runnable;

    }

    public void createBaseline(String param) {
        //一次只查询一个基线库任务
        List<PlanBaseLine> planBaseLines = planBaseLineService.getPlanBaseLineListAll();
        if(null==planBaseLines || planBaseLines.size()==0) {
            log.info("无基线计算任务");
            return;
        }
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(workQueue))
                .build();
        executor.execute(this.createBaseline(planBaseLines));
        executor.shutdown();
    }

    public Runnable createBaseline(List<PlanBaseLine> planBaseLines) {
        log.info(">>> "+"基线任务开始");
        Map<Integer, Method> consMethodMap = new HashMap<>();
        Map<Integer, Method> consMethodMap2 = new HashMap<>();
        Map<Integer, Method> consMethodMap3 = new HashMap<>();
        Map<Integer, Method> consMethodMap4 = new HashMap<>();
        Map<Integer, Method> consMethodMap5 = new HashMap<>();
        try{
            for (int j=1; j<=96; j++){
                consMethodMap.put(j, ConsBaseline.class.getMethod("setP"+j, BigDecimal.class));
                consMethodMap2.put(j, ConsCurve.class.getMethod("getP"+j));
                consMethodMap3.put(j, ConsCurve.class.getMethod("setP"+j, BigDecimal.class));
                consMethodMap4.put(j, EventPowerSample.class.getMethod("getP"+j));
                consMethodMap5.put(j, EventPowerSample.class.getMethod("setP"+j, BigDecimal.class));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        //获取用户列表
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PreparedStatement preparedStatement1 = null;
                PreparedStatement preparedStatement2 = null;
                PreparedStatement preparedStatement = null;
                PreparedStatement preparedStatement3 = null;
                PreparedStatement preparedStatement5 = null;
                Connection conn1 = null;
                Connection conn2 = null;
                Connection conn3 = null;
                Connection conn4 = null;
                Connection conn5 = null;
                if(null!=planBaseLines && planBaseLines.size()>0) {
                    //插入基线集合
                    List<ConsBaseline> insertList = new ArrayList<>();
                    //更新基线集合
                    List<ConsBaseline> updateList = new ArrayList<>();
                    //用户样本负荷
                    List<EventPowerSample> eventPowerSampleList = new ArrayList<>();
                    //查询所有用户
                    List<Cons> consList = consService.getConInfo();
                    String sampleDates = null;
                    Long baselinId= planBaseLines.get(0).getBaselinId();
                    BaseLine baseLine = baseLineService.getById(baselinId);
                    if(null!=baseLine) {
                        sampleDates = baseLine.getSimplesDate();
                    } else {
                        log.info("基线库为空");
                        return;
                    }
                    //开始下标
                    int startIndex = 1;
                    int endIndex = 96;
                    try {
                    //查询已有用户基线集合
                    List<ConsBaseline> consBaselines = baseLineService.getConsBaseLineByBaseLineIdAll(baseLine.getBaselinId());
                    //查询样本日期内的历史曲线集合
                    List<String> sampleDateList = Arrays.asList(sampleDates.split(","));
                    //List<ConsCurve> curveHisList = consCurveService.getCurveAllByDate(sampleDateList,sampleDateList.size());
                    LambdaQueryWrapper<ConsCurve> queryWrapperHis = new LambdaQueryWrapper<>();
                    queryWrapperHis.in(ConsCurve::getDataDate,sampleDateList);
                    List<String> cons = planBaseLines.stream().map(PlanBaseLine::getConsId).collect(Collectors.toList());
                    if(null!=cons && cons.size()>0) {
                        queryWrapperHis.in(ConsCurve::getConsId,cons);
                    }
                    List<Cons> consOne = null;
                    //查询补点后样本曲线是否存在
                   // List<EventPowerSample> eventPowerSampleListEx = consCurveService.getCurveAllAmendByDate(sampleDateList,cons,baselinId);
                    List<ConsCurve> curveHisList = consCurveService.list(queryWrapperHis);
                    log.info("补点开始");
                    curveHisList = getPoint(curveHisList,consMethodMap2,consMethodMap3,startIndex,endIndex,consMethodMap4,new ArrayList<>());
                    log.info("补点结束");
                    //用户历史负荷
                    List<ConsCurve> consCurveList = null;
                    List<EventPowerSample> eventPowerSampleUpdateList = new ArrayList<>();
                    log.info("循环计算开始");
                    for (PlanBaseLine planBaseLine : planBaseLines) {
                        //样本有效，生成负荷基线
                        ConsBaseline consCurveBase = new ConsBaseline();
                        StringBuffer simpleId = new StringBuffer(210);
                        //用户档案
                        consOne = consList.stream().filter(con -> planBaseLine.getConsId().equals(con.getId())).collect(Collectors.toList());
                        if (null == consOne || consOne.size() == 0) {
                            if (null == planBaseLine.getFailTimes()) {
                                planBaseLine.setFailTimes(0);
                            }
                            planBaseLine.setFailTimes(planBaseLine.getFailTimes() + 1);
                            planBaseLine.setBaselineStatus("3");
                            planBaseLine.setBaselineDesc(ConsBaseLineExceptionEnum.NO_CONS_EXCEPTION.getMessage());
                            log.error(">>>" + ConsBaseLineExceptionEnum.NO_CONS_EXCEPTION.getMessage());
                            continue;
                        }
                        consCurveList = curveHisList.stream().filter(con -> con.getConsId().equals(planBaseLine.getConsId())
                        ).collect(Collectors.toList());
                        if (null == consCurveList || consCurveList.size() == 0) {
                            planBaseLine.setBaselineStatus("3");
                            planBaseLine.setBaselineDesc("无历史负荷");
                            if (null == planBaseLine.getFailTimes()) {
                                planBaseLine.setFailTimes(0);
                            }
                            planBaseLine.setFailTimes(planBaseLine.getFailTimes() + 1);
                            continue;
                        } else {
                            //用户负荷样本集合
                            for (ConsCurve consCurve : consCurveList) {
                                /*EventPowerSample eventPowerSample = null;
                                List<EventPowerSample> eventPowerSamples = eventPowerSampleListEx.stream().filter(con ->
                                        consCurve.getConsId().equals(con.getConsId()) && consCurve.getDataDate().equals(con.getDataDate()) && baselinId.equals(con.getBaselineId())).collect(Collectors.toList());
                                if(null!=eventPowerSamples && eventPowerSamples.size()>0) {
                                    eventPowerSample = eventPowerSamples.get(0);
                                    if(null!=eventPowerSample) {
                                        for(int index = startIndex ; index <= endIndex ; index++){
                                            BigDecimal point = (BigDecimal)consMethodMap2.get(index).invoke(consCurve);
                                            consMethodMap5.get(index).invoke(eventPowerSample,point);
                                        }
                                        //BeanUtils.copyProperties(consCurve, eventPowerSample);
                                        eventPowerSample.setConsId(planBaseLine.getConsId());
                                        eventPowerSample.setIsValid(YesOrNotEnum.Y.getCode());
                                        //eventPowerSampleUpdateList.add(eventPowerSample);
                                        if ("".equals(simpleId.toString())) {
                                            simpleId = simpleId.append(eventPowerSample.getId());
                                        } else {
                                            simpleId = simpleId.append(",").append(eventPowerSample.getId());
                                        }
                                        eventPowerSampleUpdateList.add(eventPowerSample);
                                    } else {
                                        eventPowerSample = new EventPowerSample();
                                        BeanUtils.copyProperties(consCurve, eventPowerSample);
                                        eventPowerSample.setConsId(planBaseLine.getConsId());
                                        eventPowerSample.setIsValid(YesOrNotEnum.Y.getCode());
                                        eventPowerSample.setBaselineId(baselinId);
                                        //生成主键
                                        long id = IdWorker.getId();
                                        eventPowerSample.setId(id);
                                        if ("".equals(simpleId.toString())) {
                                            simpleId = simpleId.append(id);
                                        } else {
                                            simpleId = simpleId.append(",").append(id);
                                        }
                                        eventPowerSampleList.add(eventPowerSample);
                                    }
                                } else {
                                    eventPowerSample = new EventPowerSample();
                                    BeanUtils.copyProperties(consCurve, eventPowerSample);
                                    eventPowerSample.setConsId(planBaseLine.getConsId());
                                    eventPowerSample.setIsValid(YesOrNotEnum.Y.getCode());
                                    eventPowerSample.setBaselineId(baselinId);
                                    //生成主键
                                    long id = IdWorker.getId();
                                    eventPowerSample.setId(id);
                                    if ("".equals(simpleId.toString())) {
                                        simpleId = simpleId.append(id);
                                    } else {
                                        simpleId = simpleId.append(",").append(id);
                                    }
                                    eventPowerSampleList.add(eventPowerSample);
                                }*/
                                EventPowerSample eventPowerSample = new EventPowerSample();
                                BeanUtils.copyProperties(consCurve, eventPowerSample);
                                eventPowerSample.setConsId(planBaseLine.getConsId());
                                eventPowerSample.setIsValid(YesOrNotEnum.Y.getCode());
                                eventPowerSample.setBaselineId(baselinId);
                                //生成主键
                                long id = IdWorker.getId();
                                eventPowerSample.setId(id);
                                if ("".equals(simpleId.toString())) {
                                    simpleId = simpleId.append(id);
                                } else {
                                    simpleId = simpleId.append(",").append(id);
                                }
                                eventPowerSampleList.add(eventPowerSample);
                            }
                        }
                        //根据条件刷选用户基线
                        List<ConsBaseline> existConsCurveBases = consBaselines.stream().filter(con ->
                                planBaseLine.getConsId().equals(con.getConsId()) && planBaseLine.getBaselinId().equals(con.getBaselineLibId())).collect(Collectors.toList());
                        ConsBaseline existConsCurveBase = null;
                        if (null != planBaseLine.getBaseLineCal() && !"".equals(planBaseLine.getBaseLineCal())) {
                            if ("1".equals(planBaseLine.getBaseLineCal())) {
                                //蒙东基线计算规则
                                if (null == consOne.get(0)) {
                                    if (null == planBaseLine.getFailTimes()) {
                                        planBaseLine.setFailTimes(0);
                                    }
                                    planBaseLine.setFailTimes(planBaseLine.getFailTimes() + 1);
                                    planBaseLine.setBaselineStatus("3");
                                    planBaseLine.setBaselineDesc(ConsBaseLineExceptionEnum.NO_CONS_EXCEPTION.getMessage());
                                    continue;
                                }
                                //历史负荷
                                consCurveList = curveHisList.stream().filter(con -> con.getConsId().equals(planBaseLine.getConsId())).collect(Collectors.toList());
                                if (null == consCurveList || consCurveList.size() == 0) {
                                    planBaseLine.setBaselineStatus("3");
                                    planBaseLine.setBaselineDesc("无历史负荷");
                                    if (null == planBaseLine.getFailTimes()) {
                                        planBaseLine.setFailTimes(0);
                                    }
                                    planBaseLine.setFailTimes(planBaseLine.getFailTimes() + 1);
                                    continue;
                                }
                                List<BigDecimal> validSamplePowerList = new ArrayList<>();
                                for (int i = startIndex; i <= endIndex; i++) {
                                    BigDecimal total = BigDecimal.ZERO;
                                    int j = 1;
                                    for (ConsCurve consCurve : consCurveList) {
                                        //去除0值和null值数据
                                        BigDecimal consCurveHis = (BigDecimal) consMethodMap2.get(i).invoke(consCurve);
                                        if (null != consCurveHis) {
                                            total = NumberUtil.add(total,consCurveHis);
                                            if(j==consCurveList.size()) {
                                                total = NumberUtil.div(total,(consCurveList.size()));
                                                consMethodMap.get(i).invoke(consCurveBase,total);
                                                validSamplePowerList.add(total);
                                            }
                                            //validSamplePowerList.add((BigDecimal) ReflectUtil.getFieldValue(consCurve, "p" + i));
                                        }
                                        j++;
                                    }
                                }
                                //设置基线值
                                consCurveBase.setConsId(planBaseLine.getConsId());
                                consCurveBase.setBaselineLibId(planBaseLine.getBaselinId());
                                consCurveBase.setConsName(consOne.get(0).getConsName());
                                consCurveBase.setSimplesDate(sampleDates);
                                consCurveBase.setSimplesId(simpleId.toString());
                                if (validSamplePowerList.size() > 0) {
                                    BigDecimal sumSamplePower = validSamplePowerList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                                    BigDecimal avgSamplePower = NumberUtil.div(sumSamplePower, validSamplePowerList.size(), 2);
                                    //负荷基线
                                    consCurveBase.setMaxLoadBaseline(CollectionUtil.max(validSamplePowerList));
                                    consCurveBase.setMinLoadBaseline(CollectionUtil.min(validSamplePowerList));
                                    consCurveBase.setAvgLoadBaseline(avgSamplePower);
                                    consCurveBase.setNormal("Y");
                                    consCurveBase.setCalRule("2");

                                    //基线任务
                                    planBaseLine.setBaselineStatus("2");
                                    planBaseLine.setFailTimes(0);
                                } else {
                                    consCurveBase.setNormal("N");
                                    consCurveBase.setExceptionRemark("无有效负荷");
                                    planBaseLine.setBaselineStatus("3");
                                    if (null == planBaseLine.getFailTimes()) {
                                        planBaseLine.setFailTimes(0);
                                    }
                                    planBaseLine.setFailTimes(planBaseLine.getFailTimes() + 1);
                                    planBaseLine.setBaselineDesc("无有效负荷");
                                    continue;
                                }
                                //判断基线表是否存在
                                if(null != existConsCurveBases && existConsCurveBases.size()>0) {
                                    existConsCurveBase = existConsCurveBases.get(0);
                                    SpringUtil.copyPropertiesIgnoreNull(consCurveBase, existConsCurveBase);
                                    updateList.add(existConsCurveBase);
                                    planBaseLine.setBaselineCapId(existConsCurveBase.getBaselineId());
                                    planBaseLine.setBaselineDesc("");
                                } else {
                                    consCurveBase.setBaselineDate(baseLine.getGenerateDate());
                                    insertList.add(consCurveBase);
                                    //生成主键
                                    consCurveBase.setBaselineId(IdWorker.getId());
                                    planBaseLine.setBaselineCapId(consCurveBase.getBaselineId());
                                    planBaseLine.setBaselineDesc("");

                                }
                            } else if ("2".equals(planBaseLine.getBaseLineCal())) {
                                //安徽基线计算规则
                                if (null == consOne.get(0)) {
                                    if (null == planBaseLine.getFailTimes()) {
                                        planBaseLine.setFailTimes(0);
                                    }
                                    planBaseLine.setFailTimes(planBaseLine.getFailTimes() + 1);
                                    planBaseLine.setBaselineStatus("3");
                                    planBaseLine.setBaselineDesc(ConsBaseLineExceptionEnum.NO_CONS_EXCEPTION.getMessage());
                                    continue;
                                }
                                //历史负荷
                                consCurveList = curveHisList.stream().filter(con -> con.getConsId().equals(planBaseLine.getConsId())).collect(Collectors.toList());
                                if (null == consCurveList || consCurveList.size() == 0) {
                                    planBaseLine.setBaselineStatus("3");
                                    planBaseLine.setBaselineDesc("无历史负荷");
                                    if (null == planBaseLine.getFailTimes()) {
                                        planBaseLine.setFailTimes(0);
                                    }
                                    planBaseLine.setFailTimes(planBaseLine.getFailTimes() + 1);
                                    continue;
                                }
                                List<BigDecimal> validSamplePowerList = new ArrayList<>();
                                for (int i = startIndex; i <= endIndex; i++) {
                                    BigDecimal total = BigDecimal.ZERO;
                                    int j = 1;
                                    for (ConsCurve consCurve : consCurveList) {
                                        //去除0值和null值数据
                                        BigDecimal consCurveHis = (BigDecimal) consMethodMap2.get(i).invoke(consCurve);
                                        if (null != consCurveHis) {
                                            total = NumberUtil.add(total,consCurveHis);
                                            if(j==consCurveList.size()) {
                                                total = NumberUtil.div(total,(consCurveList.size()));
                                                consMethodMap.get(i).invoke(consCurveBase,total);
                                                validSamplePowerList.add(total);
                                            }
                                            //validSamplePowerList.add((BigDecimal) ReflectUtil.getFieldValue(consCurve, "p" + i));
                                        }
                                        j++;
                                    }
                                }
                                consCurveBase.setConsId(planBaseLine.getConsId());
                                consCurveBase.setBaselineLibId(planBaseLine.getBaselinId());
                                consCurveBase.setConsName(consOne.get(0).getConsName());
                                consCurveBase.setSimplesDate(sampleDates);
                                consCurveBase.setSimplesId(simpleId.toString());
                                if (validSamplePowerList.size() > 0) {
                                    BigDecimal sumSamplePower = validSamplePowerList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                                    BigDecimal avgSamplePower = NumberUtil.div(sumSamplePower, validSamplePowerList.size(), 2);
                                    //负荷基线
                                    consCurveBase.setMaxLoadBaseline(CollectionUtil.max(validSamplePowerList));
                                    consCurveBase.setMinLoadBaseline(CollectionUtil.min(validSamplePowerList));
                                    consCurveBase.setAvgLoadBaseline(avgSamplePower);
                                    consCurveBase.setNormal("Y");
                                    consCurveBase.setCalRule("2");

                                    //基线任务
                                    planBaseLine.setBaselineStatus("2");
                                    planBaseLine.setFailTimes(0);
                                } else {
                                    consCurveBase.setNormal("N");
                                    consCurveBase.setExceptionRemark("无有效负荷");
                                    planBaseLine.setBaselineStatus("3");
                                    if (null == planBaseLine.getFailTimes()) {
                                        planBaseLine.setFailTimes(0);
                                    }
                                    planBaseLine.setFailTimes(planBaseLine.getFailTimes() + 1);
                                    planBaseLine.setBaselineDesc("无有效负荷");
                                    continue;
                                }
                                //判断基线表是否存在
                                if(null != existConsCurveBases && existConsCurveBases.size()>0) {
                                    existConsCurveBase = existConsCurveBases.get(0);
                                    SpringUtil.copyPropertiesIgnoreNull(consCurveBase, existConsCurveBase);
                                    updateList.add(existConsCurveBase);
                                    planBaseLine.setBaselineCapId(existConsCurveBase.getBaselineId());
                                    planBaseLine.setBaselineDesc("");
                                } else {
                                    consCurveBase.setBaselineDate(baseLine.getGenerateDate());
                                    insertList.add(consCurveBase);
                                    //生成主键
                                    consCurveBase.setBaselineId(IdWorker.getId());
                                    planBaseLine.setBaselineCapId(consCurveBase.getBaselineId());
                                    planBaseLine.setBaselineDesc("");

                                }
                            } else {
                                planBaseLine.setBaselineStatus("3");
                                if (null == planBaseLine.getFailTimes()) {
                                    planBaseLine.setFailTimes(0);
                                }
                                planBaseLine.setFailTimes(planBaseLine.getFailTimes() + 1);
                                planBaseLine.setBaselineStatus("3");
                                planBaseLine.setBaselineDesc(ConsBaseLineExceptionEnum.UNDEFINE_RULE_EXCEPTION.getMessage());
                                log.error(">>> " + ConsBaseLineExceptionEnum.UNDEFINE_RULE_EXCEPTION.getMessage());
                                continue;
                            }
                        } else {
                            planBaseLine.setBaselineStatus("3");
                            if(null==planBaseLine.getFailTimes()) {
                                planBaseLine.setFailTimes(0);
                            }
                            planBaseLine.setFailTimes(planBaseLine.getFailTimes()+1);
                            planBaseLine.setBaselineDesc(ConsBaseLineExceptionEnum.NORULE_EXCEPTION.getMessage());
                            continue;
                        }
                    }
                        log.info("循环计算结束");
                    String sql = "INSERT INTO dr_cons_baseline_all (\n" +
                            "\tbaseline_lib_id , cons_id , cons_name , baseline_date , simples_date , p1 , p2 , p3 , p4 , p5 , p6 , p7 , p8 , p9 , p10 , p11 , p12 , p13 , p14 , p15 , p16 , p17 , p18 , p19 , p20 , p21 , p22 , p23 , p24 , p25 , p26 , p27 , p28 , p29 , p30 , p31 , p32 , p33 , p34 , p35 , p36 , p37 , p38 , p39 , p40 , p41 , p42 , p43 , p44 , p45 , p46 , p47 , p48 , p49 , p50 , p51 , p52 , p53 , p54 , p55 , p56 , p57 , p58 , p59 , p60 , p61 , p62 , p63 , p64 , p65 , p66 , p67 , p68 , p69 , p70 , p71 , p72 , p73 , p74 , p75 , p76 , p77 , p78 , p79 , p80 , p81 , p82 , p83 , p84 , p85 , p86 , p87 , p88 , p89 , p90 , p91 , p92 , p93 , p94 , p95 , p96 , max_load_baseline , min_load_baseline , avg_load_baseline , normal , exception_remark , simples_id , cal_rule , baseline_id \n" +
                            ")\n" +
                            "VALUES\n" +
                            "\t(? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    String sql1 ="update dr_plan_baseline_task_all set BASELINE_STATUS=?,BASELINE_DESC=?,BASELINE_CAP_ID=?,FAIL_TIMES=?,UPDATE_TIME=? where id=?";

                    String sql2 = "INSERT INTO dr_event_power_sample_all (\n" +
                            "\tdata_id , cons_id , data_date , data_point_flag , is_valid , p1 , p2 , p3 , p4 , p5 , p6 , p7 , p8 , p9 , p10 , p11 , p12 , p13 , p14 , p15 , p16 , p17 , p18 , p19 , p20 , p21 , p22 , p23 , p24 , p25 , p26 , p27 , p28 , p29 , p30 , p31 , p32 , p33 , p34 , p35 , p36 , p37 , p38 , p39 , p40 , p41 , p42 , p43 , p44 , p45 , p46 , p47 , p48 , p49 , p50 , p51 , p52 , p53 , p54 , p55 , p56 , p57 , p58 , p59 , p60 , p61 , p62 , p63 , p64 , p65 , p66 , p67 , p68 , p69 , p70 , p71 , p72 , p73 , p74 , p75 , p76 , p77 , p78 , p79 , p80 , p81 , p82 , p83 , p84 , p85 , p86 , p87 , p88 , p89 , p90 , p91 , p92 , p93 , p94 , p95 , p96 , id , baseline_id \n" +
                            ")\n" +
                            "VALUES\n" +
                            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    String sql5 = "UPDATE dr_event_power_sample_all \n" +
                            "SET data_id =?, cons_id =?, data_date =?, data_point_flag =?, is_valid =?, p1 =?, p2 =?, p3 =?, p4 =?, p5 =?, p6 =?, p7 =?, p8 =?, p9 =?, p10 =?, p11 =?, p12 =?, p13 =?, p14 =?, p15 =?, p16 =?, p17 =?, p18 =?, p19 =?, p20 =?, p21 =?, p22 =?, p23 =?, p24 =?, p25 =?, p26 =?, p27 =?, p28 =?, p29 =?, p30 =?, p31 =?, p32 =?, p33 =?, p34 =?, p35 =?, p36 =?, p37 =?, p38 =?, p39 =?, p40 =?, p41 =?, p42 =?, p43 =?, p44 =?, p45 =?, p46 =?, p47 =?, p48 =?, p49 =?, p50 =?, p51 =?, p52 =?, p53 =?, p54 =?, p55 =?, p56 =?, p57 =?, p58 =?, p59 =?, p60 =?, p61 =?, p62 =?, p63 =?, p64 =?, p65 =?, p66 =?, p67 =?, p68 =?, p69 =?, p70 =?, p71 =?, p72 =?, p73 =?, p74 =?, p75 =?, p76 =?, p77 =?, p78 =?, p79 =?, p80 =?, p81 =?, p82 =?, p83 =?, p84 =?, p85 =?, p86 =?, p87 =?, p88 =?, p89 =?, p90 =?, p91 =?, p92 =?, p93 =?, p94 =?, p95 =?, p96 =? , baseline_id=? \n" +
                            "WHERE id =?";

                    String sql4 = "UPDATE dr_cons_baseline_all \n" +
                    "SET baseline_lib_id =?, cons_id =?, cons_name =?, baseline_date =?, simples_date =?, p1 =?, p2 =?, p3 =?, p4 =?, p5 =?, p6 =?, p7 =?, p8 =?, p9 =?, p10 =?, p11 =?, p12 =?, p13 =?, p14 =?, p15 =?, p16 =?, p17 =?, p18 =?, p19 =?, p20 =?, p21 =?, p22 =?, p23 =?, p24 =?, p25 =?, p26 =?, p27 =?, p28 =?, p29 =?, p30 =?, p31 =?, p32 =?, p33 =?, p34 =?, p35 =?, p36 =?, p37 =?, p38 =?, p39 =?, p40 =?, p41 =?, p42 =?, p43 =?, p44 =?, p45 =?, p46 =?, p47 =?, p48 =?, p49 =?, p50 =?, p51 =?, p52 =?, p53 =?, p54 =?, p55 =?, p56 =?, p57 =?, p58 =?, p59 =?, p60 =?, p61 =?, p62 =?, p63 =?, p64 =?, p65 =?, p66 =?, p67 =?, p68 =?, p69 =?, p70 =?, p71 =?, p72 =?, p73 =?, p74 =?, p75 =?, p76 =?, p77 =?, p78 =?, p79 =?, p80 =?, p81 =?, p82 =?, p83 =?, p84 =?, p85 =?, p86 =?, p87 =?, p88 =?, p89 =?, p90 =?, p91 =?, p92 =?, p93 =?, p94 =?, p95 =?, p96 =?, max_load_baseline =?, min_load_baseline =?, avg_load_baseline =?, normal =?, exception_remark =?, simples_id =?, cal_rule =? \n" +
                    "WHERE\n" +
                    "\tbaseline_id =?";
                        String url=dataurl;
                        String user=userName;
                        String password=datapassword;
                        //保存样本曲线
                        if(null!=eventPowerSampleList && eventPowerSampleList.size()>0) {
                            int j=0;
                            //2）、加载驱动，不需要显示注册驱动
                            Class.forName(driver);
                            //获取数据库连接
                            conn1= DriverManager.getConnection(url,user,password);
                            conn1.setAutoCommit(false);
                            preparedStatement2 = conn1.prepareStatement(sql2);
                            for(EventPowerSample eventPowerSample : eventPowerSampleList) {
                                if(null!=eventPowerSample.getDataId()) {
                                    preparedStatement2.setLong(1, eventPowerSample.getDataId());
                                } else {
                                    preparedStatement2.setNull(1, Types.BIGINT);
                                }
                                preparedStatement2.setString(2,eventPowerSample.getConsId());
                                preparedStatement2.setString(3,simpleDateFormat.format(eventPowerSample.getDataDate()));
                                preparedStatement2.setString(4,eventPowerSample.getDataPointFlag());
                                preparedStatement2.setString(5,eventPowerSample.getIsValid());
                                preparedStatement2.setBigDecimal(6,eventPowerSample.getP1());
                                preparedStatement2.setBigDecimal(7,eventPowerSample.getP2());
                                preparedStatement2.setBigDecimal(8,eventPowerSample.getP3());
                                preparedStatement2.setBigDecimal(9,eventPowerSample.getP4());
                                preparedStatement2.setBigDecimal(10,eventPowerSample.getP5());
                                preparedStatement2.setBigDecimal(11,eventPowerSample.getP6());
                                preparedStatement2.setBigDecimal(12,eventPowerSample.getP7());
                                preparedStatement2.setBigDecimal(13,eventPowerSample.getP8());
                                preparedStatement2.setBigDecimal(14,eventPowerSample.getP9());
                                preparedStatement2.setBigDecimal(15,eventPowerSample.getP10());
                                preparedStatement2.setBigDecimal(16,eventPowerSample.getP11());
                                preparedStatement2.setBigDecimal(17,eventPowerSample.getP12());
                                preparedStatement2.setBigDecimal(18,eventPowerSample.getP13());
                                preparedStatement2.setBigDecimal(19,eventPowerSample.getP14());
                                preparedStatement2.setBigDecimal(20,eventPowerSample.getP15());
                                preparedStatement2.setBigDecimal(21,eventPowerSample.getP16());
                                preparedStatement2.setBigDecimal(22,eventPowerSample.getP17());
                                preparedStatement2.setBigDecimal(23,eventPowerSample.getP18());
                                preparedStatement2.setBigDecimal(24,eventPowerSample.getP19());
                                preparedStatement2.setBigDecimal(25,eventPowerSample.getP20());
                                preparedStatement2.setBigDecimal(26,eventPowerSample.getP21());
                                preparedStatement2.setBigDecimal(27,eventPowerSample.getP22());
                                preparedStatement2.setBigDecimal(28,eventPowerSample.getP23());
                                preparedStatement2.setBigDecimal(29,eventPowerSample.getP24());
                                preparedStatement2.setBigDecimal(30,eventPowerSample.getP25());
                                preparedStatement2.setBigDecimal(31,eventPowerSample.getP26());
                                preparedStatement2.setBigDecimal(32,eventPowerSample.getP27());
                                preparedStatement2.setBigDecimal(33,eventPowerSample.getP28());
                                preparedStatement2.setBigDecimal(34,eventPowerSample.getP29());
                                preparedStatement2.setBigDecimal(35,eventPowerSample.getP30());
                                preparedStatement2.setBigDecimal(36,eventPowerSample.getP31());
                                preparedStatement2.setBigDecimal(37,eventPowerSample.getP32());
                                preparedStatement2.setBigDecimal(38,eventPowerSample.getP33());
                                preparedStatement2.setBigDecimal(39,eventPowerSample.getP34());
                                preparedStatement2.setBigDecimal(40,eventPowerSample.getP35());
                                preparedStatement2.setBigDecimal(41,eventPowerSample.getP36());
                                preparedStatement2.setBigDecimal(42,eventPowerSample.getP37());
                                preparedStatement2.setBigDecimal(43,eventPowerSample.getP38());
                                preparedStatement2.setBigDecimal(44,eventPowerSample.getP39());
                                preparedStatement2.setBigDecimal(45,eventPowerSample.getP40());
                                preparedStatement2.setBigDecimal(46,eventPowerSample.getP41());
                                preparedStatement2.setBigDecimal(47,eventPowerSample.getP42());
                                preparedStatement2.setBigDecimal(48,eventPowerSample.getP43());
                                preparedStatement2.setBigDecimal(49,eventPowerSample.getP44());
                                preparedStatement2.setBigDecimal(50,eventPowerSample.getP45());
                                preparedStatement2.setBigDecimal(51,eventPowerSample.getP46());
                                preparedStatement2.setBigDecimal(52,eventPowerSample.getP47());
                                preparedStatement2.setBigDecimal(53,eventPowerSample.getP48());
                                preparedStatement2.setBigDecimal(54,eventPowerSample.getP49());
                                preparedStatement2.setBigDecimal(55,eventPowerSample.getP50());
                                preparedStatement2.setBigDecimal(56,eventPowerSample.getP51());
                                preparedStatement2.setBigDecimal(57,eventPowerSample.getP52());
                                preparedStatement2.setBigDecimal(58,eventPowerSample.getP53());
                                preparedStatement2.setBigDecimal(59,eventPowerSample.getP54());
                                preparedStatement2.setBigDecimal(60,eventPowerSample.getP55());
                                preparedStatement2.setBigDecimal(61,eventPowerSample.getP56());
                                preparedStatement2.setBigDecimal(62,eventPowerSample.getP57());
                                preparedStatement2.setBigDecimal(63,eventPowerSample.getP58());
                                preparedStatement2.setBigDecimal(64,eventPowerSample.getP59());
                                preparedStatement2.setBigDecimal(65,eventPowerSample.getP60());
                                preparedStatement2.setBigDecimal(66,eventPowerSample.getP61());
                                preparedStatement2.setBigDecimal(67,eventPowerSample.getP62());
                                preparedStatement2.setBigDecimal(68,eventPowerSample.getP63());
                                preparedStatement2.setBigDecimal(69,eventPowerSample.getP64());
                                preparedStatement2.setBigDecimal(70,eventPowerSample.getP65());
                                preparedStatement2.setBigDecimal(71,eventPowerSample.getP66());
                                preparedStatement2.setBigDecimal(72,eventPowerSample.getP67());
                                preparedStatement2.setBigDecimal(73,eventPowerSample.getP68());
                                preparedStatement2.setBigDecimal(74,eventPowerSample.getP69());
                                preparedStatement2.setBigDecimal(75,eventPowerSample.getP70());
                                preparedStatement2.setBigDecimal(76,eventPowerSample.getP71());
                                preparedStatement2.setBigDecimal(77,eventPowerSample.getP72());
                                preparedStatement2.setBigDecimal(78,eventPowerSample.getP73());
                                preparedStatement2.setBigDecimal(79,eventPowerSample.getP74());
                                preparedStatement2.setBigDecimal(80,eventPowerSample.getP75());
                                preparedStatement2.setBigDecimal(81,eventPowerSample.getP76());
                                preparedStatement2.setBigDecimal(82,eventPowerSample.getP77());
                                preparedStatement2.setBigDecimal(83,eventPowerSample.getP78());
                                preparedStatement2.setBigDecimal(84,eventPowerSample.getP79());
                                preparedStatement2.setBigDecimal(85,eventPowerSample.getP80());
                                preparedStatement2.setBigDecimal(86,eventPowerSample.getP81());
                                preparedStatement2.setBigDecimal(87,eventPowerSample.getP82());
                                preparedStatement2.setBigDecimal(88,eventPowerSample.getP83());
                                preparedStatement2.setBigDecimal(89,eventPowerSample.getP84());
                                preparedStatement2.setBigDecimal(90,eventPowerSample.getP85());
                                preparedStatement2.setBigDecimal(91,eventPowerSample.getP86());
                                preparedStatement2.setBigDecimal(92,eventPowerSample.getP87());
                                preparedStatement2.setBigDecimal(93,eventPowerSample.getP88());
                                preparedStatement2.setBigDecimal(94,eventPowerSample.getP89());
                                preparedStatement2.setBigDecimal(95,eventPowerSample.getP90());
                                preparedStatement2.setBigDecimal(96,eventPowerSample.getP91());
                                preparedStatement2.setBigDecimal(97,eventPowerSample.getP92());
                                preparedStatement2.setBigDecimal(98,eventPowerSample.getP93());
                                preparedStatement2.setBigDecimal(99,eventPowerSample.getP94());
                                preparedStatement2.setBigDecimal(100,eventPowerSample.getP95());
                                preparedStatement2.setBigDecimal(101,eventPowerSample.getP96());
                                preparedStatement2.setLong(102,eventPowerSample.getId());
                                preparedStatement2.setLong(103,eventPowerSample.getBaselineId());
                                preparedStatement2.addBatch();
                                if((j+1)%500 == 0 || j == eventPowerSampleList.size()-1) {
                                    //每1000条提交一次
                                    preparedStatement2.executeBatch();
                                    //清空记录
                                    preparedStatement2.clearBatch();
                                }
                                j++;
                            }
                        }
                        log.info("96点样本曲线保存成功,共"+eventPowerSampleList.size()+"条");
                        /*if(null!=eventPowerSampleUpdateList && eventPowerSampleUpdateList.size()>0) {
                            int j = 0;
                            conn5= DriverManager.getConnection(url,user,password);
                            conn5.setAutoCommit(false);
                            preparedStatement5 = conn5.prepareStatement(sql5);
                            for(EventPowerSample eventPowerSample : eventPowerSampleUpdateList) {
                                if(null!=eventPowerSample.getDataId()) {
                                    preparedStatement5.setLong(1, eventPowerSample.getDataId());
                                } else {
                                    preparedStatement5.setNull(1, Types.BIGINT);
                                }
                                preparedStatement5.setString(2,eventPowerSample.getConsId());
                                preparedStatement5.setString(3,simpleDateFormat.format(eventPowerSample.getDataDate()));
                                preparedStatement5.setString(4,eventPowerSample.getDataPointFlag());
                                preparedStatement5.setString(5,eventPowerSample.getIsValid());
                                preparedStatement5.setBigDecimal(6,eventPowerSample.getP1());
                                preparedStatement5.setBigDecimal(7,eventPowerSample.getP2());
                                preparedStatement5.setBigDecimal(8,eventPowerSample.getP3());
                                preparedStatement5.setBigDecimal(9,eventPowerSample.getP4());
                                preparedStatement5.setBigDecimal(10,eventPowerSample.getP5());
                                preparedStatement5.setBigDecimal(11,eventPowerSample.getP6());
                                preparedStatement5.setBigDecimal(12,eventPowerSample.getP7());
                                preparedStatement5.setBigDecimal(13,eventPowerSample.getP8());
                                preparedStatement5.setBigDecimal(14,eventPowerSample.getP9());
                                preparedStatement5.setBigDecimal(15,eventPowerSample.getP10());
                                preparedStatement5.setBigDecimal(16,eventPowerSample.getP11());
                                preparedStatement5.setBigDecimal(17,eventPowerSample.getP12());
                                preparedStatement5.setBigDecimal(18,eventPowerSample.getP13());
                                preparedStatement5.setBigDecimal(19,eventPowerSample.getP14());
                                preparedStatement5.setBigDecimal(20,eventPowerSample.getP15());
                                preparedStatement5.setBigDecimal(21,eventPowerSample.getP16());
                                preparedStatement5.setBigDecimal(22,eventPowerSample.getP17());
                                preparedStatement5.setBigDecimal(23,eventPowerSample.getP18());
                                preparedStatement5.setBigDecimal(24,eventPowerSample.getP19());
                                preparedStatement5.setBigDecimal(25,eventPowerSample.getP20());
                                preparedStatement5.setBigDecimal(26,eventPowerSample.getP21());
                                preparedStatement5.setBigDecimal(27,eventPowerSample.getP22());
                                preparedStatement5.setBigDecimal(28,eventPowerSample.getP23());
                                preparedStatement5.setBigDecimal(29,eventPowerSample.getP24());
                                preparedStatement5.setBigDecimal(30,eventPowerSample.getP25());
                                preparedStatement5.setBigDecimal(31,eventPowerSample.getP26());
                                preparedStatement5.setBigDecimal(32,eventPowerSample.getP27());
                                preparedStatement5.setBigDecimal(33,eventPowerSample.getP28());
                                preparedStatement5.setBigDecimal(34,eventPowerSample.getP29());
                                preparedStatement5.setBigDecimal(35,eventPowerSample.getP30());
                                preparedStatement5.setBigDecimal(36,eventPowerSample.getP31());
                                preparedStatement5.setBigDecimal(37,eventPowerSample.getP32());
                                preparedStatement5.setBigDecimal(38,eventPowerSample.getP33());
                                preparedStatement5.setBigDecimal(39,eventPowerSample.getP34());
                                preparedStatement5.setBigDecimal(40,eventPowerSample.getP35());
                                preparedStatement5.setBigDecimal(41,eventPowerSample.getP36());
                                preparedStatement5.setBigDecimal(42,eventPowerSample.getP37());
                                preparedStatement5.setBigDecimal(43,eventPowerSample.getP38());
                                preparedStatement5.setBigDecimal(44,eventPowerSample.getP39());
                                preparedStatement5.setBigDecimal(45,eventPowerSample.getP40());
                                preparedStatement5.setBigDecimal(46,eventPowerSample.getP41());
                                preparedStatement5.setBigDecimal(47,eventPowerSample.getP42());
                                preparedStatement5.setBigDecimal(48,eventPowerSample.getP43());
                                preparedStatement5.setBigDecimal(49,eventPowerSample.getP44());
                                preparedStatement5.setBigDecimal(50,eventPowerSample.getP45());
                                preparedStatement5.setBigDecimal(51,eventPowerSample.getP46());
                                preparedStatement5.setBigDecimal(52,eventPowerSample.getP47());
                                preparedStatement5.setBigDecimal(53,eventPowerSample.getP48());
                                preparedStatement5.setBigDecimal(54,eventPowerSample.getP49());
                                preparedStatement5.setBigDecimal(55,eventPowerSample.getP50());
                                preparedStatement5.setBigDecimal(56,eventPowerSample.getP51());
                                preparedStatement5.setBigDecimal(57,eventPowerSample.getP52());
                                preparedStatement5.setBigDecimal(58,eventPowerSample.getP53());
                                preparedStatement5.setBigDecimal(59,eventPowerSample.getP54());
                                preparedStatement5.setBigDecimal(60,eventPowerSample.getP55());
                                preparedStatement5.setBigDecimal(61,eventPowerSample.getP56());
                                preparedStatement5.setBigDecimal(62,eventPowerSample.getP57());
                                preparedStatement5.setBigDecimal(63,eventPowerSample.getP58());
                                preparedStatement5.setBigDecimal(64,eventPowerSample.getP59());
                                preparedStatement5.setBigDecimal(65,eventPowerSample.getP60());
                                preparedStatement5.setBigDecimal(66,eventPowerSample.getP61());
                                preparedStatement5.setBigDecimal(67,eventPowerSample.getP62());
                                preparedStatement5.setBigDecimal(68,eventPowerSample.getP63());
                                preparedStatement5.setBigDecimal(69,eventPowerSample.getP64());
                                preparedStatement5.setBigDecimal(70,eventPowerSample.getP65());
                                preparedStatement5.setBigDecimal(71,eventPowerSample.getP66());
                                preparedStatement5.setBigDecimal(72,eventPowerSample.getP67());
                                preparedStatement5.setBigDecimal(73,eventPowerSample.getP68());
                                preparedStatement5.setBigDecimal(74,eventPowerSample.getP69());
                                preparedStatement5.setBigDecimal(75,eventPowerSample.getP70());
                                preparedStatement5.setBigDecimal(76,eventPowerSample.getP71());
                                preparedStatement5.setBigDecimal(77,eventPowerSample.getP72());
                                preparedStatement5.setBigDecimal(78,eventPowerSample.getP73());
                                preparedStatement5.setBigDecimal(79,eventPowerSample.getP74());
                                preparedStatement5.setBigDecimal(80,eventPowerSample.getP75());
                                preparedStatement5.setBigDecimal(81,eventPowerSample.getP76());
                                preparedStatement5.setBigDecimal(82,eventPowerSample.getP77());
                                preparedStatement5.setBigDecimal(83,eventPowerSample.getP78());
                                preparedStatement5.setBigDecimal(84,eventPowerSample.getP79());
                                preparedStatement5.setBigDecimal(85,eventPowerSample.getP80());
                                preparedStatement5.setBigDecimal(86,eventPowerSample.getP81());
                                preparedStatement5.setBigDecimal(87,eventPowerSample.getP82());
                                preparedStatement5.setBigDecimal(88,eventPowerSample.getP83());
                                preparedStatement5.setBigDecimal(89,eventPowerSample.getP84());
                                preparedStatement5.setBigDecimal(90,eventPowerSample.getP85());
                                preparedStatement5.setBigDecimal(91,eventPowerSample.getP86());
                                preparedStatement5.setBigDecimal(92,eventPowerSample.getP87());
                                preparedStatement5.setBigDecimal(93,eventPowerSample.getP88());
                                preparedStatement5.setBigDecimal(94,eventPowerSample.getP89());
                                preparedStatement5.setBigDecimal(95,eventPowerSample.getP90());
                                preparedStatement5.setBigDecimal(96,eventPowerSample.getP91());
                                preparedStatement5.setBigDecimal(97,eventPowerSample.getP92());
                                preparedStatement5.setBigDecimal(98,eventPowerSample.getP93());
                                preparedStatement5.setBigDecimal(99,eventPowerSample.getP94());
                                preparedStatement5.setBigDecimal(100,eventPowerSample.getP95());
                                preparedStatement5.setBigDecimal(101,eventPowerSample.getP96());
                                preparedStatement5.setLong(102,eventPowerSample.getId());
                                preparedStatement5.setLong(103,eventPowerSample.getBaselineId());
                                preparedStatement5.addBatch();
                                if((j+1)%500 == 0 || j == eventPowerSampleUpdateList.size()-1) {
                                    //每1000条提交一次
                                    preparedStatement5.executeBatch();
                                    //清空记录
                                    preparedStatement5.clearBatch();
                                }
                                j++;
                            }
                        }
                        log.info("96点样本曲线更新成功,共"+eventPowerSampleUpdateList.size()+"条");*/
                        //保存用户基线表
                        if(null!=insertList && insertList.size()>0) {
                            int k = 0;
                            conn2= DriverManager.getConnection(url,user,password);
                            conn2.setAutoCommit(false);
                            preparedStatement = conn2.prepareStatement(sql);
                            for(ConsBaseline consBaseline : insertList) {
                                preparedStatement.setLong(1,consBaseline.getBaselineLibId());
                                preparedStatement.setString(2,consBaseline.getConsId());
                                preparedStatement.setString(3,consBaseline.getConsName());
                                if(null!=consBaseline.getBaselineDate()) {
                                    preparedStatement.setString(4, simpleDateFormat.format(consBaseline.getBaselineDate()));
                                } else {
                                    preparedStatement.setNull(4,Types.VARCHAR);
                                }
                                preparedStatement.setString(5,consBaseline.getSimplesDate());
                                preparedStatement.setBigDecimal(6,consBaseline.getP1());
                                preparedStatement.setBigDecimal(7,consBaseline.getP2());
                                preparedStatement.setBigDecimal(8,consBaseline.getP3());
                                preparedStatement.setBigDecimal(9,consBaseline.getP4());
                                preparedStatement.setBigDecimal(10,consBaseline.getP5());
                                preparedStatement.setBigDecimal(11,consBaseline.getP6());
                                preparedStatement.setBigDecimal(12,consBaseline.getP7());
                                preparedStatement.setBigDecimal(13,consBaseline.getP8());
                                preparedStatement.setBigDecimal(14,consBaseline.getP9());
                                preparedStatement.setBigDecimal(15,consBaseline.getP10());
                                preparedStatement.setBigDecimal(16,consBaseline.getP11());
                                preparedStatement.setBigDecimal(17,consBaseline.getP12());
                                preparedStatement.setBigDecimal(18,consBaseline.getP13());
                                preparedStatement.setBigDecimal(19,consBaseline.getP14());
                                preparedStatement.setBigDecimal(20,consBaseline.getP15());
                                preparedStatement.setBigDecimal(21,consBaseline.getP16());
                                preparedStatement.setBigDecimal(22,consBaseline.getP17());
                                preparedStatement.setBigDecimal(23,consBaseline.getP18());
                                preparedStatement.setBigDecimal(24,consBaseline.getP19());
                                preparedStatement.setBigDecimal(25,consBaseline.getP20());
                                preparedStatement.setBigDecimal(26,consBaseline.getP21());
                                preparedStatement.setBigDecimal(27,consBaseline.getP22());
                                preparedStatement.setBigDecimal(28,consBaseline.getP23());
                                preparedStatement.setBigDecimal(29,consBaseline.getP24());
                                preparedStatement.setBigDecimal(30,consBaseline.getP25());
                                preparedStatement.setBigDecimal(31,consBaseline.getP26());
                                preparedStatement.setBigDecimal(32,consBaseline.getP27());
                                preparedStatement.setBigDecimal(33,consBaseline.getP28());
                                preparedStatement.setBigDecimal(34,consBaseline.getP29());
                                preparedStatement.setBigDecimal(35,consBaseline.getP30());
                                preparedStatement.setBigDecimal(36,consBaseline.getP31());
                                preparedStatement.setBigDecimal(37,consBaseline.getP32());
                                preparedStatement.setBigDecimal(38,consBaseline.getP33());
                                preparedStatement.setBigDecimal(39,consBaseline.getP34());
                                preparedStatement.setBigDecimal(40,consBaseline.getP35());
                                preparedStatement.setBigDecimal(41,consBaseline.getP36());
                                preparedStatement.setBigDecimal(42,consBaseline.getP37());
                                preparedStatement.setBigDecimal(43,consBaseline.getP38());
                                preparedStatement.setBigDecimal(44,consBaseline.getP39());
                                preparedStatement.setBigDecimal(45,consBaseline.getP40());
                                preparedStatement.setBigDecimal(46,consBaseline.getP41());
                                preparedStatement.setBigDecimal(47,consBaseline.getP42());
                                preparedStatement.setBigDecimal(48,consBaseline.getP43());
                                preparedStatement.setBigDecimal(49,consBaseline.getP44());
                                preparedStatement.setBigDecimal(50,consBaseline.getP45());
                                preparedStatement.setBigDecimal(51,consBaseline.getP46());
                                preparedStatement.setBigDecimal(52,consBaseline.getP47());
                                preparedStatement.setBigDecimal(53,consBaseline.getP48());
                                preparedStatement.setBigDecimal(54,consBaseline.getP49());
                                preparedStatement.setBigDecimal(55,consBaseline.getP50());
                                preparedStatement.setBigDecimal(56,consBaseline.getP51());
                                preparedStatement.setBigDecimal(57,consBaseline.getP52());
                                preparedStatement.setBigDecimal(58,consBaseline.getP53());
                                preparedStatement.setBigDecimal(59,consBaseline.getP54());
                                preparedStatement.setBigDecimal(60,consBaseline.getP55());
                                preparedStatement.setBigDecimal(61,consBaseline.getP56());
                                preparedStatement.setBigDecimal(62,consBaseline.getP57());
                                preparedStatement.setBigDecimal(63,consBaseline.getP58());
                                preparedStatement.setBigDecimal(64,consBaseline.getP59());
                                preparedStatement.setBigDecimal(65,consBaseline.getP60());
                                preparedStatement.setBigDecimal(66,consBaseline.getP61());
                                preparedStatement.setBigDecimal(67,consBaseline.getP62());
                                preparedStatement.setBigDecimal(68,consBaseline.getP63());
                                preparedStatement.setBigDecimal(69,consBaseline.getP64());
                                preparedStatement.setBigDecimal(70,consBaseline.getP65());
                                preparedStatement.setBigDecimal(71,consBaseline.getP66());
                                preparedStatement.setBigDecimal(72,consBaseline.getP67());
                                preparedStatement.setBigDecimal(73,consBaseline.getP68());
                                preparedStatement.setBigDecimal(74,consBaseline.getP69());
                                preparedStatement.setBigDecimal(75,consBaseline.getP70());
                                preparedStatement.setBigDecimal(76,consBaseline.getP71());
                                preparedStatement.setBigDecimal(77,consBaseline.getP72());
                                preparedStatement.setBigDecimal(78,consBaseline.getP73());
                                preparedStatement.setBigDecimal(79,consBaseline.getP74());
                                preparedStatement.setBigDecimal(80,consBaseline.getP75());
                                preparedStatement.setBigDecimal(81,consBaseline.getP76());
                                preparedStatement.setBigDecimal(82,consBaseline.getP77());
                                preparedStatement.setBigDecimal(83,consBaseline.getP78());
                                preparedStatement.setBigDecimal(84,consBaseline.getP79());
                                preparedStatement.setBigDecimal(85,consBaseline.getP80());
                                preparedStatement.setBigDecimal(86,consBaseline.getP81());
                                preparedStatement.setBigDecimal(87,consBaseline.getP82());
                                preparedStatement.setBigDecimal(88,consBaseline.getP83());
                                preparedStatement.setBigDecimal(89,consBaseline.getP84());
                                preparedStatement.setBigDecimal(90,consBaseline.getP85());
                                preparedStatement.setBigDecimal(91,consBaseline.getP86());
                                preparedStatement.setBigDecimal(92,consBaseline.getP87());
                                preparedStatement.setBigDecimal(93,consBaseline.getP88());
                                preparedStatement.setBigDecimal(94,consBaseline.getP89());
                                preparedStatement.setBigDecimal(95,consBaseline.getP90());
                                preparedStatement.setBigDecimal(96,consBaseline.getP91());
                                preparedStatement.setBigDecimal(97,consBaseline.getP92());
                                preparedStatement.setBigDecimal(98,consBaseline.getP93());
                                preparedStatement.setBigDecimal(99,consBaseline.getP94());
                                preparedStatement.setBigDecimal(100,consBaseline.getP95());
                                preparedStatement.setBigDecimal(101,consBaseline.getP96());
                                preparedStatement.setBigDecimal(102,consBaseline.getMaxLoadBaseline());
                                preparedStatement.setBigDecimal(103,consBaseline.getMinLoadBaseline());
                                preparedStatement.setBigDecimal(104,consBaseline.getAvgLoadBaseline());
                                preparedStatement.setString(105,consBaseline.getNormal());
                                preparedStatement.setString(106,consBaseline.getExceptionRemark());
                                preparedStatement.setString(107,consBaseline.getSimplesId());
                                preparedStatement.setString(108,consBaseline.getCalRule());
                                preparedStatement.setLong(109,consBaseline.getBaselineId());
                                preparedStatement.addBatch();
                                if((k+1)%500 == 0 || k == insertList.size()-1) {
                                    //每1000条提交一次
                                    preparedStatement.executeBatch();
                                    //清空记录
                                    preparedStatement.clearBatch();
                                }
                                k++;
                            }
                        }
                        log.info("96点用户基线保存成功,共"+insertList.size()+"条");
                        //更新已存在的基线
                        if(null!=updateList && updateList.size()>0) {
                            int i = 0;
                            conn4= DriverManager.getConnection(url,user,password);
                            conn4.setAutoCommit(false);
                            preparedStatement3 = conn4.prepareStatement(sql4);
                            for(ConsBaseline consBaseline : updateList) {
                                if(null!=consBaseline.getBaselineLibId()) {
                                    preparedStatement3.setLong(1, consBaseline.getBaselineLibId());
                                }
                                preparedStatement3.setString(2,consBaseline.getConsId());
                                preparedStatement3.setString(3,consBaseline.getConsName());
                                if(null!=consBaseline.getBaselineDate()) {
                                    preparedStatement3.setString(4, simpleDateFormat.format(consBaseline.getBaselineDate()));
                                } else {
                                    preparedStatement3.setString(4, null);
                                }
                                preparedStatement3.setString(5,consBaseline.getSimplesDate());
                                preparedStatement3.setBigDecimal(6,consBaseline.getP1());
                                preparedStatement3.setBigDecimal(7,consBaseline.getP2());
                                preparedStatement3.setBigDecimal(8,consBaseline.getP3());
                                preparedStatement3.setBigDecimal(9,consBaseline.getP4());
                                preparedStatement3.setBigDecimal(10,consBaseline.getP5());
                                preparedStatement3.setBigDecimal(11,consBaseline.getP6());
                                preparedStatement3.setBigDecimal(12,consBaseline.getP7());
                                preparedStatement3.setBigDecimal(13,consBaseline.getP8());
                                preparedStatement3.setBigDecimal(14,consBaseline.getP9());
                                preparedStatement3.setBigDecimal(15,consBaseline.getP10());
                                preparedStatement3.setBigDecimal(16,consBaseline.getP11());
                                preparedStatement3.setBigDecimal(17,consBaseline.getP12());
                                preparedStatement3.setBigDecimal(18,consBaseline.getP13());
                                preparedStatement3.setBigDecimal(19,consBaseline.getP14());
                                preparedStatement3.setBigDecimal(20,consBaseline.getP15());
                                preparedStatement3.setBigDecimal(21,consBaseline.getP16());
                                preparedStatement3.setBigDecimal(22,consBaseline.getP17());
                                preparedStatement3.setBigDecimal(23,consBaseline.getP18());
                                preparedStatement3.setBigDecimal(24,consBaseline.getP19());
                                preparedStatement3.setBigDecimal(25,consBaseline.getP20());
                                preparedStatement3.setBigDecimal(26,consBaseline.getP21());
                                preparedStatement3.setBigDecimal(27,consBaseline.getP22());
                                preparedStatement3.setBigDecimal(28,consBaseline.getP23());
                                preparedStatement3.setBigDecimal(29,consBaseline.getP24());
                                preparedStatement3.setBigDecimal(30,consBaseline.getP25());
                                preparedStatement3.setBigDecimal(31,consBaseline.getP26());
                                preparedStatement3.setBigDecimal(32,consBaseline.getP27());
                                preparedStatement3.setBigDecimal(33,consBaseline.getP28());
                                preparedStatement3.setBigDecimal(34,consBaseline.getP29());
                                preparedStatement3.setBigDecimal(35,consBaseline.getP30());
                                preparedStatement3.setBigDecimal(36,consBaseline.getP31());
                                preparedStatement3.setBigDecimal(37,consBaseline.getP32());
                                preparedStatement3.setBigDecimal(38,consBaseline.getP33());
                                preparedStatement3.setBigDecimal(39,consBaseline.getP34());
                                preparedStatement3.setBigDecimal(40,consBaseline.getP35());
                                preparedStatement3.setBigDecimal(41,consBaseline.getP36());
                                preparedStatement3.setBigDecimal(42,consBaseline.getP37());
                                preparedStatement3.setBigDecimal(43,consBaseline.getP38());
                                preparedStatement3.setBigDecimal(44,consBaseline.getP39());
                                preparedStatement3.setBigDecimal(45,consBaseline.getP40());
                                preparedStatement3.setBigDecimal(46,consBaseline.getP41());
                                preparedStatement3.setBigDecimal(47,consBaseline.getP42());
                                preparedStatement3.setBigDecimal(48,consBaseline.getP43());
                                preparedStatement3.setBigDecimal(49,consBaseline.getP44());
                                preparedStatement3.setBigDecimal(50,consBaseline.getP45());
                                preparedStatement3.setBigDecimal(51,consBaseline.getP46());
                                preparedStatement3.setBigDecimal(52,consBaseline.getP47());
                                preparedStatement3.setBigDecimal(53,consBaseline.getP48());
                                preparedStatement3.setBigDecimal(54,consBaseline.getP49());
                                preparedStatement3.setBigDecimal(55,consBaseline.getP50());
                                preparedStatement3.setBigDecimal(56,consBaseline.getP51());
                                preparedStatement3.setBigDecimal(57,consBaseline.getP52());
                                preparedStatement3.setBigDecimal(58,consBaseline.getP53());
                                preparedStatement3.setBigDecimal(59,consBaseline.getP54());
                                preparedStatement3.setBigDecimal(60,consBaseline.getP55());
                                preparedStatement3.setBigDecimal(61,consBaseline.getP56());
                                preparedStatement3.setBigDecimal(62,consBaseline.getP57());
                                preparedStatement3.setBigDecimal(63,consBaseline.getP58());
                                preparedStatement3.setBigDecimal(64,consBaseline.getP59());
                                preparedStatement3.setBigDecimal(65,consBaseline.getP60());
                                preparedStatement3.setBigDecimal(66,consBaseline.getP61());
                                preparedStatement3.setBigDecimal(67,consBaseline.getP62());
                                preparedStatement3.setBigDecimal(68,consBaseline.getP63());
                                preparedStatement3.setBigDecimal(69,consBaseline.getP64());
                                preparedStatement3.setBigDecimal(70,consBaseline.getP65());
                                preparedStatement3.setBigDecimal(71,consBaseline.getP66());
                                preparedStatement3.setBigDecimal(72,consBaseline.getP67());
                                preparedStatement3.setBigDecimal(73,consBaseline.getP68());
                                preparedStatement3.setBigDecimal(74,consBaseline.getP69());
                                preparedStatement3.setBigDecimal(75,consBaseline.getP70());
                                preparedStatement3.setBigDecimal(76,consBaseline.getP71());
                                preparedStatement3.setBigDecimal(77,consBaseline.getP72());
                                preparedStatement3.setBigDecimal(78,consBaseline.getP73());
                                preparedStatement3.setBigDecimal(79,consBaseline.getP74());
                                preparedStatement3.setBigDecimal(80,consBaseline.getP75());
                                preparedStatement3.setBigDecimal(81,consBaseline.getP76());
                                preparedStatement3.setBigDecimal(82,consBaseline.getP77());
                                preparedStatement3.setBigDecimal(83,consBaseline.getP78());
                                preparedStatement3.setBigDecimal(84,consBaseline.getP79());
                                preparedStatement3.setBigDecimal(85,consBaseline.getP80());
                                preparedStatement3.setBigDecimal(86,consBaseline.getP81());
                                preparedStatement3.setBigDecimal(87,consBaseline.getP82());
                                preparedStatement3.setBigDecimal(88,consBaseline.getP83());
                                preparedStatement3.setBigDecimal(89,consBaseline.getP84());
                                preparedStatement3.setBigDecimal(90,consBaseline.getP85());
                                preparedStatement3.setBigDecimal(91,consBaseline.getP86());
                                preparedStatement3.setBigDecimal(92,consBaseline.getP87());
                                preparedStatement3.setBigDecimal(93,consBaseline.getP88());
                                preparedStatement3.setBigDecimal(94,consBaseline.getP89());
                                preparedStatement3.setBigDecimal(95,consBaseline.getP90());
                                preparedStatement3.setBigDecimal(96,consBaseline.getP91());
                                preparedStatement3.setBigDecimal(97,consBaseline.getP92());
                                preparedStatement3.setBigDecimal(98,consBaseline.getP93());
                                preparedStatement3.setBigDecimal(99,consBaseline.getP94());
                                preparedStatement3.setBigDecimal(100,consBaseline.getP95());
                                preparedStatement3.setBigDecimal(101,consBaseline.getP96());
                                preparedStatement3.setBigDecimal(102,consBaseline.getMaxLoadBaseline());
                                preparedStatement3.setBigDecimal(103,consBaseline.getMinLoadBaseline());
                                preparedStatement3.setBigDecimal(104,consBaseline.getAvgLoadBaseline());
                                preparedStatement3.setString(105,consBaseline.getNormal());
                                preparedStatement3.setString(106,consBaseline.getExceptionRemark());
                                preparedStatement3.setString(107,consBaseline.getSimplesId());
                                preparedStatement3.setString(108,consBaseline.getCalRule());
                                preparedStatement3.setLong(109,consBaseline.getBaselineId());
                                preparedStatement3.addBatch();
                                if((i+1)%500 == 0 || i == updateList.size()-1) {
                                    //每1000条提交一次
                                    preparedStatement3.executeBatch();
                                    //清空记录
                                    preparedStatement3.clearBatch();
                                }
                                i++;
                            }
                        }
                        log.info("96点用户基线更新成功,共" + updateList.size()+"条");
                        conn3= DriverManager.getConnection(url,user,password);
                        conn3.setAutoCommit(false);
                        preparedStatement1 = conn3.prepareStatement(sql1);
                        //更新基线任务表
                        int i = 0;
                        for (PlanBaseLine planBaseLine : planBaseLines) {
                            if(null!=planBaseLine.getBaselineStatus()) {
                                preparedStatement1.setString(1, planBaseLine.getBaselineStatus());
                            } else {
                                preparedStatement1.setNull(1,Types.VARCHAR);
                            }
                            if(null!=planBaseLine.getBaselineDesc()) {
                                preparedStatement1.setString(2, planBaseLine.getBaselineDesc());
                            } else {
                                preparedStatement1.setNull(2,Types.VARCHAR);
                            }
                            if(null!=planBaseLine.getBaselineCapId()) {
                                preparedStatement1.setLong(3, planBaseLine.getBaselineCapId());
                            } else {
                                preparedStatement1.setNull(3,Types.BIGINT);
                            }
                            preparedStatement1.setInt(4,planBaseLine.getFailTimes());
                            preparedStatement1.setString(5,dateFormat.format(new Date()));
                            preparedStatement1.setLong(6,planBaseLine.getId());
                            preparedStatement1.addBatch();
                            if((i+1)%500 == 0 || i == planBaseLines.size()-1) {
                                //每1000条提交一次
                                preparedStatement1.executeBatch();
                                //清空记录
                                preparedStatement1.clearBatch();
                            }
                            i++;
                        }
                        log.info("96点基线任务更新成功,共" + planBaseLines.size()+"条");
                        if(null!=conn1) {
                            conn1.commit();
                        }
                        if(null!=conn2) {
                            conn2.commit();
                        }
                        if(null!=conn3) {
                            conn3.commit();
                        }
                        if(null!=conn4) {
                            conn4.commit();
                        }
                        if(null!=conn5) {
                            conn5.commit();
                        }
                        log.info("96点基线计算任务完成!");

                    } catch (Exception e) {
                        try {
                            if(null!=conn1) {
                                conn1.rollback();
                            }
                            if(null!=conn2) {
                                conn2.rollback();
                            }
                            if(null!=conn3) {
                                conn3.rollback();
                            }
                            if(null!=conn4) {
                                conn4.rollback();
                            }
                            if(null!=conn5) {
                                conn5.rollback();
                            }
                        } catch (SQLException sqlException) {
                            sqlException.printStackTrace();
                        }
                        e.printStackTrace();
                    } finally {
                        if(null!=conn1) {
                            try {
                                conn1.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if(null!=conn2) {
                            try {
                                conn2.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if(null!=conn3) {
                            try {
                                conn3.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if(null!=conn4) {
                            try {
                                conn4.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if(null!=conn5) {
                            try {
                                conn5.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if(null!=preparedStatement1) {
                            try {
                                preparedStatement1.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if(null!=preparedStatement2) {
                            try {
                                preparedStatement2.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if(null!=preparedStatement) {
                            try {
                                preparedStatement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        if(null!=preparedStatement3) {
                            try {
                                preparedStatement3.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if(null!=preparedStatement5) {
                            try {
                                preparedStatement5.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if(null!=baseLine.getConsAllNum()) {
                            baseLine.setConsAllNum(baseLine.getConsAllNum()+1);
                            baseLineService.updateById(baseLine);
                        } else {
                            baseLine.setConsAllNum(1);
                            baseLineService.updateById(baseLine);
                        }

                    }
                }
            }
        };
        return runnable;
    }

    public List<ConsCurve> getPoint(List<ConsCurve> curveHisList,Map<Integer, Method> consMethodMap2, Map<Integer, Method> consMethodMap3,int startIndex,int endIndex,Map<Integer, Method> consMethodMap4,List<EventPowerSample> samples) {
        List<ConsCurve> curveHisList2 = new ArrayList<>();
        try {
            //拷贝集合
            curveHisList2 = deepCopy(curveHisList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null!=curveHisList && curveHisList.size()>0) {
            for (int k=0;k<curveHisList.size();k++) {
                try {
                    if(startIndex==1) {
                        BigDecimal p1 = (BigDecimal) consMethodMap2.get(1).invoke(curveHisList.get(k));
                        if(null==p1) {
                            curveHisList2.remove(curveHisList2.get(k));
                            curveHisList.remove(curveHisList.get(k));
                            log.error("全天基线p1为空!");
                            k--;
                            continue;
                        }
                    }
                    if(endIndex==96) {
                        BigDecimal p96 = (BigDecimal) consMethodMap2.get(96).invoke(curveHisList.get(k));
                        if(null==p96) {
                            curveHisList2.remove(curveHisList2.get(k));
                            curveHisList.remove(curveHisList.get(k));
                            log.error("全天基线p96为空!");
                            k--;
                            continue;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //给空点补点，取两个有值点之间的平均值
                for (int i = startIndex; i <= endIndex; i++) {
                    BigDecimal power = null;
                    BigDecimal minNear = null;
                    BigDecimal maxNear = null;
                    try {
                        power = (BigDecimal) consMethodMap2.get(i).invoke(curveHisList.get(k));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (null == power) {
                        BigDecimal simple = null;
                       /* try {
                            if(null!=samples && samples.size()>0) {
                                LocalDate localDate = curveHisList.get(k).getDataDate();
                                List<EventPowerSample> eventPowerSamples = samples.stream().filter(con -> con.getDataDate().compareTo(localDate)==0).collect(Collectors.toList());
                                if(null!=eventPowerSamples && eventPowerSamples.size()>0) {
                                    simple = (BigDecimal) consMethodMap4.get(i).invoke(eventPowerSamples.get(0));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        if (null == simple) {
                            if (i > 1 && i <= 96) {
                                BigDecimal powerMinNear = null;
                                BigDecimal powerMaxNear = null;
                                    //找前面的有值点
                                    for (int j = i - 1; j > 1; j--) {
                                        try {
                                            powerMinNear = (BigDecimal) consMethodMap2.get(j).invoke(curveHisList.get(k));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        if (null != powerMinNear) {
                                            minNear = powerMinNear;
                                            break;
                                        } else {
                                            if (j == 2) {
                                                minNear = null;
                                                curveHisList2.remove(curveHisList2.get(k));
                                                curveHisList.remove(curveHisList.get(k));
                                                log.error("无法补足缺点!");
                                                k--;
                                                break;
                                            }

                                        }
                                    }
                                    //找后面的有值点
                                    if (null != minNear) {
                                        for (int j = i + 1; j <= 96; j++) {
                                            try {
                                                powerMaxNear = (BigDecimal) consMethodMap2.get(j).invoke(curveHisList.get(k));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (null != powerMaxNear) {
                                                maxNear = powerMaxNear;
                                                break;
                                            } else {
                                                if (j == 96) {
                                                    log.error("到p1无点可补!");
                                                    maxNear = null;
                                                    curveHisList2.remove(curveHisList2.get(k));
                                                    curveHisList.remove(curveHisList.get(k));
                                                    k--;
                                                    break;
                                                }
                                            }
                                        }
                                        if (null == maxNear) {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                } else {
                                    log.error("到p96无点可补!");
                                    curveHisList2.remove(curveHisList2.get(k));
                                    curveHisList.remove(curveHisList.get(k));
                                    k--;
                                    break;
                                }
                                if (null != maxNear && null != minNear) {
                                    try {
                                        BigDecimal sum = NumberUtil.add(maxNear, minNear);
                                        BigDecimal avg = NumberUtil.div(sum, 2);
                                        consMethodMap3.get(i).invoke(curveHisList2.get(k), avg);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    break;
                                }
                            }  else {
                                try {
                                    consMethodMap3.get(i).invoke(curveHisList2.get(k), simple);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                        }
                    }
                }
            }
        }
        return curveHisList2;
    }
    /*public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }*/

    // 对象要序列化接口 Serializable 提供 serialVersionUID 字段
    public static <T> List<T> deepCopy(List<T> src) {
        ByteArrayOutputStream byteout = null;
        ObjectOutputStream out = null;
        ByteArrayInputStream bytein = null;
        ObjectInputStream in = null;
        try {
            byteout = new ByteArrayOutputStream();
            out = new ObjectOutputStream(byteout);
            out.writeObject(src);
            bytein = new ByteArrayInputStream(byteout.toByteArray());
            in = new ObjectInputStream(bytein);
            @SuppressWarnings("unchecked")
            List<T> dest = (List<T>) in.readObject();
            return dest;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (null!=byteout) {
                try {
                    byteout.close();
                } catch (IOException ignore) {}
            }
            if (null!=out) {
                try {
                    out.close();
                } catch (IOException ignore) {}
            }
            if (null!=bytein) {
                try {
                    bytein.close();
                } catch (IOException ignore) {}
            }
            if (null!=in) {
                try {
                    in.close();
                } catch (IOException ignore) {}
            }
        }
    }

    public List<CustBaseLineDetail> getPoint2(List<CustBaseLineDetail> curveHisList,Map<Integer, Method> consMethodMap2, Map<Integer, Method> consMethodMap3,int startIndex,int endIndex) {
        List<CustBaseLineDetail> curveHisList2 = new ArrayList<>();
        try {
            //拷贝集合
            curveHisList2 = deepCopy(curveHisList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null!=curveHisList && curveHisList.size()>0) {
            for (int k=0;k<curveHisList.size();k++) {
                //给空点补点，取两个有值点之间的平均值
                for (int i = startIndex; i <= endIndex; i++) {
                    BigDecimal power = null;
                    BigDecimal minNear = null;
                    BigDecimal maxNear = null;
                    try {
                        power = (BigDecimal) consMethodMap2.get(i).invoke(curveHisList.get(k));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (null == power) {
                        if (i > 1 && i < 96) {
                            BigDecimal powerMinNear = null;
                            BigDecimal powerMaxNear = null;
                            //找前面的有值点
                            for (int j = i-1; j > 1; j--) {
                                try {
                                    powerMinNear = (BigDecimal) consMethodMap2.get(j).invoke(curveHisList.get(k));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (null != powerMinNear) {
                                    minNear = powerMinNear;
                                    break;
                                } else {
                                    if(j==2) {
                                        minNear = null;
                                        curveHisList2.remove(curveHisList2.get(k));
                                        curveHisList.remove(curveHisList.get(k));
                                        k--;
                                        log.error("无法补足缺点!");
                                        break;
                                    }

                                }
                            }
                            //找后面的有值点
                            if(null!=minNear) {
                                for (int j = i+1; j < 96; j++) {
                                    try {
                                        powerMaxNear = (BigDecimal) consMethodMap2.get(j).invoke(curveHisList.get(k));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (null != powerMaxNear) {
                                        maxNear = powerMaxNear;
                                        break;
                                    } else {
                                        if (j == 95) {
                                            log.error("到p96无点可补!");
                                            maxNear = null;
                                            curveHisList2.remove(curveHisList2.get(k));
                                            curveHisList.remove(curveHisList.get(k));
                                            k--;
                                            break;
                                        }
                                    }
                                }
                                if(null==maxNear) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        } else {
                            log.error("到p96无点可补!");
                            curveHisList2.remove(curveHisList2.get(k));
                            curveHisList.remove(curveHisList.get(k));
                            k--;
                            break;
                        }
                        if(null!=maxNear && null!=minNear) {
                            try {
                                BigDecimal sum = NumberUtil.add(maxNear,minNear);
                                BigDecimal avg = NumberUtil.div(sum, 2);
                                consMethodMap3.get(i).invoke(curveHisList2.get(k),avg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return curveHisList2;
    }

}
