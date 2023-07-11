package com.xqxy.dr.modular.device.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.device.entity.TDevBranch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 终端分路 Mapper 接口
 * </p>
 *
 * @author dw
 * @since 2022-04-20
 */

@Mapper
@Repository
public interface TDevBranchMapper extends BaseMapper<TDevBranch> {

}
