package com.xqxy.dr.modular.gwapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.gwapp.entity.GwappCust;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.gwapp.param.GwappCustAuditParam;
import com.xqxy.dr.modular.gwapp.param.GwappCustPageQuery;
import com.xqxy.dr.modular.gwapp.param.GwappRegistry;
import com.xqxy.dr.modular.gwapp.vo.GwappCustInfoVo;
import com.xqxy.dr.modular.gwapp.vo.GwappCustPageVo;

/**
 * <p>
 * 客户(对应注册用户) 服务类
 * </p>
 *
 * @author Yechs
 * @since 2022-05-20
 */
public interface IGwappCustService extends IService<GwappCust> {

    Page<GwappCustPageVo> queryPage(GwappCustPageQuery gwappCustPageQuery);

    GwappCustInfoVo getGwAppCustInfo(Long id);

    void auditGwappCust(Long custId, GwappCustAuditParam gwappCustAuditParam);

    void gwappRegistry(GwappRegistry gwappRegistry);

    GwappCustInfoVo getInfoByTel(String tel);
}
