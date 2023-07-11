package com.xqxy.sys.modular.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.sys.modular.dict.entity.DictData;

import java.util.List;

/**
 * <p>
 * 字典值 Mapper 接口
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
public interface DictDataMapper extends BaseMapper<DictData> {

    /**
     * 通过字典类型code获取字典编码值列表
     *
     * @param dictTypeCodes 字典类型编码集合
     * @return 字典编码值列表
     * @author fengshuonan
     * @date 2020/8/9 14:27
     */
    List<String> getDictCodesByDictTypeCode(String[] dictTypeCodes);

}
