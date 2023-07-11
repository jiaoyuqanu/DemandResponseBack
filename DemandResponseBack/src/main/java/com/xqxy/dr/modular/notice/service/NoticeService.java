package com.xqxy.dr.modular.notice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.notice.entity.Notice;
import com.xqxy.dr.modular.notice.param.NoticeParam;

import java.util.List;

/**
 * <p>
 * 公示表 服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-04-27
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 分页查询公式
     *
     * @param noticeParam 添加参数
     * @author xiao jun
     * @date 2020/3/25 14:57
     */
    Page<Notice> page(NoticeParam noticeParam);

    /**
     * 添加公式
     *
     * @param noticeParam 添加参数
     * @author xiao jun
     * @date 2020/3/25 14:57
     */
    void add(NoticeParam noticeParam);

    /**
     * 修改公式
     *
     * @param noticeParam 添加参数
     * @author xiao jun
     * @date 2020/3/25 14:57
     */
    void edit(NoticeParam noticeParam);

    /**
     * 公式详情
     *
     * @param noticeParam 添加参数
     * @author xiao jun
     * @date 2020/3/25 14:57
     */
    Notice detail(NoticeParam noticeParam);

    /**
     * @description: 查询某种类型的当年公示的条数
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/5/22 17:09
     */
    Integer countByTypeAndYear(Integer code, String overYear);

    /**
     * @description: 根据次数查询某种类型的当年公示的条数
     * @param:
     * @return:
     * @author: PengChuqing
     * @date: 2021/5/24 11:23
     */
    Notice getNoticeByTime(Integer code, String year, Integer times);

    /**
     * @description: 通过关联主键查询公告的主键id
     * @param: 
     * @return: 
     * @author: PengChuqing
     * @date: 2021/8/10 17:04
     */
    List<Notice> getByBusinessRela(String formatMonth,Integer noticeType);

}
