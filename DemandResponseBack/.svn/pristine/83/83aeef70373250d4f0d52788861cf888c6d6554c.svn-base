package com.xqxy.dr.modular.powerplant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.powerplant.entity.DaConsExecute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.powerplant.param.DaConsExecuteParam;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shi
 * @since 2021-12-15
 */
public interface DaConsExecuteService extends IService<DaConsExecute> {


    Page<DaConsExecute> page (DaConsExecuteParam daConsExecuteParam);

    List<DaConsExecute> list (DaConsExecuteParam daConsExecuteParam);

    List<DaConsExecute> regionExecuteStatistics(String orgId);
}
