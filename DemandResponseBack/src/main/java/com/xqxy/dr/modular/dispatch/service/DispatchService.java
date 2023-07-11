package com.xqxy.dr.modular.dispatch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.dispatch.entity.Dispatch;
import com.xqxy.dr.modular.dispatch.entity.DispatchEvent;
import com.xqxy.dr.modular.dispatch.param.DispatchAndSoltParam;
import com.xqxy.dr.modular.dispatch.param.DispatchEditorParam;
import com.xqxy.dr.modular.dispatch.param.DispatchParam;
import com.xqxy.dr.modular.event.param.EventParam;

/**
 * <p>
 * 调度需求响应指令 服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-05-14
 */
public interface DispatchService extends IService<Dispatch> {

    /**
     * @description:
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/5/17 20:16
     */
    Long add(DispatchAndSoltParam dispatchAndSoltParam);

    /**
     * @description: 调度指令删除
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/5/17 21:21
     */
    void delete(DispatchParam dispatchParam);

    /**
     * @description: 调度指令撤销
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/17 21:21
     */
    String remove(DispatchParam dispatchParam);


    /**
     * @description: 调度指令修改
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/5/18 8:47
     */
    void edit(DispatchEditorParam dispatchAndSoltParam);
    
    /**
     * @description: 指令分页查询
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/5/18 9:17
     */
    Page<Dispatch> page(DispatchParam dispatchParam);


    Dispatch getDispatchById(DispatchParam dispatchParam);

    /**
     * @description: 指令下发
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/5/20 16:48
     */
    ResponseData issueDispatch(DispatchEditorParam dispatchParam);

    /**
     * @description: 指令分解分页查询
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/18 9:17           ,
     */
    Page<DispatchEvent> getPage(DispatchParam dispatchParam);

    /**
     * @description: 查询已下发调度列表
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/18 9:17
     */
    Page<Dispatch> getDispatchResPageList(DispatchParam dispatchParam);

    /**
     * @description: 添加事件
     * @param:
     * @return:
     * @author: chenzhijun
     * @date: 2021/5/20 16:48
     */
    ResponseData addDispatchEvent(DispatchParam dispatchParam);

     String getRegulateRangeStr (DispatchParam dispatchParam);

    ResponseData sendBaseLineAndCons(EventParam eventParam);

    ResponseData sendCityTarget(EventParam eventParam);
}
