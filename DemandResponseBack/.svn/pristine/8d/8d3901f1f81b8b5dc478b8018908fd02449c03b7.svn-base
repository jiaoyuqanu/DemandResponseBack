package com.xqxy.sys.modular.dict.service;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.sys.modular.dict.entity.DictData;
import com.xqxy.sys.modular.dict.entity.DictType;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.result.DictTreeNode;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * <p>
 * 字典类型 服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
public interface DictTypeService extends IService<DictType> {

    /**
     * 查询系统字典类型
     *
     * @param DictTypeParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 20:34
     */
    Page<DictType> page(DictTypeParam DictTypeParam);

    /**
     * 获取字典类型列表
     *
     * @param DictTypeParam 查询参数
     * @return 系统字典类型列表
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 21:07
     */
    List<DictType> list(DictTypeParam DictTypeParam);

    /**
     * 系统字典类型下拉
     *
     * @param dictTypeParam 下拉参数
     * @return 增强版hashMap，格式：[{"code:":"1", "value":"正常"}]
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 21:23
     */
    List<Dict> dropDown(DictTypeParam dictTypeParam);

    /**
     * 系统字典类型下拉
     *
     * @param dictTypeParam 下拉参数
     * @return 增强版hashMap，格式：[{"code:":"1", "value":"正常"}]
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 21:23
     */
    List<Dict> getClassifyList(DictTypeParam dictTypeParam);



    /**
     * 添加系统字典类型
     *
     * @param DictTypeParam 添加参数
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 20:35
     */
    void add(DictTypeParam DictTypeParam);

    /**
     * 删除系统字典类型
     *
     * @param DictTypeParam 删除参数
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 20:35
     */
    void delete(DictTypeParam DictTypeParam);

    /**
     * 编辑系统字典类型
     *
     * @param DictTypeParam 编辑参数
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 20:35
     */
    void edit(DictTypeParam DictTypeParam);

    /**
     * 查看系统字典类型
     *
     * @param DictTypeParam 查看参数
     * @return 系统字典类型
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 20:35
     */
    DictType detail(DictTypeParam DictTypeParam);

    /**
     * 修改状态（字典 0正常 1停用 2删除）
     *
     * @param DictTypeParam 修改参数
     * @author stylefeng
     * @date 2020/4/30 22:30
     */
    void changeStatus(DictTypeParam DictTypeParam);

    /**
     * 系统字典类型与字典值构造的树
     *
     * @return 树
     * @author xuyuxiang
     * @date 2020/4/30 22:30
     */
    List<DictTreeNode> tree();

    /**
     * 通过编码获取字典值
     *
     * @return dictdata
     * @author hu xingxing
     * @date 2022/1/6 14:20
     */
    List<DictData> getDictDataByTypeCode(String code);

    List<DictData> getTree(DictTypeParam dictTypeParam);

}
