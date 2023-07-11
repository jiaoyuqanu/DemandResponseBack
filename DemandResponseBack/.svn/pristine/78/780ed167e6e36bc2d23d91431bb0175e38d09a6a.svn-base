package com.xqxy.dr.modular.adjustable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.adjustable.DTO.DrConsAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.DTO.DrConsUserAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.VO.DrConsAdjustablePotentialVO;
import com.xqxy.dr.modular.adjustable.VO.GroupCityAdjustVO;
import com.xqxy.dr.modular.adjustable.VO.GroupConsTypeAdjustVO;
import com.xqxy.dr.modular.adjustable.VO.WorkCityYearAdjustVo;
import com.xqxy.dr.modular.adjustable.entity.DrConsAdjustablePotential;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DrConsAdjustablePotentialMapper extends BaseMapper<DrConsAdjustablePotential> {

    /**
     * <pre>用户可调节潜力分页
     * 修改接口：添加数据权限 2022/2/21<pre>
     * @param consAdjustablePotentialDTO 用户可调节潜力入参
     */
    List<DrConsAdjustablePotentialVO> pageConsAdjustable(@Param("page") Page<DrConsAdjustablePotentialVO> page, @Param("consAdjustablePotentialDTO") DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);

    /**
     * <pre>电力用户/用户可调节潜力分页
     * @param consAdjustablePotentialDTO 用户可调节潜力入参
     * @param consIds 电力用户列表
     */
    Page<DrConsAdjustablePotentialVO> pageUserConsAdjustable(@Param("page") Page<DrConsAdjustablePotentialVO> page, @Param("consIds") List<String> consIds, @Param("consAdjustablePotentialDTO") DrConsUserAdjustablePotentialDTO consAdjustablePotentialDTO);


    /**
     * 用户可调节潜力 分组条件为 市码，查询条件为年度
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    List<GroupCityAdjustVO> groupCityAdjustable(@Param("page") Page<GroupCityAdjustVO> page, @Param("consAdjustablePotentialDTO") DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);


    /**
     * 用户可调节潜力 分组条件为 用户类型，查询条件为年度 市码
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    List<GroupConsTypeAdjustVO> groupConsTypeAdjustable(@Param("page") Page<GroupConsTypeAdjustVO> page, @Param("consAdjustablePotentialDTO") DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);


    /**
     * 用户可调节潜力分页
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    List<DrConsAdjustablePotentialVO> exportConsAdjustable(DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);


    /**
     * 查询所有 用户可调节潜力 分组条件为 用户类型，查询条件为年度 市码
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    List<GroupConsTypeAdjustVO> exportConsTypeAdjustable(DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);


    /**
     * 查询所有 用户可调节潜力 分组条件为 市码，查询条件为年度
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    List<GroupCityAdjustVO> exportCityAdjustable(DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);

    WorkCityYearAdjustVo getWorkCityYearAdjust(@Param("orgNo") String orgNo, @Param("year") Integer year);
}
