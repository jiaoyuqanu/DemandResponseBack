package com.xqxy.dr.modular.gwapp.service;

import com.xqxy.dr.modular.gwapp.entity.GwappCons;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 电力用户信息表 服务类
 * </p>
 *
 * @author Yechs
 * @since 2022-05-20
 */
public interface IGwappConsService extends IService<GwappCons> {

    List<GwappCons> getConsByCustId(Long custId);

    List<GwappCons> checkCons(Long custId);
}
