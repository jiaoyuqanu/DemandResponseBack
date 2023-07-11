package com.xqxy.dr.modular.bidding.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.dr.modular.bidding.entity.BiddingDetail;
import com.xqxy.dr.modular.bidding.mapper.BiddingDetailMapper;
import com.xqxy.dr.modular.bidding.service.BiddingDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 竞价明细 服务实现类
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
@Service
public class BiddingDetailServiceImpl extends ServiceImpl<BiddingDetailMapper, BiddingDetail> implements BiddingDetailService {

    @Override
    public List<BiddingDetail> list(Long noticeId) {
        LambdaQueryWrapper<BiddingDetail> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(noticeId)) {
            queryWrapper.eq(BiddingDetail::getNoticeId,noticeId);
        }
        return this.list(queryWrapper);
    }

    @Override
    public void delete(Long noticeId) {
        LambdaQueryWrapper<BiddingDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BiddingDetail::getNoticeId,noticeId);
        this.remove(queryWrapper);
    }

    @Override
    public void addBatch(List<BiddingDetail> biddingDetailList) {
        this.saveBatch(biddingDetailList);
    }
}
