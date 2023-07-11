package com.xqxy.dr.modular.anhui.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.anhui.entity.HefeiCurverSgData;
import com.xqxy.dr.modular.anhui.enums.HisTableNameEnum;
import com.xqxy.dr.modular.anhui.mapper.CustHisMapper;
import com.xqxy.dr.modular.anhui.mapper.HefeiCurverSgDataMapper;
import com.xqxy.dr.modular.anhui.service.HefeiConsService;
import com.xqxy.dr.modular.anhui.service.HefeiCurverSgDataService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>
 * 合肥实时数据 服务实现类
 * </p>
 *
 * @author liuyu
 * @since 2021-12-01
 */
@Service
public class HefeiCurverSgDataServiceImpl extends ServiceImpl<HefeiCurverSgDataMapper, HefeiCurverSgData> implements HefeiCurverSgDataService {

    @Resource
    private HefeiCurverSgDataMapper hefeiCurverDao;

    @Resource
    private HefeiConsService hefeiConsService;

    @Resource
    private CustHisMapper custHisDao;

    private static SimpleDateFormat DataTimeFormate=new SimpleDateFormat("HH:mm");

    private static SimpleDateFormat DataTimeFormate2=new SimpleDateFormat("yyyy-MM-dd");

    //上次写入数据库的时间
    private Long lastUpdateTime= System.currentTimeMillis();

    //写入最大延迟时间
    private static final Long MAX_DELAY=15*60*1000L;

    //一次更新最大记录数
    private static final int BATCH_SIZE=5000;

    //待入库数据
    private final ThreadLocal<List<HefeiCurverSgData>> bachList=new ThreadLocal<>();

    @Override
    public  Map<String, BigDecimal> queryDataByConsDate(String consNo, String dateTime) {
        List<HefeiCurverSgData> hefeiCurverSgData = hefeiCurverDao.queryAllByConsNoDate(consNo, dateTime);
        Map<String, BigDecimal> res1=new HashMap<>();
        Map<String, BigDecimal> res2=new HashMap<>();
        if(hefeiCurverSgData!=null && hefeiCurverSgData.size()>0){
            for(int i=0;i<hefeiCurverSgData.size();i++){
                HefeiCurverSgData con = hefeiCurverSgData.get(i);
                //时刻-负荷
                res1.put(con.getDataTime().substring(11,16),con.getDataValue());
            }
            String[] dataTimeArr = getDataTimeArr();
            //判空
            for(int i=0;i<dataTimeArr.length;i++){
                BigDecimal bigDecimal = res1.get(dataTimeArr[i]);
                if(bigDecimal==null){
                    res1.put(dataTimeArr[i],null);
                }
            }
            //把时刻转换成p属性
            Map<String, String> dataTimeArrP96 = getDataTimeArrP96();
            Set<String> strings = res1.keySet();

            for(String key:strings){
                BigDecimal bigDecimal = res1.get(key);
                String p = dataTimeArrP96.get(key);
                res2.put(p,bigDecimal);
            }
        }
        return  res2;
    }
    /**
     * 生成96时段
     * @return
     */
    private static String[] getDataTimeArr(){
        String timesArr[]=new String[96];
        Calendar cal=Calendar.getInstance();
        cal.set(cal.getTime().getYear(),cal.getTime().getMonth(),cal.getTime().getDate(),0,0,0);
        timesArr[0]="00:00";
        for(int i=1;i<96;i++){
            cal.add(Calendar.MINUTE,15);
            timesArr[i]=DataTimeFormate.format(cal.getTime());
        }
        return timesArr;
    }

    /**
     * 96时刻对应P
     * @return
     */
    private static Map<String,String> getDataTimeArrP96(){
        String[] dataTimeArr = getDataTimeArr();
        Map<String,String> res=new HashMap<>();
        res.put(dataTimeArr[0],"p1");
        for(int i=1;i<96;i++){
            res.put(dataTimeArr[i],"p"+(i+1));
        }
        return res;
    }

    /**
     * 时间区间列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时间区间列表
     */
    private static  List<String> getDateListByDay (String startTime,String endTime) {
        List<String> days = new ArrayList<>();
        try {
            Date start = DataTimeFormate2.parse(startTime);
            Date end = DataTimeFormate2.parse(endTime);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(DataTimeFormate2.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        };
        return days;
    }

    @Override
    public List<ConsCurve> queryListCurve(String elecConsNo, String startDate, String endDate) {
        List<String> dateListByDay = getDateListByDay(startDate, endDate);
        List<ConsCurve> res=new ArrayList<>();
        try {
            if (dateListByDay.size() > 0) {
                for (int i = 0; i < dateListByDay.size(); i++) {
                    String data = dateListByDay.get(i);
                    String tableName = HisTableNameEnum.HIS_TABLE_NAME_SUFFIX.getCode() + data.replaceAll("-", "");
                    ConsCurve consCurve = custHisDao.queryListByTable(tableName, elecConsNo, data);
                    res.add(consCurve);
                }
            }
        }catch (Exception e){

        }
        return res;
    }
}
