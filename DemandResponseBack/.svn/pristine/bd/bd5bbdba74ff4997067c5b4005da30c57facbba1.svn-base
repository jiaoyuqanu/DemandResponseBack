package com.xqxy.sys.modular.dict.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.consts.CommonConstant;
import com.xqxy.core.enums.CommonStatusEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.exception.enums.StatusExceptionEnum;
import com.xqxy.sys.modular.dict.entity.DictData;
import com.xqxy.sys.modular.dict.enums.DictDataExceptionEnum;
import com.xqxy.sys.modular.dict.mapper.DictDataMapper;
import com.xqxy.sys.modular.dict.param.DictDataParam;
import com.xqxy.sys.modular.dict.service.DictDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典值 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    @Override
    public Page<DictData> page(DictDataParam dictDataParam) {

        //构造条件
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(dictDataParam)) {
            //根据字典类型查询
            if (ObjectUtil.isNotEmpty(dictDataParam.getTypeId())) {
                queryWrapper.eq(DictData::getTypeId, dictDataParam.getTypeId());
            }
            //根据字典值的编码模糊查询
            if (ObjectUtil.isNotEmpty(dictDataParam.getCode())) {
                queryWrapper.like(DictData::getCode, dictDataParam.getCode());
            }
            //根据字典值的内容模糊查询
            if (ObjectUtil.isNotEmpty(dictDataParam.getValue())) {
                queryWrapper.like(DictData::getValue, dictDataParam.getValue());
            }
        }
        //查询未删除的
        queryWrapper.ne(DictData::getStatus, CommonStatusEnum.DELETED.getCode());
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(DictData::getSort);
        //返回分页查询结果
        return this.page(dictDataParam.getPage(), queryWrapper);
    }

    @Override
    public List<DictData> list(DictDataParam DictDataParam) {
        //构造条件,查询某个字典类型下的
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(DictDataParam)) {
            if (ObjectUtil.isNotEmpty(DictDataParam.getTypeId())) {
                queryWrapper.eq(DictData::getTypeId, DictDataParam.getTypeId());
            }
        }
        //查询未删除的
        queryWrapper.ne(DictData::getStatus, CommonStatusEnum.DELETED.getCode());
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(DictData::getSort);
        return this.list(queryWrapper);
    }

    @Override
    public void add(DictDataParam dictDataParam) {

        //校验参数，检查是否存在重复的编码，不排除当前添加的这条记录
        checkParam(dictDataParam, false);

        //将dto转为实体
        DictData DictData = new DictData();
        BeanUtil.copyProperties(dictDataParam, DictData);

        //设置状态为启用
        DictData.setStatus(dictDataParam.getStatus());

        this.save(DictData);
    }

    @Override
    public void delete(DictDataParam DictDataParam) {

        //根据id查询实体
        DictData DictData = this.queryDictData(DictDataParam);

        //逻辑删除，修改状态
        DictData.setStatus(CommonStatusEnum.DELETED.getCode());

        //更新实体
        this.updateById(DictData);
    }

    @Override
    public void edit(DictDataParam DictDataParam) {

        //根据id查询实体
        DictData DictData = this.queryDictData(DictDataParam);

        //校验参数，检查是否存在重复的编码或者名称，排除当前编辑的这条记录
        checkParam(DictDataParam, true);

        //请求参数转化为实体
        BeanUtil.copyProperties(DictDataParam, DictData);

        //不能修改状态，用修改状态接口修改状态
        // DictData.setStatus(null);

        this.updateById(DictData);
    }

    @Override
    public DictData detail(DictDataParam DictDataParam) {
        return this.queryDictData(DictDataParam);
    }

    @Override
    public List<Dict> getDictDataListByDictTypeId(Long dictTypeId) {

        //构造查询条件
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<DictData>();
        queryWrapper.eq(DictData::getTypeId, dictTypeId).eq(DictData::getStatus, CommonStatusEnum.ENABLE.getCode());
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(DictData::getSort);
        //查询dictTypeId下所有的字典项
        List<DictData> results = this.list(queryWrapper);

        //抽取code和value封装到map返回
        List<Dict> dictList = CollectionUtil.newArrayList();
        results.forEach(DictData -> {
            Dict dict = Dict.create();
            dict.put(CommonConstant.CODE, DictData.getCode());
            dict.put(CommonConstant.VALUE, DictData.getValue());
            dictList.add(dict);
        });

        return dictList;
    }

    @Override
    public List<Dict> getDictDataListByDictParentId(String dictTypeId) {

        //构造查询条件
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<DictData>();
        queryWrapper.eq(DictData::getParentId, dictTypeId).eq(DictData::getStatus, CommonStatusEnum.ENABLE.getCode());
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(DictData::getSort);
        //查询dictTypeId下所有的字典项
        List<DictData> results = this.list(queryWrapper);

        //抽取code和value封装到map返回
        List<Dict> dictList = CollectionUtil.newArrayList();
        results.forEach(DictData -> {
            Dict dict = Dict.create();
            dict.put(CommonConstant.CODE, DictData.getCode());
            dict.put(CommonConstant.VALUE, DictData.getValue());
            dictList.add(dict);
        });

        return dictList;
    }

    @Override
    public void deleteByTypeId(Long typeId) {
        //将所有typeId为某值的记录全部置为delete状态
        LambdaUpdateWrapper<DictData> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(DictData::getTypeId, typeId)
                .set(DictData::getStatus, CommonStatusEnum.DELETED.getCode());
        this.update(queryWrapper);
    }

    @Override
    public void changeStatus(DictDataParam DictDataParam) {
        //根据id查询实体
        DictData DictData = this.queryDictData(DictDataParam);
        Long dataId = DictData.getDataId();

        Integer status = DictDataParam.getStatus();

        //校验状态在不在枚举值里
        CommonStatusEnum.validateStatus(status);

        //更新枚举，更新只能更新未删除状态的
        LambdaUpdateWrapper<DictData> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(com.xqxy.sys.modular.dict.entity.DictData::getDataId, dataId)
                .and(i -> i.ne(com.xqxy.sys.modular.dict.entity.DictData::getCode, CommonStatusEnum.DELETED.getCode()))
                .set(com.xqxy.sys.modular.dict.entity.DictData::getStatus, status);
        boolean update = this.update(updateWrapper);
        if (!update) {
            throw new ServiceException(StatusExceptionEnum.UPDATE_STATUS_ERROR);
        }
    }

    /*@Override
    public List<String> getDictCodesByDictTypeCode(String... dictTypeCodes) {
        return this.baseMapper.getDictCodesByDictTypeCode(dictTypeCodes);
    }*/

    /**
     * 校验参数，校验是否存在相同的编码
     *
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:56
     */
    private void checkParam(DictDataParam DictDataParam, boolean isExcludeSelf) {
        Long id = DictDataParam.getDataId();
        Long typeId = DictDataParam.getTypeId();
        String code = DictDataParam.getCode();

        //构建带code的查询条件
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictData::getTypeId, typeId)
                .eq(DictData::getCode, code)
                .ne(DictData::getStatus, CommonStatusEnum.DELETED.getCode());

        //如果排除自己，则增加查询条件主键id不等于本条id
        if (isExcludeSelf) {
            queryWrapper.ne(DictData::getDataId, id);
        }

        //查询重复记录的数量
        int countByCode = this.count(queryWrapper);

        // 如果存在重复的记录，抛出异常，直接返回前端
        if (countByCode >= 1) {
            throw new ServiceException(DictDataExceptionEnum.DICT_DATA_CODE_REPEAT);
        }
    }

    /**
     * 获取系统字典值
     *
     * @author xuyuxiang, fengshuonan
     * @date 2020/3/31 20:56
     */
    private DictData queryDictData(DictDataParam DictDataParam) {
        DictData DictData = this.getById(DictDataParam.getDataId());
        if (ObjectUtil.isNull(DictData)) {
            throw new ServiceException(DictDataExceptionEnum.DICT_DATA_NOT_EXIST);
        }
        return DictData;
    }

}
