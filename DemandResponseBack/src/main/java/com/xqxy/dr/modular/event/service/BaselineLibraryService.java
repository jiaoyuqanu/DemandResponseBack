package com.xqxy.dr.modular.event.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.baseline.entity.BaseLine;
import com.xqxy.dr.modular.event.entity.BaselineLibrary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.param.EventParam;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-18
 */
public interface BaselineLibraryService extends IService<BaselineLibrary> {

    /**
     * @description: 通过日期和时段获取基线库
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/10/18 14:21
     */
    BaselineLibrary getByDateAndPeriod(LocalDate regulateDate, String startTime, String endTime);

    /**
     * @description: 基线时段范围大于事件时间范围的分页
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/11/18 15:20
     */
    Page<BaselineLibrary> pageEventBaseline(EventParam eventParam);
}
