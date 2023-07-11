package com.xqxy.sys.modular.dict.service;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.sys.modular.dict.entity.DictData;
import com.xqxy.sys.modular.dict.param.DictDataParam;

import java.util.List;

/**
 * <p>
 * 字典值 服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
public interface DictDataService extends IService<DictData> {

    /**
     * 查询系统字典值
     *
     * @param DictDataParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:53
     */
    Page<DictData> page(DictDataParam DictDataParam);

    /**
     * 系统字典值列表
     *
     * @param DictDataParam 查询参数
     * @return 系统字典值列表
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 21:07
     */
    List<DictData> list(DictDataParam DictDataParam);


    /**
     * 添加系统字典值
     *
     * @param DictDataParam 添加参数
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:53
     */
    void add(DictDataParam DictDataParam);

    /**
     * 删除系统字典值
     *
     * @param DictDataParam 删除参数
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:54
     */
    void delete(DictDataParam DictDataParam);

    /**
     * 编辑系统字典值
     *
     * @param DictDataParam 编辑参数
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:54
     */
    void edit(DictDataParam DictDataParam);

    /**
     * 查看系统字典值
     *
     * @param DictDataParam 查看参数
     * @return 系统字典值
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:54
     */
    DictData detail(DictDataParam DictDataParam);

    /**
     * 根据typeId下拉
     *
     * @param dictTypeId 字典类型id
     * @return 增强版hashMap，格式：[{"code:":"1", "value":"正常"}]
     * @author xuyuxiang, fengshuonan yubaoshan
     * @date 2020/3/31 21:27
     */
    List<Dict> getDictDataListByDictTypeId(Long dictTypeId);

    /**
     * 根据typeId下拉
     *
     * @param dictTypeId 字典类型id
     * @return 增强版hashMap，格式：[{"code:":"1", "value":"正常"}]
     * @author xuyuxiang, fengshuonan yubaoshan
     * @date 2020/3/31 21:27
     */
    List<Dict> getDictDataListByDictParentId(String code);

    /**
     * 根据typeId删除
     *
     * @param dictTypeId 字典类型id
     * @author xuyuxiang, fengshuonan
     * @date 2020/4/1 10:27
     */
    void deleteByTypeId(Long dictTypeId);

    /**
     * 修改状态
     *
     * @param DictDataParam 修改参数
     * @author stylefeng
     * @date 2020/5/1 9:44
     */
    void changeStatus(DictDataParam DictDataParam);

}
