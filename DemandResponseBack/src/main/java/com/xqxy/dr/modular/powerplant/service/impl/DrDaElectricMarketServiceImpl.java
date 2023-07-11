package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.powerplant.DTO.DrDaElectricMarketDTO;
import com.xqxy.dr.modular.powerplant.entity.DrDaElectricMarket;
import com.xqxy.dr.modular.powerplant.mapper.DrDaElectricMarketMapper;
import com.xqxy.dr.modular.powerplant.service.DrDaElectricMarketService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电力市场需求信息表 服务实现类
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@Service
public class DrDaElectricMarketServiceImpl extends ServiceImpl<DrDaElectricMarketMapper, DrDaElectricMarket> implements DrDaElectricMarketService {

    /**
     * 『电力市场需求信息』 分页
     * @param
     * @return
     */
    @Override
    public Page<DrDaElectricMarket> pageElectricMarket(Page<DrDaElectricMarket> page, DrDaElectricMarketDTO daElectricMarketDTO) {
        QueryWrapper<DrDaElectricMarket> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(daElectricMarketDTO)) {
            // 根据时间
            if (ObjectUtil.isNotEmpty(daElectricMarketDTO.getStartDate())) {
                queryWrapper.ge("date",daElectricMarketDTO.getStartDate());
            }
            if (ObjectUtil.isNotEmpty(daElectricMarketDTO.getEndDate())) {
                queryWrapper.le("date",daElectricMarketDTO.getEndDate());
            }
        }


        Page<DrDaElectricMarket> marketPage = this.page(page, queryWrapper);
        return marketPage;
    }
}
