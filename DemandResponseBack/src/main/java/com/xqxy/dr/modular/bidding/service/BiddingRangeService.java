package com.xqxy.dr.modular.bidding.service;

import com.xqxy.dr.modular.bidding.entity.BiddingRange;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 竞价发布范围 服务类
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
public interface BiddingRangeService extends IService<BiddingRange> {


    /**
     * 竞价帆布范围详情
     * @param noticeId
     * @return
     *
     * @author shen
     * @date 2021-10-18-9:55
     */
    List<BiddingRange> list(Long noticeId);

    /**
     * 删除竞价发布范围
     * @param noticeId
     *
     * @author shen
     * @date 2021-10-18 10:39
     */
    void delete(Long noticeId);

    /**
     * 批量添加竞价范围
     * @param biddingRangeList
     *
     * @author shen
     * @date 2021-10-18 11:25
     */
    void addBatch(List<BiddingRange> biddingRangeList);

}
