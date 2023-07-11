package com.xqxy.dr.modular.event.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.event.entity.EventExecute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.event.entity.PlanCons;
import com.xqxy.dr.modular.event.param.EventExecuteParam;
import com.xqxy.dr.modular.newloadmanagement.vo.ExchPointCurve96Vo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 电力用户执行信息 Mapper 接口
 * </p>
 *
 * @author Shen
 * @since 2021-10-27
 */
public interface EventExecuteMapper extends BaseMapper<EventExecute> {

    /**
     * 事件实时监测
     * @param evnetId
     * @param pageNo
     * @param pageSize
     * @return
     * @author shen
     * @date 2021-07-06
     */
    @Select("<script>" +
            "SELECT\n" +
            "\texecute_time executeTime,\n" +
            "\tSUM( execute_cap ) executeCap,\n" +
            "\tSUM(baseline_cap) baselineCap,\n" +
            "\tSUM( max_load_baseline ) maxLoadBaseline,\n" +
            "\tSUM( reply_cap ) replyCap,\n" +
            "\tSUM(max_execute_cap) maxExecuteCap\n" +
            "FROM\n" +
            "\tdr_event_execute \n" +
            "WHERE\n" +
            "\tevent_id = #{eventId} \n" +
            "GROUP BY\n" +
            "\texecute_time\n" +
            "ORDER BY execute_time DESC" +
            "<if test=\"pageSize != 0 \">\n" +
            "   LIMIT #{pageNo}, #{pageSize}\n" +
            "</if>" +
            "</script>")
    List<EventExecute> eventMonitoring(Long eventId,int pageNo,int pageSize);

    Page<EventExecute> selectPageVo(Page<Object> defaultPage, @Param("eventExecuteParam") EventExecuteParam eventExecuteParam);

    List<ExchPointCurve96Vo> getMaxReduceCurve (Map<String,Object> map);

    List<ExchPointCurve96Vo> getMaxReduceDayCurve (Map<String,Object> map);

    List<PlanCons> getTowHighCons(@Param("consList") List<String> consList);

    List<String> queryExeConsIdByEventId(Long eventId);


}
