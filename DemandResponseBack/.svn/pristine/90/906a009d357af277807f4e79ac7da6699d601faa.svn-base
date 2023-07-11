package com.xqxy.dr.modular.custConfig.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.custConfig.DTO.DrCustSysConfigDTO;
import com.xqxy.dr.modular.custConfig.entity.DrCustSysConfig;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liqirui
 * @since 2021-11-12
 */
public interface DrCustSysConfigService extends IService<DrCustSysConfig> {


    /**
     * 客户对接 分页查询
     * @param drCustSysConfigDTO
     * @return
     */
    Page<DrCustSysConfig> pageCustConfig(Page<DrCustSysConfig> page,DrCustSysConfigDTO drCustSysConfigDTO);


    /**
     * 客户对接 新增
     * @param drCustSysConfig
     * @return
     */
    void addCustConfig(DrCustSysConfig drCustSysConfig);


    /**
     * 客户对接 修改
     * @param drCustSysConfig
     * @return
     */
    void editCustConfig(DrCustSysConfig drCustSysConfig);


    /**
     * 客户对接 删除
     * @param id
     * @return
     */
    void deleteCustConfig(Long id);
}
