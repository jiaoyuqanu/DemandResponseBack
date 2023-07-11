package com.xqxy.dr.modular.notice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.annotion.NeedSetValue;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 公示表
 * </p>
 *
 * @author xiao jun
 * @since 2021-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_notice")
public class Notice extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型（字典 1注册 2申报 3竞价 4事件 5补贴）
     */
    private Integer type;

    /**
     * 发布日期
     */
    private LocalDate publicDate;

    /**
     * 发布时间
     */
    private LocalDateTime publicTime;
    /**
     * 截止日期
     */
    private LocalDate deadlineDate;

    /**
     * 撤回时间
     */
    private LocalDateTime cancelTime;

    /**
     * 状态（字典 0草稿 1发布 2撤回 3删除）
     */
    private Integer status;

    /**
     * 关联业务字段信息
     */
    private String businessRela;

    /**
     * 公示附件
     */
    @TableField(exist = false)
    @NeedSetValue(beanClass = com.xqxy.dr.modular.notice.service.NoticeFileService.class, method = "listByNoticeId", params = {"id"}, targetField = "noticeFileList")
    private List<NoticeFile> noticeFileList;

}
