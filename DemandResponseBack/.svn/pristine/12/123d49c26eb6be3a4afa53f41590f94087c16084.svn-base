package com.xqxy.dr.modular.baseline.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.util.OrganizationUtil;
import com.xqxy.core.util.SecurityUtils;
import com.xqxy.dr.modular.baseline.entity.BaseLineDetail;
import com.xqxy.dr.modular.baseline.entity.BaseLineDetailData;
import com.xqxy.dr.modular.baseline.mapper.BaseLineDetailMapper;
import com.xqxy.dr.modular.baseline.param.BaseLineDetailParam;
import com.xqxy.dr.modular.baseline.service.BaseLineDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基线详情 服务实现类
 * </p>
 *
 * @author chen zhi jun
 * @since 2021-10-19
 */
@Service
public class BaseLineDetailServiceImpl extends ServiceImpl<BaseLineDetailMapper, BaseLineDetail> implements BaseLineDetailService {

    private static final Log log = Log.get();

    @Resource
    BaseLineDetailMapper baseLineDetailMapper;

    @Override
    public Page<BaseLineDetail> detail(BaseLineDetailParam baseLineDetailParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();
        //机构子集
        List<String> list = new ArrayList<>();
        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //获取所有组织机构集合
                list = OrganizationUtil.getAllOrgByOrgNo();
                if (CollectionUtil.isEmpty(list)) {
                    return new Page<>();
                }
                baseLineDetailParam.setOrgs(list);
            }
        } else {
            return new Page<>();
        }
        Page<BaseLineDetail> page = new Page<>(baseLineDetailParam.getCurrent(),baseLineDetailParam.getSize());
        List<BaseLineDetail> baseLineDetailList = baseLineDetailMapper.getBaseLineDetailPage(page,baseLineDetailParam);
        page.setRecords(baseLineDetailList);
        return page;
    }

    @Override
    public Page<BaseLineDetail> detailCust(BaseLineDetailParam baseLineDetailParam) {
        //数据权限
        CurrenUserInfo currenUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        if (null == currenUserInfo) {
            return new Page<>();
        }
        String orgNo = currenUserInfo.getOrgId();
        String orgTitle = currenUserInfo.getOrgTitle();

        if (null != orgNo && !"".equals(orgNo)) {
            if (!"1".equals(orgTitle)) {
                //市级无法看集成商数据
                return new Page<>();
            }
        } else {
            return new Page<>();
        }
        Page<BaseLineDetail> page = new Page<>(baseLineDetailParam.getCurrent(),baseLineDetailParam.getSize());
        List<BaseLineDetail> list = baseLineDetailMapper.getBaseLineDetailCustPage(page,baseLineDetailParam);
        page.setRecords(list);
        return page;
    }

    @Override
    public Map<String,Object> getDetailData(BaseLineDetailParam baseLineDetailParam) {
        List<BaseLineDetail> baseLineDetailDataOne = baseLineDetailMapper.getConsIdAndBaselineLibId(baseLineDetailParam.getBaselinId());
        BaseLineDetailData baseLineDetailData = null;
        List<BaseLineDetailData> baseLineDetailDataList = baseLineDetailMapper.getDetailData(baseLineDetailParam.getBaselinId());
        if(null!=baseLineDetailDataList && baseLineDetailDataList.size()>0) {
            baseLineDetailData = baseLineDetailDataList.get(0);
        }
        BaseLineDetailData baseLineDetailData2 = null;
        if(null!=baseLineDetailDataOne && baseLineDetailDataOne.size()>0) {
            List<BaseLineDetailData> baseLineDetailDataList1 = baseLineDetailMapper.getDetailData2(baseLineDetailDataOne.get(0).getBaselineLibId(),baseLineDetailDataOne.get(0).getConsId());
            if(null!=baseLineDetailDataList1 && baseLineDetailDataList1.size()>0) {
                baseLineDetailData2 = baseLineDetailDataList1.get(0);
            }
        }
        Map<String,Object> mapData = new HashMap<>();
        //时间段基线
        List<Map<String, Object>> list = new ArrayList<>();
        //96点基线
        List<Map<String, Object>> list2 = new ArrayList<>();
        //通过反射获取类中所有属性
        if(null!=baseLineDetailData) {
            Field[] fields = baseLineDetailData.getClass().getDeclaredFields();
            for (Field field : fields) {
                //设置允许反射访问私有变量
                field.setAccessible(true);
                try {
                    //获取属性值
                    Object value = field.get(baseLineDetailData);
                    //获取属性名
                    String name = field.getName();
                    if (null == value) {
                        value = "";
                    } else {
                        value=value.toString();
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", name);
                    map.put("value", value);
                    if (!"serialVersionUID".equals(name)) {
                        list.add(map);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        } else {
            for(int i=1;i<=96;i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("p"+i,"");
                list.add(map);
            }
        }
        //通过反射获取类中所有属性
        if(null!=baseLineDetailData2) {
            Field[] fields = baseLineDetailData2.getClass().getDeclaredFields();
            for (Field field : fields) {
                //设置允许反射访问私有变量
                field.setAccessible(true);
                try {
                    //获取属性值
                    Object value = field.get(baseLineDetailData2);
                    //获取属性名
                    String name = field.getName();
                    if (null == value) {
                        value = "";
                    } else {
                        value=value.toString();
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", name);
                    map.put("value", value);
                    if (!"serialVersionUID".equals(name)) {
                        list2.add(map);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        } else {
            for(int i=1;i<=96;i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("p"+i,"");
                list2.add(map);
            }
        }
        mapData.put("baseline",list);
        mapData.put("baselineAll",list2);
        return mapData;
    }

    @Override
    public Map<String,Object> getDetailDataCust(BaseLineDetailParam baseLineDetailParam) {
        Map<String,Object> mapData = new HashMap<>();
        BaseLineDetailData baseLineDetailData = baseLineDetailMapper.getDetailDataCust(baseLineDetailParam.getBaselinId());
        //时间段基线
        List<Map<String, Object>> list = new ArrayList<>();
        //96点基线
        List<Map<String, Object>> list2 = new ArrayList<>();
        //通过反射获取类中所有属性
        if(null!=baseLineDetailData) {
            Field[] fields = baseLineDetailData.getClass().getDeclaredFields();
            for (Field field : fields) {
                //设置允许反射访问私有变量
                field.setAccessible(true);
                try {
                    //获取属性值
                    Object value = field.get(baseLineDetailData);
                    //获取属性名
                    String name = field.getName();
                    if (null == value) {
                        value = "";
                    } else {
                        value=value.toString();
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", name);
                    map.put("value", value);
                    if (!"serialVersionUID".equals(name)) {
                        list.add(map);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        } else {
            for(int i=1;i<=96;i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("p"+i,"");
                list.add(map);
            }
        }
        for(int i=1;i<=96;i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("p"+i,"");
            list2.add(map);
        }
        mapData.put("baseline",list);
        mapData.put("baselineAll",list2);
        return mapData;
    }
}
