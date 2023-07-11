
package com.xqxy.dr.modular.data.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.core.pojo.page.PageResult;
import com.xqxy.dr.modular.data.entity.ConsCurve;
import com.xqxy.dr.modular.data.param.ConsAndDate;
import com.xqxy.dr.modular.data.param.ConsCurveConsParam;
import com.xqxy.dr.modular.data.param.ConsCurveParam;
import com.xqxy.dr.modular.event.entity.EventPowerSample;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 用户功率曲线 服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
public interface ConsCurveService extends IService<ConsCurve> {

    /**
     * 查询用户功率曲线
     *
     * @param consCurveParam 查询参数
     * @return 查询分页结果
     * @author xiao jun
     * @date 2021-03-11 15:49
     */
    PageResult<ConsCurve> page(ConsCurveParam consCurveParam);

    /**
     * 用户功率曲线列表
     *
     * @param consCurveParam 查询参数
     * @return 组织机构列表
     * @author xiao jun
     * @date 2021-03-11 15:49
     */
    Object list(ConsCurveParam consCurveParam);

    /**
     * 添加用户功率曲线
     *
     * @param consCurveParam 添加参数
     * @author xiao jun
     * @date 2020/3/25 14:57
     */
    void add(ConsCurveParam consCurveParam);

    /**
     * 删除用户功率曲线
     *
     * @param consCurveParam 删除参数
     * @author xiao jun
     * @date 2020/3/25 14:57
     */
    void delete(ConsCurveParam consCurveParam);

    /**
     * 编辑用户功率曲线
     *
     * @param consCurveParam 编辑参数
     * @author xiao jun
     * @date 2020/3/25 14:58
     */
    void edit(ConsCurveParam consCurveParam);

    /**
     * 查看用户功率曲线
     *
     * @param consCurveParam 查看参数
     * @return 组织机构
     * @author xiao jun
     * @date 2020/3/26 9:50
     */
    ConsCurve detail(ConsCurveParam consCurveParam);


    /**
     * 通过用户编号集合和日期查询曲线
     * @param consCurveConsParam
     * @return
     */
    Page<ConsCurve> getHistoricalCurve(ConsCurveConsParam consCurveConsParam);

    /**
     * @description: 通过consId和日期获取曲线
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/4/10 11:25
     */
    ConsCurve getCurveByConsIdAndDate(String consId, String dataDate);

    /**
     * @description: 通过不同策略获取曲线信息
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/20 23:30
     */
    ConsCurve getCurveByConsIdAndDate(ConsCurveParam consCurveParam);


    /**
     * @description: 用户历史负荷
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2021/11/0 13:39
     */
    List<ConsCurve> queryConsCurveByConsAndDateList(ConsAndDate consAndDate);

//    /**
//     * @description: 数据监测历史数据接口
//     * @param:
//     * @return:
//     * @author: PengChuqing
//     * @date: 2021/4/11 14:03
//     */
//    List<PointGotten> getPointPercent(ConsAndDate consAndDate);

    /**
     * @description: 传入用户集合和日期，查询指定日期下所有曲线
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/4/11 13:24
     */
//    List<ConsCurve> getCurveByConsIdList(List<Cons> directCons, LocalDate statDate);

//    /**
//     * @description: 获取当前用户今日负荷曲线
//     * @param:
//     * @return:
//     * @author: PengChuqing
//     * @date: 2021/4/11 15:26
//     */
//    ConsCurve getTodayCurve(ConsCurveParam consCurveParam);

    /**
     * @description: 获取当前用户去年今日负荷曲线
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/4/11 15:26
     */
//    ConsCurve getLastyearTodayCurve();

    /**
     * @description: 实时监测用户拿到的点数
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/4/13 16:54
     */
//    List<PointGotten> getTodayPointPercent(ConsAndDate consAndDate);

    /**
     * 通过ConsNo和日期集合批量查询曲线
     * @param consParam
     * @return
     */
//    List<ConsCurve> getCurveByIdAndDateList(ConsParam consParam);

    /**
     * 通过用户，开始时间，结束时间查询历史负荷
     * @param consId
     * @param startDate
     * @param endDate
     * @return
     */
//    List<ConsCurve> getHistoryCurveList(String consId, String startDate, String endDate);


    /**
     * @description: 负荷预测 ，根据时间和户号集合 返回一条 sum(p1),sum(p2),sum(p3), 的 历史负荷数据
     * @param:
     * @return:
     * @author: liqirui
     * @date: 2022/1/15 13:39
     */
    ConsCurve getCurveByConsIdListAndDate(List<String> condIdList, String date);

    List<ConsCurve> getCurveByConsIdListAndDate2(List<String> condIdList, String date);

    List<ConsCurve> getCurveByConsIdListAndDate3(List<String> condIdList, String date);

    List<ConsCurve> getCurveAllByDate(List<String> simpList,Integer size);

    List<EventPowerSample> getCurveAllAmendByDate(List<String> simpList,List<String> cons,Long baselinId);

    List<EventPowerSample> getCurveAmendByDate(List<String> simpList,List<String> cons,Long baselinId);

}



