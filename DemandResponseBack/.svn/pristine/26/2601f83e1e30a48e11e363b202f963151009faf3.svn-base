package com.xqxy.dr.modular.bidding.service;

import com.xqxy.dr.modular.bidding.entity.ConsBiddingDeclare;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.bidding.param.ConsBiddingDeclareParam;

import java.util.List;

/**
 * <p>
 * 用户竞价申报记录 服务类
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
public interface ConsBiddingDeclareService extends IService<ConsBiddingDeclare> {

    /**
     * 批量添加竞价信息
     * @param consBiddingDeclareList
     *
     * @author shen
     * @date 2021-10-20 :13:57
     */
    void addBatch(List<ConsBiddingDeclare> consBiddingDeclareList);

    /**
     * 删除用户竞价信息
     * @param consBiddingDeclareParam
     *
     * @author shen
     * @date 2021-10-20
     */
    void delete(ConsBiddingDeclareParam consBiddingDeclareParam);


    /**
     * 批量修改竞价信息
     * @param consBiddingDeclareList
     *
     * @author shen
     * @date 2021-10-20 :14:57
     */
    void editBatch(List<ConsBiddingDeclare> consBiddingDeclareList);

}
