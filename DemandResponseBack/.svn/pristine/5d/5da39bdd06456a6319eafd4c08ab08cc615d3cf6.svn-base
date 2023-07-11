package com.xqxy.dr.modular.grsg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.dr.modular.grsg.DTO.CustDTO;
import com.xqxy.dr.modular.grsg.DTO.DrApplyRecDTO;
import com.xqxy.dr.modular.grsg.VO.DrApplyRecVO;
import com.xqxy.dr.modular.grsg.entity.DrApplyRec;
import com.xqxy.dr.modular.grsg.mapper.DrApplyRecMapper;
import com.xqxy.dr.modular.grsg.service.DrApplyRecService;
import com.xqxy.dr.modular.strategy.DataAccessStrategy;
import com.xqxy.dr.modular.strategy.DataAccessStrategyContext;
import com.xqxy.sys.modular.cust.entity.Cons;
import com.xqxy.sys.modular.cust.entity.Cust;
import com.xqxy.sys.modular.cust.service.ConsService;
import com.xqxy.sys.modular.cust.service.CustService;
import com.xqxy.sys.modular.utils.SnowflakeIdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 绿色国网业务申请记录表 服务实现类
 * </p>
 *
 * @author liqirui
 * @since 2021-11-17
 */
@Service
public class DrApplyRecServiceImpl extends ServiceImpl<DrApplyRecMapper, DrApplyRec> implements DrApplyRecService {

    @Autowired
    private CustService custService;
    @Autowired
    private ConsService consService;

    @Resource
    private DrApplyRecMapper drApplyRecMapper;

    @Resource
    private DataAccessStrategyContext dataAccessStrategyContext;

    @Value("${dataAccessStrategy}")
    private String dataAccessStrategy;


    /**
     * @description: 申请业务工单接口
     * @param: DrApplyRec
     * @return:
     * @author: liqirui
     * @date: 2021/11/17 14:27
     */
    @Override
    public Boolean sendAppOrder(DrApplyRecDTO drApplyRecDTO) {
        DrApplyRec drApplyRec = new DrApplyRec();

        if(drApplyRecDTO != null){
            BeanUtils.copyProperties(drApplyRecDTO,drApplyRec);

            List<String> consNoList = drApplyRecDTO.getConsNoList();
            if(!CollectionUtils.isEmpty(consNoList)){
                String consNo = consNoList.toString();
                String replace = consNo.replace("[", "").replace("]", "");
                drApplyRec.setConsNoList(replace);
            }
        }

        drApplyRec.setId(SnowflakeIdWorker.generateId());
        boolean b = this.save(drApplyRec);
        return b;
    }

    /**
     * @description: 用户是否开通业务接口
     * @param: DrApplyRec
     * @return:
     * @author: liqirui
     * @date: 2021/11/17 15:27
     */
    @Override
    public String checkUserOpenApp(CustDTO custDTO) {
        QueryWrapper<Cust> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("CREDIT_CODE",custDTO.getCreditCode());
        List<Cust> list = custService.list(queryWrapper);
        if(CollectionUtils.isEmpty(list)){
            return "0";
        }
        return "1";
    }


    /**
     * @description: 分页列表展示
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    @Override
    public List<DrApplyRecVO> pageDrApplyRec(Page<DrApplyRecVO> page, DrApplyRecDTO drApplyRecDTO) {
        List<DrApplyRecVO> list = drApplyRecMapper.pageDrApplyRec(page,drApplyRecDTO);
        return list;
    }



    /**
     * @description: 需求响应 详情
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    @Override
    public DrApplyRec detailDrApplyRec( Long id) {

        DrApplyRec drApplyRec = this.getById(id);
        return drApplyRec;
    }


    /**
     * @description: 需求响应 详情
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void editDrApplyRec( DrApplyRecDTO drApplyRecDTO) {
        // 用户
        List<Cons> consList = drApplyRecDTO.getList();
        for (Cons cons : consList) {
            Cons cons1 = consService.getById(cons.getId());
            if(cons1 == null){
                consService.save(cons);
            }
        }

        //客户 入库
        Cust cust = new Cust();
        cust.setCreditCode(drApplyRecDTO.getCreditCode());

        QueryWrapper<Cust> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("credit_code",drApplyRecDTO.getCreditCode());
        List<Cust> custList = custService.list(queryWrapper);

        //验证是否唯一
        if(CollectionUtils.isEmpty(custList)){
            cust.setCustName(drApplyRecDTO.getOrganName());
            cust.setApplyName(drApplyRecDTO.getContactName());
            cust.setLegalName(drApplyRecDTO.getContactName());
            cust.setTel(drApplyRecDTO.getContactPhone());
            custService.save(cust);
        }

        DrApplyRec drApplyRec = new DrApplyRec();
        drApplyRec.setId(drApplyRecDTO.getId());
        drApplyRec.setState(drApplyRecDTO.getState());

        this.updateById(drApplyRec);
    }



    /**
     * @description: 需求响应 查询用户
     * @return:
     * @author: liqirui
     * @date: 2021/11/29 15:27
     */
    @Override
    public List<Cons> listCons(String consNoList) {
        List<Cons> list = new ArrayList<>();
        //拿到对应策略的实例
        DataAccessStrategy getDataStrategy = dataAccessStrategyContext.strategySelect(dataAccessStrategy);
        String[] consNoArr = consNoList.split(",");

        for (String consNo : consNoArr) {
            Cons cons = getDataStrategy.getConsFromMarketing(consNo, null, null);
            if(!ObjectUtils.isEmpty(cons)){
                list.add(cons);
            }
        }

        return list;
    }
}
