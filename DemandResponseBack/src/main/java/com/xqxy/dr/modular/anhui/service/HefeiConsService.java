package com.xqxy.dr.modular.anhui.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.anhui.entity.HefeiCons;

import java.util.List;

/**
 * <p>
 * 合肥策略营销档案 服务类
 * </p>
 *
 * @author liuyu
 * @since 2021-12-01
 */
public interface HefeiConsService extends IService<HefeiCons> {

    /**
     * 查询国泉表中的营销档案
     * @param elecNo 营销户号
     * @param consName 用户名
     * @param orgNo 供电单位
     * @return 国泉档案表中的用户信息
     */
    HefeiCons queryCons(String elecNo, String consName, String orgNo);

    /**]
     * 查询所有户号
      * @return
     */
    List<String> consRes();
}
