package com.xqxy.dr.modular.baseline.service.impl;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.baseline.entity.CustBaseLineDetail;
import com.xqxy.dr.modular.baseline.mapper.BaseLineMapper;
import com.xqxy.dr.modular.baseline.mapper.CustBaseLineMapper;
import com.xqxy.dr.modular.baseline.param.BaseLineParam;
import com.xqxy.dr.modular.baseline.service.BaseLineService;
import com.xqxy.dr.modular.event.entity.ConsBaseline;
import com.xqxy.dr.modular.event.entity.PlanBaselineTask;
import com.xqxy.sys.modular.cust.entity.BlackName;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.mapper.BlackNameMapper;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 * 基线管理 服务实现类
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-18
 */
@Service
public class BaseLineServiceImpl extends ServiceImpl<BaseLineMapper, BaseLine> implements BaseLineService {

    private static final Log log = Log.get();

    @Resource
    ConsService consService;

    @Resource
    CustBaseLineMapper custBaseLineMapper;

    @Resource
    BlackNameMapper blackNameMapper;

    @Value("${spring.datasource.mysql.username}")
    private String userName;

    @Value("${spring.datasource.mysql.jdbc-url}")
    private String dataurl;

    @Value("${spring.datasource.mysql.password}")
    private String datapassword;

    @Value("${spring.datasource.mysql.driver-class-name}")
    private String driver;

    @Value("${executor.corePoolSize}")
    private int corePoolSize;

    @Value("${executor.maxPoolSize}")
    private int maxPoolSize;

