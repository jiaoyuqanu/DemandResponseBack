package com.xqxy.dr.modular.grsg.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.grsg.DTO.CustDTO;
import com.xqxy.dr.modular.grsg.DTO.DrApplyRecDTO;
import com.xqxy.dr.modular.grsg.VO.DrApplyRecVO;
import com.xqxy.dr.modular.grsg.entity.DrApplyRec;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.sys.modular.cust.entity.Cons;

import java.util.List;

/**
 * <p>
 * 绿色国网业务申请记录表 服务类
 * </p>
 *
 * @author liqirui
 * @since 2021-11-17
 */
public interface DrApplyRecService extends IService<DrApplyRec> {

    /**
     * @description: 申请业务工单接口
     * @param: DrApplyRec
     * @return:
     * @author: liqirui
     * @date: 2021/11/17 14:27
     */
    Boolean sendAppOrder(DrApplyRecDTO drApplyRecDTO);


    /**
     * @description: 用户是否开通业务接口
     * @param: DrApplyRec
     * @return:
     * @author: liqirui
     * @date: 2021/11/17 15:27
     */
    String checkUserOpenApp(CustDTO custDTO);


    /**
     * @description: 分页列表展示
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    List<DrApplyRecVO> pageDrApplyRec(Page<DrApplyRecVO> page, DrApplyRecDTO drApplyRecDTO);


    /**
     * @description: 需求响应 详情
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    DrApplyRec detailDrApplyRec( Long id);


    /**
     * @description: 需求响应 详情
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    void editDrApplyRec( DrApplyRecDTO drApplyRecDTO);



    /**
     * @description: 需求响应 查询用户
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    List<Cons> listCons(String consNoList);
}
