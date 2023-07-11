package com.xqxy.dr.modular.newloadmanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.newloadmanagement.vo.EventLoadVo;
import com.xqxy.dr.modular.newloadmanagement.vo.UserListVo;

import java.util.List;

/**
 * @Description 未到位用户清单service && 需求响当前时段执行事件及实时执行情况service
 * @Author Rabbit
 * @Date 2022/6/10 10:03
 */
public interface UserListService {
    /**
     * 查询未到位用户列表
     *
     * @param page 由于mybatis-plus的分页插件，所以需要传入Page对象
     * @param data 组织代码本级以及下级 (ORG_NO)
     * @return List<UserListVo> 未到位用户列表
     * @author Rabbit
     */
    List<UserListVo> userInfo(Page<UserListVo> page, List<String> data);

    /**
     * 获取需求响当前时段执行事件及实时执行情况
     *
     * @param orgId 组织id 不传则返回省级加16个地市数据
     * @return List<EventLoadVo> 事件及实时执行情况
     * @author Rabbit
     */
    List<EventLoadVo> timeIntervalList(String orgId);
}
