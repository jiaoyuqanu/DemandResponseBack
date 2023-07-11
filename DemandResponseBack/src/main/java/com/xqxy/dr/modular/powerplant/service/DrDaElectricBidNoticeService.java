package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.entity.DrDaElectricBidNotice;
import com.xqxy.dr.modular.powerplant.param.DrDaElectricBidNoticeParam;

/**
 * <p>
 * 电力市场竞价公告信息 服务类
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
public interface DrDaElectricBidNoticeService extends IService<DrDaElectricBidNotice> {
    Page<DrDaElectricBidNotice> page(DrDaElectricBidNoticeParam param);

}
