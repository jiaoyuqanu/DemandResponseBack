package com.xqxy.dr.modular.powerplant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.annotion.NeedSetValueField;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.exception.SystemErrorType;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.bidding.entity.BiddingNotice;
import com.xqxy.dr.modular.powerplant.DTO.DrDaAggregatorDTO;
import com.xqxy.dr.modular.powerplant.entity.DaConsEffect;
import com.xqxy.dr.modular.powerplant.entity.DrDaAggregator;
import com.xqxy.dr.modular.powerplant.mapper.DrDaAggregatorMapper;
import com.xqxy.dr.modular.powerplant.service.DaConsEffectService;
import com.xqxy.dr.modular.powerplant.service.DrDaAggregatorService;
import com.xqxy.sys.modular.cust.enums.ConsExceptionEnum;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 负荷聚合商信息表 服务实现类
 * </p>
 *
 * @author liqirui
 * @since 2021-12-09
 */
@Service
public class DrDaAggregatorServiceImpl extends ServiceImpl<DrDaAggregatorMapper, DrDaAggregator> implements DrDaAggregatorService {


    @Resource
    private DrDaAggregatorMapper drDaAggregatorMapper;

    @Resource
    private DaConsEffectService daConsEffectService;

    /**
     * 『负荷聚合商信息清单』 分页查询
     * @param
     * @return
     */
    @Override
    public Page<DrDaAggregator> pageAggregator(Page<DrDaAggregator> page, DrDaAggregatorDTO drDaAggregatorDTO) {
        QueryWrapper<DrDaAggregator> queryWrapper = new QueryWrapper<>();

        if (ObjectUtil.isNotNull(drDaAggregatorDTO)) {
            // 根据市码
            if (ObjectUtil.isNotEmpty(drDaAggregatorDTO.getProvinceCode())) {
                queryWrapper.eq("province_code",drDaAggregatorDTO.getProvinceCode());
            }
            // 根据市码
            if (ObjectUtil.isNotEmpty(drDaAggregatorDTO.getCityCode())) {
                queryWrapper.eq("city_code",drDaAggregatorDTO.getCityCode());
            }
            // 根据市码
            if (ObjectUtil.isNotEmpty(drDaAggregatorDTO.getCountyCode())) {
                queryWrapper.eq("county_code",drDaAggregatorDTO.getCountyCode());
            }

            // 根据名字模糊查询
            if (ObjectUtil.isNotEmpty(drDaAggregatorDTO.getAggregatorName())) {
                queryWrapper.like("aggregator_name",drDaAggregatorDTO.getAggregatorName());
            }

            // 根据名字模糊查询
            if (ObjectUtil.isNotEmpty(drDaAggregatorDTO.getStartDate())) {
                queryWrapper.ge("create_time",drDaAggregatorDTO.getStartDate());
            }

            // 根据名字模糊查询
            if (ObjectUtil.isNotEmpty(drDaAggregatorDTO.getEndDate())) {
                queryWrapper.le("create_time",drDaAggregatorDTO.getEndDate());
            }
        }

        Page<DrDaAggregator> pageAggregator = this.page(page, queryWrapper);
        return pageAggregator;
    }


    /**
     * 『负荷聚合商信息清单』 新增
     * @param
     * @return
     */
    @Override
    public ResponseData addAggregator(DrDaAggregator drDaAggregator) {

        QueryWrapper<DrDaAggregator> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(drDaAggregator)) {
            // 根据负荷聚合商组号
            if (ObjectUtil.isNotEmpty(drDaAggregator.getAggregatorNo())) {
                queryWrapper.eq("aggregator_no",drDaAggregator.getAggregatorNo());
            }else {
                return ResponseData.fail(SystemErrorType.PARAMETER_NULL);
            }
        }

        List<DrDaAggregator> list = this.list(queryWrapper);

        if(!CollectionUtils.isEmpty(list)){
            return ResponseData.fail(SystemErrorType.AGGREGATOR_NO_REPEAT);
        }

        drDaAggregator.setStatus("1");
        this.save(drDaAggregator);


        return ResponseData.success();
    }


    /**
     * 根据负荷聚合商id 查询对应的代理用户
     * @param
     * @return
     */
    @NeedSetValueField
    @Override
    public Page<DaConsEffect> pageDaConsByAggregatorId(Page<DaConsEffect> page, DrDaAggregatorDTO drDaAggregatorDTO) {
        QueryWrapper<DaConsEffect> queryWrapper = new QueryWrapper<>();

        if (ObjectUtil.isNotNull(drDaAggregatorDTO)) {
            // 根据负荷聚合商id
            if (ObjectUtil.isNotEmpty(drDaAggregatorDTO.getId())) {
                queryWrapper.eq("cust_id",drDaAggregatorDTO.getId());
            }
        }
        Page<DaConsEffect> consPage = daConsEffectService.page(page, queryWrapper);
        return consPage;
    }


    /**
     * 『负荷聚合商信息清单』 审核
     * @param
     * @return
     */
    @Override
    public ResponseData editAggregator(DrDaAggregator drDaAggregator) {
        if(ObjectUtils.isNotEmpty(drDaAggregator)){
            DrDaAggregator daAggregator = this.getById(drDaAggregator.getId());
            if(daAggregator == null){
                return ResponseData.fail("未查找到对应对象");
            }
            daAggregator.setStatus(drDaAggregator.getStatus());

            this.updateById(daAggregator);
            return ResponseData.success();
        }
        return ResponseData.fail("必传参数为空，请确认");
    }


    /**
     * 『负荷聚合商信息清单』 下拉框模糊查询
     * @param
     * @return
     */
    @Override
    public List<DrDaAggregator> listAggregator(DrDaAggregatorDTO drDaAggregatorDTO) {
        QueryWrapper<DrDaAggregator> queryWrapper = new QueryWrapper<>();

        if (ObjectUtil.isNotNull(drDaAggregatorDTO)) {
            // 根据名字模糊查询
            if (ObjectUtil.isNotEmpty(drDaAggregatorDTO.getAggregatorName())) {
                queryWrapper.like("aggregator_name",drDaAggregatorDTO.getAggregatorName());
            }
        }

        List<DrDaAggregator> list = this.list(queryWrapper);
        return list;
    }
    @Override
    public Page<DrDaAggregator> pageAggregatorByOrg(Page<DrDaAggregator> page, DrDaAggregatorDTO drDaAggregatorDTO) {
        LambdaQueryWrapper<DrDaAggregator> queryWrapper = new LambdaQueryWrapper<>();

        String orgId = Objects.requireNonNull(SecurityUtils.getCurrentUserInfoUTF8()).getOrgId();
        if (StringUtil.isNullOrEmpty(orgId)) {
            throw new ServiceException(ConsExceptionEnum.ORG_NOT_EXIST);
        } else {
//        queryWrapper.eq("cityCode",drDaAggregatorDTO.getCityCode());
//            queryWrapper.eq(DrDaAggregator::getOrgNo, orgId);

            Page<DrDaAggregator> pageAggregator = this.page(page, queryWrapper);
            return pageAggregator;
        }
    }
    @Override
   public String getNameById(String id)
    {
        if(StringUtil.isNullOrEmpty(id))
        {
            return null;
        }
        else
        {
            DrDaAggregator drDaAggregator=this.getById(id);
            try {
                return drDaAggregator.getAggregatorName();
            }
            catch (Exception e)
            {
                return  null;
            }
        }
    }
}
