package com.xqxy.dr.modular.notice.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
@ApiModel(description = "公式 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class NoticeParam  extends BaseParam {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @NotNull(message = "consId不能为空，请检查consId参数", groups = {edit.class, delete.class, detail.class})
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
     * 文件id
     */
    private String fileIds;

    /**
     * 类型（字典 1注册 2申报 3竞价 4事件 5补贴）
     */
    @NotNull(message = "公告类型不能为空，请检查type参数", groups = {add.class})
    private Integer type;

    /**
     * 多类型
     */
    private List<Integer> types;

    /**
     * 发布时间
     */
    private LocalDateTime publicTime;

    /**
     * 截止时间
     */
    private LocalDate publicDate;

    /**
     * 截止时间
     */
    private LocalDate deadlineDate;

    /**
     * 撤回时间
     */
    private LocalDateTime cancelTime;

    /**
     * 状态（字典 0草稿 1发布 2撤回 3删除）
     */
    @NotNull(message = "公告状态不能为空，请检查status参数", groups = {add.class})
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    private Long updateUser;
    /**
     * 开始时间
     */
    private LocalDate startTime;
    /**
     * 结束时间
     */
    private LocalDate endTime;

    /**
     * 结束时间
     */
    private String businessRela;


}
