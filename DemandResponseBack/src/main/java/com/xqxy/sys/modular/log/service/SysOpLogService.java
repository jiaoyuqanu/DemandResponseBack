package com.xqxy.sys.modular.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.sys.modular.log.entity.SysOpLog;
import com.xqxy.sys.modular.log.param.SysOpLogParam;

/**
 * 系统操作日志service接口
 *
 * @author xuyuxiang
 * @date 2020/3/12 14:21
 */
public interface SysOpLogService extends IService<SysOpLog> {

    /**
     * 查询系统操作日志
     *
     * @param sysOpLogParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/3/30 10:32
     */
    Page<SysOpLog> page(SysOpLogParam sysOpLogParam);

    /**
     * 清空系统操作日志
     *
     * @author xuyuxiang
     * @date 2020/6/1 11:05
     */
    void delete();
}
