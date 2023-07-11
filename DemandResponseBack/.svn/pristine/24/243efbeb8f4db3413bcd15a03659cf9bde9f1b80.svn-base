package com.xqxy.dr.modular.powerplant.mapper;

import com.xqxy.dr.modular.powerplant.entity.DaConsExecute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shi
 * @since 2021-12-15
 */
public interface DaConsExecuteMapper extends BaseMapper<DaConsExecute> {

    @Select("<script>" +
            "select org_no ," +
            "IFNULL(sum(baseline_load),0)," +
            "IFNULL(sum(plan_load),0)," +
            "IFNULL(reply_load,0)," +
            "COUNT(is_effect='Y' OR NULL)," +
            "COUNT(is_effect='N' OR NULL)," +
            "count(1) from dr_da_cons_execute  \n" +
            "where 1=1\n"+
            "<if test=\" orgNo!= null and orgNo= ''\">\n" +
            "            AND org_no =#{orgNo}\n" +
            "        </if>" +
            "</script>")
    DaConsExecute getRegionStatistics(String orgNo);

}
