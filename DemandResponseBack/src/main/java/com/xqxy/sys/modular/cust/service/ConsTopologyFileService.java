package com.xqxy.sys.modular.cust.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.sys.modular.cust.entity.ConsTopologyFile;

import java.util.List;

/**
 * <p>
 * 用户拓扑图表 服务类
 * </p>
 *
 * @author liqirui
 * @since 2022-05-05
 */
public interface ConsTopologyFileService extends IService<ConsTopologyFile> {


    /**
     * 批量新增 用户拓扑图
     * @author lqr
     * @return
     */
    ResponseData insertBatch(List<ConsTopologyFile> consTopoologyFiles);



    /**
     * 根据主键删除 用户拓扑图
     * @author lqr
     * @return
     */
    void deleteByFileId(Long id);
}
