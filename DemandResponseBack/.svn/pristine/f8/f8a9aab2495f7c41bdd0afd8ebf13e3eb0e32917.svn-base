package com.xqxy.dr.modular.custConfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.custConfig.DTO.DrCustSysConfigDTO;
import com.xqxy.dr.modular.custConfig.entity.DrCustSysConfig;
import com.xqxy.dr.modular.custConfig.mapper.DrCustSysConfigMapper;
import com.xqxy.dr.modular.custConfig.service.DrCustSysConfigService;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.param.CustInfoParam;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.utils.SnowflakeIdWorker;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liqirui
 * @since 2021-11-12
 */
@Service
public class DrCustSysConfigServiceImpl extends ServiceImpl<DrCustSysConfigMapper, DrCustSysConfig> implements DrCustSysConfigService {


    /**
     * 客户对接 分页查询
     * @param drCustSysConfigDTO
     * @return
     */
    @Override
    public Page<DrCustSysConfig> pageCustConfig(Page<DrCustSysConfig> page,DrCustSysConfigDTO drCustSysConfigDTO) {
        QueryWrapper<DrCustSysConfig> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(drCustSysConfigDTO.getCustName())){
            queryWrapper.like("CUST_NAME",drCustSysConfigDTO.getCustName());
        }

//        List<DrCustSysConfig> list = this.list(queryWrapper);
        Page<DrCustSysConfig> pageDrCustSysConfig = this.page(page, queryWrapper);
        return pageDrCustSysConfig;
    }

    private CustService custService;
    /**
     * 客户对接 新增
     * @param drCustSysConfig
     * @return
     */
    @Override
    public void addCustConfig(DrCustSysConfig drCustSysConfig) {
        drCustSysConfig.setId(SnowflakeIdWorker.generateId());
        this.save(drCustSysConfig);
    }


    /**
     * 客户对接 修改
     * @param drCustSysConfig
     * @return
     */
    @Override
    public void editCustConfig(DrCustSysConfig drCustSysConfig) {
        this.updateById(drCustSysConfig);
    }


    /**
     * 客户对接 删除
     * @param id
     * @return
     */
    @Override
    public void deleteCustConfig(Long id) {
        this.removeById(id);
    }
}
