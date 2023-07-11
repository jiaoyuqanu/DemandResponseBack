package com.xqxy.sys.modular.cust.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.sys.modular.cust.entity.BlackName;

import java.math.BigDecimal;
import java.util.List;

/**
 * 系统用户mapper接口
 *
 * @author czj
 * @date 2020/3/11 17:49
 */
public interface BlackNameMapper extends BaseMapper<BlackName> {
    /**
     * 统计用户次日效果评估负荷响应率未达标次数
     * @param executeRate
     * @return
     */
    List<BlackName> getEvalution(BigDecimal executeRate);

    void deleteByTime();

    /**
     * 查询用户次日效果评估负荷响应率未达标参与的事件信息
     * @param executeRate
     * @return
     */
    List<BlackName> getEvalutionEvent(BigDecimal executeRate);

    /**
     * 统计用户次不参与的次数
     *
     * @return
     */
    List<BlackName> getConsInvication();

    /**
     * 查询用户次不参与的事件信息
     *
     * @return
     */
    List<BlackName> getConsInvicationEvent();

    /**
     * 查找有效黑名单id
     * @return
     */
    List<String> getBlackNameConsIds();
}
