package com.xqxy.dr.modular.statistics.service.impl;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqxy.dr.modular.statistics.entity.ConsCurveSumView;
import com.xqxy.dr.modular.statistics.mapper.ConsCurveSumViewMapper;
import com.xqxy.dr.modular.statistics.service.ConsCurveSumViewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author shi
 * @since 2022-02-22
 */
@Service
public class ConsCurveSumViewServiceImpl extends ServiceImpl<ConsCurveSumViewMapper, ConsCurveSumView> implements ConsCurveSumViewService {

    public List<ConsCurveSumView> getMaxmin() {
        LambdaQueryWrapper<ConsCurveSumView> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<ConsCurveSumView> list = this.list(lambdaQueryWrapper);
        for (ConsCurveSumView consCurveSumView : list) {
            int min=1;
            int max=1;
            for (int i = 2; i < 97; i++) {

                BigDecimal cap = this.ToBiaDecimal(ReflectUtil.getFieldValue(consCurveSumView, "p" + i));
                BigDecimal maxCap = this.ToBiaDecimal(ReflectUtil.getFieldValue(consCurveSumView, "p" + max));
                BigDecimal minCap = this.ToBiaDecimal(ReflectUtil.getFieldValue(consCurveSumView, "p" + min));
                if(cap.compareTo(maxCap)==1)
                {
                    max=i;
                }
                if(cap.compareTo(minCap)==-1)
                {
                    min=i;
                }



            }
            System.out.println(max+","+consCurveSumView.getOrgNo()+","+this.ToBiaDecimal(ReflectUtil.getFieldValue(consCurveSumView, "p" + max)));
            System.out.println(min+","+consCurveSumView.getOrgNo()+ ","+this.ToBiaDecimal(ReflectUtil.getFieldValue(consCurveSumView, "p" + min)));
        }
        return list;
    }


    public static BigDecimal ToBiaDecimal(Object value) {
        BigDecimal bigDec = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                bigDec = (BigDecimal) value;
            } else if (value instanceof String) {
                bigDec = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                bigDec = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                bigDec = new BigDecimal(((Number)
                        value).doubleValue());
            } else {
                throw new ClassCastException("Can Not make (" + value + "] into a BigDecimal.");
            }
        }
        return bigDec;

    }
}
