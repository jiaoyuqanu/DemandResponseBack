package com.xqxy.dr.modular.bidding.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.dr.modular.bidding.entity.BiddingRange;
import com.xqxy.dr.modular.bidding.mapper.BiddingRangeMapper;
import com.xqxy.dr.modular.bidding.service.BiddingRangeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 竞价发布范围 服务实现类
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
@Service
public class BiddingRangeServiceImpl extends ServiceImpl<BiddingRangeMapper, BiddingRange> implements BiddingRangeService {

    @Override
    public List<BiddingRange> list(Long noticeId) {
        LambdaQueryWrapper<BiddingRange> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(noticeId)) {
            queryWrapper.eq(BiddingRange::getNoticeId,noticeId);
        }
        return this.list(queryWrapper);
    }

    @Override
    public void delete(Long noticeId) {
        LambdaQueryWrapper<BiddingRange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BiddingRange::getNoticeId,noticeId);
        this.remove(queryWrapper);
    }

    @Override
    public void addBatch(List<BiddingRange> biddingRangeList) {
        this.saveBatch(biddingRangeList);
    }
}
