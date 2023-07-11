package com.xqxy.dr.modular.bidding.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.dr.modular.bidding.entity.ConsBiddingDeclare;
import com.xqxy.dr.modular.bidding.mapper.ConsBiddingDeclareMapper;
import com.xqxy.dr.modular.bidding.param.ConsBiddingDeclareParam;
import com.xqxy.dr.modular.bidding.service.ConsBiddingDeclareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户竞价申报记录 服务实现类
 * </p>
 *
 * @author Shen
 * @since 2021-10-15
 */
@Service
public class ConsBiddingDeclareServiceImpl extends ServiceImpl<ConsBiddingDeclareMapper, ConsBiddingDeclare> implements ConsBiddingDeclareService {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBatch(List<ConsBiddingDeclare> consBiddingDeclareList) {
//        ConsBiddingDeclareParam consBiddingDeclareParam = new ConsBiddingDeclareParam();
//        consBiddingDeclareParam.setConsId(consBiddingDeclareList.get(0).getConsId());
//        consBiddingDeclareParam.setNoticeId(consBiddingDeclareList.get(0).getNoticeId());
//        this.delete(consBiddingDeclareParam);
        this.saveBatch(consBiddingDeclareList);
    }

    @Override
    public void delete(ConsBiddingDeclareParam consBiddingDeclareParam) {
        LambdaQueryWrapper<ConsBiddingDeclare> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consBiddingDeclareParam)) {
            // 用户标识
            if (ObjectUtil.isNotEmpty(consBiddingDeclareParam.getConsId())) {
                queryWrapper.eq(ConsBiddingDeclare::getConsId,consBiddingDeclareParam.getConsId());
            }
            // 竞价标识
            if (ObjectUtil.isNotEmpty(consBiddingDeclareParam.getNoticeId())) {
                queryWrapper.eq(ConsBiddingDeclare::getNoticeId,consBiddingDeclareParam.getNoticeId());
            }
        }
        this.remove(queryWrapper);
    }

    @Override
    public void editBatch(List<ConsBiddingDeclare> consBiddingDeclareList) {
        this.updateBatchById(consBiddingDeclareList);
    }
}
