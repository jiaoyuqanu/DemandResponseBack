package com.xqxy.dr.modular.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.event.VO.OrgExecuteVO;
import com.xqxy.dr.modular.event.entity.OrgExecute;
import com.xqxy.dr.modular.event.param.OrgExecuteParam;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 组织机构执行信息 Mapper 接口
 * </p>
 *
 * @author liqirui
 * @since 2022-03-01
 */
public interface OrgExecuteMapper extends BaseMapper<OrgExecute> {


    /**
     * 执行监测 -- 组织机构监测
     *
     * @author lqr
     * @date 2022-03-01 8:49
     */
    Page<OrgExecuteVO> pageOrgExecute(@Param("page") Page page, @Param("orgExecuteParam") OrgExecuteParam orgExecuteParam);
}
