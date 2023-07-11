package com.xqxy.dr.modular.notice.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 公示附件
 * </p>
 *
 * @author xiao jun
 * @since 2021-04-27
 */
@ApiModel(description = "公示附件 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class NoticeFileParam extends BaseParam {

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    private Long updateUser;


}
