package com.xqxy.dr.modular.baseline.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.core.pojo.page.PageResult;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.baseline.entity.BaseLineDetail;
import com.xqxy.dr.modular.baseline.param.BaseLineDetailParam;
import com.xqxy.dr.modular.baseline.param.BaseLineParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基线详情 服务类
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-18
 */
public interface BaseLineDetailService extends IService<BaseLineDetail> {
    /**
     * @description:
     * @param:
     * @return:
     * @author: chen zhi jun
     * @date: 2021/10/19 13:44
     */
    Page<BaseLineDetail> detail(BaseLineDetailParam baseLineDetailParam);

    /**
     * @description:
     * @param:
     * @return:
     * @author: chen zhi jun
     * @date: 2021/10/19 13:44
     */
    Page<BaseLineDetail> detailCust(BaseLineDetailParam baseLineDetailParam);

    Map<String,Object> getDetailData(BaseLineDetailParam baseLineDetailParam);

    Map<String,Object> getDetailDataCust(BaseLineDetailParam baseLineDetailParam);


}
