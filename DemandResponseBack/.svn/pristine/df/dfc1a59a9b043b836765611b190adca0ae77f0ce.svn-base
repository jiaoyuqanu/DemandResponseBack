package com.xqxy.dr.modular.dispatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.dispatch.entity.Dispatch;
import com.xqxy.dr.modular.dispatch.entity.DispatchEvent;
import com.xqxy.dr.modular.dispatch.param.DispatchParam;

import java.util.List;

/**
 * <p>
 * 调度需求响应指令 Mapper 接口
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-14
 */
public interface DispatchMapper extends BaseMapper<Dispatch> {
    /**
     * 获取负荷调度列表
     * @return
     */
    List<Dispatch> getDispatchPageList(Page<Dispatch> page, String year);

    /**
     * 获取负荷调度详情
     * @param dispatchParam
     * @return
     */
    Dispatch getDispatchById(DispatchParam dispatchParam);

    /**
     * 总记录数
     * @param dispatchParam
     * @return
     */
    Integer getCount(DispatchParam dispatchParam);

    /**
     * 调度指令分解事件列表
     * @param page
     * @param year
     * @return
     */
    List<DispatchEvent> getEventPage(Page<DispatchEvent> page, String year);

    /**
     * 查询已下发调度列表
     */
    List<Dispatch> getDispatchResPageList(Page<Dispatch> page, String year);

}
