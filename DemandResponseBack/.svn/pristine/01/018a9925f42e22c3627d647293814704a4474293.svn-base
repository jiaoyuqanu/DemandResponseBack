package com.xqxy.dr.modular.grsg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.grsg.DTO.DrApplyRecDTO;
import com.xqxy.dr.modular.grsg.VO.DrApplyRecVO;
import com.xqxy.dr.modular.grsg.entity.DrApplyRec;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 绿色国网业务申请记录表 Mapper 接口
 * </p>
 *
 * @author liqirui
 * @since 2021-11-17
 */
public interface DrApplyRecMapper extends BaseMapper<DrApplyRec> {


    /**
     * @description: 分页列表展示
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    List<DrApplyRecVO> pageDrApplyRec(@Param("page") Page<DrApplyRecVO> page, @Param("drApplyRecDTO") DrApplyRecDTO drApplyRecDTO);
}
