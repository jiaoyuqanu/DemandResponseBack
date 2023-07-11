package com.xqxy.dr.modular.adjustable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.adjustable.DTO.DrConsAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.DTO.DrConsUserAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.VO.DrConsAdjustablePotentialVO;
import com.xqxy.dr.modular.adjustable.VO.GroupCityAdjustVO;
import com.xqxy.dr.modular.adjustable.VO.GroupConsTypeAdjustVO;
import com.xqxy.dr.modular.adjustable.entity.DrConsAdjustablePotential;

import java.math.BigDecimal;
import java.util.List;

public interface DrConsAdjustablePotentialService extends IService<DrConsAdjustablePotential> {

    /**
     * 用户可调节潜力分页
     * @param consAdjustablePotentialDTO
     * @return
     */
    List<DrConsAdjustablePotentialVO> pageConsAdjustable(Page<DrConsAdjustablePotentialVO> page,DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);

    /**
     * 电力用户/用户可调节潜力分页
     * @param consAdjustablePotentialDTO
     * @return
     */
    Page<DrConsAdjustablePotentialVO> pageUserConsAdjustable(DrConsUserAdjustablePotentialDTO consAdjustablePotentialDTO);


    /**
     * 用户可调节潜力 新增
     * @param consAdjustablePotential
     * @return
     */
    void addConsAdjustable(DrConsAdjustablePotential consAdjustablePotential);


    /**
     * 用户可调节潜力 修改
     * @param consAdjustablePotential
     * @return
     */
    void editConsAdjustable(DrConsAdjustablePotential consAdjustablePotential);


    /**
     * 用户可调节潜力 删除
     * @param consId
     * @return
     */
    void deleteConsAdjustable(String consId);


    /**
     * 用户可调节潜力 分组条件为 市码，查询条件为年度
     * @param consAdjustablePotentialDTO
     * @return
     */
    List<GroupCityAdjustVO> groupCityAdjustable( Page<GroupCityAdjustVO> page,DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);


    /**
     * 用户可调节潜力 分组条件为 用户类型，查询条件为年度 市码
     * @param consAdjustablePotentialDTO
     * @return
     */
    List<GroupConsTypeAdjustVO> groupConsTypeAdjustable(Page<GroupConsTypeAdjustVO> page,DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);

    /**
     * 用户可调节潜力 查询保安负荷
     * @param consId
     * @return
     */
    BigDecimal getSafetyLoadByConsId(String consId);

    /**
     * 根据户号查询用户潜力档案
     * @param consId
     * @return
     */
    DrConsAdjustablePotential getbyConsId(String consId);

    /**
     * 查询所有 用户可调节潜力
     * @param consAdjustablePotentialDTO
     * @return
     */
    List<DrConsAdjustablePotentialVO> exportConsAdjustable(DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);


    /**
     * 查询所有 用户可调节潜力 分组条件为 用户类型，查询条件为年度 市码
     * @param consAdjustablePotentialDTO
     * @return
     */
    List<GroupConsTypeAdjustVO> exportConsTypeAdjustable(DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);


    /**
     * 查询所有 用户可调节潜力 分组条件为 市码，查询条件为年度
     * @param consAdjustablePotentialDTO
     * @return
     */
    List<GroupCityAdjustVO> exportCityAdjustable(DrConsAdjustablePotentialDTO consAdjustablePotentialDTO);
}
