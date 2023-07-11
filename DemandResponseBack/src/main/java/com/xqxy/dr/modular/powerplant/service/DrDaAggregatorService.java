package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.powerplant.DTO.DrDaAggregatorDTO;
import com.xqxy.dr.modular.powerplant.entity.DaConsEffect;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregator;

import java.util.List;

/**
 * <p>
 * 负荷聚合商信息表 服务类
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
public interface DrDaAggregatorService extends IService<DrDaAggregator> {


    /**
     * 『负荷聚合商信息清单』 分页查询
     * @param
     * @return
     */
    Page<DrDaAggregator> pageAggregator(Page<DrDaAggregator> page, DrDaAggregatorDTO drDaAggregatorDTO);


    /**
     * 『负荷聚合商信息清单』 新增
     * @param
     * @return
     */
    ResponseData addAggregator(DrDaAggregator drDaAggregator);


    /**
     * 根据负荷聚合商id 查询对应的代理用户
     * @param
     * @return
     */
    Page<DaConsEffect> pageDaConsByAggregatorId(Page<DaConsEffect> page, DrDaAggregatorDTO drDaAggregatorDTO);


    /**
     * 『负荷聚合商信息清单』 审核
     * @param
     * @return
     */
    ResponseData editAggregator(DrDaAggregator drDaAggregator);


    /**
     * 『负荷聚合商信息清单』 下拉框模糊查询
     * @param
     * @return
     */
    List<DrDaAggregator> listAggregator(DrDaAggregatorDTO drDaAggregatorDTO);

    /**
     * 『该供电单位负荷聚合商信息清单』 分页查询
     * @param
     * @return
     */
    Page<DrDaAggregator> pageAggregatorByOrg(Page<DrDaAggregator> page, DrDaAggregatorDTO drDaAggregatorDTO);

    String getNameById(String id);
}
