package com.xqxy.dr.modular.statistics.service;

import com.xqxy.dr.modular.statistics.entity.ConsCurveSumView;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author shi
 * @since 2022-02-22
 */
public interface ConsCurveSumViewService extends IService<ConsCurveSumView> {

    List<ConsCurveSumView> getMaxmin();

}
