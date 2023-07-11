package com.xqxy.sys.modular.dict.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.enums.CommonStatusEnum;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.exception.enums.StatusExceptionEnum;
import com.xqxy.core.factory.TreeBuildFactory;
import com.xqxy.core.factory.TreeBuildFactory2;
import com.xqxy.sys.modular.dict.entity.DictData;
import com.xqxy.sys.modular.dict.entity.DictType;
import com.xqxy.sys.modular.dict.enums.DictTypeExceptionEnum;
import com.xqxy.sys.modular.dict.mapper.DictTypeMapper;
import com.xqxy.sys.modular.dict.param.DictDataParam;
import com.xqxy.sys.modular.dict.param.DictTypeParam;
import com.xqxy.sys.modular.dict.result.DictTreeNode;
import com.xqxy.sys.modular.dict.service.DictTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典类型 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-03-11
 */
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    @Resource
    private com.xqxy.sys.modular.dict.service.DictDataService DictDataService;

    private List<DictData> list = new ArrayList<>();

    @Override
    public Page<DictType> page(DictTypeParam dictTypeParam) {

        //构造条件
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();

        if (ObjectUtil.isNotNull(dictTypeParam)) {
            //根据字典类型名称模糊查询
            if (ObjectUtil.isNotEmpty(dictTypeParam.getName())) {
                queryWrapper.like(DictType::getName, dictTypeParam.getName());
            }

            //根据字典类型编码模糊查询
            if (ObjectUtil.isNotEmpty(dictTypeParam.getCode())) {
                queryWrapper.like(DictType::getCode, dictTypeParam.getCode());
            }
        }

        //查询未删除的
        queryWrapper.ne(DictType::getStatus, CommonStatusEnum.DELETED.getCode());
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(DictType::getSort);
        //查询分页结果
        return this.page(dictTypeParam.getPage(), queryWrapper);
    }

    @Override
    public List<DictType> list(DictTypeParam DictTypeParam) {

        //构造条件
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();

        //查询未删除的
        queryWrapper.ne(DictType::getStatus, CommonStatusEnum.DELETED.getCode());

        return this.list(queryWrapper);
    }

    @Override
    public List<Dict> dropDown(DictTypeParam dictTypeParam) {
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<DictType>()
                .eq(DictType::getCode, dictTypeParam.getCode());
        //查询未删除的
        queryWrapper.ne(DictType::getStatus, CommonStatusEnum.DELETED.getCode());

        DictType DictType = this.getOne(queryWrapper);
        if (ObjectUtil.isNull(DictType)) {
            throw new ServiceException(DictTypeExceptionEnum.DICT_TYPE_NOT_EXIST);
        }
        return DictDataService.getDictDataListByDictTypeId(DictType.getTypeId());
    }

    @Override
    public List<Dict> getClassifyList(DictTypeParam dictTypeParam) {
        return DictDataService.getDictDataListByDictParentId(dictTypeParam.getCode());
    }

    @Override
    public List<DictData> getTree(DictTypeParam dictTypeParam) {
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<DictType>()
                .eq(DictType::getCode, dictTypeParam.getCode());
        //查询未删除的
        queryWrapper.ne(DictType::getStatus, CommonStatusEnum.DELETED.getCode());

        DictType DictType = this.getOne(queryWrapper);
        if (ObjectUtil.isNull(DictType)) {
            throw new ServiceException(DictTypeExceptionEnum.DICT_TYPE_NOT_EXIST);
        }
        //构造查询条件
        LambdaQueryWrapper<DictData> queryDataWrapper = new LambdaQueryWrapper<DictData>();
        queryDataWrapper.eq(DictData::getTypeId, DictType.getTypeId()).eq(DictData::getStatus, CommonStatusEnum.ENABLE.getCode());
        //查询dictTypeId下所有的字典项
        List<DictData> results = DictDataService.list(queryDataWrapper);
        List<DictData> fachers = null;
        TreeBuildFactory2 treeBuildFactory = new TreeBuildFactory2();
        if(null!=results && results.size()>0) {
             fachers = results.stream().filter(dictData ->
                    dictData.getParentId().equals(dictTypeParam.getCode())).collect(Collectors.toList());
            if(null!=fachers && fachers.size()>0) {
                for(DictData dictData :fachers) {
                    treeBuildFactory.buildChildNodes2(results,dictData,CollectionUtil.newArrayList());
                   /* List<DictData> children = results.stream().filter(dictDataChild ->
                            dictData.getCode().equals(dictDataChild.getParentId())).collect(Collectors.toList());
                    if(null!=children && children.size()>0) {
                        dictData.setChilren(children);
                    }*/
                }
            }
        } else {
            return new ArrayList<>();
        }
        return fachers;
    }

    @Override
    public void add(DictTypeParam DictTypeParam) {

        //校验参数，检查是否存在重复的编码或者名称，不排除当前添加的这条记录
        checkParam(DictTypeParam, false);

        //将dto转为实体
        DictType DictType = new DictType();
        BeanUtil.copyProperties(DictTypeParam, DictType);

        //设置状态为启用
        DictType.setStatus(CommonStatusEnum.ENABLE.getCode());

        this.save(DictType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(DictTypeParam DictTypeParam) {

        //根据id查询实体
        DictType DictType = this.queryDictType(DictTypeParam);

        //逻辑删除，修改状态
        DictType.setStatus(CommonStatusEnum.DELETED.getCode());

        //更新实体
        this.updateById(DictType);

        //级联删除字典值
        DictDataService.deleteByTypeId(DictType.getTypeId());
    }

    @Override
    public void edit(DictTypeParam DictTypeParam) {

        //根据id查询实体
        DictType DictType = this.queryDictType(DictTypeParam);

        //校验参数，检查是否存在重复的编码或者名称，排除当前编辑的这条记录
        checkParam(DictTypeParam, true);

        //请求参数转化为实体
        BeanUtil.copyProperties(DictTypeParam, DictType);

        //不能修改状态，用修改状态接口修改状态
        DictType.setStatus(null);

        this.updateById(DictType);
    }

    @Override
    public DictType detail(DictTypeParam DictTypeParam) {
        return this.queryDictType(DictTypeParam);
    }

    @Override
    public void changeStatus(DictTypeParam DictTypeParam) {
        Long id = DictTypeParam.getTypeId();
        Integer status = DictTypeParam.getStatus();

        //校验状态在不在枚举值里
        CommonStatusEnum.validateStatus(status);

        //更新枚举，更新只能更新未删除状态的
        LambdaUpdateWrapper<DictType> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DictType::getTypeId, id)
                .and(i -> i.ne(DictType::getStatus, CommonStatusEnum.DELETED.getCode()))
                .set(DictType::getStatus, status);
        boolean update = this.update(updateWrapper);
        if (!update) {
            throw new ServiceException(StatusExceptionEnum.UPDATE_STATUS_ERROR);
        }
    }

    @Override
    public List<DictTreeNode> tree() {
        List<DictTreeNode> resultList = CollectionUtil.newArrayList();
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(DictType::getStatus, CommonStatusEnum.DELETED.getCode());
        this.list(queryWrapper).forEach(DictType -> {
            DictTreeNode DictTreeNode = new DictTreeNode();
            BeanUtil.copyProperties(DictType, DictTreeNode);
            DictTreeNode.setPid(0L);
            resultList.add(DictTreeNode);
        });
        DictDataService.list(new LambdaQueryWrapper<DictData>().ne(DictData::getStatus, CommonStatusEnum.DELETED.getCode()))
                .forEach(DictData -> {
                    DictTreeNode DictTreeNode = new DictTreeNode();
                    DictTreeNode.setId(DictData.getDataId());
                    DictTreeNode.setPid(DictData.getTypeId());
                    DictTreeNode.setCode(DictData.getCode());
                    DictTreeNode.setName(DictData.getValue());
                    resultList.add(DictTreeNode);
                });
        return new TreeBuildFactory<DictTreeNode>().doTreeBuild(resultList);
    }

    @Override
    public List<DictData> getDictDataByTypeCode(String code) {

        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(code)) {
            queryWrapper.eq(DictType::getCode, code);
        }

        DictType dictType = this.getOne(queryWrapper);
        long typeId = dictType.getTypeId();
        DictDataParam dictDataParam = new DictDataParam();
        dictDataParam.setTypeId(typeId);

        return DictDataService.list(dictDataParam);
    }

    /**
     * 校验参数，检查是否存在重复的编码或者名称
     *
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/25 21:23
     */
    private void checkParam(DictTypeParam DictTypeParam, boolean isExcludeSelf) {
        Long id = DictTypeParam.getTypeId();
        String name = DictTypeParam.getName();
        String code = DictTypeParam.getCode();

        //构建带name和code的查询条件
        LambdaQueryWrapper<DictType> queryWrapperByName = new LambdaQueryWrapper<>();
        queryWrapperByName.eq(DictType::getName, name)
                .ne(DictType::getStatus, CommonStatusEnum.DELETED.getCode());

        LambdaQueryWrapper<DictType> queryWrapperByCode = new LambdaQueryWrapper<>();
        queryWrapperByCode.eq(DictType::getCode, code)
                .ne(DictType::getStatus, CommonStatusEnum.DELETED.getCode());

        //如果排除自己，则增加查询条件主键id不等于本条id
        if (isExcludeSelf) {
            queryWrapperByName.ne(DictType::getTypeId, id);
            queryWrapperByCode.ne(DictType::getTypeId, id);
        }

        //查询重复记录的数量
        int countByName = this.count(queryWrapperByName);
        int countByCode = this.count(queryWrapperByCode);

        //如果存在重复的记录，抛出异常，直接返回前端
        if (countByName >= 1) {
            throw new ServiceException(DictTypeExceptionEnum.DICT_TYPE_NAME_REPEAT);
        }
        if (countByCode >= 1) {
            throw new ServiceException(DictTypeExceptionEnum.DICT_TYPE_CODE_REPEAT);
        }
    }

    /**
     * 获取系统字典类型
     *
     * @author xuyuxiang，fengshuonan
     * @date 2020/3/31 20:38
     */
    private DictType queryDictType(DictTypeParam DictTypeParam) {
        DictType DictType = this.getById(DictTypeParam.getTypeId());
        if (ObjectUtil.isNull(DictType)) {
            throw new ServiceException(DictTypeExceptionEnum.DICT_TYPE_NOT_EXIST);
        }
        return DictType;
    }

}
