package com.xqxy.dr.modular.adjustable.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.adjustable.DTO.DrConsAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.DTO.DrConsUserAdjustablePotentialDTO;
import com.xqxy.dr.modular.adjustable.VO.DrConsAdjustablePotentialVO;
import com.xqxy.dr.modular.adjustable.VO.GroupCityAdjustVO;
import com.xqxy.dr.modular.adjustable.VO.GroupConsTypeAdjustVO;
import com.xqxy.dr.modular.adjustable.entity.DrConsAdjustablePotential;
import com.xqxy.dr.modular.adjustable.mapper.DrConsAdjustablePotentialMapper;
import com.xqxy.dr.modular.adjustable.service.DrConsAdjustablePotentialService;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.sys.modular.cust.enums.OrgTitleEnum;
import com.xqxy.sys.modular.cust.service.ConsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DrConsAdjustablePotentialServiceImpl extends ServiceImpl<DrConsAdjustablePotentialMapper, DrConsAdjustablePotential> implements DrConsAdjustablePotentialService {

    @Resource
    private DrConsAdjustablePotentialMapper consAdjustablePotentialMapper;

    @Resource
    private SystemClientService systemClientService;

    @Resource
    private ConsService consService;

    /**
     * 用户可调节潜力分页
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    @Override
    public List<DrConsAdjustablePotentialVO> pageConsAdjustable(Page<DrConsAdjustablePotentialVO> page, DrConsAdjustablePotentialDTO consAdjustablePotentialDTO) {
        // 添加数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if(!OrgTitleEnum.PROVINCE.getCode().equals(currenUserInfo.getOrgTitle())){
            List<String> orgIds = OrganizationUtil.getAllOrgByOrgNo();
            consAdjustablePotentialDTO.setOrgIds(orgIds);
        }

        // 参数 cons_name cons_id 模糊查询
        List<DrConsAdjustablePotentialVO> list = consAdjustablePotentialMapper.pageConsAdjustable(page, consAdjustablePotentialDTO);
        return list;
    }

    /**
     * 用户可调节潜力分页
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    @Override
    public Page<DrConsAdjustablePotentialVO> pageUserConsAdjustable(DrConsUserAdjustablePotentialDTO consAdjustablePotentialDTO) {
        CurrenUserInfo currentUserInfoUTF8 = SecurityUtils.getCurrentUserInfoUTF8();
        List<String> consIds = consService.getConsIdListByCust(Long.valueOf(currentUserInfoUTF8.getId()));
        // 参数 cons_name cons_id 模糊查询
        Page<DrConsAdjustablePotentialVO> list = consAdjustablePotentialMapper.pageUserConsAdjustable(consAdjustablePotentialDTO.getPage(), consIds, consAdjustablePotentialDTO);
        return list;
    }


    /**
     * 用户可调节潜力分页
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    @Override
    public List<DrConsAdjustablePotentialVO> exportConsAdjustable(DrConsAdjustablePotentialDTO consAdjustablePotentialDTO) {
        // 参数 cons_name cons_id 模糊查询
        List<DrConsAdjustablePotentialVO> list = consAdjustablePotentialMapper.exportConsAdjustable(consAdjustablePotentialDTO);
        return list;
    }


    /**
     * 查询所有 用户可调节潜力 分组条件为 用户类型，查询条件为年度 市码
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    @Override
    public List<GroupConsTypeAdjustVO> exportConsTypeAdjustable(DrConsAdjustablePotentialDTO consAdjustablePotentialDTO) {
        List<GroupConsTypeAdjustVO> list = consAdjustablePotentialMapper.exportConsTypeAdjustable(consAdjustablePotentialDTO);
        return list;
    }


    /**
     * 查询所有 用户可调节潜力 分组条件为 市码，查询条件为年度
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    @Override
    public List<GroupCityAdjustVO> exportCityAdjustable(DrConsAdjustablePotentialDTO consAdjustablePotentialDTO) {
        if (consAdjustablePotentialDTO.getYear() == null || consAdjustablePotentialDTO.getYear().trim().length() == 0) {
            Calendar calendar = Calendar.getInstance();
            Integer year = calendar.get(Calendar.YEAR);
            consAdjustablePotentialDTO.setYear(year.toString());
        }
        List<GroupCityAdjustVO> list = consAdjustablePotentialMapper.exportCityAdjustable(consAdjustablePotentialDTO);

        //获取区域
        List<Region> regions = systemClientService.queryAll();
        Map<String, String> collect = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));

        for (GroupCityAdjustVO groupCityAdjustVO : list) {
            groupCityAdjustVO.setCityName(collect.get(groupCityAdjustVO.getCityCode()));
            groupCityAdjustVO.setProvinceName(collect.get(groupCityAdjustVO.getProvinceCode()));
        }
        return list;
    }

    /**
     * 用户可调节潜力 新增
     *
     * @param consAdjustablePotential
     * @return
     */
    @Override
    public void addConsAdjustable(DrConsAdjustablePotential consAdjustablePotential) {
//        consAdjustablePotential.setId(SnowflakeIdWorker.generateId().toString());
        save(consAdjustablePotential);
    }


    /**
     * 用户可调节潜力 修改
     *
     * @param consAdjustablePotential
     * @return
     */
    @Override
    public void editConsAdjustable(DrConsAdjustablePotential consAdjustablePotential) {
        this.updateById(consAdjustablePotential);
    }


    /**
     * 用户可调节潜力 删除
     *
     * @param consId
     * @return
     */
    @Override
    public void deleteConsAdjustable(String consId) {
        this.removeById(consId);
    }


    /**
     * 用户可调节潜力 分组条件为 市码，查询条件为年度
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    @Override
    public List<GroupCityAdjustVO> groupCityAdjustable(Page<GroupCityAdjustVO> page, DrConsAdjustablePotentialDTO consAdjustablePotentialDTO) {
        if (consAdjustablePotentialDTO.getYear() == null || consAdjustablePotentialDTO.getYear().trim().length() == 0) {
            Calendar calendar = Calendar.getInstance();
            Integer year = calendar.get(Calendar.YEAR);
            consAdjustablePotentialDTO.setYear(year.toString());
        }
        List<GroupCityAdjustVO> list = consAdjustablePotentialMapper.groupCityAdjustable(page, consAdjustablePotentialDTO);

        //获取区域
        List<Region> regions = systemClientService.queryAll();
        Map<String, String> collect = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));

        for (GroupCityAdjustVO groupCityAdjustVO : list) {
            groupCityAdjustVO.setCityName(collect.get(groupCityAdjustVO.getCityCode()));
            groupCityAdjustVO.setProvinceName(collect.get(groupCityAdjustVO.getProvinceCode()));
        }
        return list;
    }


    /**
     * 用户可调节潜力 分组条件为 用户类型，查询条件为年度 市码
     *
     * @param consAdjustablePotentialDTO
     * @return
     */
    @Override
    public List<GroupConsTypeAdjustVO> groupConsTypeAdjustable(Page<GroupConsTypeAdjustVO> page, DrConsAdjustablePotentialDTO consAdjustablePotentialDTO) {
        List<GroupConsTypeAdjustVO> list = consAdjustablePotentialMapper.groupConsTypeAdjustable(page, consAdjustablePotentialDTO);
        return list;
    }

    @Override
    public BigDecimal getSafetyLoadByConsId(String consId) {
        LambdaQueryWrapper<DrConsAdjustablePotential> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consId) && ObjectUtil.isNotEmpty(consId)) {
            lambdaQueryWrapper.eq(DrConsAdjustablePotential::getConsId, consId);
        }
        List<DrConsAdjustablePotential> drConsAdjustablePotentials = this.list(lambdaQueryWrapper);
        if (drConsAdjustablePotentials.size() > 0) {
            return drConsAdjustablePotentials.get(0).getSafetyLoad();
        }
        return null;
    }

    @Override
    public DrConsAdjustablePotential getbyConsId(String consId) {
        LambdaQueryWrapper<DrConsAdjustablePotential> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(consId) && ObjectUtil.isNotEmpty(consId)) {
            lambdaQueryWrapper.eq(DrConsAdjustablePotential::getConsId, consId);
        }
        List<DrConsAdjustablePotential> drConsAdjustablePotentials = this.list(lambdaQueryWrapper);
        if (drConsAdjustablePotentials.size() > 0) {
            return drConsAdjustablePotentials.get(0);
        }
        return null;
    }


}
