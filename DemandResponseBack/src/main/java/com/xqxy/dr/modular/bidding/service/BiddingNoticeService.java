package com.xqxy.dr.modular.bidding.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.bidding.entity.BiddingNotice;
import com.xqxy.dr.modular.bidding.param.BiddingNoticeParam;
import com.xqxy.dr.modular.bidding.result.BiddingNoticeInfo;
import com.xqxy.dr.modular.project.params.ExamineParam;

/**
 * <p>
 * 竞价公告 服务类
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
public interface BiddingNoticeService extends IService<BiddingNotice> {

    /**
     * 查询竞价公告
     *
     * @param biddingNoticeParam 查询参数
     * @author shen
     * @date 2021-10-15 15:49
     * @return
     */
    Page<BiddingNotice> page(BiddingNoticeParam biddingNoticeParam);

    /**
     * 查看竞价公告
     *
     * @param biddingNoticeParam 查看参数
     * @return 竞价公告
     * @author shen
     * @date 2021-10-15 9:50
     */
    BiddingNoticeInfo detail(BiddingNoticeParam biddingNoticeParam);

    /**
     * 删除竞价信息
     * @param noticeId
     *
     * @author shen
     * @date 2021-10-18 10:35
     */
    void delete(Long noticeId);

    /**
     * 新增竞价信息
     * @param biddingNoticeInfo
     *
     * @author shen
     * @date 2021-10-18 11:21
     */
    void add(BiddingNoticeInfo biddingNoticeInfo);

    /**
     * 编辑竞价信息
     * @param biddingNoticeInfo
     *
     * @author shen
     * @date 2021-10-18 11:21
     */
    void edit(BiddingNoticeInfo biddingNoticeInfo);

    /**
     * 编辑竞价状态
     * @param biddingNoticeParam
     * @author shen
     * @date 2021-10-18 18:51
     */
    void editStatus(BiddingNoticeParam biddingNoticeParam);

    /**
     * 提交审核
     *
     * @param biddingNoticeParam
     *
     * @author shen
     * @date 2021-10-18 15:48
     */
    void submitCheck(BiddingNoticeParam biddingNoticeParam);


    /**
     * 竞价审核
     * @param examineParam
     *
     * @author shen
     * @date 2021-10-18 17:08
     */
    void examine(ExamineParam examineParam);

}
