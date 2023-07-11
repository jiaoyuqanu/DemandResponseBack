package com.xqxy.dr.modular.bidding.service;

import com.xqxy.dr.modular.bidding.entity.BiddingDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 竞价明细 服务类
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
public interface BiddingDetailService extends IService<BiddingDetail> {

    /**
     *
     * @param noticeId
     * @return
     *
     * @author shen
     * @date 2021-10-18  09:41
     */
    List<BiddingDetail> list(Long noticeId);

    /**
     * 删除竞价明细信息
     * @param noticeId
     *
     * @author shen
     * @date 2021-10-18 10:42
     */
    void delete(Long noticeId);

    /**
     * 批量添加竞价明细
     * @param biddingDetailList
     *
     * @author shen
     * @date 2021-10-18 11:26
     */
    void addBatch(List<BiddingDetail> biddingDetailList);

}
