package com.xqxy.dr.modular.notice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 公示附件
 * </p>
 *
 * @author xiao jun
 * @since 2021-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dr_notice_file")
public class NoticeFile extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 关联公告ID
     */
    private Long noticeId;

    /**
     * 关联文件ID
     */
    private Long fileId;

    /**
     * 关联文件名
     */
    private String fileOriginName;


}