    @Value("${executor.workQueue}")
    private int workQueue;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(BaseLineParam baseLineParam) {
        Long state =1L;
        List<Map<String,Object>> times = baseLineParam.getTimes();
        List<BaseLine> lineList = new ArrayList<>();
        if(null!=times && times.size()>0) {
            for(Map<String,Object> map : times) {
                BaseLine baseLine = new BaseLine();
                BeanUtils.copyProperties(baseLineParam,baseLine);
                if(null!=baseLine && null!=baseLine.getSimplesDate()) {
                    String simp = "";
                    String[] date = baseLine.getSimplesDate().split(",");
                    if(null!=date && date.length>0) {
                        for(int i =0; i<date.length;i++) {
                            if(i<date.length-1) {
                                simp += date[i] + ",";
                            } else {
                                simp += date[i];
                            }
                        }
                    }
                    baseLine.setSimplesDate(simp);
                }
                if(null!=map) {
                    String startTime = (String)map.get("startPeriod");
                    String endTime = (String)map.get("endPeriod");
                    baseLine.setStartPeriod(startTime);
                    baseLine.setEndPeriod(endTime);
                }
                lineList.add(baseLine);
            }
        }
        this.saveBatch(lineList);
        List<PlanBaselineTask> planBaseLines = null;
        if(null!=lineList && lineList.size()>0) {
            CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
            state = lineList.get(0).getBaselinId();
            //插入基线任务表
            planBaseLines = new ArrayList<>();
            List<Cons> cons = consService.list();
            List<String> blackNames = blackNameMapper.getBlackNameConsIds();
            if(null==blackNames) {
                blackNames = new ArrayList<>();
            }
            for(BaseLine baseLine : lineList) {
                if (null != cons && cons.size() > 0) {
                    for (Cons con : cons) {
                        if (!blackNames.contains(con.getId())) {
                            PlanBaselineTask planBaseLine = new PlanBaselineTask();
                            planBaseLine.setBaselinId(baseLine.getBaselinId());
                            planBaseLine.setConsId(con.getId());
                            //planBaseLine.setRegulateDate(baseLine.getGenerateDate());
                            planBaseLine.setStartTime(baseLine.getStartPeriod());
                            planBaseLine.setEndTime(baseLine.getEndPeriod());
                            planBaseLine.setBaseLineCal(baseLine.getCalRule());
                            planBaseLine.setBaselineStatus("1");
                            if (null != currenUserInfo) {
                                planBaseLine.setCreateUser(Long.valueOf(currenUserInfo.getId()));
                            }
                            planBaseLines.add(planBaseLine);

                        }
                    }
                }
            }
        }
        //planBaselineTaskMapper.batchBaseLineTask(planBaseLines);
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(workQueue))
                .build();
        executor.execute(this.insertBatch(planBaseLines));
        executor.shutdown();
        return state;
    }

    @Transactional(rollbackFor = Exception.class)
    public Runnable insertBatch(List<PlanBaselineTask> planBaseLines) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                PreparedStatement preparedStatement = null;
                Connection conn = null;
                String sql = "insert into dr_plan_baseline_task(BASELIN_ID,START_TIME,END_TIME,CONS_ID,BASELINE_STATUS,BASE_LINE_CAL,CREATE_USER,CREATE_TIME,FAIL_TIMES)\n" +
                        "            values(?,?,?\n" +
                        "            ,?,?,?,?,?,?)";
                PreparedStatement preparedStatement2 = null;
                Connection conn2 = null;
                String sql2 = "insert into dr_plan_baseline_task_all(BASELIN_ID,START_TIME,END_TIME,CONS_ID,BASELINE_STATUS,BASE_LINE_CAL,CREATE_USER,CREATE_TIME,FAIL_TIMES)\n" +
                        "            values(?,?,?\n" +
                        "            ,?,?,?,?,?,?)";
                try {
                    String url=dataurl;
                    String user=userName;
                    String password=datapassword;
                    //2）、加载驱动，不需要显示注册驱动
                    Class.forName(driver);
                    //获取数据库连接
                    conn= DriverManager.getConnection(url,user,password);
                    conn.setAutoCommit(false);
                    preparedStatement = conn.prepareStatement(sql);
                    //全量点基线任务计算
                    conn2= DriverManager.getConnection(url,user,password);
                    conn2.setAutoCommit(false);
                    preparedStatement2 = conn2.prepareStatement(sql2);
                    if( null != planBaseLines && planBaseLines.size()>0) {
                        for(int i=0;i<planBaseLines.size();i++) {
                            if(null!=planBaseLines.get(i).getBaselinId()) {
                                preparedStatement.setLong(1, planBaseLines.get(i).getBaselinId());
                            } else {
                                preparedStatement.setNull(1, Types.BIGINT);
                            }
                            //preparedStatement.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd").format(planBaseLines.get(i).getRegulateDate()));
                            if(null!=planBaseLines.get(i).getStartTime()) {
                                preparedStatement.setString(2, planBaseLines.get(i).getStartTime());
                            } else {
                                preparedStatement.setNull(2, Types.VARCHAR);
                            }
                            if(null!=planBaseLines.get(i).getEndTime()) {
                                preparedStatement.setString(3, planBaseLines.get(i).getEndTime());
                            } else {
                                preparedStatement.setNull(3,Types.VARCHAR);
                            }
                            if(null!=planBaseLines.get(i).getConsId()) {
                                preparedStatement.setString(4, planBaseLines.get(i).getConsId());
                            } else {
                                preparedStatement.setNull(4,Types.VARCHAR);
                            }
                            if(null!=planBaseLines.get(i).getBaselineStatus()) {
                                preparedStatement.setString(5, planBaseLines.get(i).getBaselineStatus());
                            } else {
                                preparedStatement.setNull(5,Types.VARCHAR);
                            }
                            if(null!=planBaseLines.get(i).getBaseLineCal()) {
                                preparedStatement.setString(6, planBaseLines.get(i).getBaseLineCal());
                            } else {
                                preparedStatement.setNull(6,Types.VARCHAR);
                            }
                            if(null!=planBaseLines.get(i).getCreateUser()){
                                preparedStatement.setString(7, planBaseLines.get(i).getCreateUser().toString());
                            } else {
                                preparedStatement.setString(7, "-1");
                            }
                            preparedStatement.setString(8,dateFormat.format(new Date()));
                            preparedStatement.setInt(9,0);
                            preparedStatement.addBatch();
                            if((i+1)%1000 == 0 || i == planBaseLines.size()-1) {
                                //每1000条提交一次
                                preparedStatement.executeBatch();
                                //清空记录
                                preparedStatement.clearBatch();
                            }
                            //全量基线任务
                            if(null!=planBaseLines.get(i).getBaselinId()) {
                                preparedStatement2.setLong(1, planBaseLines.get(i).getBaselinId());
                            } else {
                                preparedStatement2.setNull(1, Types.BIGINT);
                            }
                            //preparedStatement2.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd").format(planBaseLines.get(i).getRegulateDate()));
                            if(null!=planBaseLines.get(i).getStartTime()) {
                                preparedStatement2.setString(2, planBaseLines.get(i).getStartTime());
                            } else {
                                preparedStatement2.setNull(2, Types.VARCHAR);
                            }
                            if(null!=planBaseLines.get(i).getEndTime()) {
                                preparedStatement2.setString(3, planBaseLines.get(i).getEndTime());
                            } else {
                                preparedStatement2.setNull(3,Types.VARCHAR);
                            }
                            if(null!=planBaseLines.get(i).getConsId()) {
                                preparedStatement2.setString(4, planBaseLines.get(i).getConsId());
                            } else {
                                preparedStatement2.setNull(4,Types.VARCHAR);
                            }
                            if(null!=planBaseLines.get(i).getBaselineStatus()) {
                                preparedStatement2.setString(5, planBaseLines.get(i).getBaselineStatus());
                            } else {
                                preparedStatement2.setNull(5,Types.VARCHAR);
                            }
                            if(null!=planBaseLines.get(i).getBaseLineCal()) {
                                preparedStatement2.setString(6, planBaseLines.get(i).getBaseLineCal());
                            } else {
                                preparedStatement2.setNull(6,Types.VARCHAR);
                            }
                            if(null!=planBaseLines.get(i).getCreateUser()){
                                preparedStatement2.setString(7, planBaseLines.get(i).getCreateUser().toString());
                            } else {
                                preparedStatement2.setString(7, "-1");
                            }
                            preparedStatement2.setString(8,dateFormat.format(new Date()));
                            preparedStatement2.setInt(9,0);
                            preparedStatement2.addBatch();
                            if((i+1)%1000 == 0 || i == planBaseLines.size()-1) {
                                //每1000条提交一次
                                preparedStatement2.executeBatch();
                                //清空记录
                                preparedStatement2.clearBatch();
                            }
                        }
                        conn.commit();
                        conn2.commit();
                    }
                } catch (Exception e) {
                    e.getMessage();
                } finally {
                    if(null!=conn) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.getMessage();
                        }
                    }
                    if(null!=preparedStatement) {
                        try {
                            preparedStatement.close();
                        } catch (SQLException e) {
                            e.getMessage();
                        }

                    }
                    if(null!=conn2) {
                        try {
                            conn2.close();
                        } catch (SQLException e) {
                            e.getMessage();
                        }
                    }
                    if(null!=preparedStatement2) {
                        try {
                            preparedStatement2.close();
                        } catch (SQLException e) {
                            e.getMessage();
                        }

                    }
                }

            }
        };
        return runnable;
    }


    @Override
    public Page<BaseLine> page(BaseLineParam baseLineParam) {
        LambdaQueryWrapper<BaseLine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(BaseLine::getGenerateDate);
        Page<BaseLine> page = new Page<>(baseLineParam.getCurrent(),baseLineParam.getSize());
        page = page(page,queryWrapper);
        List<BaseLine> lineList = page.getRecords();
        /*if(null!=lineList && lineList.size()>0) {
            for(BaseLine baseLine : lineList) {
                if(null!=baseLine && null != baseLine.getSimplesDate() && !"".equals(baseLine.getSimplesDate())) {
                    String[] dates = baseLine.getSimplesDate().split(",");
                    String s = "";
                    if(null!=dates && dates.length>0) {
                        int i=1;
                        for(String date : dates) {
                            String str = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
                            if(i==dates.length) {
                                s+=str;
                            } else {
                                s+=str+",";
                                i++;
                            }

                        }
                    }
                    baseLine.setSimplesDate(s);
                }
            }
        }*/
        page.setRecords(lineList);
        return page;
    }

    @Override
    public List<CustBaseLineDetail> getCustBaseLine() {
        return custBaseLineMapper.getCustBaseLine();
    }

    @Override
    public List<CustBaseLineDetail> getCustBaseLineAll() {
        return custBaseLineMapper.getCustBaseLineAll();
    }

    @Override
    public List<CustBaseLineDetail> getCustCurve(List<String> sampleDateList,List<String> cons) {
        return custBaseLineMapper.getCustCurve(sampleDateList,cons);
    }

    @Override
    public List<CustBaseLineDetail> getCustCurveAll(List<String> sampleDateList, List<String> cons) {
        return custBaseLineMapper.getCustCurveAll(sampleDateList,cons);
    }

    @Override
    public List<BaseLine> getBaseLineInfo() {
        return baseMapper.getBaseLineInfo();
    }

    @Override
    public List<ConsBaseline> getConsBaseLineInfo() {
        return baseMapper.getConsBaseLineInfo();
    }

    @Override
    public List<CustBaseLineDetail> getCustAndConsInfo() {
        return custBaseLineMapper.getCustAndConsInfo();
    }

    @Override
    public List<ConsBaseline> getConsBaseLineByBaseLineId(Long baselineId) {
        return baseMapper.getConsBaseLineByBaseLineId(baselineId);
    }

    @Override
    public List<ConsBaseline> getConsBaseLineByBaseLineIdAll(Long baselineId) {
        return baseMapper.getConsBaseLineByBaseLineIdAll(baselineId);
    }

}
