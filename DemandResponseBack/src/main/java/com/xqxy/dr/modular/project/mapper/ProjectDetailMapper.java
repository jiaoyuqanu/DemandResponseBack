package com.xqxy.dr.modular.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.xqxy.dr.modular.project.entity.ProjectDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目明细 Mapper 接口
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-11
 */
public interface ProjectDetailMapper extends BaseMapper<ProjectDetail> {
    /**
     * 查看用户签约详情
     *
     * @param wrapper 条件构造器
     * @return com.xqxy.sys.modular.cust.entity.Cons
     * @author Caoj
     * @date 11/10/2021 14:28
     */
    Map<String,Object> consDeclareDetail(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 用户签约详情
     *
     * @param contractId 用户签约标识
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @date 11/18/2021 13:58
     * @author Caoj
     */
    List<ProjectDetail> listContractInfo(String contractId);


}
