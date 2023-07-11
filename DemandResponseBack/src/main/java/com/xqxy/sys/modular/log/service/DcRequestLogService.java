package com.xqxy.sys.modular.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.sys.modular.log.entity.DcRequestLog;
import com.xqxy.sys.modular.log.param.DrRequestLogParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-06-21
 */
public interface DcRequestLogService extends IService<DcRequestLog> {

    /**
     * 中台请求日志分页查询
     * @param drRequestLogParam 查询参数
     * @return 查询分页结果
     * @author PengChuqing
     * @date 2021-03-11 15:49
     */
    Page<DcRequestLog> page(DrRequestLogParam drRequestLogParam);

}
