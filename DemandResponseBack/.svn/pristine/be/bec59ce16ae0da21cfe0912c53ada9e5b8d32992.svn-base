package com.xqxy.dr.modular.subsidy.mapper;

import com.xqxy.dr.modular.subsidy.entity.SubsidyAppeal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.subsidy.param.SubsidyAppealParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
/**
 * <p>
 * 补贴申诉 Mapper 接口
 * </p>
 *
 * @author Shen
 * @since 2021-10-25
 */
public interface SubsidyAppealMapper extends BaseMapper<SubsidyAppeal> {

    @Select("<script>"
            +"select file_origin_name from dr_sys_file_info where 1=1" +
            "            <if test=\"fileId!='' and fileId != null\">\n" +
            "                AND file_id = #{fileId}\n" +
            "            </if>\n" +
            "</script>")
    String getFileName(@Param("fileId")String fileId);

    SubsidyAppeal getDetail(Long id);

    List<SubsidyAppeal> getCityManageList(@Param("subsidyAppealParam") SubsidyAppealParam subsidyAppealParam);

    Integer getCityManageTotals(@Param("subsidyAppealParam") SubsidyAppealParam subsidyAppealParam);

    SubsidyAppeal appealDetail(Long ig);

}
